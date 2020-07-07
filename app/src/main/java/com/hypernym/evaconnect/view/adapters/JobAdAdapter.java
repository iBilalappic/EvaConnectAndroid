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
import com.hypernym.evaconnect.models.JobAd;
import com.hypernym.evaconnect.models.MyLikesModel;
import com.hypernym.evaconnect.utils.AppUtils;
import com.hypernym.evaconnect.utils.DateUtils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class JobAdAdapter extends RecyclerView.Adapter<JobAdAdapter.ViewHolder> {
    private Context context;
    private List<JobAd> jobAdList = new ArrayList<>();
    private JobAdAdapter.OnItemClickListener onItemClickListener;

    public JobAdAdapter(Context context, List<JobAd> jobAdList, JobAdAdapter.OnItemClickListener itemClickListener) {
        this.context = context;
        this.jobAdList = jobAdList;
        this.onItemClickListener = itemClickListener;

    }

    @NonNull
    @Override
    public JobAdAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_joblisting, parent, false);
        return new JobAdAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull JobAdAdapter.ViewHolder holder, int position) {

        holder.tv_name.setText(jobAdList.get(position).getJobTitle());
        holder.tv_postion.setText(jobAdList.get(position).getPosition());
        holder.tv_content.setText(jobAdList.get(position).getContent());
        DecimalFormat myFormatter = new DecimalFormat("############");
        holder.tv_salary.setText("£ " + myFormatter.format(jobAdList.get(position).getSalary()) + " pa");
        AppUtils.setGlideImage(context, (holder).profile_image, jobAdList.get(position).getJobImage());

        if (jobAdList.get(position).getIs_job_like() != null && jobAdList.get(position).getIs_job_like() > 0) {
            holder.img_like.setBackground(context.getDrawable(R.drawable.like_selected));
        } else {
            holder.img_like.setBackground(context.getDrawable(R.drawable.ic_like));
        }

    }

    @Override
    public int getItemCount() {
        return jobAdList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tv_name, tv_postion, tv_salary, tv_gotoadd,tv_content;
        CircleImageView profile_image;
        ImageView img_like, img_comment, img_share;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_postion = itemView.findViewById(R.id.tv_connections);
            tv_salary = itemView.findViewById(R.id.tv_salary);
            tv_gotoadd = itemView.findViewById(R.id.tv_gotoadd);
            profile_image = itemView.findViewById(R.id.profile_image);
            img_like = itemView.findViewById(R.id.img_like);
            img_comment = itemView.findViewById(R.id.img_comment);
            img_share = itemView.findViewById(R.id.img_share);
            tv_content = itemView.findViewById(R.id.tv_content);
            itemView.setOnClickListener(this);
            img_like.setOnClickListener(this);
            img_share.setOnClickListener(this);
            tv_gotoadd.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.img_like:
                    onItemClickListener.onItemClick(v, getAdapterPosition());
                    break;
                case R.id.tv_gotoadd:
                    onItemClickListener.onItemClick(v, getAdapterPosition());
                    break;
                case R.id.img_share:
                    onItemClickListener.onItemClick(v, getAdapterPosition());
                    break;
            }

        }
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }
    // parent activity will implement this method to respond to click events

}
