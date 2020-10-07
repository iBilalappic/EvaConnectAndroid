package com.hypernym.evaconnect.view.ui.fragments;


import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
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
import androidx.core.app.ActivityCompat;
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
import com.hypernym.evaconnect.view.adapters.AttachmentsAdapter;
import com.hypernym.evaconnect.view.adapters.PostAdapter;
import com.hypernym.evaconnect.viewmodel.PostViewModel;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class ShareArticleFragment extends BaseFragment {
    private static final int REQUEST_DOCUMENTS = 5;
    private List<String> attachments = new ArrayList<>();

    @BindView(R.id.btn_upload)
    ConstraintLayout btn_upload;

    @BindView(R.id.attachment)
    ConstraintLayout attachment;

    @BindView(R.id.attachment_preview)
    ImageView webView;

    @BindView(R.id.img_removeAttachment)
    ImageView img_removeAttachment;

    @BindView(R.id.tv_filename)
    TextView tv_filename;

    @BindView(R.id.profile_image)
    ImageView profile_image;

    @BindView(R.id.tv_name)
    TextView tv_name;

    @BindView(R.id.tv_designation)
    TextView tv_designation;

    @BindView(R.id.tv_company)
    TextView tv_company;

    @BindView(R.id.tv_address)
    TextView tv_address;

    @BindView(R.id.post)
    TextView post;

    @BindView(R.id.edt_content)
    EditText edt_content;

    @BindView(R.id.loadimage)
    WebView loadimage;


    private Post postModel = new Post();

    private Uri SelectedImageUri;
    private String GalleryImage, globalImagePath, FileName;
    public String mProfileImageDecodableString;
    private File tempFile, file_name;
    private String currentPhotoPath = "";
    private String photoVar = null;
    private List<File> MultipleFile = new ArrayList<>();
    private MultipartBody.Part part_images = null;
    PostViewModel postViewModel;

    public ShareArticleFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_share_article, container, false);
        ButterKnife.bind(this, view);
        postViewModel = ViewModelProviders.of(this, new CustomViewModelFactory(getActivity().getApplication(), getActivity())).get(PostViewModel.class);
        init();

        if(getArguments()!=null && getArguments().getBoolean("isEdit"))
        {
            webView.setVisibility(View.INVISIBLE);

            loadimage.setVisibility(View.VISIBLE);

            getPostDetails(getArguments().getInt("post"));


            attachment.setVisibility(View.VISIBLE);

            post.setBackground(getResources().getDrawable(R.drawable.button_bg));


//          loadimage.setVisibility(View.VISIBLE);



        }
        return view;
    }

    private void init() {
        User user = LoginUtils.getLoggedinUser();
        AppUtils.setGlideImage(getContext(), profile_image, user.getUser_image());
        showBackButton();
        setPageTitle(getString(R.string.menu4));
        if (!TextUtils.isEmpty(user.getUser_image())) {
            AppUtils.setGlideImage(getContext(), profile_image, user.getUser_image());

        }

        tv_name.setText(user.getFirst_name());
        tv_designation.setText(user.getDesignation() + " at ");
        tv_company.setText(user.getCompany_name());
        tv_address.setText(user.getCity() + " , " + user.getCountry());
        setPostButton();

        post.setOnClickListener(new OnOneOffClickListener() {
            @Override
            public void onSingleClick(View v) {
                if (part_images != null) {
                    if (NetworkUtils.isNetworkConnected(getContext())) {

                        if (getArguments() != null && getArguments().getBoolean("isEdit")) {
                            upDatePost();
                        } else {
                            createPost();
                        }
                    } else {
                        networkErrorDialog();
                    }
                }
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
        postModel.setContent(edt_content.getText().toString());
        postModel.setDocument(part_images);
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


    private void upDatePost() {
        showDialog();
        ArrayList<String> urlList = AppUtils.containsURL(edt_content.getText().toString());
        if (urlList.size() > 0) {
            postModel.setIs_url(true);
        } else {
            postModel.setIs_url(false);
        }
        postModel.setContent(edt_content.getText().toString());
        postModel.setDocument(part_images);
        postModel.setPost_id(getArguments().getInt("post"));
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


    @OnClick(R.id.btn_upload)
    public void setBtn_upload() {
        if (Checkpermission()) {
            LaunchGallery();
        } else {
            requestpermission();
        }
    }

    @OnClick(R.id.img_removeAttachment)
    public void removeAttachment() {
        attachment.setVisibility(View.GONE);
        img_removeAttachment.setVisibility(View.GONE);
        part_images = null;
        setPostButton();
    }


    public void LaunchGallery() {
        // String[] supportedMimeTypes = {"application/pdf","application/msword"};
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//        intent.setType("file/*");
        intent.setType("application/pdf");

        startActivityForResult(Intent.createChooser(intent, "Select File"), REQUEST_DOCUMENTS);
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

        if (requestCode == REQUEST_DOCUMENTS && resultCode == RESULT_OK) {
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

                                part_images = MultipartBody.Part.createFormData("post_document", file_name.getName(), reqFile);
                                //  MultiplePhoto.add(partImage);
                                //Drawable drawable = (Drawable)new BitmapDrawable(getResources(),);
                                webView.setImageBitmap(AppUtils.generateImageFromPdf(SelectedImageUri, getContext()));


//                                WebSettings settings = webView.getSettings();
//                                settings.setJavaScriptEnabled(true);
//                                settings.setAllowFileAccessFromFileURLs(true);
//                                settings.setAllowUniversalAccessFromFileURLs(true);
//                                settings.setBuiltInZoomControls(true);
//                                webView.setWebChromeClient(new WebChromeClient());
//                                String base = file_name.getAbsolutePath().toString();
//                                String imagePath = "file:///"+ base;
//                                webView.loadUrl(imagePath);
                                //   webView.loadDataWithBaseURL("", html, "text/html","utf-8", "");

                                tv_filename.setText(file_name.getName());
                                img_removeAttachment.setVisibility(View.VISIBLE);
                                //  attachment_preview.loadUrl(SelectedImageUri.toString());
                                attachment.setVisibility(View.VISIBLE);
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
        }
    }

    public void setPostButton() {
        if (part_images != null) {
            post.setBackground(getResources().getDrawable(R.drawable.button_bg));
        } else {
            post.setBackground(getResources().getDrawable(R.drawable.button_unfocused));
        }
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


    private void setPostData(Post post)
    {
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
//            attachments.add(post.getPost_image().get(0));
//            attachmentsAdapter.notifyDataSetChanged();
//            rc_attachments.setVisibility(View.VISIBLE);
//        }

    }

}
