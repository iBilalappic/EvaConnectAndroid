package com.hypernym.evaconnect.view.ui.fragments;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.textfield.TextInputEditText;
import com.hypernym.evaconnect.R;
import com.hypernym.evaconnect.constants.AppConstants;
import com.hypernym.evaconnect.listeners.OnOneOffClickListener;
import com.hypernym.evaconnect.models.BaseModel;
import com.hypernym.evaconnect.models.Comment;
import com.hypernym.evaconnect.models.User;
import com.hypernym.evaconnect.repositories.CustomViewModelFactory;
import com.hypernym.evaconnect.utils.AppUtils;
import com.hypernym.evaconnect.utils.GsonUtils;
import com.hypernym.evaconnect.utils.ImageFilePathUtil;
import com.hypernym.evaconnect.utils.LoginUtils;
import com.hypernym.evaconnect.view.bottomsheets.BottomSheetPictureSelection;
import com.hypernym.evaconnect.view.dialogs.SimpleDialog;
import com.hypernym.evaconnect.view.ui.activities.CreateAccount_1_Activity;
import com.hypernym.evaconnect.view.ui.activities.HomeActivity;
import com.hypernym.evaconnect.view.ui.activities.LoginActivity;
import com.hypernym.evaconnect.view.ui.activities.SplashActivity;
import com.hypernym.evaconnect.viewmodel.PostViewModel;
import com.hypernym.evaconnect.viewmodel.UserViewModel;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.theartofdev.edmodo.cropper.CropImage;

