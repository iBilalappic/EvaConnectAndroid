package com.hypernym.evaconnect.view.ui.fragments;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.hypernym.evaconnect.R;
import com.hypernym.evaconnect.constants.AppConstants;
import com.hypernym.evaconnect.models.BaseModel;
import com.hypernym.evaconnect.models.User;
import com.hypernym.evaconnect.repositories.CustomViewModelFactory;
import com.hypernym.evaconnect.utils.AppUtils;
import com.hypernym.evaconnect.utils.ImageFilePathUtil;
import com.hypernym.evaconnect.utils.LoginUtils;
import com.hypernym.evaconnect.viewmodel.UserViewModel;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;


public class EditProfilePictureFragment extends BaseFragment implements View.OnClickListener {

    @BindView(R.id.profile_image)
    CircleImageView cv_profile_image;

    @BindView(R.id.btn_save)
    TextView btn_save;

    @BindView(R.id.tv_upload_image)
    TextView tv_upload_image;

    @BindView(R.id.img_backarrow)
    ImageView img_backarrow;



    private static final int REQUEST_PHOTO_GALLERY = 4;
    private static final int CAMERAA = 1;
    private String GalleryImage, mCurrentPhotoPath, globalImagePath;
    private File tempFile, file_name;
    private String currentPhotoPath = "";
    private String photoVar = null;
    private MultipartBody.Part partImage;

    String city,country, first_name, sector, designation, company = "";
    private UserViewModel userViewModel;
    User user = new User();

    public EditProfilePictureFragment() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        tv_upload_image.setOnClickListener(this);
        btn_save.setOnClickListener(this);
        img_backarrow.setOnClickListener(this);

        user = LoginUtils.getUser();
        SettingUserProfile(user);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_profile_picture, container, false);
    }

    private void GetUserDetails() {
        showDialog();
        userViewModel.getuser_details(user.getId(), false
        ).observe(getViewLifecycleOwner(), listBaseModel -> {
            if (listBaseModel.getData() != null && !listBaseModel.isError()) {
                if (!TextUtils.isEmpty(listBaseModel.getData().get(0).getUser_image())) {
                    AppUtils.setGlideImage(getContext(), cv_profile_image, listBaseModel.getData().get(0).getUser_image());
                }
//                    else if (listBaseModel.getData().get(0).getIs_facebook() == 1 && !TextUtils.isEmpty(listBaseModel.getData().get(0).getFacebook_image_url())) {
//                        AppUtils.setGlideImage(getContext(), cv_profile_image, listBaseModel.getData().get(0).getFacebook_image_url());
//                    }
//                    else {
//                        AppUtils.setGlideImage(getContext(), cv_profile_image, listBaseModel.getData().get(0).getUser_image());
//                    }

                city = listBaseModel.getData().get(0).getCountry();
                country = listBaseModel.getData().get(0).getCity();
                first_name = listBaseModel.getData().get(0).getFirst_name();

                if (listBaseModel.getData().get(0).getDesignation() != null
                        && listBaseModel.getData().get(0).getDesignation().isEmpty()) {
                    designation = listBaseModel.getData().get(0).getDesignation();
                }

                if (listBaseModel.getData().get(0).getSector().equalsIgnoreCase("Other")) {
                    sector = listBaseModel.getData().get(0).getOther_sector();
                } else {
                    sector = listBaseModel.getData().get(0).getSector();
                }

                if (listBaseModel.getData().get(0).getType().equalsIgnoreCase("company")) {
                    /*lbl_title.setVisibility(View.GONE);
                    edt_designation.setVisibility(View.GONE);
                    img_view2.setVisibility(View.GONE);*/
                } else {
                    designation = listBaseModel.getData().get(0).getDesignation();
/*                        lbl_title.setVisibility(View.VISIBLE);
                    edt_designation.setVisibility(View.VISIBLE);
                    img_view2.setVisibility(View.VISIBLE);*/
                }


                company = listBaseModel.getData().get(0).getCompany_name();
                first_name = listBaseModel.getData().get(0).getFirst_name();
                LoginUtils.saveUser(listBaseModel.getData().get(0));
            } else {
                networkResponseDialog(getString(R.string.error), getString(R.string.err_unknown));
            }
            hideDialog();
        });
    }

    private void SettingUserProfile(User user) {
        userViewModel = ViewModelProviders.of(this, new CustomViewModelFactory(getActivity().getApplication())).get(UserViewModel.class);
        GetUserDetails();
    }

    private void UpdateProfileUser() {
        showDialog();
        userViewModel.profile_update(
                user.getId(),
               designation,
                company,
                first_name,partImage).observe(this, new Observer<BaseModel<List<Object>>>() {
            @Override
            public void onChanged(BaseModel<List<Object>> listBaseModel) {
                if (!listBaseModel.isError()) {
                    //tv_updated.setVisibility(View.VISIBLE);
                   // initHandler();
                    GetUserDetails();
                } else {
                    networkResponseDialog(getString(R.string.error), getString(R.string.err_unknown));
                }
                hideDialog();
            }
        });
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_upload_image:
                openPictureDialog();
                break;

            case R.id.btn_save:
                UpdateProfileUser();
                break;

            case R.id.img_backarrow:
               getActivity().onBackPressed();
                break;
        }
    }


}