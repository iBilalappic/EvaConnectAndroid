package com.hypernym.evaconnect.view.ui.activities;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.hypernym.evaconnect.R;
import com.hypernym.evaconnect.constants.AppConstants;
import com.hypernym.evaconnect.listeners.OnOneOffClickListener;
import com.hypernym.evaconnect.models.BaseModel;
import com.hypernym.evaconnect.models.User;
import com.hypernym.evaconnect.models.UserDetails;
import com.hypernym.evaconnect.repositories.CustomViewModelFactory;
import com.hypernym.evaconnect.utils.Constants;
import com.hypernym.evaconnect.utils.ImageFilePathUtil;
import com.hypernym.evaconnect.utils.LoginUtils;
import com.hypernym.evaconnect.utils.NetworkUtils;
import com.hypernym.evaconnect.view.bottomsheets.BottomSheetPictureSelection;
import com.hypernym.evaconnect.view.dialogs.SimpleDialog;
import com.hypernym.evaconnect.viewmodel.UserViewModel;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.onesignal.OneSignal;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;

public class SignupActivity_2 extends BaseActivity implements Validator.ValidationListener {

    String email, password, photourl, activity_type, user_type, aviation_type, JobSector, username;
    private UserViewModel userViewModel;
    private User user = new User();


    @NotEmpty
    @BindView(R.id.edt_companyname)
    EditText edt_companyname;

    @NotEmpty
    @BindView(R.id.edt_jobtitle)
    EditText edt_jobtitle;


    @BindView(R.id.tv_biodata)
    EditText tv_biodata;

    @NotEmpty
    @BindView(R.id.btn_finish)
    Button btn_finish;

    @BindView(R.id.img_profile)
    CircleImageView img_profile;


    private Validator validator;
    SimpleDialog simpleDialog;


