package com.hypernym.evaconnect.view.ui.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.hypernym.evaconnect.models.SpecficJobAd;
import com.hypernym.evaconnect.models.User;
import com.hypernym.evaconnect.repositories.CustomViewModelFactory;
import com.hypernym.evaconnect.utils.AppUtils;
import com.hypernym.evaconnect.utils.GsonUtils;
import com.hypernym.evaconnect.utils.ImageFilePathUtil;
import com.hypernym.evaconnect.utils.LoginUtils;
import com.hypernym.evaconnect.view.dialogs.ApplicationSuccess_dialog;
import com.hypernym.evaconnect.view.dialogs.JobSuccess_dialog;
import com.hypernym.evaconnect.view.dialogs.SimpleDialog;
import com.hypernym.evaconnect.viewmodel.CreateJobAdViewModel;
import com.hypernym.evaconnect.viewmodel.JobListViewModel;
import com.hypernym.evaconnect.viewmodel.UserViewModel;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import static android.app.Activity.RESULT_OK;

public class CreateJobFragment extends BaseFragment implements View.OnClickListener, Validator.ValidationListener {

    public CreateJobFragment() {
        // Required empty public constructor
    }

    @BindView(R.id.spinner_jobsector)
    Spinner spinner_jobsector;


    @BindView(R.id.spinner_jobtype)
    Spinner spinner_jobtype;

    @BindView(R.id.spinner_job_duration)
    Spinner spinner_job_duration;

    @NotEmpty
    @BindView(R.id.edit_companyName)
    EditText edit_companyName;

    @NotEmpty
    @BindView(R.id.edit_jobpostion)
    EditText edit_jobpostion;

    @NotEmpty
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

    @BindView(R.id.img_backarrow)
    ImageView img_backarrow;

    private JobListViewModel jobListViewModel;
    private static final int REQUEST_PHOTO_GALLERY = 4;
    private static final int CAMERAA = 1;
    public static final int RequestPermissionCode = 1;
    public String GalleryImage, globalImagePath;
    public String mProfileImageDecodableString;
    private File tempFile, file_name;
    private String currentPhotoPath = "";
    private String photoVar = null;
    private String mJobImage;
    MultipartBody.Part partImage;
    private Validator validator, validator_update;
    SimpleDialog simpleDialog;
    int job_id, mJob_id;

    private CreateJobAdViewModel createJobAdViewModel;
    private UserViewModel userViewModel;
    private JobSuccess_dialog jobSuccess_dialog;
    //    private String[] mSpinnerJobSector = {"Piolots", "ITSystems", "Security"};
    private String[] mSpinnerActive = {"How long would you like this listing", "12", "24"};

    private List<String> mSpinnerJobsector = new ArrayList<>();
    private List<String> mSpinnerJobtype = new ArrayList<>();
    private List<String> mSpinnerJobDuration = new ArrayList<>();
    String JobSector, JobType,JobDuration;

    private CompanyJobAdModel companyJobAdModel = new CompanyJobAdModel();
    ArrayAdapter<String> arraySectorAdapter, arrayJobDurationAdapter, arrayTypeAdapter;
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
        //   SettingJobSectorSpinner();

        postAd.setOnClickListener(this);
        tv_browsefiles.setOnClickListener(this);
        img_backarrow.setOnClickListener(this);

        init();
      //  showBackButton();
//        try{
//           // Uri uri=AppUtils.getUriToResource(getContext(),R.drawable.ic_user_profile);
//            Uri imageUrl = getURLForResource(R.drawable.ic_user_profile);
//            file_name = new File(imageUrl.getPath());
//            RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), file_name);
//            partImage = MultipartBody.Part.createFormData("job_image", file_name.getName(), reqFile);
//        }catch (Exception ex){
//            ex.printStackTrace();
//            Log.d("TAG", "onCreateView: "+ex);
//        }

