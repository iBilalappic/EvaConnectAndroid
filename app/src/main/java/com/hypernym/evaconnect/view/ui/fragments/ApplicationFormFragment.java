package com.hypernym.evaconnect.view.ui.fragments;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.hypernym.evaconnect.R;
import com.hypernym.evaconnect.constants.AppConstants;
import com.hypernym.evaconnect.models.BaseModel;
import com.hypernym.evaconnect.models.JobAd;
import com.hypernym.evaconnect.models.MyLikesModel;
import com.hypernym.evaconnect.models.User;
import com.hypernym.evaconnect.repositories.CustomViewModelFactory;
import com.hypernym.evaconnect.utils.AppUtils;
import com.hypernym.evaconnect.utils.DateUtils;
import com.hypernym.evaconnect.utils.ImageFilePathUtil;
import com.hypernym.evaconnect.utils.LoginUtils;
import com.hypernym.evaconnect.view.adapters.MyLikeAdapter;
import com.hypernym.evaconnect.view.bottomsheets.BottomSheetPictureSelection;
import com.hypernym.evaconnect.view.dialogs.SimpleDialog;
import com.hypernym.evaconnect.view.ui.activities.SignupDetailsActivity;
import com.hypernym.evaconnect.viewmodel.ApplicationSubmitViewModel;
import com.hypernym.evaconnect.viewmodel.CreateJobAdViewModel;
import com.hypernym.evaconnect.viewmodel.MylikesViewModel;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.app.Activity.RESULT_OK;

public class ApplicationFormFragment extends BaseFragment implements View.OnClickListener, Validator.ValidationListener {
    private static final int REQUEST_PHOTO_GALLERY = 4;
    SimpleDialog simpleDialog;

    public ApplicationFormFragment() {
        // Required empty public constructor
    }

    private JobAd jobAd = new JobAd();

    @BindView(R.id.profile_image)
    CircleImageView profile_image;

    @BindView(R.id.tv_name)
    TextView tv_name;

    @BindView(R.id.tv_minago)
    TextView tv_minago;

    @BindView(R.id.tv_positionName)
    TextView tv_positionName;

    @BindView(R.id.tv_createddateTime)
    TextView tv_createddateTime;
    @NotEmpty
    @BindView(R.id.edit_coverletter)
    EditText edit_coverletter;

    @BindView(R.id.browsefiles)
    TextView browsefiles;

    @BindView(R.id.tv_cvname)
    TextView tv_cvname;

    @BindView(R.id.tv_apply)
    TextView tv_apply;

