package com.hypernym.evaconnect.view.ui.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.hypernym.evaconnect.R;
import com.hypernym.evaconnect.constants.AppConstants;
import com.hypernym.evaconnect.models.BaseModel;
import com.hypernym.evaconnect.models.CompanyJobAdModel;
import com.hypernym.evaconnect.models.User;
import com.hypernym.evaconnect.repositories.CustomViewModelFactory;
import com.hypernym.evaconnect.utils.AppUtils;
import com.hypernym.evaconnect.utils.ImageFilePathUtil;
import com.hypernym.evaconnect.utils.LoginUtils;
import com.hypernym.evaconnect.view.dialogs.SimpleDialog;
import com.hypernym.evaconnect.viewmodel.CreateJobAdViewModel;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import java.io.File;
import java.io.FileOutputStream;
import java.text.DecimalFormat;
import java.util.List;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import static android.app.Activity.RESULT_OK;

public class JobCreateFragment extends BaseFragment implements View.OnClickListener, Validator.ValidationListener {

    public JobCreateFragment() {
        // Required empty public constructor
    }

    @BindView(R.id.spinner_jobsector)
    Spinner spinner_jobsector;

    @BindView(R.id.spinner_week)
    Spinner spinner_week;

    @NotEmpty
    @BindView(R.id.edit_companyName)
    EditText edit_companyName;

    @NotEmpty
    @BindView(R.id.edit_jobpostion)
    EditText edit_jobpostion;

    @BindView(R.id.edit_jobtitle)
    EditText edit_jobtitle;

    @NotEmpty
    @BindView(R.id.edit_Location)
    EditText edit_Location;

    @NotEmpty
    @BindView(R.id.edit_amount)
    EditText edit_amount;
    @NotEmpty
    @BindView(R.id.edit_jobdescription)
    EditText edit_jobdescription;

    @BindView(R.id.postAd)
    TextView postAd;

    @BindView(R.id.tv_browsefiles)
    TextView tv_browsefiles;

    @BindView(R.id.profile_image)
    CircleImageView profile_image;

    private static final int REQUEST_PHOTO_GALLERY = 4;
    private static final int CAMERAA = 1;
    public static final int RequestPermissionCode = 1;
    public String GalleryImage, globalImagePath;
    public String mProfileImageDecodableString;
    private File tempFile, file_name;
    private String currentPhotoPath = "";
    private String photoVar = null;
    MultipartBody.Part partImage;
    private Validator validator, validator_update;
    SimpleDialog simpleDialog;

    private CreateJobAdViewModel createJobAdViewModel;
    private String[] mSpinnerJobSector = {"Piolots", "ITSystems", "Security"};
    private String[] mSpinnerWeek = {"36 hours(per week)", "48 hours(per week)"};

    String JobSector, WeeklyHour;

    private CompanyJobAdModel companyJobAdModel = new CompanyJobAdModel();
    ArrayAdapter<String> arraySectorAdapter,arrayWeekAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_createjob, container, false);
        setPageTitle(getString(R.string.createJoblist));
        ButterKnife.bind(this, view);
        validator = new Validator(this);
        validator.setValidationListener(this);
        validator_update = new Validator(this);
        validator_update.setValidationListener(this);
        SettingJobSectorSpinner();
        SettingWeekSpinner();
        postAd.setOnClickListener(this);
        tv_browsefiles.setOnClickListener(this);
        init();
        return view;
    }

    @SuppressLint("SetTextI18n")
    private void init() {
        createJobAdViewModel = ViewModelProviders.of(this, new CustomViewModelFactory(getActivity().getApplication(), getActivity())).get(CreateJobAdViewModel.class);

        if ((getArguments() != null)) {
            setPageTitle("");
            postAd.setText("Update Job");
            showBackButton();
            companyJobAdModel = (CompanyJobAdModel) getArguments().getSerializable("COMPANY_AD");
            AppUtils.setGlideImage(getContext(), profile_image, companyJobAdModel.getJobImage());
         //   String companyname = getsplitCompanyName(companyJobAdModel.getJobTitle());
            String jobtitle = getsplitTitle(companyJobAdModel.getJobTitle());
            edit_companyName.setText(jobtitle);
            edit_jobtitle.setText(jobtitle);
            edit_jobpostion.setText(companyJobAdModel.getPosition());
            edit_Location.setText(companyJobAdModel.getLocation());
          //  String SalaryInt = getsplitstring(String.valueOf(companyJobAdModel.getSalary()));
            DecimalFormat myFormatter = new DecimalFormat("############");
            edit_amount.setText(myFormatter.format(companyJobAdModel.getSalary()));
            edit_jobdescription.setText(companyJobAdModel.getContent());

            if (companyJobAdModel.getJobSector() != null) {
                int spinnerPosition = arraySectorAdapter.getPosition(companyJobAdModel.getJobSector());
                spinner_jobsector.setSelection(spinnerPosition);
            }
            if (companyJobAdModel.getWeeklyHours() != null) {
                int spinnerPosition = arraySectorAdapter.getPosition(companyJobAdModel.getWeeklyHours());
                spinner_week.setSelection(spinnerPosition);
            }
        }
    }

    public String getsplitCompanyName(String value) {
        String[] parts = value.split(Pattern.quote("for"));
        value = parts[0]; // 004
        return value;
    }

    public String getsplitTitle(String value) {
        String[] parts = value.split(Pattern.quote("for"));
        value = parts[1]; // 004
        return value;
    }

    public String getsplitstring(String value) {
        String[] parts = value.split(Pattern.quote("."));
        value = parts[0]; // 004
        return value;
    }

    private void SettingWeekSpinner() {
        arrayWeekAdapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_dropdown_item_1line, mSpinnerWeek);
        spinner_week.setAdapter(arrayWeekAdapter);
        spinner_jobsector.setSelection(0);
        spinner_week.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                WeeklyHour = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });
    }

    private void SettingJobSectorSpinner() {
        arraySectorAdapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_dropdown_item_1line, mSpinnerJobSector);
        arraySectorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_jobsector.setAdapter(arraySectorAdapter);

        spinner_jobsector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                JobSector = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });
        spinner_jobsector.setSelection(0);
