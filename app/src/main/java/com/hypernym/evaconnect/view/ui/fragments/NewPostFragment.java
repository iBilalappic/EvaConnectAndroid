package com.hypernym.evaconnect.view.ui.fragments;


import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.hypernym.evaconnect.view.bottomsheets.BottomSheetPictureSelection;
import com.hypernym.evaconnect.view.bottomsheets.BottomsheetAttachmentSelection;
import com.hypernym.evaconnect.view.dialogs.LocalVideoViewDialog;
import com.hypernym.evaconnect.view.dialogs.SimpleDialog;
import com.hypernym.evaconnect.viewmodel.ConnectionViewModel;
import com.hypernym.evaconnect.viewmodel.PostViewModel;
import com.mobsandgeeks.saripaar.Validator;
import com.nguyencse.URLEmbeddedView;
import com.theartofdev.edmodo.cropper.CropImage;

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
public class NewPostFragment extends BaseFragment implements AttachmentsAdapter.ItemClickListener {

    @BindView(R.id.rc_attachments)
    RecyclerView rc_attachments;

    @BindView(R.id.browsefiles)
    TextView browsefiles;

    @BindView(R.id.camera)
    TextView camera;

    @BindView(R.id.post)
    TextView post;

    @BindView(R.id.edt_content)
    EditText edt_content;

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

    @BindView(R.id.img_add_post)
    ImageView img_add_post;

    @BindView(R.id.uev)
    URLEmbeddedView urlEmbeddedView;

    @BindView(R.id.tv_address)
    TextView tv_address;

    @BindView(R.id.tv_recordvideo)
    TextView tv_recordvideo;

    @BindView(R.id.attachment)
    ConstraintLayout attachment;

    @BindView(R.id.attachment_preview)
    ImageView webView;

    @BindView(R.id.img_removeAttachment)
    ImageView img_removeAttachment;

    @BindView(R.id.tv_filename)
    TextView tv_filename;

    @BindView(R.id.img_backarrow)
    ImageView img_backarrow;

    @BindView(R.id.loadimage)
    WebView loadimage;


    private AttachmentsAdapter attachmentsAdapter;
    private List<String> attachments = new ArrayList<>();
    private List<MultipartBody.Part> part_images = new ArrayList<>();
    private MultipartBody.Part video = null;
    private static final int REQUEST_PHOTO_GALLERY = 4;
    private static final int VIDEO_CAPTURE = 101;
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
    Uri SelectedImageUri;

    MultipartBody.Part part_images_document = null;
    private List<File> MultipleFile = new ArrayList<>();
    private static final int REQUEST_DOCUMENTS = 5;


