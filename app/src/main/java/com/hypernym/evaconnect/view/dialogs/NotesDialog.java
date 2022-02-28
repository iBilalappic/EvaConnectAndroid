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
import com.hypernym.evaconnect.view.ui.fragments.CreateNoteFragment;
import com.hypernym.evaconnect.view.ui.fragments.MeetingDetailFragment;

public class NotesDialog  extends Dialog {
    private Context context;
    private CalendarModel event;
    private TextView tv_title,tv_location,tv_createdDate,tv_description;
    private TextView btn_viewEvent;
    private ImageView img_close;

    public NotesDialog(CalendarModel event, Context context) {
        super(context);
        this.context = context;
        this.event=event;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_notes);
        tv_title=findViewById(R.id.tv_title);
        tv_createdDate=findViewById(R.id.tv_createdDate);
        tv_description=findViewById(R.id.tv_description);
        img_close = findViewById(R.id.img_close);
        tv_location=findViewById(R.id.tv_createdLocation);
        btn_viewEvent=findViewById(R.id.btn_viewEvent);
        setCanceledOnTouchOutside(true);
        setCancelable(true);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        tv_title.setText(event.getObject_details().getTitle());
        tv_description.setText(event.getObject_details().getDetails());
        tv_createdDate.setText(DateUtils.getFormattedDateDMY(event.getOccurrence_date()));
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
                CreateNoteFragment notesDetailFragment=new CreateNoteFragment();
                Bundle bundle=new Bundle();
                bundle.putInt("id",event.getObject_id());
                notesDetailFragment.setArguments(bundle);
                transaction.replace(R.id.framelayout,notesDetailFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

    }
}
