package com.hypernym.evaconnect.view.ui.fragments;


import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.hypernym.evaconnect.R;
import com.hypernym.evaconnect.constants.AppConstants;
import com.hypernym.evaconnect.listeners.OnOneOffClickListener;
import com.hypernym.evaconnect.models.BaseModel;
import com.hypernym.evaconnect.models.Post;
import com.hypernym.evaconnect.models.User;
import com.hypernym.evaconnect.repositories.CustomViewModelFactory;
import com.hypernym.evaconnect.utils.AppUtils;
import com.hypernym.evaconnect.utils.ImageFilePathUtil;
import com.hypernym.evaconnect.utils.LoginUtils;
import com.hypernym.evaconnect.view.adapters.AttachmentsAdapter;
import com.hypernym.evaconnect.viewmodel.PostViewModel;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewPostFragment extends BaseFragment implements Validator.ValidationListener  {

    @BindView(R.id.rc_attachments)
    RecyclerView rc_attachments;

    @BindView(R.id.browsefiles)
    TextView browsefiles;

    @BindView(R.id.post)
    TextView post;

    @NotEmpty
    @BindView(R.id.edt_content)
    EditText edt_content;

    @BindView(R.id.profile_image)
    ImageView profile_image;

    @BindView(R.id.tv_name)
    TextView tv_name;

    @BindView(R.id.tv_connections)
    TextView tv_connections;

    private AttachmentsAdapter attachmentsAdapter;
    private List<String> attachments=new ArrayList<>();
    private List<MultipartBody.Part> part_images=new ArrayList<>();
    private static final int REQUEST_PHOTO_GALLERY = 4;
    private static final int CAMERAA = 1;

    private String GalleryImage, mCurrentPhotoPath, globalImagePath;
    private String mProfileImageDecodableString;
    private File tempFile,file_name;
    private String currentPhotoPath = "";
    private String photoVar = null;
    private PostViewModel postViewModel;
    private Post postModel=new Post();
    private Validator validator;

    public NewPostFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_new_post, container, false);
        ButterKnife.bind(this,view);
        init();
        initRecyclerView();
        postViewModel= ViewModelProviders.of(this,new CustomViewModelFactory(getActivity().getApplication(),getActivity())).get(PostViewModel.class);

        browsefiles.setOnClickListener(new OnOneOffClickListener() {
            @Override
            public void onSingleClick(View v) {
              openPictureDialog();
            }
        });

        post.setOnClickListener(new OnOneOffClickListener() {
            @Override
            public void onSingleClick(View v) {
                validator.validate();
            }
        });

        return view;
    }

    private void init() {
        validator = new Validator(this);
        validator.setValidationListener(this);
        User user=LoginUtils.getLoggedinUser();
        AppUtils.setGlideImage(getContext(),profile_image,user.getUser_image());
        tv_name.setText(user.getFirst_name());
        tv_connections.setText(AppUtils.getConnectionsCount(user.getTotal_connection()));

    }

    private void createPost() {
            showDialog();
            postModel.setAttachments(part_images);
            postModel.setContent(edt_content.getText().toString());
            postViewModel.createPost(postModel).observe(this, new Observer<BaseModel<List<Post>>>() {
                @Override
                public void onChanged(BaseModel<List<Post>> listBaseModel) {
                    if(listBaseModel!=null && !listBaseModel.isError())
                    {
                        Toast.makeText(getContext(),getString(R.string.msg_post_created),Toast.LENGTH_LONG).show();
                      // networkResponseDialog(getString(R.string.success),getString(R.string.msg_post_created));
                        if (getFragmentManager().getBackStackEntryCount() != 0) {
                            getFragmentManager().popBackStack();
                        }
                    }
                    else
                    {
                       networkResponseDialog(getString(R.string.error),getString(R.string.err_unknown));
                    }
                    hideDialog();
                }
            });
    }

    private void initRecyclerView() {
        attachmentsAdapter=new AttachmentsAdapter(getContext(),attachments);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL, false);
        rc_attachments.setLayoutManager(linearLayoutManager);
        rc_attachments.setAdapter(attachmentsAdapter);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Result code is RESULT_OK only if the user selects an Image
        if (requestCode == REQUEST_PHOTO_GALLERY && resultCode == RESULT_OK) {
            try {
                if (data != null && data.getData() != null) {
                    Uri SelectedImageUri = data.getData();
                    GalleryImage = ImageFilePathUtil.getPath(getActivity(), SelectedImageUri);
                    mProfileImageDecodableString = ImageFilePathUtil.getPath(getActivity(), SelectedImageUri);
                    Log.e(getClass().getName(), "image file path: " + GalleryImage);

                    tempFile = new File(GalleryImage);

                    Log.e(getClass().getName(), "file path details: " + tempFile.getName() + " " + tempFile.getAbsolutePath() + "length" + tempFile.length());


                    if (tempFile.length() / AppConstants.ONE_THOUSAND_AND_TWENTY_FOUR > AppConstants.FILE_SIZE_LIMIT_IN_KB) {
                        networkResponseDialog(getString(R.string.error),getString(R.string.err_image_size_large));
                        return;
                    } else {
                        if (photoVar == null) {
                            currentPhotoPath = GalleryImage;
                            // photoVar = GalleryImage;
                            file_name = new File(ImageFilePathUtil.getPath(getActivity(), SelectedImageUri));
                            RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), file_name);

                           // partImage = MultipartBody.Part.createFormData("user_image", file_name.getName(), reqFile);
                            part_images.add(MultipartBody.Part.createFormData("post_image", file_name.getName(), reqFile));
                            if (!TextUtils.isEmpty(currentPhotoPath) || currentPhotoPath != null) {
                                attachments.add(currentPhotoPath);
                                attachmentsAdapter.notifyDataSetChanged();
                                rc_attachments.setVisibility(View.VISIBLE);

                            } else {
                                networkResponseDialog(getString(R.string.error),getString(R.string.err_internal_supported));
                            }
                        } else {
                            networkResponseDialog(getString(R.string.error),getString(R.string.err_one_file_at_a_time));
                            return;
                        }
                    }
                } else {
                    Toast.makeText(getActivity(), "Something went wrong while retrieving image", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception exc) {
                exc.printStackTrace();
                Log.e(getClass().getName(), "exc: " + exc.getMessage());
            }
        } else {
            if (requestCode == CAMERAA) {

                //mIsProfileImageAdded = true;
                File file=galleryAddPic();
                RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), file);
                // imgName = file_name.getName();
                globalImagePath = file.getAbsolutePath();
                if (file.length() / AppConstants.ONE_THOUSAND_AND_TWENTY_FOUR > AppConstants.IMAGE_SIZE_IN_KB) {
                    networkResponseDialog(getString(R.string.error),getString(R.string.err_image_size_large));
                    //   AppUtils.showSnackBar(getView(), getString(R.string.err_image_size_large, AppConstants.IMAGE_SIZE_IN_KB));
                    return;
                }
                if (!TextUtils.isEmpty(globalImagePath) || globalImagePath != null) {

                    attachments.add(globalImagePath);
                    attachmentsAdapter.notifyDataSetChanged();
                    rc_attachments.setVisibility(View.VISIBLE);

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
                    part_images.add(MultipartBody.Part.createFormData("post_image", file.getName(), reqFile));
                //   partImage = MultipartBody.Part.createFormData("user_image", file_name.getName(), reqFile);
                    //AppUtils.showSnackBar(getView(), AppUtils.getErrorMessage(getContext(), 8));

                }
            }
        }
    }

    @Override
    public void onValidationSucceeded() {
        createPost();
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error : errors) {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(getContext());
            // Display error messages
            if (view instanceof EditText) {
                ((EditText) view).setError(message);
            } else {
                Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
            }
        }
    }

}