    private static final int REQUEST_PHOTO_GALLERY = 4;
    private static final int CAMERAA = 1;
    public static final int RequestPermissionCode = 1;
    private String GalleryImage, mCurrentPhotoPath, globalImagePath;
    private String mProfileImageDecodableString;
    private File tempFile, file_name;
    private String currentPhotoPath = "";
    private String photoVar = null;
    MultipartBody.Part partImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_2);
        ButterKnife.bind(this);
        validator = new Validator(this);
        validator.setValidationListener(this);
        init();

    }

    private void init() {
        userViewModel = ViewModelProviders.of(this, new CustomViewModelFactory(this.getApplication())).get(UserViewModel.class);

        if ("LinkedinActivity".equals(getIntent().getStringExtra(Constants.ACTIVITY_NAME))) {
            email = getIntent().getStringExtra("Email");
            photourl = getIntent().getStringExtra("Photo");
            user_type = getIntent().getStringExtra("userType");
            aviation_type = getIntent().getStringExtra("aviation_type");
            JobSector = getIntent().getStringExtra("job_sector");
            username = getIntent().getStringExtra("username");
            user.setUsername(email);
            user.setEmail(email);
            user.setPassword("hypernym");
            user.setIsLinkedin(1);
            user.setLinkedin_image_url(photourl);
            user.setType(user_type);
            user.setWork_aviation(aviation_type);
            user.setFirst_name(username);
            user.setSector(JobSector);
            Glide.with(this).load(photourl).into(img_profile);
            img_profile.setEnabled(false);
        } else {
            email = getIntent().getStringExtra("Email");
            password = getIntent().getStringExtra("Password");
            user_type = getIntent().getStringExtra("userType");
            aviation_type = getIntent().getStringExtra("aviation_type");
            JobSector = getIntent().getStringExtra("job_sector");
            username = getIntent().getStringExtra("username");
            user.setWork_aviation(aviation_type);
            user.setUsername(email);
            user.setEmail(email);
            user.setPassword(password);
            user.setIsLinkedin(0);
            user.setType(user_type);
            user.setFirst_name(username);
            user.setSector(JobSector);
            user.setLinkedin_image_url("");

        }

        btn_finish.setOnClickListener(new OnOneOffClickListener() {
            @Override
            public void onSingleClick(View v) {
                validator.validate();


            }
        });
        img_profile.setOnClickListener(new OnOneOffClickListener() {
            @Override
            public void onSingleClick(View v) {
                if (Checkpermission()) {
                    BottomSheetPictureSelection bottomSheetPictureSelection = new BottomSheetPictureSelection(new YourDialogFragmentDismissHandler());
                    bottomSheetPictureSelection.show(getSupportFragmentManager(), bottomSheetPictureSelection.getTag());
                } else {
                    requestpermission();
                }
            }
        });


    }


    @Override
    public void onValidationSucceeded() {
        if (!tv_biodata.getText().toString().isEmpty() || tv_biodata.getText().toString().length() > 0) {
            user.setBio_data(tv_biodata.getText().toString());
        } else {
            user.setBio_data("");
        }
        user.setCompany_name(edt_companyname.getText().toString());
        user.setDesignation(edt_jobtitle.getText().toString());
        user.setStatus(AppConstants.USER_STATUS);
        if (NetworkUtils.isNetworkConnected(this)) {
            showDialog();
            callSignupApi();
        } else {
            networkErrorDialog();
        }

    }

    public void callSignupApi() {
        userViewModel.signUp(user, partImage).observe(this, new Observer<BaseModel<List<User>>>() {
            @Override
            public void onChanged(BaseModel<List<User>> logins) {
                if (logins != null && !logins.isError()) {
                    LoginUtils.saveUser(user);
                    if ("LinkedinActivity".equals(getIntent().getStringExtra(Constants.ACTIVITY_NAME))) {
                        JustLoginApiCall();
                    } else {
                        simpleDialog = new SimpleDialog(SignupActivity_2.this, getString(R.string.success), getString(R.string.msg_signup), null, getString(R.string.ok), new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                callLoginApi();

                            }
                        });
                        simpleDialog.show();
                    }

                } else {
                    simpleDialog = networkResponseDialog(getString(R.string.error), logins.getMessage());
                }
            }
        });
    }

    private void JustLoginApiCall() {

        if (NetworkUtils.isNetworkConnected(this)) {
//            showDialog();
            userViewModel.linkedin_login(email).observe(this, new Observer<BaseModel<List<User>>>() {
                @Override
                public void onChanged(BaseModel<List<User>> listBaseModel) {
                    LoginUtils.userLoggedIn();
                    User userData = listBaseModel.getData().get(0);
                    userData.setUser_id(userData.getId());
                    LoginUtils.saveUser(listBaseModel.getData().get(0));
                    OneSignal.sendTag("email", userData.getEmail());
                    UserDetails.username = userData.getFirst_name();
                    if (listBaseModel.getData().get(0) != null) {
                        LoginUtils.saveUserToken(listBaseModel.getData().get(0).getToken());
                    }

                    Intent intent = new Intent(SignupActivity_2.this, HomeActivity.class);
                    // set the new task and clear flags
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
            });
        }
    }

    private void callLoginApi() {
        showDialog();
        userViewModel.login(user).observe(this, new Observer<BaseModel<List<User>>>() {
            @Override
            public void onChanged(BaseModel<List<User>> user) {
                if (user != null && !user.isError() && user.getData().get(0) != null) {
                    LoginUtils.userLoggedIn();

                    User userData = user.getData().get(0);
                    userData.setUser_id(userData.getId());
                    LoginUtils.saveUser(user.getData().get(0));
                    OneSignal.sendTag("email", userData.getEmail());
                    UserDetails.username = userData.getFirst_name();
                    if (user.getData().get(0) != null) {
                        LoginUtils.saveUserToken(user.getData().get(0).getToken());
                    }
                    simpleDialog.dismiss();
                    Intent intent = new Intent(SignupActivity_2.this, HomeActivity.class);
                    // set the new task and clear flags
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);

                } else if (user != null && user.isError()) {
                    networkResponseDialog(getString(R.string.error), getString(R.string.err_login));
                } else if (user == null) {
                    networkResponseDialog(getString(R.string.error), getString(R.string.err_unknown));
                }
                hideDialog();
            }
        });
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


    public boolean Checkpermission() {

        int ExternalReadResult = ContextCompat.checkSelfPermission(this, READ_EXTERNAL_STORAGE);
        int CameraResult = ContextCompat.checkSelfPermission(this, CAMERA);
        return ExternalReadResult == PackageManager.PERMISSION_GRANTED &&
                CameraResult == PackageManager.PERMISSION_GRANTED;
    }


    protected class YourDialogFragmentDismissHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 50) {
                LaunchGallery();
            } else if (msg.what == 45) {
                takePhotoFromCamera();
            }

        }
    }

    private void LaunchGallery() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("*/*");
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), REQUEST_PHOTO_GALLERY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Result code is RESULT_OK only if the user selects an Image
        if (requestCode == REQUEST_PHOTO_GALLERY && resultCode == RESULT_OK) {
            try {
                if (data != null && data.getData() != null) {
                    Uri SelectedImageUri = data.getData();

                    GalleryImage = ImageFilePathUtil.getPath(this, SelectedImageUri);
                    mProfileImageDecodableString = ImageFilePathUtil.getPath(this, SelectedImageUri);
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
                            file_name = new File(ImageFilePathUtil.getPath(this, SelectedImageUri));
                            RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), file_name);
                            partImage = MultipartBody.Part.createFormData("user_image", file_name.getName(), reqFile);
                            if (!TextUtils.isEmpty(currentPhotoPath) || currentPhotoPath != null) {
                                  Glide.with(this).load(currentPhotoPath).into(img_profile);

                            } else {
                                networkResponseDialog(getString(R.string.error), getString(R.string.err_internal_supported));
                            }
                        } else {
                            networkResponseDialog(getString(R.string.error), getString(R.string.err_one_file_at_a_time));
                            return;
                        }
                    }
                } else {
                    Toast.makeText(this, "Something went wrong while retrieving image", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception exc) {
                exc.printStackTrace();
                Log.e(getClass().getName(), "exc: " + exc.getMessage());
            }
        } else {
            if (requestCode == CAMERAA && resultCode == RESULT_OK) {

                //mIsProfileImageAdded = true;
                galleryAddPic();
                RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), file_name);
                // imgName = file_name.getName();
                globalImagePath = file_name.getAbsolutePath();
                if (file_name.length() / AppConstants.ONE_THOUSAND_AND_TWENTY_FOUR > AppConstants.IMAGE_SIZE_IN_KB) {
                    networkResponseDialog(getString(R.string.error), getString(R.string.err_image_size_large));
                    return;
                }
                if (!TextUtils.isEmpty(globalImagePath) || globalImagePath != null) {

                    Glide.with(this).load(loadFromFile(globalImagePath))
                            .apply(new RequestOptions())
                            .into(img_profile);

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
                    partImage = MultipartBody.Part.createFormData("user_image", file_name.getName(), reqFile);
                    //AppUtils.showSnackBar(getView(), AppUtils.getErrorMessage(getContext(), 8));

                }
            }
        }
    }

    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(mCurrentPhotoPath);
        file_name = f;
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        sendBroadcast(mediaScanIntent);
    }

    public static Bitmap loadFromFile(String filename) {
        try {
            File f = new File(filename);
            if (!f.exists()) {
                return null;
            }
            Bitmap tmp = BitmapFactory.decodeFile(filename);
            return tmp;
        } catch (Exception e) {
            return null;
        }
    }

    private void takePhotoFromCamera() {
        Intent takePictureIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        // Create the File where the photo should go
        File photoFile = null;
        try {
            photoFile = createImageFile();
        } catch (IOException ex) {
            // Error occurred while creating the File
        }
        if (Checkpermission()) {
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.hypernym.evaconnect.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, CAMERAA);

            }
        } else {
            // AppUtils.showSnackBar(getView(), AppUtils.getErrorMessage(getContext(), 11));
            requestpermission();
        }

    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void requestpermission() {
        ActivityCompat.requestPermissions(this, new String[]
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


}
