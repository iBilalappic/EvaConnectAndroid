package com.hypernym.evaconnect.view.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Toast;

import com.hypernym.evaconnect.R;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import butterknife.BindView;
import butterknife.ButterKnife;

public class JobCreateFragment extends BaseFragment implements View.OnClickListener{

    public JobCreateFragment() {
        // Required empty public constructor
    }

    @BindView(R.id.spinner_jobsector)
    MaterialBetterSpinner spinner_jobsector;

    @BindView(R.id.spinner_week)
    MaterialBetterSpinner spinner_week;

    @BindView(R.id.edit_companyName)
    EditText edit_companyName;

    @BindView(R.id.edit_Location)
    EditText edit_Location;

    @BindView(R.id.edit_amount)
    EditText edit_amount;

    @BindView(R.id.edit_jobdescription)
    EditText edit_jobdescription;


    private String[] mSpinnerJobSector = {"Piolots", "ITSystems", "Security"};
    private String[] mSpinnerWeek = {"36 hours(per annum)"};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_createjob, container, false);
        setPageTitle(getString(R.string.createJoblist));
        ButterKnife.bind(this, view);
        SettingJobSectorSpinner();
        SettingWeekSpinner();
        return view;
    }

    private void SettingWeekSpinner() {
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_dropdown_item_1line, mSpinnerWeek);
        spinner_week.setAdapter(arrayAdapter);
        spinner_week.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getContext(), "clicked"+parent.getItemAtPosition(position), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void SettingJobSectorSpinner() {
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_dropdown_item_1line, mSpinnerJobSector);
        spinner_jobsector.setAdapter(arrayAdapter);
        spinner_jobsector.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getContext(), "clicked"+parent.getItemAtPosition(position), Toast.LENGTH_SHORT).show();
            }
        });


    }


    @Override
    public void onClick(View v) {

    }
}