    public NewPostFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_new_post, container, false);
        ButterKnife.bind(this, view);
        ButterKnife.bind(this, view);
        img_backarrow.setOnClickListener(new OnOneOffClickListener() {
            @Override
            public void onSingleClick(View v) {
               getActivity().onBackPressed();
            }
        });
        postViewModel = ViewModelProviders.of(this, new CustomViewModelFactory(getActivity().getApplication(), getActivity())).get(PostViewModel.class);
        connectionViewModel = ViewModelProviders.of(this, new CustomViewModelFactory(getActivity().getApplication(), getActivity())).get(ConnectionViewModel.class);
        init();
        initRecyclerView();
        browsefiles.setOnClickListener(new OnOneOffClickListener() {
            @Override
            public void onSingleClick(View v) {
                LaunchGallery();
            }
        });

        img_add_post.setOnClickListener(new OnOneOffClickListener() {
            @Override
            public void onSingleClick(View v) {
                BottomsheetAttachmentSelection bottomSheetPictureSelection = new BottomsheetAttachmentSelection(new BaseFragment.YourDialogFragmentDismissHandler());
                bottomSheetPictureSelection.show(getActivity().getSupportFragmentManager(), bottomSheetPictureSelection.getTag());
            }
        });


        camera.setOnClickListener(new OnOneOffClickListener() {
            @Override
            public void onSingleClick(View v) {
                takePhotoFromCamera();
            }
        });

        post.setOnClickListener(new OnOneOffClickListener() {
            @Override
            public void onSingleClick(View v) {
                if (edt_content.getText().length() > 0 || part_images.size() > 0 || video != null) {
                    if (NetworkUtils.isNetworkConnected(getContext())) {

                        if (getArguments() != null && getArguments().getBoolean("document_type")) {
                            if (getArguments() != null && getArguments().getBoolean("isEdit")) {
                                upDateDocument();
                            } else {
                                createPost();
                            }
                        } else {
                            if (getArguments() != null && getArguments().getBoolean("isEdit")) {

                                updatePost();
                            } else {
                                createPost();
                            }
                        }


                    } else {
                        networkErrorDialog();
                    }
                }
            }
        });

        tv_recordvideo.setOnClickListener(new OnOneOffClickListener() {
            @Override
            public void onSingleClick(View v) {
                recodeVideoFromCamera();
            }
        });

        return view;
    }

    private void init() {
        User user = LoginUtils.getLoggedinUser();
        AppUtils.setGlideImage(getContext(), profile_image, user.getUser_image());

        if (!TextUtils.isEmpty(user.getUser_image())) {
            AppUtils.setGlideImage(getContext(), profile_image, user.getUser_image());

        }
//        else if (user.getIs_facebook() == 1 && !TextUtils.isEmpty(user.getFacebook_image_url())){
//            AppUtils.setGlideImage(getContext(), profile_image, user.getFacebook_image_url());
//        }
//        else {
//            AppUtils.setGlideImage(getContext(), profile_image, user.getUser_image());
//        }
        tv_name.setText(user.getFirst_name());
        if (user.getDesignation() != null) {
            tv_designation.setText(user.getDesignation() + " at ");
        }

        tv_company.setText(user.getCompany_name());
        tv_address.setText(user.getCity() + " , " + user.getCountry());
        // getConnectionCount();

       // showBackButton();
        setPostButton();

        if (getArguments().getBoolean("document_type")) {
            webView.setVisibility(View.INVISIBLE);

            loadimage.setVisibility(View.VISIBLE);

            getDocumentDetails(getArguments().getInt("post"));

            attachment.setVisibility(View.VISIBLE);

            post.setBackground(getResources().getDrawable(R.drawable.button_gradient_1));
            post.setText("Post");
            setPageTitle("Back");

        } else {
            if (getArguments().getBoolean("isVideo")) {
                setPageTitle("Back");
                post.setText("Post");
                tv_recordvideo.setVisibility(View.VISIBLE);
                camera.setVisibility(View.GONE);
            } else {
                setPageTitle("Back");
                post.setText("Post");
                tv_recordvideo.setVisibility(View.GONE);
                camera.setVisibility(View.VISIBLE);
            }
            if (getArguments().getBoolean("isEdit")) {
                getPostDetails(getArguments().getInt("post"));
                post.setText("Post");
            }
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

    private void getDocumentDetails(int id) {
        postViewModel.getPostByID(id).observe(this, new Observer<BaseModel<List<Post>>>() {
            @Override
            public void onChanged(BaseModel<List<Post>> listBaseModel) {
                if (listBaseModel != null && !listBaseModel.isError()) {
                    //post = listBaseModel.getData().get(0);
                    // settingpostType();
                    setDocumentData(listBaseModel.getData().get(0));

                } else {
                    networkResponseDialog(getString(R.string.error), getString(R.string.err_unknown));
                }
            }
        });
    }

    private void setDocumentData(Post post) {
        edt_content.setText(post.getContent());
//        if(post.getPost_document().size()>0)
//        {

        edt_content.setText(post.getContent());

        loadimage.setWebViewClient(new WebViewClient());
        loadimage.getSettings().setSupportZoom(false);
        loadimage.getSettings().setJavaScriptEnabled(true);
        loadimage.getSettings().getAllowFileAccess();
        loadimage.getSettings().getAllowUniversalAccessFromFileURLs();
        loadimage.getSettings().getAllowFileAccessFromFileURLs();
        loadimage.setOnTouchListener(null);

        // cv_url = getArguments().getString("applicant_cv");
        loadimage.loadUrl("https://docs.google.com/gview?embedded=true&url=" + post.getPost_document());
        String fileName = post.getPost_document().substring(post.getPost_document().lastIndexOf('/') + 1);
        tv_filename.setText(fileName);
//            attachments.add(post.getPost_image().get(0));
//            attachmentsAdapter.notifyDataSetChanged();
//            rc_attachments.setVisibility(View.VISIBLE);
//        }

    }


    private void upDateDocument() {
        showDialog();
        ArrayList<String> urlList = AppUtils.containsURL(edt_content.getText().toString());
        if (urlList.size() > 0) {
            postModel.setIs_url(true);
        } else {
            postModel.setIs_url(false);
        }
        postModel.setContent(edt_content.getText().toString());
        postModel.setDocument(video);
        postModel.setId(getArguments().getInt("post"));
        postViewModel.editPost(postModel).observe(this, new Observer<BaseModel<List<Post>>>() {
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


    private void getPostDetails(int id) {
        postViewModel.getPostByID(id).observe(this, new Observer<BaseModel<List<Post>>>() {
            @Override
            public void onChanged(BaseModel<List<Post>> listBaseModel) {
                if (listBaseModel != null && !listBaseModel.isError()) {
                    //post = listBaseModel.getData().get(0);
                    // settingpostType();
                    setPostData(listBaseModel.getData().get(0));

                } else {
                    networkResponseDialog(getString(R.string.error), getString(R.string.err_unknown));
                }
            }
        });
    }

    private void setPostData(Post post) {
        edt_content.setText(post.getContent());
        if (post.getPost_image().size() > 0) {
            attachments.addAll(post.getPost_image());
            attachmentsAdapter.notifyDataSetChanged();
            rc_attachments.setVisibility(View.VISIBLE);
        }
        postModel.setId(post.getId());

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
        postModel.setContent(edt_content.getText().toString());
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

    private void initRecyclerView() {
        attachmentsAdapter = new AttachmentsAdapter(getContext(), attachments, this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        rc_attachments.setLayoutManager(linearLayoutManager);
        rc_attachments.setAdapter(attachmentsAdapter);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Result code is RESULT_OK only if the user selects an Image

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == RESULT_OK) {
                // add updated cropped image in recyclerview.
                addUpdatedImaged(result);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                if (result != null) {
                    Exception error = result.getError();
                    error.printStackTrace();
                }
            }
        }

        if (requestCode == REQUEST_PHOTO_GALLERY && resultCode == RESULT_OK) {
            try {
                if (data != null && data.getData() != null) {
                    Uri SelectedImageUri = data.getData();
                    GalleryImage = ImageFilePathUtil.getPath(getActivity(), SelectedImageUri);

                    if (TextUtils.isEmpty(GalleryImage)) {
                        networkResponseDialog(getString(R.string.error), getString(R.string.err_internal_supported));
                        return;
                    } else {
                        mProfileImageDecodableString = ImageFilePathUtil.getPath(getActivity(), SelectedImageUri);
                        Log.e(getClass().getName(), "image file path: " + GalleryImage);

                        tempFile = new File(GalleryImage);

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
                                        attachments.clear();
                                        attachmentsAdapter.notifyDataSetChanged();
                                        rc_attachments.setVisibility(View.GONE);
                                        attachment.setVisibility(View.GONE);
                                        img_removeAttachment.setVisibility(View.GONE);


                                    } else {
                                        //Do not add getActivity instead of getContext().
                                        CropImage.activity(SelectedImageUri)
                                                .start(getContext(), this);
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
                }

            } catch (Exception exc) {
                exc.printStackTrace();
                Log.e(getClass().getName(), "exc: " + exc.getMessage());
            }
        } else if (requestCode == VIDEO_CAPTURE && resultCode == RESULT_OK) {
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
                                img_video.setVisibility(View.VISIBLE);
                                img_play.setVisibility(View.VISIBLE);
                                AppUtils.setGlideVideoThumbnail(getContext(), img_video, currentPhotoPath);
                                video = MultipartBody.Part.createFormData("post_video", file_name.getName(), reqFile);
                                setPostButton();
                                attachments.clear();
                                attachmentsAdapter.notifyDataSetChanged();
                                rc_attachments.setVisibility(View.GONE);
                                attachment.setVisibility(View.GONE);
                                img_removeAttachment.setVisibility(View.GONE);


                            } else {
                                networkResponseDialog(getString(R.string.error), getString(R.string.err_internal_supported));
                            }
                        } else {
                            networkResponseDialog(getString(R.string.error), getString(R.string.err_one_file_at_a_time));
                            return;
                        }
                    }
                }

            } catch (Exception exc) {
                exc.printStackTrace();
                Log.e(getClass().getName(), "exc: " + exc.getMessage());
            }
        } else if (requestCode == REQUEST_DOCUMENTS && resultCode == RESULT_OK) {
            try {
                if (data != null && data.getData() != null) {

                    SelectedImageUri = data.getData();

                    // ImageFilePathUtil.checkFileType(".pdf");
                    GalleryImage = ImageFilePathUtil.getPath(getActivity(), SelectedImageUri);
                    mProfileImageDecodableString = ImageFilePathUtil.getPath(getActivity(), SelectedImageUri);
                    Log.e(getClass().getName(), "image file path: " + GalleryImage);

                    tempFile = new File(GalleryImage);

                    currentPhotoPath = GalleryImage;

                    if (tempFile.toString().equalsIgnoreCase("File path not found")) {
                        networkResponseDialog(getString(R.string.error), getString(R.string.err_internal_storage));
                    } else {
                        if (tempFile.length() / AppConstants.ONE_THOUSAND_AND_TWENTY_FOUR > AppConstants.FILE_SIZE_LIMIT_IN_KB) {
                            networkResponseDialog(getString(R.string.error), getString(R.string.err_image_size_large));
                            return;
                        } else {
                            if (photoVar == null) {
                                MultipleFile.add(tempFile);
                                Log.e(getClass().getName(), "file path details: " + tempFile.getName() + " " + tempFile.getAbsolutePath() + "length" + tempFile.length());

                                // photoVar = GalleryImage;
                                file_name = new File(ImageFilePathUtil.getPath(getActivity(), SelectedImageUri));
                                RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), file_name);

                                video = MultipartBody.Part.createFormData("post_document", file_name.getName(), reqFile);
                                //  MultiplePhoto.add(partImage);
                                //Drawable drawable = (Drawable)new BitmapDrawable(getResources(),);
                                webView.setImageBitmap(AppUtils.generateImageFromPdf(SelectedImageUri, getContext()));
                                loadimage.setVisibility(View.GONE);
                                webView.setVisibility(View.VISIBLE);
                                img_video.setVisibility(View.GONE);
                                img_play.setVisibility(View.GONE);

                                tv_filename.setText(file_name.getName());
                                img_removeAttachment.setVisibility(View.VISIBLE);
                                attachment.setVisibility(View.VISIBLE);
                                //  attachment_preview.loadUrl(SelectedImageUri.toString());
                                attachments.clear();
                                attachmentsAdapter.notifyDataSetChanged();
                                rc_attachments.setVisibility(View.GONE);
                                setPostButton();
                                if (!TextUtils.isEmpty(currentPhotoPath) || currentPhotoPath != null) {

                                } else {
                                    networkResponseDialog(getString(R.string.error), getString(R.string.err_internal_supported));
                                }
                            } else {
                                networkResponseDialog(getString(R.string.error), getString(R.string.err_one_file_at_a_time));
                                return;
                            }
                        }
                    }

                } else {
                    Toast.makeText(getActivity(), "Something went wrong while retrieving image", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception exc) {
                exc.printStackTrace();
                Log.e(getClass().getName(), "exc: " + exc.getMessage());
            }
        } else if (requestCode == CAMERAA) {
            try {
                SelectedImageUri = Uri.fromFile(galleryAddPic());

                CropImage.activity(SelectedImageUri)
                        .start(getContext(), this);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (requestCode == 3) {
            ///IMAGE CROPPING FROM GALLERY////////////////
            //  SelectedImageUri = data.getParcelableExtra("path");
            //  SelectedImageUri=(Uri) data.getExtras().get("data");
            SelectedImageUri = getPickImageResultUri(data);
            // SelectedImageUri= data.getParcelableExtra("path");
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
                            attachments.clear();
                            attachmentsAdapter.notifyDataSetChanged();
                            rc_attachments.setVisibility(View.GONE);
                            img_removeAttachment.setVisibility(View.GONE);


                        } else {
                            attachments.add(currentPhotoPath);
                            attachmentsAdapter.notifyDataSetChanged();
                            rc_attachments.setVisibility(View.VISIBLE);
                            img_video.setVisibility(View.GONE);
                            img_play.setVisibility(View.GONE);
                            attachment.setVisibility(View.GONE);
                            img_removeAttachment.setVisibility(View.GONE);

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
        } else if (requestCode == 6) {
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
                    attachments.clear();
                    attachmentsAdapter.notifyDataSetChanged();
                    rc_attachments.setVisibility(View.GONE);
                    attachment.setVisibility(View.GONE);

                    img_removeAttachment.setVisibility(View.GONE);

                } else {

                    attachments.add(globalImagePath);
                    attachmentsAdapter.notifyDataSetChanged();
                    rc_attachments.setVisibility(View.VISIBLE);
                    img_video.setVisibility(View.GONE);
                    img_play.setVisibility(View.GONE);
                    attachment.setVisibility(View.GONE);
                    img_removeAttachment.setVisibility(View.GONE);

                    part_images.add(MultipartBody.Part.createFormData("post_image", file.getName(), reqFile));
                    setPostButton();
                }
            }
        }
    }

    // add updated cropped image in recyclerview.
    private void addUpdatedImaged(CropImage.ActivityResult result) {
        try {
            if (result != null) {
                Uri resultUri = result.getUri();
                String updatedImage = ImageFilePathUtil.getPath(getActivity(), resultUri);

                if (!TextUtils.isEmpty(updatedImage)) {
                    File file = new File(updatedImage);
                    RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), file);

                    attachments.add(updatedImage);
                    attachmentsAdapter.notifyDataSetChanged();
                    rc_attachments.setVisibility(View.VISIBLE);
                    img_video.setVisibility(View.GONE);
                    img_play.setVisibility(View.GONE);
                    attachment.setVisibility(View.GONE);
                    img_removeAttachment.setVisibility(View.GONE);

                    part_images.add(MultipartBody.Part.createFormData("post_image", file.getName(), reqFile));
                    setPostButton();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Uri getPickImageResultUri(Intent data) {
        boolean isCamera = true;
        if (data != null) {
            String action = data.getAction();
            isCamera = action != null && action.equals(MediaStore.ACTION_IMAGE_CAPTURE);
        }
        return isCamera ? getCaptureImageOutputUri() : data.getData();
    }

    /**
     * Get URI to image received from capture by camera.
     */
    private Uri getCaptureImageOutputUri() {
        Uri outputFileUri = null;
        File getImage = getActivity().getExternalCacheDir();
        if (getImage != null) {
            outputFileUri = Uri.fromFile(new File(getImage.getPath(), "pickImageResult.jpeg"));
        }
        return outputFileUri;
    }

    @OnClick(R.id.img_video)
    public void playVideo() {
        LocalVideoViewDialog videoViewDialog = new LocalVideoViewDialog(getContext(), currentPhotoPath);
        videoViewDialog.show();
    }

    @OnClick(R.id.img_removeAttachment)
    public void removeAttachment() {
        attachment.setVisibility(View.GONE);
        img_removeAttachment.setVisibility(View.GONE);
        video = null;
        setPostButton();
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

    private void updatePost() {
        showDialog();
        ArrayList<String> urlList = AppUtils.containsURL(edt_content.getText().toString());
        if (urlList.size() > 0) {
            postModel.setIs_url(true);
        } else {
            postModel.setIs_url(false);
        }
        postModel.setAttachments(part_images);
        postModel.setContent(edt_content.getText().toString());
        postModel.setVideo(video);
        postViewModel.editPost(postModel).observe(this, new Observer<BaseModel<List<Post>>>() {
            @Override
            public void onChanged(BaseModel<List<Post>> listBaseModel) {
                if (listBaseModel != null && !listBaseModel.isError()) {

                    newPost();

                    Toast.makeText(getContext(), getString(R.string.msg_post_updated), Toast.LENGTH_LONG).show();
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

    public void setPostButton() {
        if (edt_content.getText().length() > 0 || part_images.size() > 0 || video != null) {
            post.setBackground(getResources().getDrawable(R.drawable.button_gradient_1));
        } else {
            post.setBackground(getResources().getDrawable(R.drawable.button_unfocused));
        }
    }


}
