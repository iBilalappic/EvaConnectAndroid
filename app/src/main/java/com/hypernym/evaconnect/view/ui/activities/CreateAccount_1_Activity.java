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
import android.widget.TextView;
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
import com.hypernym.evaconnect.repositories.CustomViewModelFactory;
import com.hypernym.evaconnect.utils.Constants;
import com.hypernym.evaconnect.utils.ImageFilePathUtil;
import com.hypernym.evaconnect.view.bottomsheets.BottomSheetPictureSelection;
import com.hypernym.evaconnect.viewmodel.UserViewModel;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.theartofdev.edmodo.cropper.CropImage;

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

public class CreateAccount_1_Activity extends BaseActivity implements Validator.ValidationListener, View.OnClickListener {

    @BindView(R.id.btn_next)
    Button btn_next;

    @BindView(R.id.tv_company)
    TextView tv_company;

    @BindView(R.id.tv_individual)
    TextView tv_individual;

    @NotEmpty
    @BindView(R.id.edt_firstname)
    EditText edt_firstname;

    @NotEmpty
    @BindView(R.id.edt_email)
    EditText edt_email;

    @BindView(R.id.tv_upload_image)
    TextView tv_upload_image;

    @BindView(R.id.tv_already_account)
    TextView tv_already_account;

    @NotEmpty
    @BindView(R.id.edt_surname)
    EditText edt_surname;

    @BindView(R.id.img_profile)
    CircleImageView img_profile;


    @BindView(R.id.img_backarrow)
    ImageView img_backarrow;

    @BindView(R.id.img_cross)
    ImageView img_cross;


    private static final int REQUEST_PHOTO_GALLERY = 4;
    private static final int CAMERAA = 1;
    public static final int RequestPermissionCode = 1;
    private String GalleryImage, mCurrentPhotoPath, globalImagePath;
    private String mProfileImageDecodableString;
    private File tempFile, file_name;
    private String currentPhotoPath = "";
    private String photoVar = null;
    MultipartBody.Part partImage;
    Uri SelectedUri;
    private UserViewModel userViewModel;

    private Validator validator;

    private String userType = "user";