import org.w3c.dom.Text;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Collections;
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
import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class EditProfileFragment extends BaseFragment implements Validator.ValidationListener {
    @NotEmpty
    @BindView(R.id.edt_location)
    EditText edt_location;

    @NotEmpty
    @BindView(R.id.edt_title)
    EditText edt_designation;

    @NotEmpty
    @BindView(R.id.edt_sector)
    EditText edt_sector;

    @NotEmpty
    @BindView(R.id.edt_firstname)
    EditText edt_firstname;

    @NotEmpty
    @BindView(R.id.edt_company)
    EditText edt_company;

    @BindView(R.id.tv_name)
    TextView tv_name;

    @BindView(R.id.tv_location)
    TextView tv_location;

    @BindView(R.id.tv_text_manage)
    TextView tv_text_manage;


    @BindView(R.id.img_edit)
    ImageView img_edit;

    @BindView(R.id.img_backarrow)
    ImageView img_backarrow;

    @BindView(R.id.tv_updated)
    TextView tv_updated;

    @BindView(R.id.btn_save)
    TextView btn_save;


    @BindView(R.id.profile_image)
    CircleImageView cv_profile_image;


    private UserViewModel userViewModel;


    private static final int REQUEST_PHOTO_GALLERY = 4;
    private static final int CAMERAA = 1;
    private String GalleryImage, mCurrentPhotoPath, globalImagePath;
    private File tempFile, file_name;
    private String currentPhotoPath = "";
    private String photoVar = null;
    private MultipartBody.Part partImage;

    User user = new User();
    private Validator validator;

    public EditProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_edit_profile, container, false);
        ButterKnife.bind(this, view);
        validator = new Validator(this);
        validator.setValidationListener(this);

        user = LoginUtils.getUser();
        SettingUserProfile(user);

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validator.validate();
            }
        });

        img_edit.setOnClickListener(new OnOneOffClickListener() {
            @Override
            public void onSingleClick(View v) {
                openPictureDialog();

            }
        });
        img_backarrow.setOnClickListener(new OnOneOffClickListener() {
            @Override
            public void onSingleClick(View v) {
                getActivity().onBackPressed();
            }
        });
        tv_text_manage.setOnClickListener(new OnOneOffClickListener() {
            @Override
            public void onSingleClick(View v) {
                SettingsFragment settingsFragment = new SettingsFragment();
                loadFragment(R.id.framelayout, settingsFragment, getContext(), true);
            }
        });

        return view;
    }

    private void SettingUserProfile(User user) {
        userViewModel = ViewModelProviders.of(this, new CustomViewModelFactory(getActivity().getApplication())).get(UserViewModel.class);
        GetUserDetails();
    }


    @Override
    public void onValidationSucceeded() {
           UpdateProfileUser();
    }

    private void UpdateProfileUser() {
        showDialog();
        userViewModel.profile_update(
                user.getId(),
                edt_designation.getText().toString(),
                edt_company.getText().toString(),
                edt_firstname.getText().toString(),partImage).observe(this, new Observer<BaseModel<List<Object>>>() {
            @Override
            public void onChanged(BaseModel<List<Object>> listBaseModel) {
                if (!listBaseModel.isError()) {
                   tv_updated.setVisibility(View.VISIBLE);
                   initHandler();
                    GetUserDetails();
                } else {
                    networkResponseDialog(getString(R.string.error), getString(R.string.err_unknown));
                }
                hideDialog();
            }
        });
    }

    private void initHandler() {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                tv_updated.setVisibility(View.GONE);
            }
        }, 3000);
    }

    private void GetUserDetails() {
        showDialog();
        userViewModel.getuser_details(user.getId()
        ).observe(this, new Observer<BaseModel<List<User>>>() {
            @Override
            public void onChanged(BaseModel<List<User>> listBaseModel)
            {
                if (listBaseModel.getData() != null && !listBaseModel.isError())
                {
                    if (!TextUtils.isEmpty(listBaseModel.getData().get(0).getUser_image()))
                    {
                        AppUtils.setGlideImage(getContext(), cv_profile_image, listBaseModel.getData().get(0).getUser_image());
                    }
//                    else if (listBaseModel.getData().get(0).getIs_facebook() == 1 && !TextUtils.isEmpty(listBaseModel.getData().get(0).getFacebook_image_url())) {
//                        AppUtils.setGlideImage(getContext(), cv_profile_image, listBaseModel.getData().get(0).getFacebook_image_url());
//                    }
//                    else {
//                        AppUtils.setGlideImage(getContext(), cv_profile_image, listBaseModel.getData().get(0).getUser_image());
//                    }

                    tv_location.setText(listBaseModel.getData().get(0).getCountry() + "," + listBaseModel.getData().get(0).getCity());
                    edt_location.setText(listBaseModel.getData().get(0).getCountry() + "," + listBaseModel.getData().get(0).getCity());
                    edt_firstname.setText(listBaseModel.getData().get(0).getFirst_name());
                    edt_sector.setText(listBaseModel.getData().get(0).getSector());
                    edt_designation.setText(listBaseModel.getData().get(0).getDesignation());
                    edt_company.setText(listBaseModel.getData().get(0).getCompany_name());
                    tv_name.setText(listBaseModel.getData().get(0).getFirst_name());
                    LoginUtils.saveUser(listBaseModel.getData().get(0));
                } else {
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
            String message = error.getCollatedErrorMessage(getContext());

            if (view instanceof TextInputEditText) {
                ((TextInputEditText) view).setError(message);
            } else {
                Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
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
                        String imagePath = ImageFilePathUtil.getPath(getActivity(), galleryImageUri);

                        if (TextUtils.isEmpty(imagePath)){
                            networkResponseDialog(getString(R.string.error), getString(R.string.err_internal_supported));
                            return;
                        }
                        else{
                            //Do not add getActivity instead of getContext().
                            CropImage.activity(galleryImageUri)
                                    .start(getContext(), this);
                        }
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                }
                else {
                    Toast.makeText(getContext(), "Something went wrong while retrieving image", Toast.LENGTH_SHORT).show();
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
                            .start(getContext(), this);
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }

    private void setImage(Uri uri)
    {
        String updatedImage = ImageFilePathUtil.getPath(getActivity(), uri);

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
                        .into(cv_profile_image);

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

    private void galleryAddPics() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        mCurrentPhotoPath=getCurrentPhotoPath();
        File f = new File(mCurrentPhotoPath);
        file_name = f;
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        getActivity().sendBroadcast(mediaScanIntent);
    }
}
