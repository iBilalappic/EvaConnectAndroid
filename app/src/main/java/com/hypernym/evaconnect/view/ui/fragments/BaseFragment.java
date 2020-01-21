package com.hypernym.evaconnect.view.ui.fragments;

import android.content.Context;
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
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.hypernym.evaconnect.R;
import com.hypernym.evaconnect.constants.AppConstants;
import com.hypernym.evaconnect.utils.ImageFilePathUtil;
import com.hypernym.evaconnect.view.bottomsheets.BottomSheetPictureSelection;
import com.hypernym.evaconnect.view.dialogs.CustomProgressBar;
import com.hypernym.evaconnect.view.dialogs.SimpleDialog;
import com.hypernym.evaconnect.view.ui.activities.BaseActivity;
import com.hypernym.evaconnect.view.ui.activities.SignupDetailsActivity;

import org.w3c.dom.Text;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.app.Activity.RESULT_OK;

public class BaseFragment extends Fragment {
    private CustomProgressBar customProgressBar=new CustomProgressBar();
    private SimpleDialog simpleDialog;
    public static final int RequestPermissionCode = 1;
    private static final int REQUEST_PHOTO_GALLERY = 4;
    private static final int CAMERAA = 1;
    private String mCurrentPhotoPath;
    private File tempFile,file_name;
    /**
     * Could handle back press.
     * @return true if back press was handled
     */
    public boolean onBackPressed() {
        return false;
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    public void showDialog() {

        if(customProgressBar != null && !customProgressBar.isShowing())
            customProgressBar.showProgress(getContext(),false);
    }

    public void hideDialog() {

        if(customProgressBar != null && customProgressBar.isShowing())
            customProgressBar.hideProgress();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        hideDialog();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        hideDialog();
    }

    public void networkErrorDialog() {
        simpleDialog=new SimpleDialog(getActivity(),getString(R.string.error),getString(R.string.network_error),null,getString(R.string.ok), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                simpleDialog.dismiss();
            }
        });
        simpleDialog.show();
    }
    public void networkResponseDialog(String title,String message) {
        simpleDialog=new SimpleDialog(getActivity(),title,message,null,getString(R.string.ok), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                simpleDialog.dismiss();
            }
        });
        simpleDialog.show();
    }
    public void loadFragment(int id, Fragment fragment, Context context, boolean isBack)
    {
        FragmentTransaction transaction =((AppCompatActivity)context).getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
        transaction.replace(id, fragment);
        if(isBack)
        {
            transaction.addToBackStack(null);
        }
        transaction.commit();
    }
    public void loadFragment_bundle(int id, Fragment fragment, Context context, boolean isBack, Bundle bundle)
    {
        FragmentTransaction transaction =((AppCompatActivity)context).getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
        transaction.replace(id, fragment);
        if(isBack)
        {
            transaction.addToBackStack(null);
        }
        transaction.commit();
    }

    public void openPictureDialog()
    {
        if (Checkpermission()) {
            BottomSheetPictureSelection bottomSheetPictureSelection = new BottomSheetPictureSelection(new BaseFragment.YourDialogFragmentDismissHandler());
            bottomSheetPictureSelection.show(getActivity().getSupportFragmentManager(), bottomSheetPictureSelection.getTag());
        }
        else
        {
            requestpermission();
        }
    }
    public boolean Checkpermission() {

        int ExternalReadResult = ContextCompat.checkSelfPermission(getActivity(), READ_EXTERNAL_STORAGE);
        int CameraResult = ContextCompat.checkSelfPermission(getActivity(), CAMERA);
        return          ExternalReadResult == PackageManager.PERMISSION_GRANTED &&
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
    private void requestpermission() {
        ActivityCompat.requestPermissions(getActivity(), new String[]
                {
                        READ_EXTERNAL_STORAGE,
                        CAMERA
                }, RequestPermissionCode);
    }
    private void LaunchGallery() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("*/*");
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), REQUEST_PHOTO_GALLERY);
    }



    public File galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        mCurrentPhotoPath=getCurrentPhotoPath();
        File f = new File(mCurrentPhotoPath);
        file_name = f;
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        getActivity().sendBroadcast(mediaScanIntent);
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
//
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
                Uri photoURI = FileProvider.getUriForFile(getActivity(),
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
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }
    public String getCurrentPhotoPath()
    {
        return mCurrentPhotoPath;
    }

    public void showBackButton()
    {
        getActivity().findViewById(R.id.tv_back).setVisibility(View.VISIBLE);
    }
    public void hideBackButton()
    {
        getActivity().findViewById(R.id.tv_back).setVisibility(View.GONE);
    }


}