    String email, password, photourl, activity_type,path;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account_1);
        ButterKnife.bind(this);
        init();
        btn_next.setOnClickListener(new OnOneOffClickListener() {
            @Override
            public void onSingleClick(View v) {
                validator.validate();
                Log.d("TAAAG",""+ file_name);
            }
        });
    }

    private void init() {
        userViewModel = ViewModelProviders.of(this, new CustomViewModelFactory(getApplication(), this)).get(UserViewModel.class);

        validator = new Validator(this);
        validator.setValidationListener(this);
        img_backarrow.setOnClickListener(this);
        tv_company.setOnClickListener(this);
        tv_individual.setOnClickListener(this);
        img_cross.setOnClickListener(this);
        img_backarrow.setOnClickListener(this);
        tv_upload_image.setOnClickListener(this);
        tv_already_account.setOnClickListener(this);
        tv_company.setBackground(getDrawable(R.drawable.rounded_button_border));
        tv_individual.setBackground(getDrawable(R.drawable.rounded_button_red));

        String type = getIntent().getStringExtra(Constants.ACTIVITY_NAME);

        if ("LinkedinActivity".equals(getIntent().getStringExtra(Constants.ACTIVITY_NAME))) {
            email = getIntent().getStringExtra("Email");
            photourl = getIntent().getStringExtra("Photo");
            path = getIntent().getStringExtra("Path");
            activity_type = "LinkedinActivity";
            Glide.with(this).load(photourl).into(img_profile);
            tv_upload_image.setEnabled(false);
            edt_email.setText(email);
            edt_email.setEnabled(false);

        }
        else if (!TextUtils.isEmpty(type) && type.equals(AppConstants.FACEBOOK_LOGIN_TYPE)){
            email = getIntent().getStringExtra("Email");
            photourl = getIntent().getStringExtra("Photo");
            path = getIntent().getStringExtra("Path");
            activity_type = AppConstants.FACEBOOK_LOGIN_TYPE;
            Glide.with(this).load(photourl).into(img_profile);
            tv_upload_image.setEnabled(false);
            edt_email.setText(email);
            edt_email.setEnabled(false);
        }
        else {
            edt_email.setText("");
            edt_email.setEnabled(true);
            //email = getIntent().getStringExtra("Email");
            activity_type = "normal_type";
        }
    }


    @Override
    public void onValidationSucceeded() {

        if (activity_type.equals("LinkedinActivity")) {
            Intent intent = new Intent(CreateAccount_1_Activity.this, CreateAccount_2_Activity.class);
            intent.putExtra("Email", edt_email.getText().toString());
            intent.putExtra("Photo", photourl);
            intent.putExtra("Path", path);
            intent.putExtra("userType", userType);
            intent.putExtra("FirstName", edt_firstname.getText().toString());
            intent.putExtra("SurName", edt_surname.getText().toString());
            intent.putExtra(Constants.ACTIVITY_NAME, activity_type);
            startActivity(intent);

        }
        else if (activity_type.equals(AppConstants.FACEBOOK_LOGIN_TYPE)){
            Intent intent = new Intent(CreateAccount_1_Activity.this, CreateAccount_2_Activity.class);
            intent.putExtra("Email", edt_email.getText().toString());
            intent.putExtra("Photo", photourl);
            intent.putExtra("Path", path);
            intent.putExtra("userType", userType);
            intent.putExtra("FirstName", edt_firstname.getText().toString());
            intent.putExtra("SurName", edt_surname.getText().toString());
            intent.putExtra(Constants.ACTIVITY_NAME, activity_type);
            startActivity(intent);
        }
        else {
            isEmailExist();
        }
    }

    private void isEmailExist() {

        userViewModel.isEmailExist(edt_email.getText().toString()).observe(this, new Observer<BaseModel<List<User>>>() {
            @Override
            public void onChanged(BaseModel<List<User>> listBaseModel) {
                if (listBaseModel != null && !listBaseModel.isError() && listBaseModel.getData().get(0) != null) {
                    networkResponseDialog(getString(R.string.error),listBaseModel.getMessage());
                    //  callLoginApi();
                } else {
                    hideDialog();

                    if(file_name!=null){
                        Intent intent = new Intent(CreateAccount_1_Activity.this, CreateAccount_2_Activity.class);
                        intent.putExtra("Email", edt_email.getText().toString());
                        intent.putExtra("FirstName", edt_firstname.getText().toString());
                        intent.putExtra("SurName", edt_surname.getText().toString());
                        intent.putExtra("FilePath", file_name.toString());
                        intent.putExtra("userType", userType);
                        intent.putExtra(Constants.ACTIVITY_NAME, activity_type);
                        startActivity(intent);
                    }else{
                        Intent intent = new Intent(CreateAccount_1_Activity.this, CreateAccount_2_Activity.class);
                        intent.putExtra("Email", edt_email.getText().toString());
                        intent.putExtra("FirstName", edt_firstname.getText().toString());
                        intent.putExtra("SurName", edt_surname.getText().toString());
                        intent.putExtra("userType", userType);
                        intent.putExtra(Constants.ACTIVITY_NAME, activity_type);
                        startActivity(intent);
                    }
                }
            }

        });

    }



    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error : errors) {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(this);
            if (view.getId() == R.id.edt_firstname) {
                message = getString(R.string.msg_firstname);
            }
            if (view.getId() == R.id.edt_surname) {
                message = getString(R.string.msg_surname);
            }
            // Display error messages
            if (view instanceof EditText) {
                ((EditText) view).setError(message);
            } else {
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(CreateAccount_1_Activity.this, LoginActivity.class);
        switch (v.getId()) {
            case R.id.img_backarrow:
                this.finish();
                break;

            case R.id.img_cross:
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                break;

            case R.id.tv_upload_image:
                if (Checkpermission()) {
                    BottomSheetPictureSelection bottomSheetPictureSelection = new BottomSheetPictureSelection(new YourDialogFragmentDismissHandler());
                    bottomSheetPictureSelection.show(getSupportFragmentManager(), bottomSheetPictureSelection.getTag());
                } else {
                    requestpermission();
                }
                break;

            case R.id.tv_individual:
                tv_company.setBackground(getDrawable(R.drawable.rounded_button_border));
                tv_individual.setBackground(getDrawable(R.drawable.rounded_button_red));
                tv_company.setTextColor(getResources().getColor(R.color.gray));
                tv_individual.setTextColor(getResources().getColor(R.color.white));
                userType = "user";
                break;

            case R.id.tv_company:
                tv_company.setBackground(getDrawable(R.drawable.rounded_button_red));
                tv_company.setTextColor(getResources().getColor(R.color.white));
                tv_individual.setBackground(getDrawable(R.drawable.rounded_button_border));
                tv_individual.setTextColor(getResources().getColor(R.color.gray));
                userType = "company";
                break;

            case R.id.tv_already_account:
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                break;

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

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE)
        {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == RESULT_OK){

                if (result != null && result.getUri()!=null)
                {
                    setImage(result.getUri());
                }

            }
            else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                if (result != null) {
                    Exception error = result.getError();
                    error.printStackTrace();
                }
            }
        }

        if (requestCode == REQUEST_PHOTO_GALLERY && resultCode == RESULT_OK) {

            try {
                if (data != null && data.getData() != null) {
                    Uri galleryImageUri = data.getData();

                    try {
                        String imagePath = ImageFilePathUtil.getPath(this, galleryImageUri);

                        if (TextUtils.isEmpty(imagePath)){
                            networkResponseDialog(getString(R.string.error), getString(R.string.err_internal_supported));
                            return;
                        }
                        else{
                            //Do not add getActivity instead of getContext().
                            CropImage.activity(galleryImageUri)
                                    .start(this);
                        }
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                }
                else {
                    Toast.makeText(this, "Something went wrong while retrieving image", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception exc) {
                exc.printStackTrace();
                Log.e(getClass().getName(), "exc: " + exc.getMessage());
            }
        } else {
            if (requestCode == CAMERAA&& resultCode == RESULT_OK )
            {
                try {
                    Uri SelectedImageUri;

                    SelectedImageUri = Uri.fromFile(galleryAddPic());

                    //Do not add getActivity instead of getContext().
                    CropImage.activity(SelectedImageUri)
                            .start(this);
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }

    private void setImage(Uri uri)
    {
        String updatedImage = ImageFilePathUtil.getPath(this, uri);

        if (!TextUtils.isEmpty(updatedImage) || updatedImage != null)
        {
            File file = new File(updatedImage);
            RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), file);

            globalImagePath = file.getAbsolutePath();

            if (file.length() / AppConstants.ONE_THOUSAND_AND_TWENTY_FOUR > AppConstants.IMAGE_SIZE_IN_KB) {
                networkResponseDialog(getString(R.string.error), getString(R.string.err_image_size_large));
                return;
            }

            if (!TextUtils.isEmpty(globalImagePath) || globalImagePath != null) {

                Glide.with(this).load(loadFromFile(globalImagePath))
                        .apply(new RequestOptions())
                        .into(img_profile);

                Bitmap orignal = loadFromFile(globalImagePath);
                File filenew = new File(globalImagePath);
                file_name = filenew;
                try {
                    FileOutputStream out = new FileOutputStream(filenew);
                    orignal.compress(Bitmap.CompressFormat.JPEG, 50, out);
                    out.flush();
                    out.close();

                } catch (Exception e) {
                    e.printStackTrace();
                }

                partImage = MultipartBody.Part.createFormData("user_image", file.getName(), reqFile);
            }
            else {
                networkResponseDialog(getString(R.string.error), getString(R.string.err_internal_supported));
            }
        }
        else {
            networkResponseDialog(getString(R.string.error), getString(R.string.err_internal_supported));
        }
    }

    public File galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(mCurrentPhotoPath);
        file_name = f;
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        sendBroadcast(mediaScanIntent);
        return f;

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
