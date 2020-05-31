package com.hypernym.evaconnect.view.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
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
import com.hypernym.evaconnect.utils.GsonUtils;
import com.hypernym.evaconnect.utils.NetworkUtils;
import com.hypernym.evaconnect.utils.RecyclerItemClickListener;
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

    @BindView(R.id.textView4)
    TextView textView4;

    @BindView(R.id.btn_next)
    Button btn_next;

    @BindView(R.id.img_cross)
    ImageView img_cross;

    @BindView(R.id.layout_header)
    LinearLayout layout_header;


    private NewsAdapter newsAdapter;
    private LinearLayoutManager linearLayoutManager;
    private List<NewSources> NewsSources = new ArrayList<>();
    private List<NewSources> MultiSelect = new ArrayList<>();
    private List<Integer> NewsSelectedids = new ArrayList<>();
    boolean isMultiSelect = false;
    ActionMode mActionMode;
    int Postion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        ButterKnife.bind(this);
        validator = new Validator(this);
        validator.setValidationListener(this);
        img_cross.setOnClickListener(this);
        img_backarrow.setOnClickListener(this);
        btn_next.setOnClickListener(this);
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
        newsAdapter = new NewsAdapter(this, NewsSources, this, MultiSelect);
        linearLayoutManager = new LinearLayoutManager(this);
        rc_newsources.setLayoutManager(new GridLayoutManager(getApplicationContext(), 3));
        rc_newsources.addItemDecoration(new ItemDecorationAlbumColumns(
                getResources().getDimensionPixelSize(R.dimen.spacing_small), 3));

        rc_newsources.setAdapter(newsAdapter);

        rc_newsources.addOnItemTouchListener(new RecyclerItemClickListener(this, rc_newsources, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Postion = position;
                if (!isMultiSelect) {

                    MultiSelect = new ArrayList<NewSources>();
                    isMultiSelect = true;
                    btn_next.setVisibility(View.VISIBLE);
                    textView4.setVisibility(View.GONE);
                    layout_header.setVisibility(View.GONE);
                    if (mActionMode == null) {
                        mActionMode = startActionMode(mActionModeCallback);
                    }
                }
                multi_select(position);
                //      Toast.makeText(getContext(), ""+user_list.get(position).getSelected_person(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onItemLongClick(View view, int position) {
                //       Toast.makeText(getContext(), ""+user_list.get(position).getSelected_person(), Toast.LENGTH_SHORT).show();
                Postion = position;
                if (!isMultiSelect) {
                    MultiSelect = new ArrayList<NewSources>();
                    isMultiSelect = true;

                    if (mActionMode == null) {
                        mActionMode = startActionMode(mActionModeCallback);
                    }
                }

                multi_select(position);

            }
        }));

    }

    public void multi_select(int position) {
        if (mActionMode != null) {
            if (MultiSelect.contains(NewsSources.get(position)))
                MultiSelect.remove(NewsSources.get(position));
            else
                MultiSelect.add(NewsSources.get(position));


            if (MultiSelect.size() > 0)
                mActionMode.setTitle("" + MultiSelect.size());


            else
                mActionMode.setTitle("");

            refreshAdapter();

        }
    }

    public void refreshAdapter() {
        newsAdapter.selected_NewsList = MultiSelect;
        newsAdapter.newSources = NewsSources;
        newsAdapter.notifyDataSetChanged();
    }

    private ActionMode.Callback mActionModeCallback = new ActionMode.Callback() {

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            // Inflate a menu resource providing context menu items
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return true; // Return false if nothing is done
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            return false;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            mActionMode = null;
            isMultiSelect = false;
            MultiSelect = new ArrayList<NewSources>();
            btn_next.setVisibility(View.GONE);
            textView4.setVisibility(View.VISIBLE);
            layout_header.setVisibility(View.VISIBLE);
            refreshAdapter();
        }
    };


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
                NewsSelectedids.clear();
                this.finish();
                break;

            case R.id.img_cross:
                NewsSelectedids.clear();
                this.finish();
                break;
            case R.id.btn_next:
                CheckSelectedNews();

                break;
        }
    }

    private void CheckSelectedNews() {
        NewsSelectedids.clear();
        for (int i = 0; i < MultiSelect.size(); i++) {
            NewsSelectedids.add(MultiSelect.get(i).getId());
        }
        homeViewModel.setNewSources(NewsSelectedids).observe(this, new Observer<BaseModel<List<NewSources>>>() {
            @Override
            public void onChanged(BaseModel<List<NewSources>> getNewsources) {
                if (getNewsources != null && !getNewsources.isError()) {
                    Intent intent = new Intent(NewsActivity.this, HomeActivity.class);
                    // set the new task and clear flags
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                  //  Toast.makeText(NewsActivity.this, "sucess", Toast.LENGTH_SHORT).show();
                } else {
                    networkResponseDialog(getString(R.string.error), getString(R.string.err_unknown));
                }
                hideDialog();

            }
        });
    }
}
