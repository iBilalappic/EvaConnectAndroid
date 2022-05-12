package com.hypernym.evaconnect.view.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.hypernym.evaconnect.R;
import com.hypernym.evaconnect.models.MyActivitiesModel;
import com.hypernym.evaconnect.models.User;
import com.hypernym.evaconnect.utils.AppUtils;
import com.hypernym.evaconnect.utils.DateUtils;
import com.hypernym.evaconnect.utils.LoginUtils;
import com.hypernym.evaconnect.viewmodel.ConnectionViewModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MyActivitiesAdapter extends RecyclerView.Adapter<MyActivitiesAdapter.ViewHolder> {
    private Context context;
    private List<MyActivitiesModel> hActivitiesList;
    private OnItemClickListener onItemClickListener;
    private boolean isLoaderVisible = false;
    private ConnectionViewModel connectionViewModel;

    public MyActivitiesAdapter(Context context, List<MyActivitiesModel> notifications, OnItemClickListener itemClickListener) {
        this.context = context;
        this.hActivitiesList = notifications;
        this.onItemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public MyActivitiesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_activities, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyActivitiesAdapter.ViewHolder holder, int position) {
        User user = new User();
        user = LoginUtils.getLoggedinUser();
        AppUtils.setGlideImage(context, holder.profile_image, user.getUser_image());
        holder.tv_status.setText(hActivitiesList.get(position).getContent());

        Log.d("TAG", "onBindViewHolder: " + hActivitiesList.get(position).getCreatedDatetime());
        holder.tv_date.setText(DateUtils.formatToYesterdayOrToday(hActivitiesList.get(position).getCreatedDatetime()));
        if (hActivitiesList.get(position).getContent().contains("commented")) {
            holder.tv_visit.setVisibility(View.VISIBLE);
        } else {
            holder.tv_visit.setVisibility(View.GONE);
        }
        if (hActivitiesList.get(position).getObjectType().equalsIgnoreCase("connection")) {
            holder.tv_connect.setVisibility(View.GONE);
            holder.tv_visit.setVisibility(View.VISIBLE);

        } else {
            holder.tv_connect.setVisibility(View.GONE);
        }
        holder.itemView.setOnClickListener(v -> {
          /*  if(!hActivitiesList.get(position).getObject_type().equalsIgnoreCase("connection"))
            {*/
            onItemClickListener.onItemClick(v, position);
            //  }
        });

        holder.tv_connect.setOnClickListener(v -> onItemClickListener.onAcceptClick(v, position));
    }

    @Override
    public int getItemCount() {
        return hActivitiesList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.profile_image)
        ImageView profile_image;

        @BindView(R.id.tv_status)
        TextView tv_status;

        @BindView(R.id.tv_date)
        TextView tv_date;

        @BindView(R.id.linearLayout10)
        ConstraintLayout linearLayout10;

        @BindView(R.id.tv_connect)
        TextView tv_connect;


        @BindView(R.id.tv_visit)
        TextView tv_visit;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);


        }
//
//        @Override
//        public void onClick(View v) {
//            if(v.getId()==R.id.tv_connect)
//            onItemClickListener.onItemClick(v, getAdapterPosition());
//        }
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);

        void onAcceptClick(View view, int position);
    }

    public void removeLoading() {
        isLoaderVisible = false;
        int position = hActivitiesList.size() - 1;
        MyActivitiesModel item = getItem(position);
        if (item != null) {
            // hActivitiesList.remove(position);
            // notifyItemRemoved(position);
        }
    }

    MyActivitiesModel getItem(int position) {
        if (hActivitiesList.size() > 0)
            return hActivitiesList.get(position);
        else
            return null;
    }

    public void clear() {
        hActivitiesList.clear();
        notifyDataSetChanged();
    }


}
