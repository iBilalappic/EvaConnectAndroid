package com.hypernym.evaconnect.view.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.hypernym.evaconnect.R;
import com.hypernym.evaconnect.models.CompanyJobAdModel;
import com.hypernym.evaconnect.utils.AppUtils;

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
        holder.tv_sector.setText(companyJobAdModelList.get(position).getJobSector());
        holder.tv_content.setText(companyJobAdModelList.get(position).getContent());
        holder.tv_activehour.setText("Active for "+String.valueOf(companyJobAdModelList.get(position).getActive_hours()+" hrs"));
     //   holder.tv_date.setText(DateUtils.getFormattedDate(companyJobAdModelList.get(position).getCreatedDatetime()));
        if (companyJobAdModelList.get(position).getApplicant_count() != null) {
            holder.tv_totalapplicant.setText(String.valueOf(companyJobAdModelList.get(position).getApplicant_count() + " Applicants"));
        } else {
            holder.tv_totalapplicant.setText("0 Applicants");
        }
        AppUtils.setGlideImage(context, (holder).profile_image, companyJobAdModelList.get(position).getJobImage());
    }

    @Override
    public int getItemCount() {
        return companyJobAdModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tv_name, tv_content, tv_edit, tv_totalapplicant,tv_sector,tv_activehour;
        CircleImageView profile_image;
        ConstraintLayout contraintlayout;
//        ImageView img_like, img_comment, img_share;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_sector = itemView.findViewById(R.id.tv_sector);
            tv_content = itemView.findViewById(R.id.tv_content);
            tv_edit = itemView.findViewById(R.id.tv_edit);
            tv_totalapplicant = itemView.findViewById(R.id.tv_totalapplicant);
            profile_image = itemView.findViewById(R.id.profile_image);
            tv_activehour = itemView.findViewById(R.id.tv_activehour);
            contraintlayout = itemView.findViewById(R.id.contraintlayout);
            itemView.setOnClickListener(this);
            tv_edit.setOnClickListener(this);
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
