package com.hypernym.evaconnect.view.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hypernym.evaconnect.R;
import com.hypernym.evaconnect.models.Posts;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomePostsAdapter extends RecyclerView.Adapter<HomePostsAdapter.ViewHolder> {
    private Context context;
    private List<Posts> posts=new ArrayList<>();
    private HomePostsAdapter.ItemClickListener mClickListener;
    public HomePostsAdapter(Context context, List<Posts> postsList,HomePostsAdapter.ItemClickListener mClickListener)
    {
        this.context=context;
        this.posts=postsList;
        this.mClickListener=mClickListener;
    }
    @NonNull
    @Override
    public HomePostsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.item_post,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HomePostsAdapter.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        @BindView(R.id.tv_viewcomments)
        TextView tv_viewcomments;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            tv_viewcomments.setOnClickListener(this);
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
