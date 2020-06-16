package com.hypernym.evaconnect.view.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.hypernym.evaconnect.R;
import com.hypernym.evaconnect.models.User;
import com.hypernym.evaconnect.utils.AppUtils;
import com.hypernym.evaconnect.utils.Constants;
import com.hypernym.evaconnect.utils.LoginUtils;
import com.hypernym.evaconnect.utils.PrefUtils;
import com.hypernym.evaconnect.view.ui.fragments.CalendarFragment;
import com.hypernym.evaconnect.view.ui.fragments.JobListingFragment;
import com.hypernym.evaconnect.view.ui.fragments.SearchResultFragment;
import com.hypernym.evaconnect.view.ui.fragments.ShareConnectionFragment;

import butterknife.BindView;
import de.hdodenhof.circleimageview.CircleImageView;

public class SearchDialog extends Dialog implements View.OnClickListener {

    private Context context;
    TextView btn_next;
    EditText edt_keyword;


    public SearchDialog(Context context) {
        super(context);
        this.context = context;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_search);
        btn_next = findViewById(R.id.btn_next);
        edt_keyword = findViewById(R.id.edt_keyword);
        btn_next.setOnClickListener(this);
        setCanceledOnTouchOutside(false);
        setCancelable(false);
        Window window = getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.flags &= WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(wlp);
        WindowManager.LayoutParams params = this.getWindow().getAttributes();
        params.y = 50;
        window.setLayout(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT);
        this.getWindow().setAttributes(params);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);


    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_next:
                if(!edt_keyword.getText().toString().isEmpty()){
                    dismiss();
                    SearchResultFragment searchResultFragment = new SearchResultFragment();
                    FragmentTransaction transaction = ((AppCompatActivity) context).getSupportFragmentManager().beginTransaction();
                    transaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
                    transaction.replace(R.id.framelayout, searchResultFragment);
                    transaction.addToBackStack(null);
                    Bundle bundle = new Bundle();
                    bundle.putString(Constants.SEARCH,edt_keyword.getText().toString());
                    searchResultFragment.setArguments(bundle);
                    transaction.commit();
                }else{
                    Toast.makeText(context, "please enter keyword", Toast.LENGTH_SHORT).show();
                }

                break;
        }
    }
}

