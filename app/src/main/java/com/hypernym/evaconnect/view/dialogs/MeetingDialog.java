package com.hypernym.evaconnect.view.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.hypernym.evaconnect.R;
import com.hypernym.evaconnect.models.CalendarModel;
import com.hypernym.evaconnect.utils.DateUtils;
import com.hypernym.evaconnect.view.ui.fragments.MeetingDetailFragment;

public class MeetingDialog extends Dialog {
    private Context context;
    private CalendarModel event;
    private TextView tv_title,tv_location,tv_createdDate,tv_description;
    private TextView btn_viewEvent;
    private ImageView img_close;

    public MeetingDialog(CalendarModel event, Context context) {
        super(context);
        this.context = context;
        this.event=event;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_meeting);
        tv_title=findViewById(R.id.tv_title);
        tv_createdDate=findViewById(R.id.tv_createdDate);
        tv_description=findViewById(R.id.tv_description);
        img_close = findViewById(R.id.img_close);
        tv_location=findViewById(R.id.tv_createdLocation);
        btn_viewEvent=findViewById(R.id.btn_viewEvent);
        setCanceledOnTouchOutside(true);
        setCancelable(true);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        tv_title.setText(event.getObject_details().getName());
        tv_location.setText(event.getObject_details().getAddress());
        tv_description.setText(event.getObject_details().getContent());
        tv_createdDate.setText(DateUtils.getFormattedDateDMY(event.getObject_details().getStart_date()));
        img_close.setOnClickListener(v -> dismiss());

        btn_viewEvent.setOnClickListener(v -> {
            dismiss();
            FragmentTransaction transaction = ((AppCompatActivity) context).getSupportFragmentManager().beginTransaction();
            transaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
            MeetingDetailFragment meetingDetailFragment = new MeetingDetailFragment();
            Bundle bundle = new Bundle();
            bundle.putInt("id", event.getObject_id());
            meetingDetailFragment.setArguments(bundle);
            transaction.replace(R.id.framelayout, meetingDetailFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        });

    }
}
