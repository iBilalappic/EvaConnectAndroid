package com.hypernym.evaconnect.view.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.hypernym.evaconnect.R;
import com.hypernym.evaconnect.models.User;
import com.hypernym.evaconnect.viewmodel.UserViewModel;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MultipartBody;


public class EditProfileBioFragment extends Fragment implements View.OnClickListener {
    @BindView(R.id.profile_image)
    CircleImageView cv_profile_image;

    @BindView(R.id.btn_save)
    TextView btn_save;

    @BindView(R.id.tv_upload_image)
    TextView tv_upload_image;

    @BindView(R.id.img_backarrow)
    TextView img_backarrow;

    @BindView(R.id.edt_jobtitle)
    EditText edt_jobtitle;


    private static final int REQUEST_PHOTO_GALLERY = 4;
    private static final int CAMERAA = 1;
    private String GalleryImage, mCurrentPhotoPath, globalImagePath;
    private File tempFile, file_name;
    private String currentPhotoPath = "";
    private String photoVar = null;
    private MultipartBody.Part partImage;

    String city, country, first_name, sector, designation, company = "";
    private UserViewModel userViewModel;
    User user = new User();


    public EditProfileBioFragment() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        ButterKnife.bind(this, view);
        img_backarrow.setOnClickListener(this);
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_profile_bio, container, false);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.img_backarrow) {
            getActivity().onBackPressed();
        }
    }
}