//        if ((getArguments() != null)) {
//
//            if (companyJobAdModel.getJobSector() != null) {
//                int spinnerPosition = arraySectorAdapter.getPosition(companyJobAdModel.getJobSector());
//                spinner_jobsector.setSelection(0);
//            }
//        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.postAd:
                if (postAd.getText().toString().equals("Update Job")) {
                    if (JobSector == null) {
                        Toast.makeText(getContext(), "Please select job sector", Toast.LENGTH_SHORT).show();
                    } else if (WeeklyHour == null) {
                        Toast.makeText(getContext(), "Please select weekly hour", Toast.LENGTH_SHORT).show();
                    } else if (partImage == null && companyJobAdModel.getJobImage()==null) {
                        Toast.makeText(getContext(), "Please add image for job post", Toast.LENGTH_SHORT).show();
                    } else {
                        validator.validate();
                    }
                } else {
                    if (JobSector == null) {
                        Toast.makeText(getContext(), "Please select job sector", Toast.LENGTH_SHORT).show();
                    } else if (WeeklyHour == null) {
                        Toast.makeText(getContext(), "Please select weekly hour", Toast.LENGTH_SHORT).show();
                    } else if (partImage == null) {
                        Toast.makeText(getContext(), "Please add image for job post", Toast.LENGTH_SHORT).show();
                    } else {
                        validator.validate();
                    }
                }

                break;
            case R.id.tv_browsefiles:
                openPictureDialog();
                break;
        }
    }

    private void UpdateJobAd() {
        showDialog();
        User user = LoginUtils.getLoggedinUser();
        edit_jobtitle.setText( edit_jobpostion.getText().toString() + " for " + edit_companyName.getText().toString());
        createJobAdViewModel.UpdateJobAd(companyJobAdModel.getId(), user, partImage, JobSector, WeeklyHour,
                Integer.parseInt(edit_amount.getText().toString()),
                edit_companyName.getText().toString(),
                edit_jobdescription.getText().toString(),
                edit_Location.getText().toString(),
                edit_jobtitle.getText().toString(),
                edit_jobpostion.getText().toString()).observe(this, new Observer<BaseModel<List<Object>>>() {
            @Override
            public void onChanged(BaseModel<List<Object>> getnetworkconnection) {
                if (getnetworkconnection != null && !getnetworkconnection.isError()) {
                    simpleDialog = new SimpleDialog(getActivity(), getString(R.string.success), getString(R.string.msg_jobAd_update), null, getString(R.string.ok), new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                           // getActivity().onBackPressed();
                            loadFragment(R.id.framelayout,new JobListingFragment(),getContext(),false);
                            simpleDialog.dismiss();
                        }
                    });
                    hideDialog();
                } else {
                    networkResponseDialog(getString(R.string.error), getString(R.string.err_unknown));
                }
                hideDialog();
                if (!simpleDialog.isShowing())
                    simpleDialog.show();
            }

        });
    }

    private void CreateJobAd() {
        showDialog();
        User user = LoginUtils.getLoggedinUser();
        edit_jobtitle.setText( edit_jobpostion.getText().toString() + " for " + edit_companyName.getText().toString());
        createJobAdViewModel.createJobAd(user, partImage, JobSector, WeeklyHour,
                Integer.parseInt(edit_amount.getText().toString()),
                edit_companyName.getText().toString(),
                edit_jobdescription.getText().toString(),
                edit_Location.getText().toString(),
                edit_jobtitle.getText().toString(),
                edit_jobpostion.getText().toString()).observe(this, new Observer<BaseModel<List<Object>>>() {
            @Override
            public void onChanged(BaseModel<List<Object>> getnetworkconnection) {
                if (getnetworkconnection != null && !getnetworkconnection.isError()) {
                    simpleDialog = new SimpleDialog(getActivity(), getString(R.string.success), getString(R.string.msg_jobAd), null, getString(R.string.ok), new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            getActivity().onBackPressed();
                            simpleDialog.dismiss();
                        }
                    });
                    hideDialog();
                } else {
                    networkResponseDialog(getString(R.string.error), getString(R.string.err_unknown));
                }
                hideDialog();
                if (!simpleDialog.isShowing())
                    simpleDialog.show();
            }

        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Result code is RESULT_OK only if the user selects an Image
        if (requestCode == REQUEST_PHOTO_GALLERY && resultCode == RESULT_OK) {
            try {
                if (data != null && data.getData() != null) {
                    Uri SelectedImageUri = data.getData();

                    GalleryImage = ImageFilePathUtil.getPath(getContext(), SelectedImageUri);
                    mProfileImageDecodableString = ImageFilePathUtil.getPath(getContext(), SelectedImageUri);
                    Log.e(getClass().getName(), "image file path: " + GalleryImage);

                    tempFile = new File(GalleryImage);

                    Log.e(getClass().getName(), "file path details: " + tempFile.getName() + " " + tempFile.getAbsolutePath() + "length" + tempFile.length());


                    if (tempFile.length() / AppConstants.ONE_THOUSAND_AND_TWENTY_FOUR > AppConstants.FILE_SIZE_LIMIT_IN_KB) {
                        networkResponseDialog(getString(R.string.error), getString(R.string.err_image_size_large));
                        return;
                    } else {
                        if (photoVar == null) {
                            currentPhotoPath = GalleryImage;
                            // photoVar = GalleryImage;
                            file_name = new File(ImageFilePathUtil.getPath(getContext(), SelectedImageUri));
                            RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), file_name);
                            partImage = MultipartBody.Part.createFormData("job_image", file_name.getName(), reqFile);
                            if (!TextUtils.isEmpty(currentPhotoPath) || currentPhotoPath != null) {
                                Glide.with(this).load(currentPhotoPath).into(profile_image);

                            } else {
                                networkResponseDialog(getString(R.string.error), getString(R.string.err_internal_supported));
                            }
                        } else {
                            networkResponseDialog(getString(R.string.error), getString(R.string.err_one_file_at_a_time));
                            return;
                        }
                    }
                } else {
                    Toast.makeText(getContext(), "Something went wrong while retrieving image", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception exc) {
                exc.printStackTrace();
                Log.e(getClass().getName(), "exc: " + exc.getMessage());
            }
        } else {
            if (requestCode == CAMERAA) {

                //mIsProfileImageAdded = true;
                File file = galleryAddPic();
                RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), file);
                // imgName = file_name.getName();

                globalImagePath = file.getAbsolutePath();
                if (file.length() / AppConstants.ONE_THOUSAND_AND_TWENTY_FOUR > AppConstants.IMAGE_SIZE_IN_KB) {
                    networkResponseDialog(getString(R.string.error), getString(R.string.err_image_size_large));
                    return;
                }
                if (!TextUtils.isEmpty(globalImagePath) || globalImagePath != null) {

                    Glide.with(this).load(loadFromFile(globalImagePath))
                            .apply(new RequestOptions())
                            .into(profile_image);

                    Bitmap orignal = loadFromFile(globalImagePath);
                    File filenew = new File(globalImagePath);
                    try {
                        FileOutputStream out = new FileOutputStream(filenew);
                        orignal.compress(Bitmap.CompressFormat.JPEG, 50, out);
                        out.flush();
                        out.close();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    partImage = MultipartBody.Part.createFormData("job_image", file.getName(), reqFile);
                    //AppUtils.showSnackBar(getView(), AppUtils.getErrorMessage(getContext(), 8));

                } else {
                    profile_image.setImageResource(R.drawable.app_logo);
                }

            }
        }
    }


    @Override
    public void onValidationSucceeded() {
        if (postAd.getText().toString().equals("Update Job")) {
            UpdateJobAd();
        } else {
            CreateJobAd();
        }

    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error : errors) {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(getContext());
            // Display error messages
            if (view.getId() == R.id.edit_amount) {
                message = getString(R.string.err_salary);
            }
            if (view.getId() == R.id.edit_companyName) {
                message = getString(R.string.err_companyname);
            }
            if (view.getId() == R.id.edit_Location) {
                message = getString(R.string.err_location);
            }
            if (view.getId() == R.id.edit_jobdescription) {
                message = getString(R.string.err_role);
            }
            if (view.getId() == R.id.edit_jobpostion) {
                message = getString(R.string.err_position);
            }

            if (view instanceof EditText) {
                ((EditText) view).setError(message);
            } else {
                Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
            }
        }
    }
}