        return view;
    }

    @SuppressLint("SetTextI18n")
    private void init() {
        jobListViewModel = ViewModelProviders.of(this, new CustomViewModelFactory(getActivity().getApplication(), getActivity())).get(JobListViewModel.class);
        createJobAdViewModel = ViewModelProviders.of(this, new CustomViewModelFactory(getActivity().getApplication(), getActivity())).get(CreateJobAdViewModel.class);
        userViewModel = ViewModelProviders.of(this, new CustomViewModelFactory(getActivity().getApplication())).get(UserViewModel.class);
        setPageTitle("Create a Job Listing");
User user = LoginUtils.getLoggedinUser();
        if ((getArguments() != null)) {
            setPageTitle("");
            postAd.setText("Update Job Listing");
           // setPageTitle("Update Job Listing");
            //  showBackButton();
            job_id = getArguments().getInt("job_id");
            if (job_id != 0) {
                GetJob_id(job_id);
                mJob_id = job_id;
            } else {
                companyJobAdModel = (CompanyJobAdModel) getArguments().getSerializable("COMPANY_AD");
                Log.d("TAAAG", GsonUtils.toJson(companyJobAdModel));
                AppUtils.setGlideImage(getContext(), profile_image, companyJobAdModel.getJobImage());
                //   String companyname = getsplitCompanyName(companyJobAdModel.getJobTitle());
              //  String jobtitle = getsplitTitle(companyJobAdModel.getJobTitle());
                edit_companyName.setText(LoginUtils.getUser().getCompany_name());
                edit_jobtitle.setText(companyJobAdModel.getJobTitle());
                edit_jobpostion.setText(companyJobAdModel.getPosition());
                edit_Location.setText(companyJobAdModel.getLocation());
                //  String SalaryInt = getsplitstring(String.valueOf(companyJobAdModel.getSalary()));
                DecimalFormat myFormatter = new DecimalFormat("############");
                edit_amount.setText(myFormatter.format(companyJobAdModel.getSalary()));
                edit_jobdescription.setText(companyJobAdModel.getContent());
                mJob_id = companyJobAdModel.getId();
                getSectorFromApi(LoginUtils.getUser().getWork_aviation());
                getJobTypes();
                // getSectorFromApi(LoginUtils.getUser().getWork_aviation());
            }
//            if (companyJobAdModel.getWeeklyHours() != null) {
//                int spinnerPosition = arraySectorAdapter.getPosition(companyJobAdModel.getWeeklyHours());
//                spinner_week.setSelection(spinnerPosition);
//            }
        }




        try
        {
            File path = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);

            file_name=new File(path,"job_image.jpg");
            InputStream inputStream = getResources().openRawResource(R.drawable.no_thumbnail);
            OutputStream out=new FileOutputStream(file_name);
            byte buf[]=new byte[1024];
            int len;
            while((len=inputStream.read(buf))>0)
                out.write(buf,0,len);
            out.close();
            inputStream.close();
        }
        catch (IOException e){
        }
      /*  try
        {
            file_name=new File(user.getUser_image());
            InputStream inputStream = getResources().openRawResource(R.drawable.no_thumbnail);
            OutputStream out=new FileOutputStream(file_name);
            byte buf[]=new byte[1024];
            int len;
            while((len=inputStream.read(buf))>0)
                out.write(buf,0,len);
            out.close();
            inputStream.close();
        }
        catch (IOException e){}*/

        if (file_name!=null) {
            RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), file_name);
            partImage = MultipartBody.Part.createFormData("job_image", file_name.getName(), reqFile);
        }
        edit_companyName.setText(LoginUtils.getUser().getCompany_name());
        getSectorFromApi(LoginUtils.getUser().getWork_aviation());
        getJobTypes();
        getJobDuration();

    }

    private void GetJob_id(Integer id) {
        jobListViewModel.getJobId(id).observe(getViewLifecycleOwner(), new Observer<BaseModel<List<SpecficJobAd>>>() {
            @Override
            public void onChanged(BaseModel<List<SpecficJobAd>> getjobAd) {
                if (getjobAd != null && !getjobAd.isError()) {
                    edit_companyName.setText(LoginUtils.getUser().getCompany_name());
                    edit_jobtitle.setText(getjobAd.getData().get(0).getJobTitle());
                    edit_jobpostion.setText(getjobAd.getData().get(0).getPosition());
                    edit_Location.setText(getjobAd.getData().get(0).getLocation());
                    mJobImage = getjobAd.getData().get(0).getJobImage();
                    companyJobAdModel.setJobSector(getjobAd.getData().get(0).getJobSector());
                    AppUtils.setGlideImage(getContext(), profile_image, getjobAd.getData().get(0).getJobImage());
                    //  String SalaryInt = getsplitstring(String.valueOf(companyJobAdModel.getSalary()));
                    DecimalFormat myFormatter = new DecimalFormat("############");
                    edit_amount.setText(myFormatter.format(getjobAd.getData().get(0).getSalary()));
                    edit_jobdescription.setText(getjobAd.getData().get(0).getContent());
                    companyJobAdModel.setJobType(getjobAd.getData().get(0).getJobType());
                    getSectorFromApi(LoginUtils.getUser().getWork_aviation());
                    getJobTypes();



                    // getSectorFromApi(LoginUtils.getUser().getWork_aviation());

                } else {
                    networkResponseDialog(getString(R.string.error), getString(R.string.err_unknown));
                }
                hideDialog();
            }
        });
    }

    private void getJobDuration() {
        mSpinnerJobDuration.add("12");
        mSpinnerJobDuration.add("24");
        mSpinnerJobDuration.add("37");

        SettingJobDurationSpinner();

    }

    private void SettingJobDurationSpinner() {
        arrayJobDurationAdapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_dropdown_item_1line, mSpinnerJobDuration);
        arrayJobDurationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_job_duration.setAdapter(arrayJobDurationAdapter);

