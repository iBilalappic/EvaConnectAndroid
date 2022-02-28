package com.hypernym.evaconnect.view.ui.fragments;

import static android.content.Context.CLIPBOARD_SERVICE;
import static com.hypernym.evaconnect.listeners.PaginationScrollListener.PAGE_START;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.hypernym.evaconnect.R;
import com.hypernym.evaconnect.constants.AppConstants;
import com.hypernym.evaconnect.listeners.OnOneOffClickListener;
import com.hypernym.evaconnect.listeners.PaginationScrollListener;
import com.hypernym.evaconnect.models.BaseModel;
import com.hypernym.evaconnect.models.Post;
import com.hypernym.evaconnect.models.User;
import com.hypernym.evaconnect.repositories.CustomViewModelFactory;
import com.hypernym.evaconnect.utils.Constants;
import com.hypernym.evaconnect.utils.LoginUtils;
import com.hypernym.evaconnect.utils.NetworkUtils;
import com.hypernym.evaconnect.view.adapters.JobHomeAdapter;
import com.hypernym.evaconnect.view.bottomsheets.BottomsheetShareSelection;
import com.hypernym.evaconnect.viewmodel.JobListViewModel;
import com.hypernym.evaconnect.viewmodel.PostViewModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class JobIndustryFragment extends BaseFragment implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener, JobHomeAdapter.ItemClickListener {

    @BindView(R.id.rc_job)
    RecyclerView rc_job;

    @BindView(R.id.newpost)
    TextView newpost;

    @BindView(R.id.tv_empty)
    TextView tv_empty;

    @BindView(R.id.swipeRefresh)
    SwipeRefreshLayout swipeRefresh;

    @BindView(R.id.tv_create_job)
    TextView tv_create_job;

    private JobListViewModel jobListViewModel;
    private PostViewModel postViewModel;
    private JobHomeAdapter postAdapter;
    private List<Post> posts = new ArrayList<>();
    int itemCount = 0;
    private LinearLayoutManager linearLayoutManager;
    private int currentPage = PAGE_START;
    private boolean isLastPage = false;
    private boolean isLoading = false;
    int item_position;

    public JobIndustryFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_job, container, false);
        ButterKnife.bind(this, view);
        return view;
    }


    @Override
    public void onClick(View v) {

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
        jobListViewModel = ViewModelProviders.of(this, new CustomViewModelFactory(getActivity().getApplication(), getActivity())).get(JobListViewModel.class);
        postViewModel = ViewModelProviders.of(this, new CustomViewModelFactory(getActivity().getApplication(), getActivity())).get(PostViewModel.class);
        //   currentPage = PAGE_START;
        postAdapter = new JobHomeAdapter(getContext(), posts, this);
        linearLayoutManager = new LinearLayoutManager(getContext());
        rc_job.setLayoutManager(linearLayoutManager);
        rc_job.setAdapter(postAdapter);

        User user=LoginUtils.getLoggedinUser();
        if (user.getType().equalsIgnoreCase("company")) {
            tv_create_job.setVisibility(View.VISIBLE);
        }else{
            tv_create_job.setVisibility(View.GONE);
        }

        RecyclerView.ItemAnimator animator = rc_job.getItemAnimator();
        if (animator instanceof SimpleItemAnimator) {
            ((SimpleItemAnimator) animator).setSupportsChangeAnimations(false);
        }
        swipeRefresh.setOnRefreshListener(this);

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




    private void callPostsApi() {
        User user = LoginUtils.getLoggedinUser();
        user.setFilter("industry");

        jobListViewModel.getJob(user/*, AppConstants.TOTAL_PAGES, currentPage*/).observe(this, new Observer<BaseModel<List<Post>>>() {
            @Override
            public void onChanged(BaseModel<List<Post>> dashboardBaseModel) {

                //   homePostsAdapter.clear();
                tv_empty.setVisibility(View.GONE);
                rc_job.setVisibility(View.VISIBLE);
                if (dashboardBaseModel != null && !dashboardBaseModel.isError() && dashboardBaseModel.getData().size() > 0 && dashboardBaseModel.getData().get(0) != null) {
                    for (Post post : dashboardBaseModel.getData()) {
                        if (post.getContent() == null) {
                            post.setContent("");
                        }
                        else if (post.getType().equalsIgnoreCase("job")) {
                            post.setPost_type(AppConstants.JOB_TYPE);
                        }
                    }
                    posts.addAll(dashboardBaseModel.getData());
                    postAdapter.notifyDataSetChanged();
                    swipeRefresh.setRefreshing(false);
                    //  postAdapter.removeLoading();
                    isLoading = false;
                } else if (dashboardBaseModel != null && !dashboardBaseModel.isError() && dashboardBaseModel.getData().size() == 0) {
                    isLastPage = true;
                    // postAdapter.removeLoading();
                    isLoading = false;
                    swipeRefresh.setRefreshing(false);
                    tv_empty.setVisibility(View.VISIBLE);
                    tv_empty.setText(dashboardBaseModel.getMessage());
                    rc_job.setVisibility(View.GONE);
                } else {
                    swipeRefresh.setRefreshing(false);
                    networkResponseDialog(getString(R.string.error), getString(R.string.err_unknown));
                }

            }
        });
    }

    @Override
    public void onItemClick(View view, int position) {

    }


    @Override
    public void onJobLikeClick(View view, int position, TextView likeCount) {
        if (posts.get(position).getIs_job_like() != null && posts.get(position).getIs_job_like() > 0) {
            SetJobUnLike(posts.get(position).getId(), position);
        } else {
            SetJobLike(posts.get(position).getId(), position);
        }
    }
    private void SetJobLike(Integer id, int position) {

        jobListViewModel.setJobLike(LoginUtils.getUser(), id, "like").observe(this, new Observer<BaseModel<List<Object>>>() {
            @Override
            public void onChanged(BaseModel<List<Object>> setlike) {
                Post post = posts.get(position);
                post.setAction(AppConstants.LIKE);
                if (post.getIs_job_like() == null) {
                    post.setIs_job_like(1);
                    if (post.getLike_count() == null)
                        post.setLike_count(0);
                    else
                        post.setLike_count(post.getLike_count() + 1);
                } else {
                    post.setIs_job_like(post.getIs_job_like() + 1);
                    if (post.getLike_count() == null)
                        post.setLike_count(0);
                    else
                        post.setLike_count(post.getLike_count() + 1);
                }
                postAdapter.notifyItemChanged(position);
//                if (setlike != null && !setlike.isError()) {
                //     onRefresh();
//                } else {
//                    networkResponseDialog(getString(R.string.error), getString(R.string.err_unknown));
//                }

                hideDialog();

            }
        });
    }

    private void SetJobUnLike(Integer id, int position) {
        jobListViewModel.setJobLike(LoginUtils.getUser(), id, "unlike").observe(this, new Observer<BaseModel<List<Object>>>() {
            @Override
            public void onChanged(BaseModel<List<Object>> setlike) {
                // onRefresh();
                Post post = posts.get(position);
                post.setAction(AppConstants.UNLIKE);
                if (post.getIs_job_like() > 0) {
                    post.setIs_job_like(post.getIs_job_like() - 1);
                    post.setLike_count(post.getLike_count() - 1);
                } else {
                    post.setIs_job_like(0);
                    post.setLike_count(0);
                }

                postAdapter.notifyItemChanged(position);
                hideDialog();
            }
        });
    }

    @Override
    public void onShareClick(View view, int position) {
//        ShareDialog shareDialog;
//        Bundle bundle = new Bundle();
//        bundle.putSerializable("PostData",posts.get(position));
//        shareDialog = new ShareDialog(getContext(),bundle);
//        shareDialog.show();

        item_position = position;
        BottomsheetShareSelection bottomSheetPictureSelection = new BottomsheetShareSelection(new JobIndustryFragment.YourDialogFragmentDismissHandler());
        bottomSheetPictureSelection.show(getActivity().getSupportFragmentManager(), bottomSheetPictureSelection.getTag());
    }

    @Override
    public void onApplyClick(View view, int position) {
        SpecficJobFragment specficJobFragment = new SpecficJobFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("job_id", posts.get(position).getId());
        specficJobFragment.setArguments(bundle);
        loadFragment(R.id.framelayout, specficJobFragment, getContext(), true);
    }

    @Override
    public void onApplyCommentClick(View view, int position) {
        SpecficJobComments specficJobComment = new SpecficJobComments();
        Bundle bundlecomment = new Bundle();
        bundlecomment.putInt("job_id", posts.get(position).getId());
        specficJobComment.setArguments(bundlecomment);
        loadFragment(R.id.framelayout, specficJobComment, getContext(), true);
    }

    @Override
    public void onEditClick(View view, int position) {
        CreateJobFragment createJobFragment = new CreateJobFragment();
        Bundle bundle1 = new Bundle();
        bundle1.putSerializable("job_id", posts.get(position).getId());
        createJobFragment.setArguments(bundle1);
        loadFragment(R.id.framelayout, createJobFragment, getContext(), true);

    }

    @Override
    public void onDeleteClick(View view, int position) {
        postViewModel.deleteJob(posts.get(position)).observe(this, new Observer<BaseModel<List<Post>>>() {
            @Override
            public void onChanged(BaseModel<List<Post>> listBaseModel) {
                if (NetworkUtils.isNetworkConnected(getContext())) {
//                    posts.clear();
//                    callPostsApi();
                    onRefresh();
                } else {
                    networkErrorDialog();
                }
            }
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
                FragmentTransaction transaction = ((AppCompatActivity) requireActivity()).getSupportFragmentManager().beginTransaction();
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
            }
            else if(msg.what==103){
                ClipboardManager clipboard = (ClipboardManager) getContext().getSystemService(CLIPBOARD_SERVICE);
                ClipData clip;
                clip = ClipData.newPlainText("label", "https://www.evaintmedia.com/" + posts.get(item_position).getType() + "/" + posts.get(item_position).getId());
                clipboard.setPrimaryClip(clip);
                Toast.makeText(requireContext(), "link copied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}