package com.hypernym.evaconnect.view.ui.fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class EventDetailFragment extends BaseFragment implements Validator.ValidationListener, CommentsAdapter.OnItemClickListener, View.OnClickListener{
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
    TextView modify_event;

    @BindView(R.id.accept_invite)
    TextView accept_invite;

    @BindView(R.id.interested)
    TextView interested;

    @BindView(R.id.register)
    TextView register;

    @BindView(R.id.like_click)
    LinearLayout like_click;

    @BindView(R.id.comment_click)
    LinearLayout comment_click;

    @BindView(R.id.share_click)
    LinearLayout share_click;

    @BindView(R.id.layout_editcomment)
    LinearLayout layout_editcomment;

    @BindView(R.id.button_cancel)
    Button button_cancel;

    @BindView(R.id.button_save)
    Button button_save;

    @BindView(R.id.save)
    ImageButton save;

    @BindView(R.id.interested_header)
    TextView interested_header;

    @BindView(R.id.img_backarrow)
    ImageView img_backarrow;

    @BindView(R.id.tv_interested)
    TextView tv_interested;

    private List<Comment> comments = new ArrayList<>();
    private List<EventAttendees> eventAttendees = new ArrayList<>();

    private EventViewModel eventViewModel;
    private CommentsAdapter commentsAdapter;
    private EventAttendeesAdapter eventAttendeesAdapter;
    private Validator validator;
    int event_id, comment_id, user_id;
    private Event event = new Event();
    private InvitedUsersAdapter usersAdapter;
    private List<User> invitedConnections = new ArrayList<>();

    public EventDetailFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_event_detail, container, false);
        ButterKnife.bind(this, view);
        getActivity().findViewById(R.id.seprator_line).setVisibility(View.VISIBLE);
        img_backarrow.setOnClickListener(this);
        init();
        return view;
    }

    private void init() {
        //  showBackButton();
        event_id = getArguments().getInt("id");
        user_id = getArguments().getInt("user_id");
        eventViewModel = ViewModelProviders.of(this, new CustomViewModelFactory(getActivity().getApplication(), getActivity())).get(EventViewModel.class);
        validator = new Validator(this);
        validator.setValidationListener(this);
        commentsAdapter = new CommentsAdapter(getContext(), comments, this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rc_comments.setLayoutManager(linearLayoutManager);
        rc_comments.setAdapter(commentsAdapter);

        AppUtils.setGlideImage(getContext(), img_user, LoginUtils.getLoggedinUser().getUser_image());
        setAttendeesAdapter();
        if (NetworkUtils.isNetworkConnected(getContext())) {
            getEventDetails(event_id, user_id);

        } else {
            networkErrorDialog();
        }
        setPageTitle("Event Details");

        like_click.setOnClickListener(new OnOneOffClickListener() {
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
        accept_invite.setOnClickListener(new OnOneOffClickListener() {
            @Override
            public void onSingleClick(View v) {
                if (event.getIs_private() == 0) {
                    addAttendance(event);
                } else {
                    updateAttendance(event);
                }
            }
        });
        register.setOnClickListener(new OnOneOffClickListener() {
            @Override
            public void onSingleClick(View v) {
                if (event.getIs_private() == 0) {
                    addAttendance(event);
                } else {
                    updateAttendance(event);
                }
            }
        });
    }

    private void setAttendeesAdapter() {
        usersAdapter = new InvitedUsersAdapter(getContext(), invitedConnections, false);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        invite_people.setLayoutManager(linearLayoutManager);
        invite_people.setAdapter(usersAdapter);
    }


    private void getEventComments(int event_id) {
        comments.clear();
        eventViewModel.getEventComments(event_id).observe(this, new Observer<BaseModel<List<Comment>>>() {
            @Override
            public void onChanged(BaseModel<List<Comment>> listBaseModel) {
                if (listBaseModel != null && !listBaseModel.isError()) {
                    comments.addAll(listBaseModel.getData());
                    if (event.getUser_id().equals(LoginUtils.getLoggedinUser().getId())) {
                        for (Comment comment : comments) {
                            comment.setPostMine(true);
                        }
                    }
                    //  Collections.reverse(comments);
                    commentsAdapter.notifyDataSetChanged();
                    if (comments.size() > 0)
                        rc_comments.smoothScrollToPosition(comments.size() - 1);
                    layout_editcomment.setVisibility(View.GONE);
                    btn_addcomment.setVisibility(View.VISIBLE);
                    edt_comment.setText("");
                } else {
                    networkResponseDialog(getString(R.string.error), getString(R.string.err_unknown));
                }
            }
        });
    }

    private void getEventDetails(int event_id, int user_id) {
        invitedConnections.clear();
        eventViewModel.getEventDetails(event_id, user_id).observe(getViewLifecycleOwner(), new Observer<BaseModel<List<Event>>>() {
            @Override
            public void onChanged(BaseModel<List<Event>> listBaseModel) {
                if (listBaseModel != null && !listBaseModel.isError() && listBaseModel.getData().size() > 0) {
                    event = listBaseModel.getData().get(0);
                    setEventData(listBaseModel.getData().get(0));
                    for (EventAttendees user : event.getAttendees()) {
                        invitedConnections.add(user.getUser());
                    }

                    if(invitedConnections.size()>0){
                        tv_interested.setVisibility(View.VISIBLE);
                    }else{
                        tv_interested.setVisibility(View.GONE);
                    }
                    usersAdapter.notifyDataSetChanged();
                    getEventComments(event_id);
                } else {
                    networkResponseDialog(getString(R.string.error), getString(R.string.err_unknown));
                }
                if (event.getCreated_by_id() == LoginUtils.getLoggedinUser().getId()) {
                    modify_event.setVisibility(View.VISIBLE);
                    interested_header.setVisibility(View.GONE);
                    interested.setVisibility(View.GONE);
                    save.setVisibility(View.GONE);
                    register.setVisibility(View.GONE);

                } else {
                    modify_event.setVisibility(View.GONE);
                    if (event.getIs_attending() != null && event.getIs_attending().equalsIgnoreCase("Pending")) {
                        accept_invite.setVisibility(View.VISIBLE);
                        interested.setVisibility(View.GONE);
                    } else if (event.getIs_attending() != null && !event.getIs_attending().equalsIgnoreCase("Pending")) {
                        accept_invite.setVisibility(View.GONE);
                        interested.setVisibility(View.VISIBLE);
                    } else {
                        accept_invite.setVisibility(View.GONE);
                        interested.setVisibility(View.VISIBLE);
                        save.setVisibility(View.VISIBLE);
                    }
                }
            }

            private void setEventData(Event event) {
                tv_name.setText(event.getName());

                if (event.getEvent_image().size() > 0) {
                    AppUtils.setGlideImageUrl(getContext(), img_event, event.getEvent_image().get(0));
                } else {
                    img_event.setBackground(getContext().getDrawable(R.drawable.no_thumbnail));
                }
                tv_content.setText(event.getContent());

                tv_date.setText(DateUtils.getFormattedDateDMY(event.getStart_date()) + " - " + DateUtils.getFormattedDateDMY(event.getEnd_date()) + " | " + event.getStart_time() + " - " + event.getEnd_time());

                tv_location.setText(event.getAddress());
                if (event.getIs_private() == 0) {
                    tv_event_type.setText("Open to the public");
                } else {
                    tv_event_type.setText("Private to the public");
                }

                tv_likecount.setText(String.valueOf(event.getLike_count()));
                tv_comcount.setText(String.valueOf(event.getComment_count()));
                if (event.getIs_event_like() != null && event.getIs_event_like() > 0) {
                    img_like.setBackground(getContext().getDrawable(R.drawable.like_selected));
                } else {
                    img_like.setBackground(getContext().getDrawable(R.drawable.ic_like));
                }
            }
        });
    }

    private void updateAttendance(Event event) {
        event.setEvent_id(event_id);
        event.setModified_by_id(LoginUtils.getLoggedinUser().getId());
        event.setModified_datetime(DateUtils.GetCurrentdatetime());
        event.setStatus(AppConstants.ACTIVE);
        event.setUser_id(LoginUtils.getLoggedinUser().getId());
        event.setAttendance_status("Going");

        eventViewModel.updateEventAttendance(event).observe(this, new Observer<BaseModel<List<Event>>>() {
            @Override
            public void onChanged(BaseModel<List<Event>> listBaseModel) {
                if (listBaseModel != null && listBaseModel.getData() != null) {
                    accept_invite.setVisibility(View.GONE);
                    interested.setVisibility(View.VISIBLE);
                    invitedConnections.add(LoginUtils.getLoggedinUser());
                    usersAdapter.notifyDataSetChanged();
                } else {
                    networkResponseDialog(getString(R.string.error), getString(R.string.err_unknown));
                }
            }
        });
    }

    private void addAttendance(Event event) {
        event.setEvent_id(event_id);
        event.setModified_by_id(LoginUtils.getLoggedinUser().getId());
        event.setModified_datetime(DateUtils.GetCurrentdatetime());
        event.setStatus(AppConstants.ACTIVE);
        event.setUser_id(LoginUtils.getLoggedinUser().getId());
        event.setAttendance_status("Going");
        eventViewModel.addEventAttendance(event).observe(this, new Observer<BaseModel<List<Event>>>() {
            @Override
            public void onChanged(BaseModel<List<Event>> listBaseModel) {
                if (listBaseModel != null && listBaseModel.getData() != null) {
                    accept_invite.setVisibility(View.GONE);
                    interested.setVisibility(View.VISIBLE);
                    invitedConnections.add(LoginUtils.getLoggedinUser());
                    usersAdapter.notifyDataSetChanged();
                } else {
                    networkResponseDialog(getString(R.string.error), getString(R.string.err_unknown));
                }
            }
        });
    }

    @OnClick(R.id.btn_addcomment)
    public void addComment() {
        validator.validate();
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
//                        if(comments.size()>0)
//                            rc_comments.smoothScrollToPosition(comments.size() - 1);
                    getEventComments(event_id);
                } else {
                    networkResponseDialog(getString(R.string.error), getString(R.string.err_unknown));
                }
                hideDialog();
            }
        });
    }

    @OnClick(R.id.modify_event)
    public void modifyevent() {
        CreateEventFragment createEventFragment = new CreateEventFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("event", event);
        createEventFragment.setArguments(bundle);
        loadFragment(R.id.framelayout, createEventFragment, getContext(), true);
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


    @Override
    public void onEditComment(View view, int position, String comment) {
        layout_editcomment.setVisibility(View.VISIBLE);
        btn_addcomment.setVisibility(View.GONE);
        edt_comment.setText(comment);
        comment_id = comments.get(position).getId();
    }

    @Override
    public void onDeleteComment(View view, int position) {
        eventViewModel.deleteComment(comments.get(position).getId()).observe(this, new Observer<BaseModel<List<Comment>>>() {
            @Override
            public void onChanged(BaseModel<List<Comment>> listBaseModel) {
                comments.remove(position);
                commentsAdapter.notifyDataSetChanged();
            }
        });
    }

    @OnClick(R.id.save)
    public void save() {
       save.setBackground(getResources().getDrawable(R.drawable.ic_star_selected));
    }

    @OnClick(R.id.button_cancel)
    public void cancel() {
        layout_editcomment.setVisibility(View.GONE);
        btn_addcomment.setVisibility(View.VISIBLE);
        edt_comment.setText("");
    }

    @OnClick(R.id.button_save)
    public void saveComment() {
        Comment newcomment = new Comment();
        newcomment.setId(comment_id);
        newcomment.setContent(edt_comment.getText().toString());
        newcomment.setModified_by_id(LoginUtils.getLoggedinUser().getId());
        newcomment.setModified_datetime(DateUtils.GetCurrentdatetime());

        eventViewModel.editComment(newcomment).observe(this, new Observer<BaseModel<List<Comment>>>() {
            @Override
            public void onChanged(BaseModel<List<Comment>> listBaseModel) {
                if (NetworkUtils.isNetworkConnected(getContext())) {
                    getEventDetails(event_id, user_id);
                    getEventComments(event_id);
                } else {
                    networkErrorDialog();
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.img_backarrow){
            getActivity().onBackPressed();
        }
    }
}
