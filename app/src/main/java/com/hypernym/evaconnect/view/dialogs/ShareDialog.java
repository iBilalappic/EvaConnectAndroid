package com.hypernym.evaconnect.view.dialogs;

import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.hypernym.evaconnect.BuildConfig;
import com.hypernym.evaconnect.R;
import com.hypernym.evaconnect.models.JobAd;
import com.hypernym.evaconnect.models.Post;
import com.hypernym.evaconnect.models.SpecficJobAd;
import com.hypernym.evaconnect.utils.Constants;
import com.hypernym.evaconnect.utils.GsonUtils;
import com.hypernym.evaconnect.view.ui.fragments.EventDetailFragment;
import com.hypernym.evaconnect.view.ui.fragments.ShareConnectionFragment;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.CLIPBOARD_SERVICE;

public class ShareDialog extends Dialog implements View.OnClickListener {


    private LinearLayout whatsapp, email, sms, tweet, connection, copylink;
    private Context context;
    private Bundle bundle;
    Post post = new Post();
    JobAd jobAd = new JobAd();
    SpecficJobAd specficJobAd = new SpecficJobAd();
    String fragment_name;

    public ShareDialog(Context context, Bundle bundle) {
        super(context);
        this.context = context;
        this.bundle = bundle;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_share);
        whatsapp = findViewById(R.id.whatsapp);
        connection = findViewById(R.id.layoutconnection);
        copylink = findViewById(R.id.copylink);
        sms = findViewById(R.id.sms);
        email = findViewById(R.id.email);
        tweet = findViewById(R.id.tweet);
        whatsapp.setOnClickListener(this);
        email.setOnClickListener(this);
        sms.setOnClickListener(this);
        tweet.setOnClickListener(this);
        connection.setOnClickListener(this);
        copylink.setOnClickListener(this);
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
        jobAd = (JobAd) bundle.getSerializable("JobData");

        post = (Post) bundle.getSerializable("PostData");

        specficJobAd = (SpecficJobAd) bundle.getSerializable("specficJob");
        fragment_name = bundle.getString(Constants.FRAGMENT_NAME);
        Log.d("TAAAG", "" + GsonUtils.toJson(post));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.whatsapp:
                Intent whatsappIntent = new Intent(Intent.ACTION_SEND);
                whatsappIntent.setType("text/plain");
                whatsappIntent.setPackage("com.whatsapp");
                if (jobAd != null) {
                    whatsappIntent.putExtra(Intent.EXTRA_TEXT, "https://www.evaintmedia.com/" + "job" + "/" + jobAd.getId());

                } else if (specficJobAd != null) {
                    whatsappIntent.putExtra(Intent.EXTRA_TEXT, "https://www.evaintmedia.com/" + "job" + "/" + specficJobAd.getId());

                } else {
                    whatsappIntent.putExtra(Intent.EXTRA_TEXT, "https://www.evaintmedia.com/" + post.getType() + "/" + post.getId());
                }
                try {
                    getContext().startActivity(whatsappIntent);
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(context, "Whatsapp have not been installed.", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.email:

                Intent gmail = new Intent(Intent.ACTION_SEND);
                gmail.putExtra(Intent.EXTRA_SUBJECT, post.getType());
                if (jobAd != null) {
                    gmail.putExtra(Intent.EXTRA_TEXT, "https://www.evaintmedia.com/" + "job" + "/" + jobAd.getId());

                } else if (specficJobAd != null) {
                    gmail.putExtra(Intent.EXTRA_TEXT, "https://www.evaintmedia.com/" + "job" + "/" + specficJobAd.getId());

                } else {
                    gmail.putExtra(Intent.EXTRA_TEXT, "https://www.evaintmedia.com/" + post.getType() + "/" + post.getId());

                }
                gmail.setType("message/rfc822");
                gmail.setPackage("com.google.android.gm");
                getContext().startActivity(gmail);
                break;

            case R.id.sms:
                Intent smsIntent = new Intent(android.content.Intent.ACTION_VIEW);
                smsIntent.setType("vnd.android-dir/mms-sms");
                if (jobAd != null) {
                    smsIntent.putExtra("sms_body", "https://www.evaintmedia.com/" + "job" + "/" + jobAd.getId());
                } else if (specficJobAd != null) {
                    smsIntent.putExtra(Intent.EXTRA_TEXT, "https://www.evaintmedia.com/" + "job" + "/" + specficJobAd.getId());

                } else {
                    smsIntent.putExtra("sms_body", "https://www.evaintmedia.com/" + post.getType() + "/" + post.getId());
                }
                getContext().startActivity(smsIntent);
                break;

            case R.id.tweet:
                try {
                    // Check if the Twitter app is installed on the phone.
                    getContext().getPackageManager().getPackageInfo("com.twitter.android", 0);
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setClassName("com.twitter.android", "com.twitter.android.composer.ComposerActivity");
                    intent.setType("text/plain");
                    if (jobAd != null) {
                        intent.putExtra(Intent.EXTRA_TEXT, "https://www.evaintmedia.com/" + "job" + "/" + jobAd.getId());

                    } else if (specficJobAd != null) {
                        intent.putExtra(Intent.EXTRA_TEXT, "https://www.evaintmedia.com/" + "job" + "/" + specficJobAd.getId());

                    } else {
                        intent.putExtra(Intent.EXTRA_TEXT, "https://www.evaintmedia.com/" + post.getType() + "/" + post.getId());

                    }
                    getContext().startActivity(intent);

                } catch (Exception e) {
                    Toast.makeText(getContext(), "Twitter is not installed on this device", Toast.LENGTH_LONG).show();

                }

            case R.id.layoutconnection:
                ShareConnectionFragment shareConnectionFragment = new ShareConnectionFragment();
                FragmentTransaction transaction = ((AppCompatActivity) context).getSupportFragmentManager().beginTransaction();
                transaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
                transaction.replace(R.id.framelayout, shareConnectionFragment);
                Bundle bundle = new Bundle();
                if (jobAd != null) {
                    bundle.putInt(Constants.DATA, jobAd.getId());
                    bundle.putString(Constants.TYPE, jobAd.getType());
                    bundle.putString(Constants.FRAGMENT_NAME, "JOB_FRAGMENT");
                } else if (specficJobAd != null) {
                    bundle.putInt(Constants.DATA, specficJobAd.getId());
                    bundle.putString(Constants.TYPE, specficJobAd.getType());
                    bundle.putString(Constants.FRAGMENT_NAME, "JOB_FRAGMENT");
                } else {
                    bundle.putInt(Constants.DATA, post.getId());
                    bundle.putString(Constants.TYPE, post.getType());
                }
                shareConnectionFragment.setArguments(bundle);
                if (true) {
                    transaction.addToBackStack(null);
                }
                transaction.commit();
                dismiss();
                break;

            case R.id.copylink:

                ClipboardManager clipboard = (ClipboardManager) getContext().getSystemService(CLIPBOARD_SERVICE);
                ClipData clip;
                if (jobAd != null) {
                    clip = ClipData.newPlainText("label", "https://www.evaintmedia.com/" + "job" + "/" + jobAd.getId());

                } else if (specficJobAd != null) {
                    clip = ClipData.newPlainText("label", "https://www.evaintmedia.com/" + "job" + "/" + specficJobAd.getId());

                } else {
                    clip = ClipData.newPlainText("label", "https://www.evaintmedia.com/" + post.getType() + "/" + post.getId());
                }
                clipboard.setPrimaryClip(clip);
                Toast.makeText(context, "link copied", Toast.LENGTH_SHORT).show();
                break;


        }


    }
}
