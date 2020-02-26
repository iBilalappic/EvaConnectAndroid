package com.hypernym.evaconnect.view.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hypernym.evaconnect.R;
import com.hypernym.evaconnect.models.AppliedApplicants;
import com.hypernym.evaconnect.models.MyLikesModel;
import com.hypernym.evaconnect.utils.AppUtils;
import com.hypernym.evaconnect.utils.DateUtils;
import com.hypernym.evaconnect.viewmodel.AppliedApplicantViewModel;

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
        AppUtils.setGlideImage(context, (holder).mProfileImage, appliedApplicantModels.get(position).getUser().getUserImage());
        holder.tv_description.setText(appliedApplicantModels.get(position).getContent());
        holder.tv_name.setText(appliedApplicantModels.get(position).getUser().getFirstName());
        holder.tv_activefor.setText(DateUtils.getTimeAgo(appliedApplicantModels.get(position).getCreatedDatetime()));

    }

    @Override
    public int getItemCount() {
        return appliedApplicantModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        CircleImageView mProfileImage;
        TextView tv_name, tv_activefor, tv_description;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mProfileImage = itemView.findViewById(R.id.profile_image);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_activefor = itemView.findViewById(R.id.tv_activefor);
            tv_description = itemView.findViewById(R.id.tv_description);
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