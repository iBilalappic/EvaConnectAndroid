package com.hypernym.evaconnect.view.ui.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.hypernym.evaconnect.BuildConfig;
import com.hypernym.evaconnect.R;
import com.hypernym.evaconnect.constants.AppConstants;
import com.hypernym.evaconnect.models.BaseModel;
import com.hypernym.evaconnect.models.JobAd;
import com.hypernym.evaconnect.models.User;
import com.hypernym.evaconnect.repositories.CustomViewModelFactory;
import com.hypernym.evaconnect.utils.AppUtils;
import com.hypernym.evaconnect.utils.ImageFilePathUtil;
import com.hypernym.evaconnect.utils.LoginUtils;
import com.hypernym.evaconnect.view.dialogs.ApplicationSuccess_dialog;
import com.hypernym.evaconnect.view.dialogs.SearchDialog;
import com.hypernym.evaconnect.view.dialogs.SimpleDialog;
import com.hypernym.evaconnect.viewmodel.ApplicationSubmitViewModel;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.net.URI;
import java.nio.ByteBuffer;
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

    @BindView(R.id.tv_positionName)
    TextView tv_positionName;

    @NotEmpty
    @BindView(R.id.edit_coverletter)
    EditText edit_coverletter;

    @BindView(R.id.browsefiles)
    TextView browsefiles;

    @BindView(R.id.tv_cvname)
    TextView tv_cvname;

    @BindView(R.id.tv_apply)
    TextView tv_apply;

    @BindView(R.id.img_backarrow)
    ImageView img_backarrow;

    ApplicationSuccess_dialog applicationSuccess_dialog;

    String mProfileImageDecodableString;
    private File tempFile, file_name;
    private String currentPhotoPath = "";
    private String photoVar = null;
    MultipartBody.Part partImage;
    private Validator validator;
    private ApplicationSubmitViewModel applicationSubmitViewModel;

    private static final int OPEN_DIRECTORY_REQUEST_CODE = 1;

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
        img_backarrow.setOnClickListener(this);
        init();
        return view;
    }

    private void init() {
       // setPageTitle("Job Details");
       //  showBackButton();
        if ((getArguments() != null)) {

            jobAd = (JobAd) getArguments().getSerializable("JOB_AD");
            AppUtils.setGlideImage(getContext(), profile_image, jobAd.getJobImage());
            tv_name.setText(jobAd.getJobTitle());
            tv_positionName.setText(jobAd.getPosition());
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
            case R.id.img_backarrow:
                getActivity().onBackPressed();
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
                    applicationSuccess_dialog = new ApplicationSuccess_dialog(requireActivity(),getContext());
                    applicationSuccess_dialog.show();

//                    simpleDialog = new SimpleDialog(getActivity(), getString(R.string.success), getString(R.string.msg_application_form), null, getString(R.string.ok), new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                             onBackPressed();
//
//                            if (getFragmentManager().getBackStackEntryCount() != 0) {
//                                getFragmentManager().popBackStack();
//                            }
//                            simpleDialog.dismiss();
//                        }
//                    });
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

    public void LaunchGallery() {
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

                    mProfileImageDecodableString = SelectedImageUri.getPath();

                    tempFile = getFile(getActivity(),SelectedImageUri);

                    Log.e(getClass().getName(), "file path details: " + tempFile.getName() + " " + tempFile.getAbsolutePath() + "length" + tempFile.length());


                    if (tempFile.length() / AppConstants.ONE_THOUSAND_AND_TWENTY_FOUR > AppConstants.FILE_SIZE_LIMIT_IN_KB) {
                        networkResponseDialog(getString(R.string.error), getString(R.string.err_image_size_large));
                        return;
                    }
                    else if(tempFile.toString().equalsIgnoreCase("File path not found"))
                    {
                        networkResponseDialog(getString(R.string.error), getString(R.string.err_internal_storage));
                       // tv_cvname.setText(tempFile.toString());
                        partImage=null;
                        return;
                    }
                    else {
                        if (photoVar == null) {
                            currentPhotoPath = mProfileImageDecodableString;
                            // photoVar = GalleryImage;
                            file_name = getFile(getActivity(),SelectedImageUri);
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


    public static File getFile(Context context, Uri uri) throws IOException {
        File destinationFilename = new File(context.getFilesDir().getPath() + File.separatorChar + queryName(context, uri));
        try (InputStream ins = context.getContentResolver().openInputStream(uri)) {
            createFileFromStream(ins, destinationFilename);
        } catch (Exception ex) {
            Log.e("Save File", ex.getMessage());
            ex.printStackTrace();
        }
        return destinationFilename;
    }

    public static void createFileFromStream(InputStream ins, File destination) {
        try (OutputStream os = new FileOutputStream(destination)) {
            byte[] buffer = new byte[4096];
            int length;
            while ((length = ins.read(buffer)) > 0) {
                os.write(buffer, 0, length);
            }
            os.flush();
        } catch (Exception ex) {
            Log.e("Save File", ex.getMessage());
            ex.printStackTrace();
        }
    }

    private static String queryName(Context context, Uri uri) {
        Cursor returnCursor =
                context.getContentResolver().query(uri, null, null, null, null);
        assert returnCursor != null;
        int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
        returnCursor.moveToFirst();
        String name = returnCursor.getString(nameIndex);
        returnCursor.close();
        return name;
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

