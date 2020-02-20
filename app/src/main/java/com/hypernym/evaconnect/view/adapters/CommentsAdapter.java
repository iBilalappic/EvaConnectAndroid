package com.hypernym.evaconnect.view.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.hypernym.evaconnect.R;
import com.hypernym.evaconnect.models.Comment;
import com.hypernym.evaconnect.utils.DateUtils;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.ViewHolder> {
    private Context context;
    private List<Comment> comments=new ArrayList<>();

    public CommentsAdapter(Context context, List<Comment> commentList)
    {
        this.context=context;
        this.comments= commentList;
    }

    @NonNull
    @Override
    public CommentsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.item_comment,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentsAdapter.ViewHolder holder, int position) {
        Glide.with(context) //1
                .load(comments.get(position).getUser().getUser_image())
                .placeholder(R.drawable.ic_default)
                .error(R.mipmap.ic_launcher)
                .skipMemoryCache(true) //2
                .apply(RequestOptions.circleCropTransform())
                .diskCacheStrategy(DiskCacheStrategy.NONE) //3
                .into(holder.profile_image);
        holder.tv_name.setText(comments.get(position).getUser().getFirst_name());
        holder.tv_content.setText(comments.get(position).getContent());
        holder.tv_date.setText(DateUtils.getFormattedDateTime(comments.get(position).getCreated_datetime()));
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.profile_image)
        ImageView profile_image;

        @BindView(R.id.tv_name)
        TextView tv_name;

        @BindView(R.id.tv_content)
        TextView tv_content;

        @BindView(R.id.tv_date)
        TextView tv_date;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }

        @Override
        public void onClick(View v) {

        }
    }
}
