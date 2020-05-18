package com.hypernym.evaconnect.view.ui.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.hypernym.evaconnect.R;
import com.hypernym.evaconnect.listeners.OnOneOffClickListener;
import com.hypernym.evaconnect.models.BaseModel;
import com.hypernym.evaconnect.models.User;
import com.hypernym.evaconnect.repositories.CustomViewModelFactory;
import com.hypernym.evaconnect.utils.AppUtils;
import com.hypernym.evaconnect.utils.Constants;
import com.hypernym.evaconnect.utils.NetworkUtils;
import com.hypernym.evaconnect.viewmodel.UserViewModel;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Password;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.CALL_PHONE;
import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;

public class CreateAccount_2_Activity extends BaseActivity implements Validator.ValidationListener,
        com.google.android.gms.location.LocationListener {


    private Validator validator;

    @BindView(R.id.btn_next)
    Button btn_next;

    @NotEmpty
    @BindView(R.id.tv_country)
    TextView tv_country;

    @NotEmpty
    @BindView(R.id.tv_city)
    TextView tv_city;

    @BindView(R.id.img_backarrow)
    ImageView img_backarrow;

    @BindView(R.id.img_cross)
    ImageView img_cross;

    String email, photourl, activity_type,user_type,firstname,surname,file_name;

    public static final int RequestPermissionCode = 1;
    LatLng mLastLocation;
    long UPDATE_INTERVAL = 15000;  /* 1 sec */
    long FASTEST_INTERVAL = 501; /* 1/2 sec */
    public static int counter = 0;
    private LocationRequest mLocationRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account_2);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        if ("LinkedinActivity".equals(getIntent().getStringExtra(Constants.ACTIVITY_NAME))) {
            email = getIntent().getStringExtra("Email");
            photourl = getIntent().getStringExtra("Photo");
            user_type = getIntent().getStringExtra("userType");
            activity_type = "LinkedinActivity";
            firstname = getIntent().getStringExtra("FirstName");
            surname = getIntent().getStringExtra("SurName");

        } else {
            email = getIntent().getStringExtra("Email");
            activity_type = "normal_type";
            file_name = getIntent().getStringExtra("FilePath");
            user_type = getIntent().getStringExtra("userType");
            firstname = getIntent().getStringExtra("FirstName");
            surname = getIntent().getStringExtra("SurName");
        }

        validator = new Validator(this);
        validator.setValidationListener(this);
        btn_next.setOnClickListener(new OnOneOffClickListener() {
            @Override
            public void onSingleClick(View v) {

                if (Checkpermission()) {
                    // LaunchGallery();
                    startLocationUpdates();
                    validator.validate();

                } else {
                    requestlocationpermission();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        startLocationUpdates();
    }

    @Override
    public void onValidationSucceeded() {

        if (activity_type.equals("LinkedinActivity")) {
            Intent intent = new Intent(CreateAccount_2_Activity.this, SignupActivity_1.class);
            intent.putExtra("Email", email);
            intent.putExtra("Photo", photourl);
            intent.putExtra("userType", user_type);
            intent.putExtra("FirstName",firstname);
            intent.putExtra("SurName", surname);
            intent.putExtra("city", tv_city.getText().toString());
            intent.putExtra("country", tv_country.getText().toString());
            intent.putExtra(Constants.ACTIVITY_NAME, activity_type);
            startActivity(intent);

        } else {
            Intent intent = new Intent(CreateAccount_2_Activity.this, SignupActivity_1.class);

            intent.putExtra("Email", email);
            intent.putExtra("FirstName", firstname);
            intent.putExtra("SurName", surname);
            intent.putExtra("FilePath", file_name);
            intent.putExtra("userType", user_type);
            intent.putExtra("city", tv_city.getText().toString());
            intent.putExtra("country", tv_country.getText().toString());
            intent.putExtra(Constants.ACTIVITY_NAME, activity_type);
            startActivity(intent);
        }

    }


    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error : errors) {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(this);
            if (view.getId() == R.id.tv_country) {
                message = getString(R.string.err_password);
            }
            if (view.getId() == R.id.tv_city) {
                message = getString(R.string.err_password);
            }
            // Display error messages
            if (view instanceof EditText) {
                ((EditText) view).setError(message);
            } else {
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            }
        }
    }

    @SuppressLint("RestrictedApi")
    protected void startLocationUpdates() {

        // Create the location request to start receiving updates
        mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);

        // Create LocationSettingsRequest object using location request
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        final LocationSettingsRequest locationSettingsRequest = builder.build();

        // Check whether location settings are satisfied
        // https://developers.google.com/android/reference/com/google/android/gms/location/SettingsClient
        SettingsClient settingsClient = LocationServices.getSettingsClient(this);
        settingsClient.checkLocationSettings(locationSettingsRequest);

        // new Google API SDK v11 uses getFusedLocationProviderClient(this)
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        LocationServices.getFusedLocationProviderClient(this).requestLocationUpdates(mLocationRequest, new LocationCallback() {
                    @Override
                    public void onLocationResult(LocationResult locationResult) {
                        // do work here
                        counter++;
                        try {
                            if (counter > 1) {
                                onLocationChanged(locationResult.getLastLocation());
                                LocationServices.getFusedLocationProviderClient(getApplicationContext()).removeLocationUpdates(this);
                            }
                        } catch (Exception ex) {

                        }
                    }
                },
                Looper.myLooper());
    }


    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = new LatLng(location.getLatitude(), location.getLongitude());
        showDialog();
        setAddress(mLastLocation.latitude, mLastLocation.longitude);
    }

    private void setAddress(Double latitude, Double longitude) {
        if (latitude != null && longitude != null) {

            Geocoder geocoder;
            List<Address> addresses = null;
            geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());

            try {
                addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (addresses.size() > 0) {
                    Log.d("max", " " + addresses.get(0).getMaxAddressLineIndex());

                    String address = addresses.get(0).getAddressLine(0);
                    // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                    String city = addresses.get(0).getLocality();
                    String state = addresses.get(0).getAdminArea();
                    String country = addresses.get(0).getCountryName();
                    String postalCode = addresses.get(0).getPostalCode();
                    String knownName = addresses.get(0).getFeatureName(); // Only if available else return NULL

                    addresses.get(0).getAdminArea();
                    Log.d("address", address);
                    String[] splitArray = address.split(",");
                    String new_text = splitArray[3];
                    String street = splitArray[0];
                    String sector = splitArray[1];

                    tv_city.setText(city);
                    tv_country.setText(country);
                    hideDialog();

                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public boolean Checkpermission() {

        int FirstPermissionResult = ContextCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION);
        int CameraResult = ContextCompat.checkSelfPermission(this, CAMERA);


        return FirstPermissionResult == PackageManager.PERMISSION_GRANTED &&
                CameraResult == PackageManager.PERMISSION_GRANTED;
    }

    private void requestlocationpermission() {
        ActivityCompat.requestPermissions(this, new String[]
                {
                        ACCESS_FINE_LOCATION,
                        CAMERA


                }, RequestPermissionCode);


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {

            case RequestPermissionCode:

                if (grantResults.length > 0) {

                    boolean LocationPermission = grantResults[0] == PackageManager.PERMISSION_GRANTED;


                    if (  LocationPermission) {
                        startLocationUpdates();
                        // Toast.makeText(HomeActivity.this, "Permission Granted", Toast.LENGTH_LONG).show();
                    } else {
                        //Toast.makeText(HomeActivity.this,"Permission Denied",Toast.LENGTH_LONG).show();
                    }
                }

                break;
        }
    }


}
