package com.hypernym.evaconnect.view.ui.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hypernym.evaconnect.R;
import com.hypernym.evaconnect.utils.Constants;

import butterknife.BindView;
import butterknife.ButterKnife;


public class TermConditionHelpPolicyFragment extends BaseFragment implements View.OnClickListener {

    @BindView(R.id.header_title)
    TextView header_title;

    @BindView(R.id.title)
    TextView title;

    @BindView(R.id.email)
    TextView email;

    @BindView(R.id.description)
    TextView description;

    @BindView(R.id.img_backarrow)
    ImageView img_backarrow;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ButterKnife.bind(this, view);
        img_backarrow.setOnClickListener(this);
        init();
    }

    private void init() {

            Bundle bundle = getArguments();
            if (bundle != null) {
                String fragment_name = bundle.getString(Constants.FRAGMENT_NAME);

                switch (fragment_name) {
                    case Constants.HELP:
                        header_title.setText(R.string.help);
                        title.setText("Please contact us on");
                        title.setTextColor(getResources().getColor(R.color.grayish_black_text));
                        email.setVisibility(View.VISIBLE);
                        description.setText(R.string.large_dummy_text);
                        break;
                    case Constants.TERMS_AND_CONDITION:
                        header_title.setText("Terms of Service");
                        title.setText("Terms of Service");
                        title.setTextColor(getResources().getColor(R.color.colorAccent));
                        email.setVisibility(View.GONE);
                        description.setText(R.string.large_dummy_text);
                        break;
                    case Constants.COOKIES_POLICY:
                        header_title.setText("Cookies Policy");
                        title.setText("Cookies Policy");
                        title.setTextColor(getResources().getColor(R.color.colorAccent));
                        email.setVisibility(View.GONE);
                        description.setText(R.string.large_dummy_text);
                        break;
                    default:

                        break;
                }
            }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_term_condition_help_policy, container, false);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.img_backarrow) {
            getActivity().onBackPressed();
        }
    }
}