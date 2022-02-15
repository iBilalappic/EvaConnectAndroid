package com.hypernym.evaconnect.view.ui.fragments;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.textfield.TextInputEditText;
import com.hypernym.evaconnect.R;
import com.hypernym.evaconnect.models.User;
import com.hypernym.evaconnect.repositories.CustomViewModelFactory;
import com.hypernym.evaconnect.utils.ImageFilePathUtil;
import com.hypernym.evaconnect.utils.LoginUtils;
import com.hypernym.evaconnect.viewmodel.UserViewModel;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.MultipartBody;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class EditProfileFragment extends BaseFragment implements View.OnClickListener, Validator.ValidationListener {
  /*  @NotEmpty
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

    @BindView(R.id.lbl_title)
    TextView lbl_title;

    @BindView(R.id.img_view2)
    View img_view2;*/

    @BindView(R.id.ly_change_profile_picture)
    LinearLayout ly_change_profile_picture;

    @BindView(R.id.ly_edit_job_title)
    LinearLayout ly_edit_job_title;

    @BindView(R.id.ly_edit_bio)
    LinearLayout ly_edit_bio;

    @BindView(R.id.ly_change_password)
    LinearLayout ly_change_password;

    @BindView(R.id.ly_edit_location)
    LinearLayout ly_edit_location;

    @BindView(R.id.img_backarrow)
    ImageView img_backarrow;

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
        ly_change_profile_picture.setOnClickListener(this);
        ly_edit_job_title.setOnClickListener(this);
        ly_edit_bio.setOnClickListener(this);
        ly_change_password.setOnClickListener(this);
        ly_edit_location.setOnClickListener(this);
        img_backarrow.setOnClickListener(this);
        user = LoginUtils.getUser();
        SettingUserProfile(user);


        return view;
    }

    private void SettingUserProfile(User user) {
        userViewModel = ViewModelProviders.of(this, new CustomViewModelFactory(getActivity().getApplication())).get(UserViewModel.class);
        //GetUserDetails();
    }


    @Override
    public void onValidationSucceeded() {
       //    UpdateProfileUser();
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
                    //setImage(result.getUri());
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

/*    private void setImage(Uri uri)
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
    }*/

    private void galleryAddPics() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        mCurrentPhotoPath=getCurrentPhotoPath();
        File f = new File(mCurrentPhotoPath);
        file_name = f;
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        getActivity().sendBroadcast(mediaScanIntent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ly_change_profile_picture:
                EditProfilePictureFragment editProfilePictureFragment = new EditProfilePictureFragment();
                loadFragment(R.id.framelayout, editProfilePictureFragment, getContext(), true);
                break;

            case R.id.ly_edit_job_title:
                EditProfileJobTitleFragment editProfileJobTitleFragment = new EditProfileJobTitleFragment();
                loadFragment(R.id.framelayout, editProfileJobTitleFragment, getContext(), true);
                break;

            case R.id.ly_edit_bio:
                EditProfileBioFragment editProfileBioFragment = new EditProfileBioFragment();
                loadFragment(R.id.framelayout, editProfileBioFragment, getContext(), true);
                break;

            case R.id.ly_change_password:
                EditProfileChangePasswordFragment editProfileChangePasswordFragment = new EditProfileChangePasswordFragment();
                loadFragment(R.id.framelayout, editProfileChangePasswordFragment, getContext(), true);
                break;

            case R.id.ly_edit_location:
                EditProfileLocationFragment editProfileLocationFragment = new EditProfileLocationFragment();
                loadFragment(R.id.framelayout, editProfileLocationFragment, getContext(), true);
                break;

            case R.id.img_backarrow:
                getActivity().onBackPressed();
                break;


        }

    }
}
