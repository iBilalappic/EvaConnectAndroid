package com.hypernym.evaconnect.view.ui.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.hypernym.evaconnect.R;
import com.hypernym.evaconnect.decorators.ItemDecorationAlbumColumns;
import com.hypernym.evaconnect.models.BaseModel;
import com.hypernym.evaconnect.models.JobAd;
import com.hypernym.evaconnect.models.NewSources;
import com.hypernym.evaconnect.models.User;
import com.hypernym.evaconnect.repositories.CustomViewModelFactory;
import com.hypernym.evaconnect.utils.NetworkUtils;
import com.hypernym.evaconnect.view.adapters.CompanyJobAdAdapter;
import com.hypernym.evaconnect.view.adapters.JobAdAdapter;
import com.hypernym.evaconnect.view.adapters.NewsAdapter;
import com.hypernym.evaconnect.view.dialogs.SimpleDialog;
import com.hypernym.evaconnect.viewmodel.HomeViewModel;
import com.hypernym.evaconnect.viewmodel.JobListViewModel;
import com.hypernym.evaconnect.viewmodel.UserViewModel;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NewsActivity extends BaseActivity implements Validator.ValidationListener, NewsAdapter.OnItemClickListener, View.OnClickListener {


    private Validator validator;
    private HomeViewModel homeViewModel;

    @BindView(R.id.swipeRefresh)
    SwipeRefreshLayout swipeRefresh;

    @BindView(R.id.rc_newsources)
    RecyclerView rc_newsources;


    @BindView(R.id.img_backarrow)
    ImageView img_backarrow;

    @BindView(R.id.img_cross)
    ImageView img_cross;

    private NewsAdapter newsAdapter;
    private LinearLayoutManager linearLayoutManager;
    private List<NewSources> NewsSources = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        ButterKnife.bind(this);
        validator = new Validator(this);
        validator.setValidationListener(this);
        img_cross.setOnClickListener(this);
        img_backarrow.setOnClickListener(this);
        init();
    }

    private void init() {
        homeViewModel = ViewModelProviders.of(this, new CustomViewModelFactory(this.getApplication(), this)).get(HomeViewModel.class);
        GetNewSources();
    }

    private void GetNewSources() {
        homeViewModel.getNewSources().observe(this, new Observer<BaseModel<List<NewSources>>>() {
            @Override
            public void onChanged(BaseModel<List<NewSources>> getNewsources) {
                if (getNewsources != null && !getNewsources.isError()) {
                    NewsSources.clear();
                    NewsSources.addAll(getNewsources.getData());
                    setupRecyclerview();
                } else {
                    networkResponseDialog(getString(R.string.error), getString(R.string.err_unknown));
                }
                hideDialog();

            }
        });
    }

    private void setupRecyclerview() {
        newsAdapter = new NewsAdapter(this, NewsSources, this);
        linearLayoutManager = new LinearLayoutManager(this);
        rc_newsources.setLayoutManager(new GridLayoutManager(getApplicationContext(), 3));
        rc_newsources.addItemDecoration(new ItemDecorationAlbumColumns(
                getResources().getDimensionPixelSize(R.dimen.spacing_small), 3));

        rc_newsources.setAdapter(newsAdapter);
    }


    @Override
    public void onValidationSucceeded() {
        if (NetworkUtils.isNetworkConnected(this)) {
            showDialog();
        } else {
            networkErrorDialog();
        }

    }


    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error : errors) {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(this);
            // Display error messages
            if (view instanceof EditText) {
                ((EditText) view).setError(message);
            } else {
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onItemClick(View view, int position) {


    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.img_backarrow:
                this.finish();
                break;

            case R.id.img_cross:
                this.finish();
                break;
        }
    }
}
