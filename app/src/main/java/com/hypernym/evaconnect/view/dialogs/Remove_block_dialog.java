package com.hypernym.evaconnect.view.dialogs;

import android.app.Activity;
import android.app.Application;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.hypernym.evaconnect.R;
import com.hypernym.evaconnect.models.BaseModel;
import com.hypernym.evaconnect.models.Post;
import com.hypernym.evaconnect.models.User;
import com.hypernym.evaconnect.repositories.CustomViewModelFactory;
import com.hypernym.evaconnect.utils.AppUtils;
import com.hypernym.evaconnect.utils.GsonUtils;
import com.hypernym.evaconnect.view.ui.fragments.PersonDetailFragment;
import com.hypernym.evaconnect.viewmodel.ConnectionViewModel;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.hypernym.evaconnect.utils.AppUtils.getApplicationContext;

public class Remove_block_dialog extends Dialog implements View.OnClickListener {


    private LinearLayout unfollow, block;
    private Application context;
    CircleImageView profile_image;
    TextView tv_profilename;
    ImageView img_close;
    PersonDetailFragment context1;
    private Bundle bundle;
    Post post = new Post();

    private ConnectionViewModel connectionViewModel;
    SimpleDialog simpleDialog;
    FragmentActivity application;

    public Remove_block_dialog(PersonDetailFragment context1, Application context, Bundle bundle, FragmentActivity application) {
        super(context);
        this.context = context;
        this.context1 = context1;
        this.bundle = bundle;
        this.application = application;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_remove_block);
        profile_image = findViewById(R.id.profile_image);
        img_close = findViewById(R.id.img_close);
        tv_profilename = findViewById(R.id.tv_profilename);
        unfollow = findViewById(R.id.unfollow);
        block = findViewById(R.id.block);
        unfollow.setOnClickListener(this);
        block.setOnClickListener(this);
        img_close.setOnClickListener(this);
        setCanceledOnTouchOutside(true);
        setCancelable(true);


//        Window window = getWindow();
        //  WindowManager.LayoutParams wlp = window.getAttributes();
        // wlp.gravity = Gravity.TOP | Gravity.RIGHT;
//        wlp.flags &= WindowManager.LayoutParams.FLAG_DIM_BEHIND;
//        window.setAttributes(wlp);
//        WindowManager.LayoutParams params = this.getWindow().getAttributes();
//        params.y = 50;
//        this.getWindow().setAttributes(params);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        init();

    }

    private void init() {
        connectionViewModel = ViewModelProviders.of(context1, new CustomViewModelFactory(context)).get(ConnectionViewModel.class);

        post = (Post) bundle.getSerializable("Data");
        tv_profilename.setText(post.getUser().getFirst_name());
        AppUtils.setGlideImage(getContext(), profile_image, post.getUser().getUser_image());
        Log.d("TAAAG", "" + GsonUtils.toJson(post));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_close:
                this.dismiss();
                break;
            case R.id.unfollow:
                RemoveUserApiCall();
                break;

        }


    }

    private void RemoveUserApiCall() {
        if (post.getConnection_id() != null) {
            connectionViewModel.remove_user(post.getConnection_id()).observe(new PersonDetailFragment() , new Observer<BaseModel<List<Object>>>() {
                @Override
                public void onChanged(BaseModel<List<Object>> listBaseModel) {
                    if (!listBaseModel.isError()) {
                        simpleDialog = new SimpleDialog(getContext(), getContext().getString(R.string.success), getContext().getString(R.string.msg_remove_user), null, getContext().getString(R.string.ok), new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                getOwnerActivity().onBackPressed();
                                simpleDialog.dismiss();
                            }
                        });
                    } else {
                        simpleDialog = new SimpleDialog(getContext(), getContext().getString(R.string.success), "Your connection is already remove", null, getContext().getString(R.string.ok), new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                getOwnerActivity().onBackPressed();
                                simpleDialog.dismiss();
                            }
                        });
                    }
                    simpleDialog.show();
                    simpleDialog.setCancelable(false);
                }
            });
        }

    }
}
