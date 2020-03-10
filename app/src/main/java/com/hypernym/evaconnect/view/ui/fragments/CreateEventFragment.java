package com.hypernym.evaconnect.view.ui.fragments;


import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
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
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.hypernym.evaconnect.R;
import com.hypernym.evaconnect.constants.AppConstants;
import com.hypernym.evaconnect.dateTimePicker.DateTime;
import com.hypernym.evaconnect.dateTimePicker.DateTimePicker;
import com.hypernym.evaconnect.dateTimePicker.SimpleDateTimePicker;
import com.hypernym.evaconnect.models.BaseModel;
import com.hypernym.evaconnect.models.Event;
import com.hypernym.evaconnect.models.User;
import com.hypernym.evaconnect.repositories.CustomViewModelFactory;
import com.hypernym.evaconnect.utils.AppUtils;
import com.hypernym.evaconnect.utils.DateUtils;
import com.hypernym.evaconnect.utils.ImageFilePathUtil;
import com.hypernym.evaconnect.utils.NetworkUtils;
import com.hypernym.evaconnect.view.bottomsheets.BottomSheetPictureSelection;
import com.hypernym.evaconnect.view.dialogs.SimpleDialog;
import com.hypernym.evaconnect.view.ui.activities.SignupActivity;
import com.hypernym.evaconnect.view.ui.activities.SignupDetailsActivity;
import com.hypernym.evaconnect.viewmodel.EventViewModel;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

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
public class CreateEventFragment extends BaseFragment implements DateTimePicker.OnDateTimeSetListener , Validator.ValidationListener  {
    @BindView(R.id.tv_startdate)
    TextView tv_startdate;

    @BindView(R.id.tv_enddate)
    TextView tv_enddate;

    @BindView(R.id.tv_startTime)
    TextView tv_startTime;

    @BindView(R.id.tv_endTime)
    TextView tv_endTime;

    @NotEmpty
    @BindView(R.id.edt_eventname)
    EditText edt_eventname;

    @NotEmpty
    @BindView(R.id.edt_eventlocation)
    EditText edt_eventCity;

    @NotEmpty
    @BindView(R.id.edt_content)
    EditText edt_description;

    @BindView(R.id.edt_eventAddress)
    EditText edt_eventAddress;

    @BindView(R.id.img_event)
    ImageView img_event;

    @BindView(R.id.post)
    TextView post;

    private boolean isStartDate=false;
    private Date startDate=new Date();
    DateFormat time = new SimpleDateFormat("hh:mm a");
    DateFormat dateformat = new SimpleDateFormat("E, dd MMM yyyy");
    private static final String TAG_DATETIME_FRAGMENT = "TAG_DATETIME_FRAGMENT";
    private static final int REQUEST_PHOTO_GALLERY = 4;
    private static final int CAMERAA = 1;
    public static final int RequestPermissionCode = 1;
    private String GalleryImage, mCurrentPhotoPath, globalImagePath;
    private String mProfileImageDecodableString;
    private File tempFile, file_name;
    private String currentPhotoPath = "";
    private String photoVar = null;
    MultipartBody.Part partImage;

    private EventViewModel eventViewModel;
    private Validator validator;
    private SimpleDialog simpleDialog;