//        if (companyJobAdModel.getActive_hours() != null) {
//            int spinnerPosition = arrayTypeAdapter.getPosition(companyJobAdModel.getActive_hours().toString());
//            spinner_job_duration.setSelection(spinnerPosition);
//        }

        spinner_job_duration.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                JobDuration = parent.getItemAtPosition(position).toString();
                Log.d("TAG", "onItemSelected: "+JobDuration);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });
    }


    private void getSectorFromApi(String aviation_type) {
        userViewModel.getSector(aviation_type).observe(getViewLifecycleOwner(), new Observer<BaseModel<List<String>>>() {
            @Override
            public void onChanged(BaseModel<List<String>> listBaseModel) {
                if (listBaseModel.getData() != null && !listBaseModel.isError()) {
                    mSpinnerJobsector.addAll(listBaseModel.getData());
                    SettingJobSectorSpinner();
                    Log.d("TAAAG", "" + mSpinnerJobsector);
                } else {
                    networkResponseDialog(getString(R.string.error), getString(R.string.err_unknown));
                }
                hideDialog();
            }
        });
    }

    private void getJobTypes() {
        createJobAdViewModel.getJobType().observe(getViewLifecycleOwner(), new Observer<BaseModel<List<String>>>() {
            @Override
            public void onChanged(BaseModel<List<String>> listBaseModel) {
                if (listBaseModel.getData() != null && !listBaseModel.isError()) {
                    mSpinnerJobtype.addAll(listBaseModel.getData());
                    SettingJobTypeSpinner();
                    Log.d("TAAAG", "" + mSpinnerJobtype);
                } else {
                    networkResponseDialog(getString(R.string.error), getString(R.string.err_unknown));
                }
                hideDialog();
            }
        });
    }

    public String getsplitCompanyName(String value) {
        String[] parts = value.split(Pattern.quote("for"));
        value = parts[0]; // 004
        return value;
    }

    public String getsplitTitle(String value) {
        String[] parts = value.split(Pattern.quote("for"));
        value = parts[0]; // 004
        return value;
    }

    public String getsplitstring(String value) {
        String[] parts = value.split(Pattern.quote("."));
        value = parts[0]; // 004
        return value;
    }


    private void SettingJobSectorSpinner() {
        arraySectorAdapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_dropdown_item_1line, mSpinnerJobsector);
        arraySectorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_jobsector.setAdapter(arraySectorAdapter);

        if (companyJobAdModel.getJobSector() != null) {
            int spinnerPosition = arraySectorAdapter.getPosition(companyJobAdModel.getJobSector());
            spinner_jobsector.setSelection(spinnerPosition);
        }

        spinner_jobsector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                JobSector = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });
        //  spinner_jobsector.setSelection(0);
