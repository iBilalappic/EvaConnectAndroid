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
            holder.itemView.setVisibility(View.VISIBLE);

            holder.eventTitle.setText("Job Interview at "+events.get(position).getObject_details().getCompany_name()+" - "+events.get(position).getObject_details().getPosition()
                    +" | "+ events.get(position).getObject_details().getAddress());
            holder.time.setText(DateUtils.getFormattedEventTime(events.get(position).getObject_details().getInterview_time()));
            if(events.get(position).getObject_details().getEvent_start_date()!=null)
              holder.month.setText(DateUtils.extractMonth(events.get(position).getObject_details().getEvent_start_date()));
            holder.type.setText(events.get(position).getObject_type());
            holder.type.setTextColor(context.getResources().getColor(R.color.red_2));

            holder.day.setText(DateUtils.extractDay(events.get(position).getObject_details().getEvent_start_date()));
        }
        else if(events.get(position).getObject_type().equalsIgnoreCase("event"))
        {
            holder.itemView.setVisibility(View.VISIBLE);

            holder.eventTitle.setText(events.get(position).getObject_details().getEvent_name() + " | " + events.get(position).getObject_details().getEvent_city());
            holder.time.setText(DateUtils.get12formant(events.get(position).getObject_details().getStart_time())+" - "+DateUtils.get12formant(events.get(position).getObject_details().getEnd_time()));
            holder.month.setText(DateUtils.extractMonth(events.get(position).getObject_details().getEvent_start_date()));
            holder.type.setText(events.get(position).getObject_type());
            holder.type.setTextColor(context.getResources().getColor(R.color.red_2));

            holder.day.setText(DateUtils.extractDay(events.get(position).getObject_details().getEvent_start_date()));
        }
        else if(events.get(position).getObject_type().equalsIgnoreCase("meeting"))
        {
            holder.itemView.setVisibility(View.VISIBLE);

            holder.eventTitle.setText(events.get(position).getObject_details().getEvent_name() + " | " + events.get(position).getObject_details().getEvent_city());
            holder.time.setText(DateUtils.get12formant(events.get(position).getObject_details().getStart_time())+" - "+DateUtils.get12formant(events.get(position).getObject_details().getEnd_time()));
            holder.month.setText(DateUtils.extractMonth(events.get(position).getObject_details().getEvent_start_date()));
            holder.type.setText(events.get(position).getObject_type());
            holder.type.setTextColor(context.getResources().getColor(R.color.calendar_meetings));

            holder.day.setText(DateUtils.extractDay(events.get(position).getObject_details().getEvent_start_date()));
        }
        else{
            holder.itemView.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder  {
        @BindView(R.id.day)
        TextView day;

        @BindView(R.id.month)
        TextView month;

        @BindView(R.id.eventTitle)
        TextView eventTitle;

        @BindView(R.id.object_type)
        TextView type;

        @BindView(R.id.time)
        TextView time;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }

    }
}
