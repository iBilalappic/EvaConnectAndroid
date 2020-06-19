package com.hypernym.evaconnect.view.ui.fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hypernym.evaconnect.R;
import com.hypernym.evaconnect.constants.AppConstants;
import com.hypernym.evaconnect.listeners.OnOneOffClickListener;
import com.hypernym.evaconnect.models.BaseModel;
import com.hypernym.evaconnect.models.Comment;
import com.hypernym.evaconnect.models.Event;
import com.hypernym.evaconnect.models.EventAttendees;
import com.hypernym.evaconnect.models.User;
import com.hypernym.evaconnect.repositories.CustomViewModelFactory;
import com.hypernym.evaconnect.utils.AppUtils;
import com.hypernym.evaconnect.utils.DateUtils;
import com.hypernym.evaconnect.utils.LoginUtils;
import com.hypernym.evaconnect.utils.NetworkUtils;
import com.hypernym.evaconnect.view.adapters.CommentsAdapter;
import com.hypernym.evaconnect.view.adapters.EventAttendeesAdapter;
import com.hypernym.evaconnect.view.adapters.InvitedUsersAdapter;
import com.hypernym.evaconnect.viewmodel.EventViewModel;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;

import org.apache.commons.lang3.StringEscapeUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class EventDetailFragment extends BaseFragment implements Validator.ValidationListener {
   @BindView(R.id.tv_name)
   TextView tv_name;


   @BindView(R.id.img_event)
    ImageView img_event;

   @BindView(R.id.tv_content)
   TextView tv_content;



    @BindView(R.id.tv_eventdate)
    TextView tv_date;


    @BindView(R.id.tv_eventLocation)
    TextView tv_location;

    @BindView(R.id.rc_comments)
    RecyclerView rc_comments;

    @BindView(R.id.img_user)
    ImageView img_user;


    @BindView(R.id.btn_addcomment)
    ImageView btn_addcomment;

    @NotEmpty
    @BindView(R.id.edt_comment)
    EditText edt_comment;

    @BindView(R.id.tv_likecount)
    TextView tv_likecount;

    @BindView(R.id.tv_comcount)
    TextView tv_comcount;

    @BindView(R.id.img_like)
    ImageView img_like;

    @BindView(R.id.invite_people)
    RecyclerView invite_people;

    @BindView(R.id.tv_event_type)
    TextView tv_event_type;

    @BindView(R.id.modify_event)
    ImageButton modify_event;

    @BindView(R.id.accept_invite)
    ImageButton accept_invite;

    @BindView(R.id.interested)
    ImageButton interested;

    @BindView(R.id.register)
    ImageButton register;

    private List<Comment> comments=new ArrayList<>();
    private List<EventAttendees> eventAttendees=new ArrayList<>();

   private EventViewModel eventViewModel;
   private CommentsAdapter commentsAdapter;
   private EventAttendeesAdapter eventAttendeesAdapter;
    private Validator validator;
    int event_id;
    private Event event=new Event();
    private InvitedUsersAdapter usersAdapter;
    private List<User> invitedConnections = new ArrayList<>();

    public EventDetailFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_event_detail, container, false);
        ButterKnife.bind(this,view);
        init();
        return view;
    }

    private void init() {
        showBackButton();
        event_id=getArguments().getInt("id");
        eventViewModel = ViewModelProviders.of(this,new CustomViewModelFactory(getActivity().getApplication(),getActivity())).get(EventViewModel.class);
        validator = new Validator(this);
        validator.setValidationListener(this);

        commentsAdapter=new CommentsAdapter(getContext(),comments);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext());
        rc_comments.setLayoutManager(linearLayoutManager);
        rc_comments.setAdapter(commentsAdapter);

        AppUtils.setGlideImage(getContext(),img_user, LoginUtils.getLoggedinUser().getUser_image());
        setAttendeesAdapter();
         if(NetworkUtils.isNetworkConnected(getContext()))
         {
             getEventDetails(event_id);
             getEventComments(event_id);
         }
         else
         {
             networkErrorDialog();
         }

        img_like.setOnClickListener(new OnOneOffClickListener() {
            @Override
            public void onSingleClick(View v) {
                //   showDialog();

                User user = LoginUtils.getLoggedinUser();
                event.setCreated_by_id(user.getId());
                event.setEvent_id(event_id);
                if (event.getIs_event_like() == null || event.getIs_event_like() < 1) {
                    event.setAction(AppConstants.LIKE);
                    if (event.getIs_event_like() == null) {
                        event.setIs_event_like(1);

                    } else {
                        event.setIs_event_like(event.getIs_event_like() + 1);

                    }
                } else {
                    event.setAction(AppConstants.UNLIKE);
                    event.setIs_event_like(event.getIs_event_like() - 1);
                    //  post.setLike_count(post.getLike_count()-1);
                }
               // Log.d("Detail status", post.getAction() + " count" + post.getIs_post_like());
                eventViewModel.likeEvent(event).observe(getActivity(), new Observer<BaseModel<List<Event>>>() {
                    @Override
                    public void onChanged(BaseModel<List<Event>> listBaseModel) {
                        if (listBaseModel != null && !listBaseModel.isError()) {

                            AppUtils.setLikeCount(getContext(), tv_likecount, event.getAction(), img_like);
                            event.setLike_count(Integer.parseInt(tv_likecount.getText().toString()));

                        } else {
                            networkResponseDialog(getString(R.string.error), getString(R.string.err_unknown));
                        }
                        hideDialog();
                    }
                });
            }
        });
    }

    private void setAttendeesAdapter() {
            usersAdapter = new InvitedUsersAdapter(getContext(), invitedConnections);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
            invite_people.setLayoutManager(linearLayoutManager);
            invite_people.setAdapter(usersAdapter);
    }


    private void getEventComments(int event_id) {
        eventViewModel.getEventComments(event_id).observe(this, new Observer<BaseModel<List<Comment>>>() {
            @Override
            public void onChanged(BaseModel<List<Comment>> listBaseModel) {
                if(listBaseModel!=null && !listBaseModel.isError())
                {
                    comments.addAll(listBaseModel.getData());
                    Collections.reverse(comments);
                    commentsAdapter.notifyDataSetChanged();
               }
                else {
                    networkResponseDialog(getString(R.string.error),getString(R.string.err_unknown));
                }
            }
        });
    }

    private void getEventDetails(int event_id) {
        eventViewModel.getEventDetails(event_id).observe(this, new Observer<BaseModel<List<Event>>>() {
            @Override
            public void onChanged(BaseModel<List<Event>> listBaseModel) {
                if(listBaseModel!=null && !listBaseModel.isError() && listBaseModel.getData().size()>0)
                {
                    event=listBaseModel.getData().get(0);
                    setEventData(listBaseModel.getData().get(0));
                    for(EventAttendees user:event.getAttendees())
                    {
                        invitedConnections.add(user.getUser());
                    }
                usersAdapter.notifyDataSetChanged();
                }
                else
                {
                    networkResponseDialog(getString(R.string.error),getString(R.string.err_unknown));
                }
                if(event.getCreated_by_id()== LoginUtils.getLoggedinUser().getId())
                {
                    modify_event.setVisibility(View.VISIBLE);
                }
                else
                {
                    modify_event.setVisibility(View.GONE);
                    if(event.getIs_attending().equalsIgnoreCase("Pending"))
                    {
                        accept_invite.setVisibility(View.VISIBLE);
                        interested.setVisibility(View.GONE);
                    }
                    else
                    {
                        accept_invite.setVisibility(View.GONE);
                        interested.setVisibility(View.VISIBLE);
                    }
                }
            }

            private void setEventData(Event event) {
                tv_name.setText(event.getName());

                if(event.getEvent_image().size()>0)
                {
                    AppUtils.setGlideImageUrl(getContext(),img_event,event.getEvent_image().get(0));
                }
              else {
                    img_event.setBackground(getContext().getDrawable(R.drawable.no_thumbnail));
                }
                tv_content.setText(event.getContent());

                tv_date.setText(DateUtils.getFormattedDateDMY(event.getStart_date())+" - "+ DateUtils.getFormattedDateDMY(event.getEnd_date()) +" | "+event.getStart_time()+" - "+event.getEnd_time());

                tv_location.setText(event.getEvent_city());
                if(event.getIs_private()==0)
                {
                    tv_event_type.setText("Open to the public");
                }
                else
                {
                    tv_event_type.setText("Private to the public");
                }

                tv_likecount.setText(String.valueOf(event.getLike_count()));
                tv_comcount.setText(String.valueOf(event.getComment_count()));
                if (event.getIs_event_like() != null && event.getIs_event_like() > 0) {
                    img_like.setBackground(getContext().getDrawable(R.drawable.ic_like));
                } else {
                    img_like.setBackground(getContext().getDrawable(R.drawable.ic_like));
                }
            }
        });
    }





    private void updateAttendance(Event event) {
        eventViewModel.updateEventAttendance(event).observe(this, new Observer<BaseModel<List<Event>>>() {
            @Override
            public void onChanged(BaseModel<List<Event>> listBaseModel) {
                if(listBaseModel!=null && listBaseModel.getData()!=null)
                {
                    accept_invite.setVisibility(View.GONE);
                    interested.setVisibility(View.VISIBLE);
                }
                else
                {
                    networkResponseDialog(getString(R.string.error),getString(R.string.err_unknown));
                }
            }
        });
    }

    private void addAttendance(Event event) {

        eventViewModel.addEventAttendance(event).observe(this, new Observer<BaseModel<List<Event>>>() {
            @Override
            public void onChanged(BaseModel<List<Event>> listBaseModel) {
                if(listBaseModel!=null && listBaseModel.getData()!=null)
                {

                }
                else
                {
                    networkResponseDialog(getString(R.string.error),getString(R.string.err_unknown));
                }
            }
        });
    }

    @OnClick(R.id.btn_addcomment)
    public void addComment()
    {
        validator.validate();
    }

    @OnClick(R.id.accept_invite)
    public void accept_invite()
    {
        event.setAttendance_status("Going");
        updateAttendance(event);
    }

    @Override
    public void onValidationSucceeded() {
        addEventComment(event_id);
    }

    private void addEventComment(int event_id) {

            showDialog();
            User user = LoginUtils.getUser();
            Comment comment = new Comment();
            String toServerUnicodeEncoded = StringEscapeUtils.escapeJava(edt_comment.getText().toString());
            comment.setContent(toServerUnicodeEncoded);
            comment.setCreated_by_id(user.getId());
            comment.setStatus(AppConstants.STATUS_ACTIVE);
            comment.setEvent_id(event_id);
            eventViewModel.addComment(comment).observe(this, new Observer<BaseModel<List<Comment>>>() {
                @Override
                public void onChanged(BaseModel<List<Comment>> listBaseModel) {
                    if (!listBaseModel.isError()) {

                        // Toast.makeText(getContext(), getString(R.string.msg_comment_created), Toast.LENGTH_LONG).show();
                        edt_comment.setText("");
                        //networkResponseDialog(getString(R.string.success),getString(R.string.msg_comment_created));
                        getEventComments(event_id);
                    } else {
                        networkResponseDialog(getString(R.string.error), getString(R.string.err_unknown));
                    }
                    hideDialog();
                }
            });

    }
    @OnClick(R.id.modify_event)
    public void modifyevent()
    {
        CreateEventFragment createEventFragment=new CreateEventFragment();
        Bundle bundle=new Bundle();
        bundle.putSerializable("event",event);
        createEventFragment.setArguments(bundle);
        loadFragment(R.id.framelayout,createEventFragment,getContext(),true);
    }
    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error : errors) {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(getContext());
            // Display error messages
            if (view instanceof EditText) {
                ((EditText) view).setError(message);
            } else {
                Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
            }
        }
    }

}
