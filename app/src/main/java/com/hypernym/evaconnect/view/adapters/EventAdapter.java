package com.hypernym.evaconnect.view.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hypernym.evaconnect.R;
import com.hypernym.evaconnect.models.CalendarModel;
import com.hypernym.evaconnect.models.Event;
import com.hypernym.evaconnect.models.EventAttendees;
import com.hypernym.evaconnect.utils.DateUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.ViewHolder> {
    private Context context;
    private List<CalendarModel> events;

    public EventAdapter(Context context, List<CalendarModel> events)
    {
        this.context=context;
        this.events=events;
    }

    @NonNull
    @Override
    public EventAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.item_event,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if(events.get(position).getObject_type().equalsIgnoreCase("interview"))
        {
            holder.tv_title.setText("Job Interview at "+events.get(position).getObject_details().getCompany_name()+" - "+events.get(position).getObject_details().getPosition()
                    +" | "+ events.get(position).getObject_details().getAddress());
            holder.tv_datetime.setText(DateUtils.getFormattedTime(events.get(position).getObject_details().getInterview_time()));
            holder.tv_title.setCompoundDrawablesWithIntrinsicBounds( R.drawable.redcircle, 0, 0, 0);
            holder.tv_datetime.setVisibility(View.VISIBLE);
        }
        else if(events.get(position).getObject_type().equalsIgnoreCase("event"))
        {
            holder.tv_title.setText(events.get(position).getObject_details().getEvent_name()+" | "+ events.get(position).getObject_details().getEvent_city());
            holder.tv_datetime.setText(DateUtils.getFormattedTime(events.get(position).getObject_details().getStart_time())+" - "+DateUtils.getFormattedTime(events.get(position).getObject_details().getEnd_time()));
            holder.tv_title.setCompoundDrawablesWithIntrinsicBounds( R.drawable.blackcircle, 0, 0, 0);
            holder.tv_datetime.setVisibility(View.VISIBLE);
        }
        else if(events.get(position).getObject_type().equalsIgnoreCase("note"))
        {
            holder.tv_title.setText(events.get(position).getNotes());
            holder.tv_datetime.setVisibility(View.GONE);
            holder.tv_title.setCompoundDrawablesWithIntrinsicBounds( R.drawable.bluecircle, 0, 0, 0);
        }
    }


    @Override
    public int getItemCount() {
        return events.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder  {
        @BindView(R.id.tv_title)
        TextView tv_title;

        @BindView(R.id.tv_datetime)
        TextView tv_datetime;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }

    }

}
