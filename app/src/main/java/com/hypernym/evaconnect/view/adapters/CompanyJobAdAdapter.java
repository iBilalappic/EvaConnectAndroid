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
import com.hypernym.evaconnect.models.CompanyJobAdModel;
import com.hypernym.evaconnect.models.JobAd;
import com.hypernym.evaconnect.utils.AppUtils;
import com.hypernym.evaconnect.utils.DateUtils;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CompanyJobAdAdapter extends RecyclerView.Adapter<CompanyJobAdAdapter.ViewHolder> {
    private Context context;
    private List<CompanyJobAdModel> companyJobAdModelList = new ArrayList<>();
    private JobAdAdapter.OnItemClickListener onItemClickListener;

    public CompanyJobAdAdapter(Context context, List<CompanyJobAdModel> companyJobAdModelList, JobAdAdapter.OnItemClickListener itemClickListener) {
        this.context = context;
        this.companyJobAdModelList = companyJobAdModelList;
        this.onItemClickListener = itemClickListener;

    }

    @NonNull
    @Override
    public CompanyJobAdAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_companyjoblisting, parent, false);
        return new CompanyJobAdAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CompanyJobAdAdapter.ViewHolder holder, int position) {

        holder.tv_name.setText(companyJobAdModelList.get(position).getJobTitle());
        holder.tv_postion.setText(companyJobAdModelList.get(position).getPosition());
        holder.tv_date.setText(DateUtils.getFormattedDate(companyJobAdModelList.get(position).getCreatedDatetime()));
        holder.tv_totalapplicant.setText("12 Applicants");
        AppUtils.setGlideImage(context, (holder).profile_image, companyJobAdModelList.get(position).getJobImage());

    }

    @Override
    public int getItemCount() {
        return companyJobAdModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tv_name, tv_postion, tv_date, tv_totalapplicant;
        CircleImageView profile_image;
//        ImageView img_like, img_comment, img_share;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_postion = itemView.findViewById(R.id.tv_connections);
            tv_date = itemView.findViewById(R.id.tv_date);
            tv_totalapplicant = itemView.findViewById(R.id.tv_totalapplicant);
//            tv_salary = itemView.findViewById(R.id.tv_salary);
//            tv_gotoadd = itemView.findViewById(R.id.tv_gotoadd);
            profile_image = itemView.findViewById(R.id.profile_image);
//            img_like = itemView.findViewById(R.id.img_like);
//            img_comment = itemView.findViewById(R.id.img_comment);
//            img_share = itemView.findViewById(R.id.img_share);
//            itemView.setOnClickListener(this);
//            img_like.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
//            switch (v.getId()) {
//                case R.id.img_like:
//                    onItemClickListener.onItemClick(v, getAdapterPosition());
//                    break;
//            }

        }
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }
    // parent activity will implement this method to respond to click events

}
