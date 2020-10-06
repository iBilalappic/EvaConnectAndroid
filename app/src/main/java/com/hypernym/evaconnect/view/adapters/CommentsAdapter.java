package com.hypernym.evaconnect.view.adapters;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.hypernym.evaconnect.R;
import com.hypernym.evaconnect.listeners.OnOneOffClickListener;
import com.hypernym.evaconnect.models.Comment;
import com.hypernym.evaconnect.utils.DateUtils;
import com.hypernym.evaconnect.utils.LoginUtils;
import com.hypernym.evaconnect.view.dialogs.EditCommentDialog;
import com.hypernym.evaconnect.view.dialogs.SimpleDialog;
import com.hypernym.evaconnect.viewmodel.PostViewModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.ViewHolder> {
    private Context context;
    private List<Comment> comments=new ArrayList<>();
    EditCommentDialog editCommentDialog;
    PostViewModel postViewModel;
    private CommentsAdapter.OnItemClickListener onItemClickListener;
    SimpleDialog simpleDialog;


    public CommentsAdapter(Context context, List<Comment> commentList, CommentsAdapter.OnItemClickListener onItemClickListener)
    {
        this.context=context;
        this.comments= commentList;
        this.onItemClickListener=onItemClickListener;
    }

    @NonNull
    @Override
    public CommentsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.item_comment,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentsAdapter.ViewHolder holder, int position) {

        if (!TextUtils.isEmpty(comments.get(position).getUser().getUser_image()))
        {
            Glide.with(context) //1
                    .load(comments.get(position).getUser().getUser_image())
                    .placeholder(R.drawable.ic_default)
                    .error(R.mipmap.ic_launcher)
                    .skipMemoryCache(true) //2
                    .apply(RequestOptions.circleCropTransform())
                    .diskCacheStrategy(DiskCacheStrategy.NONE) //3
                    .into(holder.profile_image);
        }
//        else if (!TextUtils.isEmpty(comments.get(position).getUser().getUser_image()))
//        {
//            Glide.with(context) //1
//                    .load(comments.get(position).getUser().getUser_image())
//                    .placeholder(R.drawable.ic_default)
//                    .error(R.mipmap.ic_launcher)
//                    .skipMemoryCache(true) //2
//                    .apply(RequestOptions.circleCropTransform())
//                    .diskCacheStrategy(DiskCacheStrategy.NONE) //3
//                    .into(holder.profile_image);
//        }
//        else{
//            Glide.with(context) //1
//                    .load(comments.get(position).getUser().getUser_image())
//                    .placeholder(R.drawable.ic_default)
//                    .error(R.mipmap.ic_launcher)
//                    .skipMemoryCache(true) //2
//                    .apply(RequestOptions.circleCropTransform())
//                    .diskCacheStrategy(DiskCacheStrategy.NONE) //3
//                    .into(holder.profile_image);
//        }

        holder.tv_name.setText(comments.get(position).getUser().getFirst_name());
        holder.tv_content.setText(comments.get(position).getContent());
        holder.tv_date.setText(DateUtils.getFormattedDateTime(comments.get(position).getCreated_datetime()));

        if(comments.get(position).getUser().getId()==LoginUtils.getLoggedinUser().getId() || comments.get(position).isPostMine())
        {
            holder.more.setVisibility(View.VISIBLE);
        }
        else
        {
            holder.more.setVisibility(View.GONE);
        }

        holder.more.setOnClickListener(new OnOneOffClickListener() {
            @Override
            public void onSingleClick(View v) {
//                final OvershootInterpolator interpolator = new OvershootInterpolator();
//                ViewCompat.animate(holder.more).
//                        withLayer().rotation(0).
//                        setInterpolator(interpolator).
//                        start();
                /** Instantiating PopupMenu class */
                PopupMenu popup = new PopupMenu(context, v);

                /** Adding menu items to the popumenu */
                popup.getMenuInflater().inflate(R.menu.comment_menu, popup.getMenu());


                if(comments.get(position).getUser().getId()== LoginUtils.getLoggedinUser().getId())
                {
                    popup.getMenu().findItem(R.id.action_edit).setVisible(true);
                }
                else
                {
                    popup.getMenu().findItem(R.id.action_edit).setVisible(false);
                }
//                if(comments.get(position).isPostMine())
//                {
//                    holder.more.setVisibility(View.VISIBLE);
//                }
//                else
//                {
//                    holder.more.setVisibility(View.GONE);
//                }
                /** Defining menu item click listener for the popup menu */
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        //    Toast.makeText(getContext(), item.getGroupId()+"You selected the action : " + item.getTitle(), Toast.LENGTH_SHORT).show();
                        if (item.getTitle().toString().equalsIgnoreCase(context.getString(R.string.edit_comment))) {
                            onItemClickListener.onEditComment(v, position,comments.get(position).getContent());
//                            editCommentDialog=new EditCommentDialog(context,holder.tv_content.getText().toString(),context.getString(R.string.cancel),context.getString(R.string.button_save_changes), new View.OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
//                                    switch (v.getId()) {
//                                        case R.id.button_positive:
//                                            EditText comment=(EditText)editCommentDialog.findViewById(R.id.edt_comment);
//                                            onItemClickListener.onEditComment(v, position,comment.getText().toString());
//                                            break;
//                                        case R.id.button_negative:
//                                            break;
//                                    }
//                                    editCommentDialog.dismiss();
//                                }
//                            });
//                            editCommentDialog.show();

                        }  else if (item.getTitle().toString().equalsIgnoreCase(context.getString(R.string.delete_comment))) {
                            simpleDialog = new SimpleDialog(context, context.getString(R.string.confirmation), context.getString(R.string.msg_remove_comment), context.getString(R.string.button_no), context.getString(R.string.button_yes), new OnOneOffClickListener() {
                                @Override
                                public void onSingleClick(View v) {
                                    switch (v.getId()) {
                                        case R.id.button_positive:
                                            onItemClickListener.onDeleteComment(v,position);
                                           break;
                                        case R.id.button_negative:
                                           break;
                                    }
                                    simpleDialog.dismiss();
                                }
                            });
                            simpleDialog.show();
                        }

                        return true;
                    }
                });
                popup.setForceShowIcon(true);

                /** Showing the popup menu */
                popup.show();

            }
        });
    }

    public interface OnItemClickListener {
        void onEditComment(View view, int position,String comment);

        void onDeleteComment(View view, int position);
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

        @BindView(R.id.more)
        ImageView more;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);

        }

        @Override
        public void onClick(View v) {

        }
    }
}
