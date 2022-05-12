package com.hypernym.evaconnect.view.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hypernym.evaconnect.R;
import com.hypernym.evaconnect.models.AppliedApplicants;
import com.hypernym.evaconnect.utils.AppUtils;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AppliedApplicantAdapter extends RecyclerView.Adapter<AppliedApplicantAdapter.ViewHolder> {
    private Context context;
    private List<AppliedApplicants> appliedApplicantModels = new ArrayList<>();
    private AppliedApplicantAdapter.OnItemClickListener onItemClickListener;

    public AppliedApplicantAdapter(Context context, List<AppliedApplicants> appliedApplicantModels, AppliedApplicantAdapter.OnItemClickListener itemClickListener) {
        this.context = context;
        this.appliedApplicantModels = appliedApplicantModels;
        this.onItemClickListener = itemClickListener;

    }

    @NonNull
    @Override
    public AppliedApplicantAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_applicant, parent, false);
        return new AppliedApplicantAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AppliedApplicantAdapter.ViewHolder holder, int position) {

//        if (appliedApplicantModels.get(position).getUser().getIs_linkedin() == 1) {
//            AppUtils.setGlideImage(context, (holder).mProfileImage, appliedApplicantModels.get(position).getUser().getLinkedin_image_url());
//        } else {
            AppUtils.setGlideImage(context, (holder).mProfileImage, appliedApplicantModels.get(position).getUser().getUserImage());
//        }
        //  holder.tv_description.setText(appliedApplicantModels.get(position).getContent());
        holder.tv_name.setText(appliedApplicantModels.get(position).getUser().getFirstName());
        holder.tv_date.setText(appliedApplicantModels.get(position).getCreatedDatetime());
        //    holder.tv_activefor.setText("Submitted "+DateUtils.getTimeAgo(appliedApplicantModels.get(position).getCreatedDatetime()));
        if (appliedApplicantModels.get(position).getIs_hidden() == 1) {
            holder.tv_view.setText("Hidden");
            holder.tv_view.setTextColor(context.getResources().getColor(R.color.unselected_grey));
            holder.tv_name.setTextColor(context.getResources().getColor(R.color.lite_black));
            holder.tv_view.setBackgroundResource(R.drawable.rounded_button_border_grey);
        } else {
            holder.tv_view.setText("View");
            holder.tv_view.setTextColor(context.getResources().getColor(R.color.skyblue));
            holder.tv_name.setTextColor(context.getResources().getColor(com.skydoves.powermenu.R.color.black));
            holder.tv_view.setBackgroundResource(R.drawable.rounded_button_border_blue);
        }
    }

    @Override
    public int getItemCount() {
        return appliedApplicantModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        CircleImageView mProfileImage;
        TextView tv_name, tv_date, tv_view;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mProfileImage = itemView.findViewById(R.id.profile_image);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_date = itemView.findViewById(R.id.tv_date);
            tv_view = itemView.findViewById(R.id.tv_view);
            // tv_description = itemView.findViewById(R.id.tv_description);
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
    // parent activity will implement this method to respond to click events

}
