package com.hypernym.evaconnect.view.ui.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.model.LatLng;
import com.hypernym.evaconnect.R;
import com.hypernym.evaconnect.constants.AppConstants;
import com.hypernym.evaconnect.listeners.OnOneOffClickListener;
import com.hypernym.evaconnect.utils.Constants;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.skydoves.powerspinner.IconSpinnerItem;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.CAMERA;

public class CreateAccount_3_Activity extends BaseActivity implements Validator.ValidationListener,
        com.google.android.gms.location.LocationListener, View.OnClickListener {


    private Validator validator;

    @BindView(R.id.btn_next)
    Button btn_next;


    @BindView(R.id.tv_dob)
    TextView tv_dob;

    @BindView(R.id.title)
    TextView title;

    @NotEmpty
    @BindView(R.id.ed_country)
    EditText ed_country;

    @NotEmpty
    @BindView(R.id.tv_city)
    EditText tv_city;

    @BindView(R.id.img_backarrow)
    ImageView img_backarrow;

    @BindView(R.id.img_cross)
    ImageView img_cross;


    @BindView(R.id.tv_already_account)
    TextView tv_already_account;

    @BindView(R.id.layout_date)
    LinearLayout layout_date;

    @BindView(R.id.edit_date)
    EditText edit_date;

    @BindView(R.id.edit_month)
    EditText edit_month;

    @BindView(R.id.edit_year)
    EditText edit_year;



    String email, photourl, activity_type, user_type, firstname, surname, file_name, path, about, dob;
    final Calendar myCalendar = Calendar.getInstance();

    String dob_str = "";
    public static final int RequestPermissionCode = 1;
    LatLng mLastLocation;
    long UPDATE_INTERVAL = 15000;  /* 1 sec */
    long FASTEST_INTERVAL = 501; /* 1/2 sec */
    public static int counter = 0;
    private LocationRequest mLocationRequest;
    private LocationManager locationManager;

    DatePickerDialog.OnDateSetListener date = (view, year, monthOfYear, dayOfMonth) -> {
        // TODO Auto-generated method stub
        myCalendar.set(Calendar.YEAR, year);
        myCalendar.set(Calendar.MONTH, monthOfYear);
        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        updateLabel();
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account_3);
        ButterKnife.bind(this);
        tv_already_account.setOnClickListener(this);
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        isLocationEnabled();
        init();


    }

    private void isLocationEnabled() {
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            return;
        } else {
            buildAlertMessageNoGps();
        }
    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Enable GPS", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.setCancelable(false);
        alert.setIcon(R.drawable.ic_location_disabled_black_24dp);
        alert.setCanceledOnTouchOutside(false);
        alert.show();
    }


    private void init() {
        String type = getIntent().getStringExtra(Constants.ACTIVITY_NAME);

        if ("LinkedinActivity".equals(getIntent().getStringExtra(Constants.ACTIVITY_NAME))) {
            email = getIntent().getStringExtra("Email");
            photourl = getIntent().getStringExtra("Photo");
            user_type = getIntent().getStringExtra("userType");
            path = getIntent().getStringExtra("Path");
            activity_type = "LinkedinActivity";
            firstname = getIntent().getStringExtra("FirstName");
            surname = getIntent().getStringExtra("SurName");
            about = getIntent().getStringExtra("about");


        } else if (!TextUtils.isEmpty(type) && type.equals(AppConstants.FACEBOOK_LOGIN_TYPE)) {
            email = getIntent().getStringExtra("Email");
            photourl = getIntent().getStringExtra("Photo");
            path = getIntent().getStringExtra("Path");
            user_type = getIntent().getStringExtra("userType");
            activity_type = AppConstants.FACEBOOK_LOGIN_TYPE;
            firstname = getIntent().getStringExtra("FirstName");
            surname = getIntent().getStringExtra("SurName");
            about = getIntent().getStringExtra("about");

        } else {
            email = getIntent().getStringExtra("Email");
            activity_type = "normal_type";
            file_name = getIntent().getStringExtra("FilePath");
            user_type = getIntent().getStringExtra("userType");
            firstname = getIntent().getStringExtra("FirstName");
            surname = getIntent().getStringExtra("SurName");
            about = getIntent().getStringExtra("about");
        }
        if (user_type.equals("user")) {
            layout_date.setVisibility(View.VISIBLE);
            tv_dob.setVisibility(View.VISIBLE);
            title.setText("Date of Birth / Location");
        } else {
            layout_date.setVisibility(View.GONE);
            tv_dob.setVisibility(View.GONE);
            title.setText("Location");
        }

        validator = new Validator(this);
        validator.setValidationListener(this);
        img_backarrow.setOnClickListener(this);
        img_cross.setOnClickListener(this);

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

        ed_country.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().equalsIgnoreCase("UK")) {
                    List<String> iconSpinnerItems = new ArrayList<>();
                    iconSpinnerItems.add("item1");

                }
                else {

                }

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        edit_date.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(CreateAccount_3_Activity.this,R.style.DialogTheme, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        edit_month.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(CreateAccount_3_Activity.this,R.style.DialogTheme, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        edit_year.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(CreateAccount_3_Activity.this,R.style.DialogTheme, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    private void updateLabel() {
        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        edit_date.setText(String.valueOf(DateFormat.format("dd",   myCalendar.getTime())));
        edit_month.setText(String.valueOf(DateFormat.format("MM",   myCalendar.getTime())));
        edit_year.setText(String.valueOf(DateFormat.format("YYYY", myCalendar.getTime())));
        dob_str = sdf.format(myCalendar.getTime());

      /*  String dayOfTheWeek = (String) DateFormat.format("EEEE", myCalendar.getTime()); // Thursday
        String day          = (String) DateFormat.format("dd",   myCalendar.getTime()); // 20
        String monthString  = (String) DateFormat.format("MMM",  myCalendar.getTime()); // Jun
        String monthNumber  = (String) DateFormat.format("MM",   myCalendar.getTime()); // 06
        String year         = (String) DateFormat.format("YY", myCalendar.getTime()); // 2013*/
       /* edit_dob.setText(sdf.format(myCalendar.getTime()));*/
    }

    @Override
    protected void onResume() {
        super.onResume();

        startLocationUpdates();
    }

    @Override
    public void onValidationSucceeded() {

        if (activity_type.equals("LinkedinActivity")) {
            Intent intent = new Intent(CreateAccount_3_Activity.this, CreateAccount_4_Activity.class);
            intent.putExtra("Email", email);
            intent.putExtra("Photo", photourl);
            intent.putExtra("Path", path);
            intent.putExtra("userType", user_type);
            intent.putExtra("FirstName", firstname);
            intent.putExtra("SurName", surname);
            intent.putExtra("city", tv_city.getText().toString());
            intent.putExtra("country", ed_country.getText().toString());
            intent.putExtra("about", about);
            intent.putExtra("dob", dob_str);
            intent.putExtra(Constants.ACTIVITY_NAME, activity_type);
            startActivity(intent);

        } else if (activity_type.equals(AppConstants.FACEBOOK_LOGIN_TYPE)) {
            Intent intent = new Intent(CreateAccount_3_Activity.this, CreateAccount_4_Activity.class);
            intent.putExtra("Email", email);
            intent.putExtra("Photo", photourl);
            intent.putExtra("Path", path);
            intent.putExtra("userType", user_type);
            intent.putExtra("FirstName", firstname);
            intent.putExtra("SurName", surname);
            intent.putExtra("about", about);
            intent.putExtra("dob", dob_str);
            intent.putExtra("city", tv_city.getText().toString());
            intent.putExtra("country", ed_country.getText().toString());
            intent.putExtra(Constants.ACTIVITY_NAME, activity_type);
            startActivity(intent);
        } else {
            Intent intent = new Intent(CreateAccount_3_Activity.this, CreateAccount_4_Activity.class);
            if (file_name != null) {

                intent.putExtra("Email", email);
                intent.putExtra("FirstName", firstname);
                intent.putExtra("SurName", surname);
                intent.putExtra("FilePath", file_name);
                intent.putExtra("userType", user_type);
                intent.putExtra("about", about);
                intent.putExtra("dob", dob_str);
                intent.putExtra("city", tv_city.getText().toString());
                intent.putExtra("country", ed_country.getText().toString());
                intent.putExtra(Constants.ACTIVITY_NAME, activity_type);
                startActivity(intent);
            } else {
                intent.putExtra("Email", email);
                intent.putExtra("FirstName", firstname);
                intent.putExtra("SurName", surname);
                intent.putExtra("userType", user_type);
                intent.putExtra("about", about);
                intent.putExtra("dob", dob_str);
                intent.putExtra("city", tv_city.getText().toString());
                intent.putExtra("country", ed_country.getText().toString());
                intent.putExtra(Constants.ACTIVITY_NAME, activity_type);
                startActivity(intent);
            }
        }

    }


    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error : errors) {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(this);
            if (view.getId() == R.id.ed_country) {
                message = getString(R.string.msg_country);
            }
            if (view.getId() == R.id.tv_city) {
                message = getString(R.string.msg_city);
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
        showDialog();
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
        setAddress(mLastLocation.latitude, mLastLocation.longitude);
    }

    private void setAddress(Double latitude, Double longitude) {
        if (latitude != null && longitude != null) {
            showDialog();
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
                    if (addresses.get(0).getLocality() != null)
                        city = addresses.get(0).getLocality();
                    else if (addresses.get(0).getSubAdminArea() != null)
                        city = addresses.get(0).getSubAdminArea();
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
                    ed_country.setText(country);
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
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {

            case RequestPermissionCode:

                if (grantResults.length > 0) {

                    boolean LocationPermission = grantResults[0] == PackageManager.PERMISSION_GRANTED;


                    if (LocationPermission) {
                        startLocationUpdates();
                        // Toast.makeText(HomeActivity.this, "Permission Granted", Toast.LENGTH_LONG).show();
                    } else {
                        //Toast.makeText(HomeActivity.this,"Permission Denied",Toast.LENGTH_LONG).show();
                    }
                }

                break;
        }
    }


    @Override
    public void onClick(View v) {
        Intent intent = new Intent(CreateAccount_3_Activity.this, LoginActivity.class);
        switch (v.getId()) {
            case R.id.img_backarrow:
                this.finish();
                break;

            case R.id.img_cross:

            case R.id.tv_already_account:
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                break;

            case R.id.edit_month:
            case R.id.edit_year:
            case R.id.edit_date:
                new DatePickerDialog(CreateAccount_3_Activity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                break;
        }
    }


}