    public CreateEventFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_create_event, container, false);
        ButterKnife.bind(this,view);
        init();
        return view;
    }

    private void init() {
        setPageTitle("Create Event");
        tv_startdate.setText(dateformat.format(new Date()));
        tv_startTime.setText(time.format(new Date()));
        tv_enddate.setText(dateformat.format(new Date()));
        tv_endTime.setText(time.format(new Date()));
        validator = new Validator(this);
        validator.setValidationListener(this);
        eventViewModel= ViewModelProviders.of(this,new CustomViewModelFactory(getActivity().getApplication(),getActivity())).get(EventViewModel.class);
    }

    @OnClick({R.id.tv_startdate,R.id.tv_startTime})
    public void setStartDate()
    {
        isStartDate=true;
        startDate=new Date();
        showDateTimePicker("Set Start Date & Time");
    }


    @OnClick({R.id.tv_enddate,R.id.tv_endTime})
    public void setEndDate()
    {
        isStartDate=false;
        showDateTimePicker("Set End Date & Time");
    }

    private void showDateTimePicker(String title) {
        // Create a SimpleDateTimePicker and Show it
        SimpleDateTimePicker simpleDateTimePicker = SimpleDateTimePicker.make(
                title,
                new Date(),startDate,
                this,
                getFragmentManager()
        );

        simpleDateTimePicker.show();
    }


    @Override
    public void DateTimeSet(Date date) {
        DateTime mDateTime = new DateTime(date);
        if(isStartDate)
        {
            tv_startdate.setText(dateformat.format(mDateTime.getDate()));
            tv_startTime.setText(time.format(mDateTime.getDate()));
            startDate=mDateTime.getDate();
        }
        tv_enddate.setText(dateformat.format(mDateTime.getDate()));
        tv_endTime.setText(time.format(mDateTime.getDate()));

        Log.v("TEST_TAG","Date and Time selected: " + mDateTime.getDateString());
    }

    @Override
    public void onValidationSucceeded() {
        if(NetworkUtils.isNetworkConnected(getContext())) {
            showDialog();
            Event event=new Event();
            event.setEvent_name(edt_eventname.getText().toString());
            event.setEvent_start_date(DateUtils.getFormattedEventDate(tv_startdate.getText().toString()));
            event.setEvent_end_date(DateUtils.getFormattedEventDate(tv_enddate.getText().toString()));
            event.setStart_time(tv_startTime.getText().toString());
            event.setEnd_time(tv_endTime.getText().toString());
            event.setContent(edt_description.getText().toString());
            event.setEvent_city(edt_eventCity.getText().toString());
            event.setEvent_address(edt_eventAddress.getText().toString());

            eventViewModel.createEvent(event,partImage).observe(this, new Observer<BaseModel<List<Event>>>() {
                @Override
                public void onChanged(BaseModel<List<Event>> listBaseModel) {
                    if(listBaseModel!=null && !listBaseModel.isError())
                    {
                        simpleDialog = new SimpleDialog(getActivity(), getString(R.string.success), getString(R.string.msg_event_created), null, getString(R.string.ok), new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                // getActivity().onBackPressed();
                                if (getFragmentManager().getBackStackEntryCount() != 0) {
                                    getFragmentManager().popBackStack();
                                }
                                simpleDialog.dismiss();
                            }
                        });
                        simpleDialog.show();
                    }
                    else
                    {
                        networkResponseDialog(getString(R.string.error),getString(R.string.err_unknown));
                    }
                    hideDialog();
                }
            });
        }
        else
        {
            networkErrorDialog();
        }

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

    @OnClick(R.id.img_event)
    public void setImage()
    {
        if (Checkpermission()) {
            BottomSheetPictureSelection bottomSheetPictureSelection = new BottomSheetPictureSelection(new YourDialogFragmentDismissHandler());
            bottomSheetPictureSelection.show(getFragmentManager(), bottomSheetPictureSelection.getTag());
        } else {
            requestpermission();
        }
    }

    @OnClick(R.id.post)
    public void post()
    {
        validator.validate();
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
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), REQUEST_PHOTO_GALLERY);
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
                Uri photoURI = FileProvider.getUriForFile(getContext(),
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
        File storageDir = getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }
    public void galleryPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(mCurrentPhotoPath);
        file_name = f;
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        getContext().sendBroadcast(mediaScanIntent);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Result code is RESULT_OK only if the user selects an Image
        if (requestCode == REQUEST_PHOTO_GALLERY && resultCode == RESULT_OK) {
            try {
                if (data != null && data.getData() != null) {
                    Uri SelectedImageUri = data.getData();

                    GalleryImage = ImageFilePathUtil.getPath(getContext(), SelectedImageUri);
                    mProfileImageDecodableString = ImageFilePathUtil.getPath(getContext(), SelectedImageUri);
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
                            file_name = new File(ImageFilePathUtil.getPath(getContext(), SelectedImageUri));
                            RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), file_name);
                            partImage = MultipartBody.Part.createFormData("event_image", file_name.getName(), reqFile);
                            if (!TextUtils.isEmpty(currentPhotoPath) || currentPhotoPath != null) {
                                Glide.with(this).load(currentPhotoPath).into(img_event);

                            } else {
                                networkResponseDialog(getString(R.string.error), getString(R.string.err_internal_supported));
                            }
                        } else {
                            networkResponseDialog(getString(R.string.error), getString(R.string.err_one_file_at_a_time));
                            return;
                        }
                    }
                } else {
                    Toast.makeText(getContext(), "Something went wrong while retrieving image", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception exc) {
                exc.printStackTrace();
                Log.e(getClass().getName(), "exc: " + exc.getMessage());
            }
        } else {
            if (requestCode == CAMERAA && resultCode == RESULT_OK) {

                //mIsProfileImageAdded = true;
                galleryPic();

                RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), file_name);
                // imgName = file_name.getName();
                globalImagePath = file_name.getAbsolutePath();
                if (file_name.length() / AppConstants.ONE_THOUSAND_AND_TWENTY_FOUR > AppConstants.IMAGE_SIZE_IN_KB) {
                    networkResponseDialog(getString(R.string.error), getString(R.string.err_image_size_large));
                    return;
                }
                if (!TextUtils.isEmpty(globalImagePath) || globalImagePath != null) {

                    Glide.with(this).load(loadFromFile(globalImagePath))
                            .apply(new RequestOptions())
                            .into(img_event);

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
                    partImage = MultipartBody.Part.createFormData("event_image", file_name.getName(), reqFile);
                    //AppUtils.showSnackBar(getView(), AppUtils.getErrorMessage(getContext(), 8));

                }
            }
        }
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
}
