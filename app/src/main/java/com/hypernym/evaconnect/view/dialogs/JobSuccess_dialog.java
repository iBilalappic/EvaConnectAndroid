package com.hypernym.evaconnect.view.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.hypernym.evaconnect.R;
import com.hypernym.evaconnect.models.AppliedApplicants;
import com.hypernym.evaconnect.models.BaseModel;
import com.hypernym.evaconnect.models.CompanyJobAdModel;
import com.hypernym.evaconnect.models.User;
import com.hypernym.evaconnect.repositories.CustomViewModelFactory;
import com.hypernym.evaconnect.utils.AppUtils;
import com.hypernym.evaconnect.utils.Constants;
import com.hypernym.evaconnect.utils.DateUtils;
import com.hypernym.evaconnect.utils.GsonUtils;
import com.hypernym.evaconnect.utils.LoginUtils;
import com.hypernym.evaconnect.view.ui.fragments.BaseFragment;
import com.hypernym.evaconnect.view.ui.fragments.ChatFragment;
import com.hypernym.evaconnect.view.ui.fragments.WebviewCvFragment;
import com.hypernym.evaconnect.viewmodel.AppliedApplicantViewModel;
import com.hypernym.evaconnect.viewmodel.ConnectionViewModel;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class JobSuccess_dialog extends Dialog implements View.OnClickListener {

        private Context context;
        private Activity activity;
        TextView tv_ok;


        public JobSuccess_dialog(Activity activity,Context context) {
                super(context);
                this.context = context;
                this.activity =activity;

        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
                requestWindowFeature(Window.FEATURE_NO_TITLE);
                super.onCreate(savedInstanceState);
                setContentView(R.layout.dialog_application_success);

                tv_ok = findViewById(R.id.tv_ok);
                tv_ok.setOnClickListener(this);
                setCanceledOnTouchOutside(true);
                setCancelable(true);

                getWindow().setBackgroundDrawableResource(android.R.color.transparent);


        }



        @Override
        public void onClick(View v) {
                switch (v.getId()) {
                        case R.id.tv_ok:
                                activity.onBackPressed();
                                dismiss();
                                break;
                }
        }
}