    String mProfileImageDecodableString;
    private File tempFile, file_name;
    private String currentPhotoPath = "";
    private String photoVar = null;
    MultipartBody.Part partImage;
    private Validator validator;
    private ApplicationSubmitViewModel applicationSubmitViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_applicationform, container, false);
        ButterKnife.bind(this, view);
        validator = new Validator(this);
        validator.setValidationListener(this);
        browsefiles.setOnClickListener(this);
        tv_apply.setOnClickListener(this);
        init();
        return view;
    }

    private void init() {
        if ((getArguments() != null)) {
            setPageTitle("");
            showBackButton();
            jobAd = (JobAd) getArguments().getSerializable("JOB_AD");
            AppUtils.setGlideImage(getContext(), profile_image, jobAd.getJobImage());
            tv_name.setText(jobAd.getJobTitle());
            tv_positionName.setText(jobAd.getPosition());
            tv_createddateTime.setText(DateUtils.getFormattedDateTime(jobAd.getCreatedDatetime()));
            tv_minago.setText(DateUtils.getTimeAgo(jobAd.getCreatedDatetime()));
        }
        applicationSubmitViewModel = ViewModelProviders.of(this, new CustomViewModelFactory(getActivity().getApplication(), getActivity())).get(ApplicationSubmitViewModel.class);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.browsefiles:
                if (Checkpermission()) {
                    LaunchGallery();
                } else {
                    requestpermission();
                }
                break;
            case R.id.tv_apply:
                if (partImage == null) {
                    Toast.makeText(getContext(), "Please attach cv for job post", Toast.LENGTH_SHORT).show();
                } else {
                    validator.validate();
                }
                break;
        }
    }

    private void SubmitApplicationForm() {
        showDialog();
        User user = LoginUtils.getLoggedinUser();
        applicationSubmitViewModel.submitApplication(user, jobAd.getId(), edit_coverletter.getText().toString(), partImage).observe(this, new Observer<BaseModel<List<Object>>>() {
            @Override
            public void onChanged(BaseModel<List<Object>> getnetworkconnection) {
                if (getnetworkconnection != null && !getnetworkconnection.isError()) {
                    simpleDialog = new SimpleDialog(getActivity(), getString(R.string.success), getString(R.string.msg_application_form), null, getString(R.string.ok), new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // onBackPressed();
                            if (getFragmentManager().getBackStackEntryCount() != 0) {
                                getFragmentManager().popBackStack();
                            }
                            simpleDialog.dismiss();
                        }
                    });
                    hideDialog();
                } else {
                    networkResponseDialog(getString(R.string.error), getString(R.string.err_unknown));
                }
                hideDialog();
                if (simpleDialog!=null && !simpleDialog.isShowing())
                    simpleDialog.show();
            }

        });
    }

    private void LaunchGallery() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("application/msword");
        intent.setType("application/pdf");
        startActivityForResult(Intent.createChooser(intent, "Select File"), REQUEST_PHOTO_GALLERY);
    }


    public boolean Checkpermission() {

        int ExternalReadResult = ContextCompat.checkSelfPermission(getContext(), READ_EXTERNAL_STORAGE);
        int CameraResult = ContextCompat.checkSelfPermission(getContext(), CAMERA);
        return ExternalReadResult == PackageManager.PERMISSION_GRANTED &&
                CameraResult == PackageManager.PERMISSION_GRANTED;
    }

    private void requestpermission() {
        ActivityCompat.requestPermissions(getActivity(), new String[]
                {
                        READ_EXTERNAL_STORAGE,
                        CAMERA
                }, RequestPermissionCode);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {

            case RequestPermissionCode:

                if (grantResults.length > 0) {

                    boolean ReadPermission = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean CameraPermission = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                    if (ReadPermission && CameraPermission) {
                        // Toast.makeText(HomeActivity.this, "Permission Granted", Toast.LENGTH_LONG).show();
                    } else {
                        //Toast.makeText(HomeActivity.this,"Permission Denied",Toast.LENGTH_LONG).show();
                    }
                }

                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Result code is RESULT_OK only if the user selects an Image
        if (requestCode == REQUEST_PHOTO_GALLERY && resultCode == RESULT_OK) {
            try {
                if (data != null && data.getData() != null) {
                    Uri SelectedImageUri = data.getData();

                    mProfileImageDecodableString = ImageFilePathUtil.getPath(getContext(), SelectedImageUri);

                    tempFile = new File(mProfileImageDecodableString);

                    Log.e(getClass().getName(), "file path details: " + tempFile.getName() + " " + tempFile.getAbsolutePath() + "length" + tempFile.length());


                    if (tempFile.length() / AppConstants.ONE_THOUSAND_AND_TWENTY_FOUR > AppConstants.FILE_SIZE_LIMIT_IN_KB) {
                        networkResponseDialog(getString(R.string.error), getString(R.string.err_image_size_large));
                        return;
                    }
                    else if(tempFile.toString().equalsIgnoreCase("File path not found"))
                    {
                        tv_cvname.setText(tempFile.toString());
                        partImage=null;
                        return;
                    }
                    else {
                        if (photoVar == null) {
                            currentPhotoPath = mProfileImageDecodableString;
                            // photoVar = GalleryImage;
                            file_name = new File(ImageFilePathUtil.getPath(getContext(), SelectedImageUri));
                            RequestBody reqFile = RequestBody.create(MediaType.parse("file/*"), file_name);
                            partImage = MultipartBody.Part.createFormData("application_attachment", file_name.getName(), reqFile);
                            if (!TextUtils.isEmpty(currentPhotoPath) || currentPhotoPath != null) {
                                //setting file path
                                tv_cvname.setText(file_name.getName());
                                //   Glide.with(this).load(currentPhotoPath).into(img_profile);

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
        }
    }

    @Override
    public void onValidationSucceeded() {
        SubmitApplicationForm();
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error : errors) {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(getContext());
            // Display error messages
            if (view.getId() == R.id.edit_coverletter) {
                message = getString(R.string.err_coverletter);
            }
            if (view instanceof EditText) {
                ((EditText) view).setError(message);
            } else {
                Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
            }
        }
    }
}

