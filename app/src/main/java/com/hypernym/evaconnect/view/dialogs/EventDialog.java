package com.hypernym.evaconnect.view.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.hypernym.evaconnect.R;
import com.hypernym.evaconnect.models.CalendarModel;
import com.hypernym.evaconnect.utils.DateUtils;
import com.hypernym.evaconnect.view.ui.fragments.EventDetailFragment;

public class EventDialog extends Dialog {
    private Context context;
    private CalendarModel event;
    private TextView tv_title,tv_createdby,tv_location,tv_createdDate,tv_description,tv_event_type;
    private TextView btn_viewEvent;
    private ImageView img_close;

    public EventDialog(CalendarModel event, Context context) {
        super(context);
        this.context = context;
        this.event=event;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_event);
        tv_title=findViewById(R.id.tv_title);
        tv_createdby=findViewById(R.id.tv_createdby);
        tv_createdDate=findViewById(R.id.tv_createdDate);
        tv_description=findViewById(R.id.tv_description);
        img_close = findViewById(R.id.img_close);
        tv_location=findViewById(R.id.tv_createdLocation);
        tv_event_type=findViewById(R.id.tv_event_type);
        btn_viewEvent=findViewById(R.id.btn_viewEvent);
        setCanceledOnTouchOutside(true);
        setCancelable(true);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        if (event.getObject_type().equalsIgnoreCase("event")) {
            tv_title.setText(event.getObject_details().getEvent_name());
            tv_location.setText(event.getObject_details().getEvent_city());
            tv_description.setText(event.getObject_details().getContent());
            tv_createdby.setText("Created By "+event.getObject_details().getUser().getFirst_name());
            tv_event_type.setVisibility(View.VISIBLE);
            if(event.getObject_details().getIs_private()==0)
            {
                tv_event_type.setText("Open to the public");
            }
            else
            {
                tv_event_type.setText("Private to the public");
            }
            tv_createdDate.setText(DateUtils.getFormattedDateDMY(event.getObject_details().getEvent_start_date()));

            btn_viewEvent.setText("View Event");
        }
        else if (event.getObject_type().equalsIgnoreCase("meeting"))
        {
            tv_title.setText(event.getObject_details().getSubject());
            tv_location.setText(event.getObject_details().getAddress());
            tv_description.setText(event.getObject_details().getNotes());
            tv_createdby.setText("Created By "+event.getObject_details().getUser().getFirst_name());
            tv_event_type.setVisibility(View.GONE);
            tv_createdDate.setText(DateUtils.getFormattedDateDMY(event.getObject_details().getStart_date()));

            btn_viewEvent.setText("View Meeting");
        }

        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        btn_viewEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                FragmentTransaction transaction =((AppCompatActivity)context).getSupportFragmentManager().beginTransaction();
                transaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
                EventDetailFragment eventDetailFragment=new EventDetailFragment();
                Bundle bundle=new Bundle();
                bundle.putInt("id",event.getObject_id());
                eventDetailFragment.setArguments(bundle);
                transaction.replace(R.id.framelayout,eventDetailFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

    }
}
