package com.hypernym.evaconnect.view.ui.fragments;


import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

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
import com.hypernym.evaconnect.utils.NetworkUtils;
import com.hypernym.evaconnect.utils.URLTextWatcher;
import com.hypernym.evaconnect.view.adapters.AttachmentsAdapter;
import com.hypernym.evaconnect.view.dialogs.SimpleDialog;
import com.hypernym.evaconnect.viewmodel.ConnectionViewModel;
import com.hypernym.evaconnect.viewmodel.PostViewModel;
import com.mobsandgeeks.saripaar.Validator;
import com.nguyencse.URLEmbeddedView;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class ShareVideoFragment extends BaseFragment implements AttachmentsAdapter.ItemClickListener {


    @BindView(R.id.post)
    TextView post;

    @BindView(R.id.edt_content)
    EditText edt_content;

    @BindView(R.id.edt_description)
    EditText edt_description;

    @BindView(R.id.profile_image)
    ImageView profile_image;

    @BindView(R.id.tv_name)
    TextView tv_name;

    @BindView(R.id.tv_designation)
    TextView tv_designation;

    @BindView(R.id.tv_company)
    TextView tv_company;

    @BindView(R.id.img_video)
    ImageView img_video;

    @BindView(R.id.img_play)
    ImageView img_play;

    @BindView(R.id.uev)
    URLEmbeddedView urlEmbeddedView;

    @BindView(R.id.tv_address)
    TextView tv_address;


    private AttachmentsAdapter attachmentsAdapter;
    private List<String> attachments = new ArrayList<>();
    private List<MultipartBody.Part> part_images = new ArrayList<>();
    private MultipartBody.Part video = null;
    private static final int REQUEST_PHOTO_GALLERY = 4;
    private static final int CAMERAA = 1;

    private String GalleryImage, mCurrentPhotoPath, globalImagePath;
    private String mProfileImageDecodableString;
    private File tempFile, file_name;
    private String currentPhotoPath = "";
    private String photoVar = null;
    private PostViewModel postViewModel;
    private Post postModel = new Post();
    private Validator validator;
    private SimpleDialog simpleDialog;
    private ConnectionViewModel connectionViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_share_video, container, false);
        ButterKnife.bind(this, view);
        postViewModel = ViewModelProviders.of(this, new CustomViewModelFactory(getActivity().getApplication(), getActivity())).get(PostViewModel.class);
        connectionViewModel = ViewModelProviders.of(this, new CustomViewModelFactory(getActivity().getApplication(), getActivity())).get(ConnectionViewModel.class);
        init();
        post.setOnClickListener(new OnOneOffClickListener() {
            @Override
            public void onSingleClick(View v) {
                if (edt_content.getText().length() > 0 || part_images.size() > 0 || video != null) {
                    if (NetworkUtils.isNetworkConnected(getContext())) {
                        createPost();
                    } else {
                        networkErrorDialog();
                    }
                }

            }
        });

        return view;
    }

    private void init() {
        User user = LoginUtils.getLoggedinUser();
        AppUtils.setGlideImage(getContext(), profile_image, user.getUser_image());

        if ( !TextUtils.isEmpty(user.getUser_image())) {
            AppUtils.setGlideImage(getContext(), profile_image, user.getUser_image());

        }
//        else if (user.getIs_facebook() == 1 && !TextUtils.isEmpty(user.getFacebook_image_url())){
//            AppUtils.setGlideImage(getContext(), profile_image, user.getFacebook_image_url());
//        }
//        else {
//            AppUtils.setGlideImage(getContext(), profile_image, user.getUser_image());
//        }
        tv_name.setText(user.getFirst_name());
        if(user.getDesignation()!=null)
        {
            tv_designation.setText(user.getDesignation()+" at ");
        }

        tv_company.setText(user.getCompany_name());
        tv_address.setText(user.getCity()+" , "+user.getCountry());
        // getConnectionCount();

     //   showBackButton();
        if(getArguments().getBoolean("isVideo"))
        {
            setPageTitle(getString(R.string.menu2));
            post.setText(getString(R.string.menu2));
            edt_content.setHint("Share URL/embed of video");
        }
        else
        {
            setPageTitle(getString(R.string.menu4));
            post.setText(getString(R.string.menu4));
            edt_content.setHint("Share URL");
        }

        edt_content.addTextChangedListener(new URLTextWatcher(getActivity(), edt_content, urlEmbeddedView));
        edt_content.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                setPostButton();
            }
        });
    }
    private void createPost() {
        showDialog();
        ArrayList<String> urlList = AppUtils.containsURL(edt_content.getText().toString());
        if (urlList.size() > 0) {
            postModel.setIs_url(true);
        } else {
            postModel.setIs_url(false);
        }
        postModel.setAttachments(part_images);
        postModel.setContent(edt_content.getText().toString()+" "+edt_description.getText().toString() );
        postModel.setVideo(video);
        postViewModel.createPost(postModel).observe(this, new Observer<BaseModel<List<Post>>>() {
            @Override
            public void onChanged(BaseModel<List<Post>> listBaseModel) {
                if (listBaseModel != null && !listBaseModel.isError()) {
                    newPost();

                    Toast.makeText(getContext(), getString(R.string.msg_post_created), Toast.LENGTH_LONG).show();
                    // networkResponseDialog(getString(R.string.success),getString(R.string.msg_post_created));
                    if (getFragmentManager().getBackStackEntryCount() != 0) {
                        getFragmentManager().popBackStack();
                    }
                } else {
                    networkResponseDialog(getString(R.string.error), getString(R.string.err_unknown));
                }
                hideDialog();
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
                    GalleryImage = ImageFilePathUtil.getPath(getActivity(), SelectedImageUri);
                    mProfileImageDecodableString = ImageFilePathUtil.getPath(getActivity(), SelectedImageUri);
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
                            file_name = new File(ImageFilePathUtil.getPath(getActivity(), SelectedImageUri));
                            RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), file_name);

                            // partImage = MultipartBody.Part.createFormData("user_image", file_name.getName(), reqFile);

                            if (!TextUtils.isEmpty(currentPhotoPath) || currentPhotoPath != null) {
                                if (currentPhotoPath.toString().endsWith(".mp4")) {
                                    img_video.setVisibility(View.VISIBLE);
                                    img_play.setVisibility(View.VISIBLE);
                                    AppUtils.setGlideVideoThumbnail(getContext(), img_video, currentPhotoPath);
                                    video = MultipartBody.Part.createFormData("post_video", file_name.getName(), reqFile);
                                    setPostButton();
                                } else {
                                    attachments.add(currentPhotoPath);
                                    attachmentsAdapter.notifyDataSetChanged();
                                    img_video.setVisibility(View.GONE);
                                    img_play.setVisibility(View.GONE);
                                    part_images.add(MultipartBody.Part.createFormData("post_image", file_name.getName(), reqFile));
                                    setPostButton();
                                }

                            } else {
                                networkResponseDialog(getString(R.string.error), getString(R.string.err_internal_supported));
                            }
                        } else {
                            networkResponseDialog(getString(R.string.error), getString(R.string.err_one_file_at_a_time));
                            return;
                        }
                    }
                }
