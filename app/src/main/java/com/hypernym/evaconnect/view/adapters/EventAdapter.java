package com.hypernym.evaconnect.view.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
    private OnItemClickListener onItemClickListener;

    public EventAdapter(Context context, List<CalendarModel> events,  OnItemClickListener itemClickListener)
    {
        this.context=context;
        this.events=events;
        this.onItemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public EventAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.item_event,parent,false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        try {
            /*if ( position == 0) {
              holder.divider.setVisibility(View.GONE);
            } else {
                holder.divider.setVisibility(View.VISIBLE);
            }*/

            if (events.get(position).getObject_type().equalsIgnoreCase("interview")) {
                if (events.get(position).getObject_details() != null) {
                    holder.itemView.setVisibility(View.VISIBLE);

                    holder.eventTitle.setText("Job Interview at " + events.get(position).getObject_details().getCompany_name() + " - " + events.get(position).getObject_details().getPosition());
                            /*+ " | " + events.get(position).getObject_details().getAddress());*/
                    holder.time.setText(DateUtils.getFormattedEventTime(events.get(position).getObject_details().getInterview_time()));
                    if (events.get(position).getObject_details().getStart_date() != null)
                        holder.month.setText(DateUtils.extractMonth(events.get(position).getObject_details().getStart_date()));
                    holder.type.setText(events.get(position).getObject_type());
                    holder.type.setTextColor(context.getResources().getColor(R.color.red_2));
                    setDayTextWithSuperScript(holder, position);
                }

            } else if (events.get(position).getObject_type().equalsIgnoreCase("event")) {
                holder.itemView.setVisibility(View.VISIBLE);
                if (events.get(position).getObject_details() != null) {
                    if (events.get(position).getObject_details().getName() != null) {
                        holder.eventTitle.setText(events.get(position).getObject_details().getName() /*+ " | " + events.get(position).getObject_details().getAddress()*/);
                    }

                    //  holder.time.setText(DateUtils.get12formant(events.get(position).getObject_details().getStart_time())+" - "+DateUtils.get12formant(events.get(position).getObject_details().getEnd_time()));
                    holder.time.setText(DateUtils.getTimeUTC(events.get(position).getObject_details().getStart_time()) + " - " + DateUtils.getTimeUTC(events.get(position).getObject_details().getEnd_time()));
                    if (events.get(position).getObject_details().getStart_date() != null)
                        holder.month.setText(DateUtils.extractMonth(events.get(position).getObject_details().getStart_date()));
                    holder.type.setText(events.get(position).getObject_type());
                    holder.type.setTextColor(context.getResources().getColor(R.color.red_2));
                    setDayTextWithSuperScript(holder, position);
                }


            } else if (events.get(position).getObject_type().equalsIgnoreCase("meeting")) {
                if (events.get(position).getObject_details() != null) {
                    holder.itemView.setVisibility(View.VISIBLE);
                    holder.eventTitle.setText(events.get(position).getObject_details().getName() /*+ " | " + events.get(position).getObject_details().getAddress()*/);
                    //   holder.time.setText(DateUtils.get12formant(events.get(position).getObject_details().getStart_time()) + " - " + DateUtils.get12formant(events.get(position).getObject_details().getEnd_time()));
                    holder.time.setText(DateUtils.getTimeUTC(events.get(position).getObject_details().getStart_time()) + " - " + DateUtils.getTimeUTC(events.get(position).getObject_details().getEnd_time()));
                    if (events.get(position).getObject_details().getStart_date() != null)
                        holder.month.setText(DateUtils.extractMonth(events.get(position).getObject_details().getStart_date()));
                    holder.type.setText(events.get(position).getObject_type());
                    holder.type.setTextColor(context.getResources().getColor(R.color.calendar_meetings));

                    setDayTextWithSuperScript(holder, position);
                }
            } else if (events.get(position).getObject_type().equalsIgnoreCase("note")) {
                holder.itemView.setVisibility(View.VISIBLE);


                if (events.get(position).getNotes() != null) {
                    holder.eventTitle.setText(events.get(position).getObject_details().getTitle() /*+ " | " + events.get(position).getObject_details().getDetails()*/);
                }

                holder.time.setText(DateUtils.getTimeUTC(events.get(position).getObject_details().getOccurrence_time()));
//                    if (events.get(position).getObject_details().getStart_date() != null)
                holder.month.setText(DateUtils.extractMonth(events.get(position).getOccurrence_date()));
                holder.type.setText(events.get(position).getObject_type());
                holder.type.setTextColor(context.getResources().getColor(R.color.light_blue));
                setDayTextWithSuperScript(holder, position);

            } else {
                holder.itemView.setVisibility(View.GONE);
            }
        } catch (Exception ex) {
            ex.getLocalizedMessage();

        }

    }

    private void setDayTextWithSuperScript(ViewHolder holder, int position) {
        String date = "";

        if (events.get(position).getObject_type().equalsIgnoreCase("event"))
        {
            date = DateUtils.extractDay(events.get(position).getObject_details().getStart_date());
        }
        else  if (events.get(position).getObject_type().equalsIgnoreCase("note"))
        {
            date = DateUtils.extractDay(events.get(position).getOccurrence_date());
        }
        else{
            date = DateUtils.extractDay(events.get(position).getObject_details().getStart_date());
        }

        String superscript = "";

        if (date.equals("1")|| date.equals("01") || date.equals("21") || date.equals("31"))
            superscript = "st";
        else if (date.equals("2") ||date.equals("02") || date.equals("22"))
            superscript = "nd";
        else if (date.equals("3") || date.equals("03") || date.equals("23"))
            superscript = "rd";
        else
            superscript = "th";

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            holder.day.setText(Html.fromHtml(date + "<small><sup>" + superscript + "</sup></small>", Html.FROM_HTML_MODE_LEGACY));
        }
        else {
            holder.day.setText(Html.fromHtml(date + "<small><sup>" + superscript + "</sup></small>"));
        }
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener  {
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

        @BindView(R.id.divider)
        ImageView divider;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onItemClickListener.onItemClick(v, getAdapterPosition());
        }
    }
    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }
}
