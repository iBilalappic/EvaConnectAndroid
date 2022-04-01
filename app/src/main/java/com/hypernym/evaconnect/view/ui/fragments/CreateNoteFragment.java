package com.hypernym.evaconnect.view.ui.fragments;

import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.hypernym.evaconnect.R;
import com.hypernym.evaconnect.dateTimePicker.DateTime;
import com.hypernym.evaconnect.dateTimePicker.DateTimePicker;
import com.hypernym.evaconnect.dateTimePicker.SimpleDateTimePicker;
import com.hypernym.evaconnect.models.BaseModel;
import com.hypernym.evaconnect.models.NotesData;
import com.hypernym.evaconnect.repositories.CustomViewModelFactory;
import com.hypernym.evaconnect.utils.DateUtils;
import com.hypernym.evaconnect.viewmodel.NotesViewModel;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class CreateNoteFragment extends BaseFragment implements View.OnClickListener, DateTimePicker.OnDateTimeSetListener,Validator.ValidationListener {

    @BindView(R.id.img_backarrow)
    ImageView img_backarrow;


    @BindView(R.id.tv_startdate)
    EditText tv_startdate;

    @BindView(R.id.tv_startTime)
    EditText tv_startTime;

    @NotEmpty
    @BindView(R.id.edt_eventname)
    EditText edt_eventname;

    @NotEmpty
    @BindView(R.id.edt_content)
    EditText edt_content;

    @BindView(R.id.tv_remind_me)
    TextView tv_remind_me;

    @BindView(R.id.post)
    TextView post;
    @BindView(R.id.tv_header_name)
    TextView tv_header_name;


    private Validator validator;

    private boolean isStartDate = false;
    private int notes_id = 0;
    private boolean setReminder = false;
    private Date startDate = new Date();
    private Date selectedDate = new Date();

    Date endselectDate;
    DateFormat time = new SimpleDateFormat("hh:mm a");
    DateFormat dateformat = new SimpleDateFormat("E, dd MMM yyyy");
    SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);

    private NotesViewModel notesViewModel;

    public CreateNoteFragment() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        init();
        validator = new Validator(this);
        validator.setValidationListener(this);

        img_backarrow.setOnClickListener(this);
        tv_remind_me.setOnClickListener(this);
        post.setOnClickListener(this);
    }

    private void init() {
        // setPageTitle("Create a Meeting Schedule");
        notesViewModel = ViewModelProviders.of(this, new CustomViewModelFactory(getActivity().getApplication(), getActivity())).get(NotesViewModel.class);

        tv_startdate.setInputType(InputType.TYPE_NULL);
        tv_startdate.setText(dateformat.format(new Date()));

        tv_startTime.setText(time.format(new Date()));
        tv_startTime.setInputType(InputType.TYPE_NULL);
        int note_id = getArguments().getInt("id");

        if(note_id!=0){
            tv_header_name.setText("Update a Note");
            post.setText("Update");
            getNotesDetail(note_id);
        }


       /* tv_enddate.setInputType(InputType.TYPE_NULL);
        tv_endTime.setInputType(InputType.TYPE_NULL);*/

    }

    private void getNotesDetail(int note_id) {
        notesViewModel.get_notes(note_id).observe(requireActivity(), new Observer<BaseModel<List<NotesData>>>() {
            @Override
            public void onChanged(BaseModel<List<NotesData>> viewdata) {
                if(viewdata!=null&&!viewdata.isError()){

                    hideDialog();
//                    tv_startdate.setText(dateformat.format(viewdata.getData().get(0).occurrenceDate));
//                    tv_startTime.setText(time.format(viewdata.getData().get(0).occurrenceTime));
                    edt_content.setText(viewdata.getData().get(0).details);
                    edt_eventname.setText(viewdata.getData().get(0).title);
                    notes_id=viewdata.getData().get(0).objectId;
                    if(viewdata.getData().get(0).isNotify){
                        setReminder = true;
                        tv_remind_me.setBackground(getResources().getDrawable(R.drawable.ic_blue_button));
                        tv_remind_me.setTextColor(getResources().getColor(com.skydoves.powermenu.R.color.white));
                    }else{
                        setReminder = false;
                        tv_remind_me.setBackground(getResources().getDrawable(R.drawable.ic_holo_blue_button));
                        tv_remind_me.setTextColor(getResources().getColor(R.color.skyblue));
                    }
                }
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_create_note, container, false);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.img_backarrow) {
            (getActivity()).onBackPressed();
        }
        else if(v.getId() == R.id.tv_remind_me){

            if(!setReminder) {
                setReminder = true;
                tv_remind_me.setBackground(getResources().getDrawable(R.drawable.ic_blue_button));
                tv_remind_me.setTextColor(getResources().getColor(com.skydoves.powermenu.R.color.white));
            }else{
                setReminder=false;
                tv_remind_me.setBackground(getResources().getDrawable(R.drawable.ic_holo_blue_button));
                tv_remind_me.setTextColor(getResources().getColor(R.color.skyblue));
            }
        }
        else if(v.getId()==R.id.post){
            validator.validate();

        }
    }

    private void callNotesApi() {
        notesViewModel.create_notes(DateUtils.getFormattedMeetingDate(tv_startdate.getText().toString()),DateUtils.getTime_utc(tv_startTime.getText().toString())
        ,edt_content.getText().toString(),edt_eventname.getText().toString(),setReminder).observe(this, new Observer<BaseModel<List<Object>>>() {
            @Override
            public void onChanged(BaseModel<List<Object>> viewdata) {
                if(!viewdata.isError()){
                    hideDialog();
                    getActivity().onBackPressed();
                }
            }
        });
    }

    @OnClick({R.id.tv_startdate, R.id.tv_startTime})
    public void setStartDate() {
        isStartDate = true;
        try {
            selectedDate = dateformat.parse(tv_startdate.getText().toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        startDate = new Date();
        showDateTimePicker("Set Start Date & Time");
    }

    private void showDateTimePicker(String title) {
        // Create a SimpleDateTimePicker and Show it

        SimpleDateTimePicker simpleDateTimePicker = SimpleDateTimePicker.make(
                title,
                selectedDate, startDate,
                this,
                getFragmentManager()
        );

        simpleDateTimePicker.show();
    }

    @Override
    public void DateTimeSet(Date date) throws ParseException {
        Date selectedDate = null;
        Date endDate = null;
        DateTime mDateTime = new DateTime(date);

        if (isStartDate) {
            tv_startdate.setText(dateformat.format(mDateTime.getDate()));
            tv_startTime.setText(time.format(mDateTime.getDate()));
            startDate = mDateTime.getDate();
            //tv_enddate.setText("");
            //tv_endTime.setText("");
            /*tv_enddate.setText(dateformat.format(mDateTime.getDate()));
            tv_endTime.setText(time.format(mDateTime.getDate()));*/
        }
        else{
            String str2 = mDateTime.getDate().toString();
            try {
                endDate = formatter.parse(str2);
                Log.d("TAAAG", "enddate:" + endDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        if (endDate != null && startDate!=null)
        {
            if (mDateTime.getDate().getMonth() == startDate.getMonth() && mDateTime.getDate().getDate() == startDate.getDate()) {

                if (endDate.compareTo(startDate) > 0) {
                 //   tv_enddate.setText(dateformat.format(mDateTime.getDate()));
                 //   tv_endTime.setText(time.format(mDateTime.getDate()));

                } else {
                    Toast.makeText(getContext(), "End time must be greater than start time", Toast.LENGTH_SHORT).show();
                }
            } else if (endDate.compareTo(startDate) > 0) {
               // tv_enddate.setText(dateformat.format(mDateTime.getDate()));
               // tv_endTime.setText(time.format(mDateTime.getDate()));
            } else {
              //  tv_enddate.setText(dateformat.format(mDateTime.getDate()));
              //  tv_endTime.setText(time.format(mDateTime.getDate()));
            }
        }

        Log.v("TEST_TAG", "Date and Time selected: " + mDateTime.getDateString());
    }

    @Override
    public void onValidationSucceeded() {
        showDialog();
        if(post.getText().toString().equalsIgnoreCase("Update")){
            callUpdateNoteApi();
        }else{
            callNotesApi();

        }
    }

    private void callUpdateNoteApi() {
        notesViewModel.update_notes(DateUtils.getFormattedMeetingDate(tv_startdate.getText().toString()),DateUtils.getTime_utc(tv_startTime.getText().toString())
                ,edt_content.getText().toString(),edt_eventname.getText().toString(),setReminder,notes_id).observe(this, new Observer<BaseModel<List<Object>>>() {
            @Override
            public void onChanged(BaseModel<List<Object>> viewdata) {
                if(!viewdata.isError()){
                    hideDialog();
                    getActivity().onBackPressed();
                }
            }
        });
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