//                else if(data.getClipData() != null && data.getClipData().getItemCount()<3)
//                {
//                    ClipData mClipData = data.getClipData();
//
//                    for (int i = 0; i < mClipData.getItemCount(); i++) {
//                        ClipData.Item item = mClipData.getItemAt(i);
//                        Uri uri = item.getUri();
//                        file_name = new File(ImageFilePathUtil.getPath(getActivity(), uri));
//                        attachments.add( ImageFilePathUtil.getPath(getActivity(), uri));
//                        RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), file_name);
//                        part_images.add(MultipartBody.Part.createFormData("post_image", file_name.getName(), reqFile));
//                    }
//                    attachmentsAdapter.notifyDataSetChanged();
//                    rc_attachments.setVisibility(View.VISIBLE);
//                    img_video.setVisibility(View.GONE);
//                    img_play.setVisibility(View.GONE);
//                    setPostButton();
//                }
//                else {
//                    Toast.makeText(getActivity(), "You haven't picked image", Toast.LENGTH_SHORT).show();
//                }
            } catch (Exception exc) {
                exc.printStackTrace();
                Log.e(getClass().getName(), "exc: " + exc.getMessage());
            }
        } else {
            if (requestCode == CAMERAA && resultCode == RESULT_OK) {

                //mIsProfileImageAdded = true;
                File file = galleryAddPic();
                RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), file);
                // imgName = file_name.getName();
                globalImagePath = file.getAbsolutePath();
                if (file.length() / AppConstants.ONE_THOUSAND_AND_TWENTY_FOUR > AppConstants.IMAGE_SIZE_IN_KB) {
                    networkResponseDialog(getString(R.string.error), getString(R.string.err_image_size_large));
                    //   AppUtils.showSnackBar(getView(), getString(R.string.err_image_size_large, AppConstants.IMAGE_SIZE_IN_KB));
                    return;
                }
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
                if (!TextUtils.isEmpty(globalImagePath) || globalImagePath != null) {
                    if (globalImagePath.toString().endsWith(".mp4")) {
                        img_video.setVisibility(View.VISIBLE);
                        img_play.setVisibility(View.VISIBLE);
                        AppUtils.setGlideVideoThumbnail(getContext(), img_video, globalImagePath);
                        video = MultipartBody.Part.createFormData("post_video", file_name.getName(), reqFile);
                        setPostButton();
                    } else {

                        attachments.add(globalImagePath);
                        attachmentsAdapter.notifyDataSetChanged();
                        img_video.setVisibility(View.GONE);
                        img_play.setVisibility(View.GONE);
                        part_images.add(MultipartBody.Part.createFormData("post_image", file.getName(), reqFile));
                        setPostButton();
                    }

                }
            }
        }
    }


    @OnClick(R.id.img_video)
    public void playVideo() {
        AppUtils.playVideo(getActivity(), currentPhotoPath);
    }

    @Override
    public void onItemClick(View view, int position) {
        simpleDialog = new SimpleDialog(getContext(), getString(R.string.confirmation), getString(R.string.msg_remove_attachment), getString(R.string.button_no), getString(R.string.button_yes), new OnOneOffClickListener() {
            @Override
            public void onSingleClick(View v) {
                switch (v.getId()) {
                    case R.id.button_positive:
                        attachments.remove(position);
                        attachmentsAdapter.notifyDataSetChanged();
                        part_images.remove(position);
                        setPostButton();
                        break;
                    case R.id.button_negative:
                        break;
                }

                simpleDialog.dismiss();
            }
        });
        simpleDialog.show();
    }

    public void setPostButton() {
        if (edt_content.getText().length() > 0 || part_images.size() > 0 || video != null) {
            post.setBackground(getResources().getDrawable(R.drawable.button_bg));
        } else {
            post.setBackground(getResources().getDrawable(R.drawable.button_unfocused));
        }
    }


}
