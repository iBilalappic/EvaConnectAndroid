package com.hypernym.evaconnect.view.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hypernym.evaconnect.R;
import com.hypernym.evaconnect.models.EventAttendees;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EventAttendeesAdapter extends RecyclerView.Adapter<EventAttendeesAdapter.ViewHolder> {
    private Context context;
    private List<EventAttendees> eventAttendees;
    private EventAttendeesAdapter.ItemClickListener mClickListener;

    public EventAttendeesAdapter(Context context, List<EventAttendees> eventAttendees,EventAttendeesAdapter.ItemClickListener mClickListener)
    {
        this.context=context;
        this.eventAttendees=eventAttendees;
        this.mClickListener=mClickListener;
    }
    @NonNull
    @Override
    public EventAttendeesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.item_event_attendee,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventAttendeesAdapter.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return eventAttendees.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.tv_name)
        TextView tv_name;

        @BindView(R.id.tv_count)
        TextView tv_count;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            tv_name.setOnClickListener(this);
        }
        @Override
        public void onClick(View v) {
            if (mClickListener != null) mClickListener.onItemClick(v, getAdapterPosition());
        }
    }
    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
