package com.hypernym.evaconnect.view.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.hypernym.evaconnect.BuildConfig;
import com.hypernym.evaconnect.R;
import com.hypernym.evaconnect.models.Post;
import com.hypernym.evaconnect.utils.GsonUtils;

public class ShareDialog extends Dialog implements View.OnClickListener {


    private LinearLayout whatsapp, email, sms, tweet, mLike, calendar;
    private Context context;
    private Bundle bundle;
    Post post = new Post();

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
        sms = findViewById(R.id.sms);
        email = findViewById(R.id.email);
        tweet = findViewById(R.id.tweet);
        whatsapp.setOnClickListener(this);
        email.setOnClickListener(this);
        sms.setOnClickListener(this);
        tweet.setOnClickListener(this);
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
        post = (Post) bundle.getSerializable("PostData");
        Log.d("TAAAG", "" + GsonUtils.toJson(post));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.whatsapp:
//                Intent whatsappIntent = new Intent(Intent.ACTION_SEND);
////                whatsappIntent.setType("text/plain");
////                whatsappIntent.setPackage("com.whatsapp");
////                whatsappIntent.putExtra(Intent.EXTRA_TEXT, "The text you wanted to share");
////                try {
////                    getContext().startActivity(whatsappIntent);
////                } catch (android.content.ActivityNotFoundException ex) {
////                    Toast.makeText(context, "Whatsapp have not been installed.", Toast.LENGTH_SHORT).show();
////                }

                try {
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("text/plain");
                    shareIntent.putExtra(Intent.EXTRA_SUBJECT, "My application name");
                    String shareMessage= "\nLet me recommend you this application\n\n";
                    shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID +"\n\n";
                    shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                    getContext().startActivity(Intent.createChooser(shareIntent, "choose one"));
                } catch(Exception e) {
                    //e.toString();
                }
                break;
            case R.id.email:
                Intent it = new Intent(Intent.ACTION_SEND);
                //   it.putExtra(Intent.EXTRA_EMAIL, new String[]{mailTo.getText().toString()});
                it.putExtra(Intent.EXTRA_SUBJECT, "Post");
                it.putExtra(Intent.EXTRA_TEXT, "The text you wanted to share");
                it.setType("message/rfc822");
                getContext().startActivity(Intent.createChooser(it, "Choose Mail App"));
                break;

            case R.id.sms:
                Intent smsIntent = new Intent(android.content.Intent.ACTION_VIEW);
                smsIntent.setType("vnd.android-dir/mms-sms");
                smsIntent.putExtra("sms_body", "your desired message");
                smsIntent.setFlags(android.content.Intent.FLAG_ACTIVITY_NEW_TASK);
                getContext().startActivity(smsIntent);

            case R.id.tweet:
                try {
                    // Check if the Twitter app is installed on the phone.
                    getContext().getPackageManager().getPackageInfo("com.twitter.android", 0);
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setClassName("com.twitter.android", "com.twitter.android.composer.ComposerActivity");
                    intent.setType("text/plain");
                    intent.putExtra(Intent.EXTRA_TEXT, "Your text");
                    getContext().startActivity(intent);

                } catch (Exception e) {
                    Toast.makeText(getContext(), "Twitter is not installed on this device", Toast.LENGTH_LONG).show();

                }


        }


    }
}