//        if ((getArguments() != null)) {
//
//            if (companyJobAdModel.getJobSector() != null) {
//                int spinnerPosition = arraySectorAdapter.getPosition(companyJobAdModel.getJobSector());
//                spinner_jobsector.setSelection(0);
//            }
//        }
    }

    private void SettingJobTypeSpinner() {
        arrayTypeAdapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_dropdown_item_1line, mSpinnerJobtype);
        arrayTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_jobtype.setAdapter(arrayTypeAdapter);

        if (companyJobAdModel.getJobType() != null) {
            int spinnerPosition = arrayTypeAdapter.getPosition(companyJobAdModel.getJobType());
            spinner_jobtype.setSelection(spinnerPosition);
        }

        spinner_jobtype.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                JobType = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });
        //  spinner_jobsector.setSelection(0);
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
                if (postAd.getText().toString().equals("Update Job Listing")) {
                    if (job_id != 0) {
                        if (JobSector == null) {
                            Toast.makeText(getContext(), "Please select job sector", Toast.LENGTH_SHORT).show();
                        } else if (JobType == null) {
                            Toast.makeText(getContext(), "Please select job type", Toast.LENGTH_SHORT).show();
                        } else if (partImage == null) {
                            Toast.makeText(getContext(), "Please add image for job post", Toast.LENGTH_SHORT).show();
                        }*/ else {
                            validator.validate();
                        }

                    } else {
                        if (JobSector == null) {
                            Toast.makeText(getContext(), "Please select job sector", Toast.LENGTH_SHORT).show();
                        } else if (JobType == null) {
                            Toast.makeText(getContext(), "Please select job type", Toast.LENGTH_SHORT).show();
                        } else if (partImage == null ) {
                            Toast.makeText(getContext(), "Please add image for job post", Toast.LENGTH_SHORT).show();
                        }*/ else {
                            validator.validate();
                        }
                    }

                } else {
                    if (JobSector == null) {
                        Toast.makeText(getContext(), "Please select job sector", Toast.LENGTH_SHORT).show();
                    } else if (JobType == null) {
                        Toast.makeText(getContext(), "Please select job type", Toast.LENGTH_SHORT).show();
                    } /*else if (partImage == null) {
                        Toast.makeText(getContext(), "Please add image for job post", Toast.LENGTH_SHORT).show();
                    }*/ else {
                        validator.validate();
                    }
                }

                break;
            case R.id.tv_browsefiles:
                openPictureDialog();
                break;
            case R.id.img_backarrow:
                getActivity().onBackPressed();
                break;
        }
    }

    private void UpdateJobAd() {
        showDialog();
        User user = LoginUtils.getLoggedinUser();
        //   edit_jobtitle.setText(edit_jobpostion.getText().toString() + " for " + edit_companyName.getText().toString());
        createJobAdViewModel.UpdateJobAd(mJob_id, user, partImage, JobSector,
                Integer.parseInt(edit_amount.getText().toString()),
                edit_companyName.getText().toString(),
                edit_jobdescription.getText().toString(),
                edit_Location.getText().toString(),
                edit_jobtitle.getText().toString(),
                edit_jobpostion.getText().toString(), JobType,JobDuration).observe(this, new Observer<BaseModel<List<Object>>>() {
            @Override
            public void onChanged(BaseModel<List<Object>> getnetworkconnection) {
                if (getnetworkconnection != null && !getnetworkconnection.isError()) {
                    simpleDialog = new SimpleDialog(getActivity(), getString(R.string.success), getString(R.string.msg_jobAd_update), null, getString(R.string.ok), new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            getActivity().onBackPressed();
                            //loadFragment(R.id.framelayout, new JobListingFragment(), getContext(), false);
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
        // edit_jobtitle.setText(edit_jobpostion.getText().toString() + " for " + edit_companyName.getText().toString());
        createJobAdViewModel.createJobAd(user, partImage, JobSector,
                Integer.parseInt(edit_amount.getText().toString()),
                edit_companyName.getText().toString(),
                edit_jobdescription.getText().toString(),
                edit_Location.getText().toString(),
                edit_jobtitle.getText().toString(),
                edit_jobpostion.getText().toString(), JobType,JobDuration).observe(this, new Observer<BaseModel<List<Object>>>() {
            @Override
            public void onChanged(BaseModel<List<Object>> getnetworkconnection) {
                if (getnetworkconnection != null && !getnetworkconnection.isError()) {
                    jobSuccess_dialog = new JobSuccess_dialog(requireActivity(),getContext());
                    jobSuccess_dialog.show();
                    hideDialog();
                } else {
                    networkResponseDialog(getString(R.string.error), getString(R.string.err_unknown));
                }
                hideDialog();
                if (simpleDialog !=null && !simpleDialog.isShowing())
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
        if (postAd.getText().toString().equals("Update Job Listing")) {
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