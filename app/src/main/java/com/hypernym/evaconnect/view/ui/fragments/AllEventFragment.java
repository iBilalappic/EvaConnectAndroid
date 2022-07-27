package com.hypernym.evaconnect.view.ui.fragments;

import static android.content.Context.CLIPBOARD_SERVICE;
import static com.hypernym.evaconnect.listeners.PaginationScrollListener.PAGE_START;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.hypernym.evaconnect.R;
import com.hypernym.evaconnect.constants.AppConstants;
import com.hypernym.evaconnect.listeners.OnOneOffClickListener;
import com.hypernym.evaconnect.listeners.PaginationScrollListener;
import com.hypernym.evaconnect.models.Event;
import com.hypernym.evaconnect.models.Post;
import com.hypernym.evaconnect.models.User;
import com.hypernym.evaconnect.repositories.CustomViewModelFactory;
import com.hypernym.evaconnect.utils.Constants;
import com.hypernym.evaconnect.utils.LoginUtils;
import com.hypernym.evaconnect.utils.NetworkUtils;
import com.hypernym.evaconnect.view.adapters.EventHomeAdapter;
import com.hypernym.evaconnect.view.bottomsheets.BottomsheetShareSelection;
import com.hypernym.evaconnect.viewmodel.EventViewModel;
import com.hypernym.evaconnect.viewmodel.PostViewModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AllEventFragment extends BaseFragment implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener, EventHomeAdapter.ItemClickListener {

    @BindView(R.id.rc_event)
    RecyclerView rc_event;

    @BindView(R.id.newpost)
    TextView newpost;

    @BindView(R.id.tv_create_event)
    TextView tv_create_event;

    @BindView(R.id.tv_empty)
    TextView tv_empty;


    @BindView(R.id.swipeRefresh)
    SwipeRefreshLayout swipeRefresh;
    private EventViewModel eventViewModel;
    private PostViewModel postViewModel;
    private EventHomeAdapter postAdapter;
    private List<Post> posts = new ArrayList<>();
    int itemCount = 0;
    private LinearLayoutManager linearLayoutManager;
    private int currentPage = PAGE_START;
    private boolean isLastPage = false;
    private boolean isLoading = false;
    int item_position;

    public AllEventFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_event, container, false);

        ButterKnife.bind(this, view);
        tv_create_event.setOnClickListener(this);

        Log.d("allevents", "onCreateView: All Event ");
        return view;
    }


    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.tv_create_event){
            CreateEventFragment createEventFragment = new CreateEventFragment();
           /* Bundle bundle = new Bundle();
            bundle.putInt("post", posts.get(position).getId());
            Log.d("TAAAGNOTIFY", "" + posts.get(position).getId());*/
            //createEventFragment.setArguments(bundle);
            loadFragment(R.id.framelayout, createEventFragment, getContext(), true);
        }

    }


    @Override
    public void onResume() {
        super.onResume();
        init();
        onRefresh();
        newpost.setOnClickListener(new OnOneOffClickListener() {
            @Override
            public void onSingleClick(View v) {
                loadFragment(R.id.framelayout, new NewPostFragment(), getContext(), true);
            }
        });
    }

    private void init() {
        User user=LoginUtils.getLoggedinUser();
        eventViewModel = ViewModelProviders.of(this, new CustomViewModelFactory(getActivity().getApplication(), getActivity())).get(EventViewModel.class);
        postViewModel = ViewModelProviders.of(this, new CustomViewModelFactory(getActivity().getApplication(), getActivity())).get(PostViewModel.class);
        //   currentPage = PAGE_START;
        postAdapter = new EventHomeAdapter(getContext(), posts, this);
        linearLayoutManager = new LinearLayoutManager(getContext());

        if (user.getType().equalsIgnoreCase("company")) {
            tv_create_event.setVisibility(View.VISIBLE);
        }else{
            tv_create_event.setVisibility(View.GONE);
        }

        rc_event.setLayoutManager(linearLayoutManager);
        rc_event.setAdapter(postAdapter);
        rc_event.setAdapter(postAdapter);
        rc_event.setHasFixedSize(true);
        rc_event.setItemViewCacheSize(15);
        RecyclerView.ItemAnimator animator = rc_event.getItemAnimator();
        if (animator instanceof SimpleItemAnimator) {
            ((SimpleItemAnimator) animator).setSupportsChangeAnimations(false);
        }
        swipeRefresh.setOnRefreshListener(this);
        /**
         * add scroll listener while user reach in bottom load more will call
         */

        rc_event.addOnScrollListener(new PaginationScrollListener(linearLayoutManager) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
                currentPage = AppConstants.TOTAL_PAGES + currentPage;
                if (NetworkUtils.isNetworkConnected(getContext())) {
                    callPostsApi();
                } else {
                    networkErrorDialog();
                }
            }

            @Override
            public boolean isLastPage() {
                return isLastPage;
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }
        });

    }


    @Override
    public void onRefresh() {
        itemCount = 0;
        currentPage = PAGE_START;
        isLastPage = false;
        postAdapter.clear();
        if (NetworkUtils.isNetworkConnected(getContext())) {
            callPostsApi();
        } else {
            networkErrorDialog();
        }
    }

    @Override
    public void onItemClick(View view, int position) {
        if (view.getId()==R.id.tv_attending) {

            TextView textView = view.findViewById(R.id.tv_attending);
            textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_check, 0, 0, 0);

        } else {
            PostDetailsFragment postDetailsFragment = new PostDetailsFragment();
            Bundle bundle = new Bundle();
            bundle.putInt("post", posts.get(position).getId());
            Log.d("TAAAGNOTIFY", "" + posts.get(position).getId());
            postDetailsFragment.setArguments(bundle);
            loadFragment(R.id.framelayout, postDetailsFragment, getContext(), true);
        }
    }

    @Override
    public void onLikeClick(View view, int position, TextView likeCount) {
        Post post = posts.get(position);
        User user = LoginUtils.getLoggedinUser();
        post.setPost_id(post.getId());
        post.setCreated_by_id(user.getId());
        if (post.getIs_post_like() == null || post.getIs_post_like() < 1) {
            post.setAction(AppConstants.LIKE);
            if (post.getIs_post_like() == null) {
                post.setIs_post_like(1);
                if (post.getLike_count() == null)
                    post.setLike_count(0);
                else
                    post.setLike_count(post.getLike_count() + 1);
            } else {
                post.setIs_post_like(post.getIs_post_like() + 1);
                if (post.getLike_count() == null)
                    post.setLike_count(0);
                else
                    post.setLike_count(post.getLike_count() + 1);
            }
        } else {
            post.setAction(AppConstants.UNLIKE);
            if (post.getIs_post_like() > 0) {
                post.setIs_post_like(post.getIs_post_like() - 1);
                post.setLike_count(post.getLike_count() - 1);
            } else {
                post.setIs_post_like(0);
                post.setLike_count(0);
            }

        }
        Log.d("Listing status", post.getAction() + " count" + post.getIs_post_like());
        if (NetworkUtils.isNetworkConnected(getContext())) {

            likePost(post, position);
        } else {
            networkErrorDialog();
        }

    }

    @Override
    public void onJobLikeClick(View view, int position, TextView likeCount) {

    }

    private void likePost(Post post, int position) {
        postViewModel.likePost(post).observe(this, listBaseModel -> {
            if (listBaseModel != null && !listBaseModel.isError()) {
                postAdapter.notifyItemChanged(position);
            } else {
                networkResponseDialog(getString(R.string.error), getString(R.string.err_unknown));
            }
            hideDialog();
        });
    }

    public void onShareClick(View view, int position) {
        item_position = position;
        BottomsheetShareSelection bottomSheetPictureSelection = new BottomsheetShareSelection(new YourDialogFragmentDismissHandler());
        bottomSheetPictureSelection.show(getActivity().getSupportFragmentManager(), bottomSheetPictureSelection.getTag());
    }

    @Override
    public void onConnectClick(View view, int position) {

    }

    @Override
    public void onVideoClick(View view, int position) {

    }

    @Override
    public void onURLClick(View view, int position) {

    }

    @Override
    public void onProfileClick(View view, int position) {
        PersonProfileFragment personDetailFragment = new PersonProfileFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("PostData", posts.get(position));
        personDetailFragment.setArguments(bundle);
        loadFragment(R.id.framelayout, personDetailFragment, getContext(), true);


    }

    @Override
    public void onApplyClick(View view, int position) {

    }

    @Override
    public void onEventItemClick(View view, int position) {

        if (view.getId()==R.id.tv_attending) {
            TextView textView = view.findViewById(R.id.tv_attending);
            Drawable[] compoundDrawables = textView.getCompoundDrawables();

            Drawable leftCompoundDrawable = compoundDrawables[0];
            if (leftCompoundDrawable==null) {
             //   textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_check, 0, 0, 0);

                showInterestedApiCall(position,"Going");
            } else {
             //   textView.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                showInterestedApiCall(position,"Not Going");

            }

        }else if(view.getId()==R.id.tv_interested){


            EventInterestedFragment eventDetailFragment = new EventInterestedFragment();
            Bundle bundle = new Bundle();
            bundle.putInt("event_id", posts.get(position).getId());
            eventDetailFragment.setArguments(bundle);
            loadFragment(R.id.framelayout, eventDetailFragment, getContext(), true);
        } else {
            EventDetailFragment eventDetailFragment = new EventDetailFragment();
            Bundle bundle = new Bundle();
            bundle.putInt("id", posts.get(position).getId());
            bundle.putInt("user_id", posts.get(position).getUser().getId());
            eventDetailFragment.setArguments(bundle);
            loadFragment(R.id.framelayout, eventDetailFragment, getContext(), true);
        }

    }

    private void showInterestedApiCall(int position, String attendee_Status) {

//        showDialog();
        eventViewModel.showInterestEvent(posts.get(position).getId(), LoginUtils.getUser().getId(), "active", attendee_Status).observe(this, listBaseModel -> {
            if (!listBaseModel.isError()) {
                posts.get(position).setLikeLoading(false);
                postAdapter.notifyItemChanged(position);



                callPostsApi();
            } else {

                networkResponseDialog(getString(R.string.error), getString(R.string.err_unknown));
            }

        });

    }

    @Override
    public void onEventLikeClick(View view, int position, TextView likeCount) {
        Post post = posts.get(position);
        User user = LoginUtils.getLoggedinUser();
        post.setEvent_id(post.getId());
        post.setCreated_by_id(user.getId());
        if (post.getIs_event_like() == null || post.getIs_event_like() < 1) {
            post.setAction(AppConstants.LIKE);
            if (post.getIs_event_like() == null) {
                post.setIs_event_like(1);
                if (post.getLike_count() == null)
                    post.setLike_count(0);
                else
                    post.setLike_count(post.getLike_count() + 1);
            } else {
                post.setIs_event_like(post.getIs_event_like() + 1);
                if (post.getLike_count() == null)
                    post.setLike_count(0);
                else
                    post.setLike_count(post.getLike_count() + 1);
            }
        } else {
            post.setAction(AppConstants.UNLIKE);
            if (post.getIs_event_like() > 0) {
                post.setIs_event_like(post.getIs_event_like() - 1);
                post.setLike_count(post.getLike_count() - 1);
            } else {
                post.setIs_event_like(0);
                post.setLike_count(0);
            }

        }
        // Log.d("Listing status",post.getAction()+" count"+post.getIs_post_like());
        if (NetworkUtils.isNetworkConnected(getContext())) {
            likeEvent(post, position);
        } else {
            networkErrorDialog();
        }
    }

    @Override
    public void onEditClick(View view, int position) {
        getEventDetails(posts.get(position).getId(),posts.get(position).getUser().getId());

    }
    private void getEventDetails(int event_id, int user_id) {
       // invitedConnections.clear();
        eventViewModel.getEventDetails(event_id, user_id).observe(this, listBaseModel -> {
            if(listBaseModel!=null && !listBaseModel.isError() && listBaseModel.getData().size()>0)
            {
                CreateEventFragment createEventFragment=new CreateEventFragment();
                Bundle bundle=new Bundle();
                bundle.putSerializable("event",listBaseModel.getData().get(0));
                createEventFragment.setArguments(bundle);
                loadFragment(R.id.framelayout,createEventFragment,getContext(),true);
            }
            else
            {
                networkResponseDialog(getString(R.string.error),getString(R.string.err_unknown));
            }

        });
    }
    @Override
    public void onDeleteClick(View view, int position) {
        eventViewModel.deleteEvent(posts.get(position)).observe(this, listBaseModel -> {
            if (NetworkUtils.isNetworkConnected(getContext())) {
                posts.clear();
                callPostsApi();
            } else {
                networkErrorDialog();
            }
        });
    }

    private void likeEvent(Post post, int position) {
        Event event = new Event();
        event.setEvent_id(post.getEvent_id());
        event.setCreated_by_id(LoginUtils.getLoggedinUser().getId());
        event.setAction(post.getAction());
        event.setStatus(AppConstants.ACTIVE);
        eventViewModel.likeEvent(event).observe(this, listBaseModel -> {
            if (listBaseModel != null && !listBaseModel.isError()) {
                postAdapter.notifyItemChanged(position);
            } else {
                networkResponseDialog(getString(R.string.error), getString(R.string.err_unknown));
            }
            hideDialog();
        });
    }

    private void callPostsApi() {
        User user = LoginUtils.getLoggedinUser();

//        showDialog();

        if (user.getType().equalsIgnoreCase("user")) {
            user.setFilter("all");
        } else {
            user.setFilter("all");
        }


        eventViewModel.getEvent(user, AppConstants.TOTAL_PAGES, currentPage).observe(this, dashboardBaseModel -> {
            Log.d("api", "i am call: ");
            // postAdapter.clear();


            if (dashboardBaseModel != null && !dashboardBaseModel.isError() && dashboardBaseModel.getData().size() > 0 && dashboardBaseModel.getData().get(0) != null) {
                Log.d("api", "true: ");
                if (dashboardBaseModel.getData().size() == 0 && currentPage == AppConstants.TOTAL_PAGES) {
                    rc_event.setVisibility(View.GONE);
                    tv_empty.setVisibility(View.VISIBLE);
                } else {
                    tv_empty.setVisibility(View.GONE);
                    rc_event.setVisibility(View.VISIBLE);
                }
                for (Post post : dashboardBaseModel.getData()) {
                    if (post.getContent() == null) {
                        post.setContent("");
                    } else if (post.getType().equalsIgnoreCase("event")) {
                        post.setPost_type(AppConstants.EVENT_TYPE);
                    }
                }
                posts.addAll(dashboardBaseModel.getData());
                postAdapter.notifyDataSetChanged();
                swipeRefresh.setRefreshing(false);
//                postAdapter.removeLoading();
                isLoading = false;

            } else if (dashboardBaseModel != null && !dashboardBaseModel.isError() && dashboardBaseModel.getData().size() == 0) {
                isLastPage = true;
//                postAdapter.removeLoading();
                Log.d("api", "false: ");
                isLoading = false;
            } else {
                Log.d("aliis", "i am call: "+dashboardBaseModel.isError() );
                networkResponseDialog(getString(R.string.error), getString(R.string.err_unknown));
            }

            hideDialog();

        });

    }


    protected class YourDialogFragmentDismissHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 102) {
                Intent whatsappIntent = new Intent(Intent.ACTION_SEND);
                whatsappIntent.setType("text/plain");
                whatsappIntent.setPackage("com.whatsapp");
                whatsappIntent.putExtra(Intent.EXTRA_TEXT, "https://www.evaintmedia.com/" + posts.get(item_position).getType() + "/" + posts.get(item_position).getId());
                try {
                    getContext().startActivity(whatsappIntent);
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(requireContext(), "Whatsapp have not been installed.", Toast.LENGTH_SHORT).show();
                }
            }
            else if(msg.what==100){
                ShareConnectionFragment shareConnectionFragment = new ShareConnectionFragment();
                FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
                transaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
                transaction.replace(R.id.framelayout, shareConnectionFragment);
                Bundle bundle = new Bundle();
                {
                    bundle.putInt(Constants.DATA, posts.get(item_position).getId());
                    bundle.putString(Constants.TYPE,  posts.get(item_position).getType());
                }
                shareConnectionFragment.setArguments(bundle);
                if (true) {
                    transaction.addToBackStack(null);
                }
                transaction.commit();
            }else if(msg.what==103){
                ClipboardManager clipboard = (ClipboardManager) getContext().getSystemService(CLIPBOARD_SERVICE);
                ClipData clip;
                clip = ClipData.newPlainText("label", "https://www.evaintmedia.com/" + posts.get(item_position).getType() + "/" + posts.get(item_position).getId());
                clipboard.setPrimaryClip(clip);
                Toast.makeText(requireContext(), "link copied", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
