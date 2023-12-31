package com.hypernym.evaconnect.view.ui.activities;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.CAMERA;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
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
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

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
import com.hypernym.evaconnect.models.City;
import com.hypernym.evaconnect.repositories.CustomViewModelFactory;
import com.hypernym.evaconnect.utils.Constants;
import com.hypernym.evaconnect.utils.NetworkUtils;
import com.hypernym.evaconnect.view.adapters.MySpinnerAdapter;
import com.hypernym.evaconnect.viewmodel.LocationViewModel;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CreateAccount_3_Activity extends BaseActivity implements Validator.ValidationListener,
        com.google.android.gms.location.LocationListener, View.OnClickListener {
    private Validator validator;
    private LocationViewModel viewModel;
    private String hCountyCodeFromLocation = "";
    List<String> hCitiesList = new ArrayList<String>();

    @BindView(R.id.btn_next)
    Button btn_next;

    @BindView(R.id.tv_dob)
    TextView tv_dob;

    @BindView(R.id.title)
    TextView title;

    @NotEmpty
    @BindView(R.id.ed_country)
    EditText ed_country;

   /* @NotEmpty
    @BindView(R.id.tv_city)
    EditText tv_city;*/

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

    @BindView(R.id.tv_city)
    Spinner spinCities;

    @BindView(R.id.edit_language)
    Spinner edit_language;

    String email, photourl, activity_type, user_type, firstname, surname, file_name, path, about, dob, city, companyUrl;
    final Calendar myCalendar = Calendar.getInstance();

    String dob_str = "";
    public static final int RequestPermissionCode = 1;
    LatLng mLastLocation;
    long UPDATE_INTERVAL = 15000;  /* 1 sec */
    long FASTEST_INTERVAL = 501; /* 1/2 sec */
    public static int counter = 0;
    private LocationRequest mLocationRequest;
    private LocationManager locationManager;
    DatePickerDialog.OnDateSetListener myDateListener;

    DatePickerDialog.OnDateSetListener date = (view, year, monthOfYear, dayOfMonth) -> {
        // TODO Auto-generated method stub
        myCalendar.set(Calendar.YEAR, year);
        myCalendar.set(Calendar.MONTH, monthOfYear);
        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        updateLabel();
    };
    private String selected_val ="English";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account_3);
        ButterKnife.bind(this);

        tv_already_account.setOnClickListener(this);
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        init();

        if (NetworkUtils.isNetworkConnected(this)) {
            showDialog();


            if(!isLocationEnabled()) {
                hSetDefaultCountry();

            }
            else{
                hideDialog();
            }

        } else {
            hideDialog();
            Toast.makeText(getBaseContext(), "Check Your Internet Connection", Toast.LENGTH_LONG).show();
        }
    }

    private boolean isLocationEnabled() {
        if ((ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) ){
            return true;
        }
        else{
            Toast.makeText(this, "Turn ON your location ", Toast.LENGTH_LONG).show();
            return false;

        }
//        if (
//        locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)&& locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
//
//        } else {
//
//        }
    }


    private void hSetDefaultCountry() {
        ed_country.setText("United Kingdom");
        showDialog();

        if (hCountyCodeFromLocation.equals("")) {
            hCountyCodeFromLocation = "GB";
        }

        if (NetworkUtils.isNetworkConnected(this)) {
            viewModel.hGetAllCities(hCountyCodeFromLocation).observe(this, new Observer<List<City>>() {
                @Override
                public void onChanged(List<City> response) {


                    spinCities.setSelection(0);

                    if (response != null) {

                        Log.d("cities", "onChanged: "+response);
                     for (int i = 0; i < response.size(); i++) {
                            hCitiesList.add(response.get(i).name);
                        }
                        hideDialog();

                        hSetCitySpinner();

                    } else {
                        Toast.makeText(getBaseContext(), "Some thing went wrong...", Toast.LENGTH_LONG).show();
                        hideDialog();

                    }

                }
            });
        }


    }

  /*  private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Enable GPS", (dialog, id) -> startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS)))
                .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
        final AlertDialog alert = builder.create();
        alert.setCancelable(false);
        alert.setIcon(R.drawable.ic_location_disabled_black_24dp);
        alert.setCanceledOnTouchOutside(false);
        alert.show();
    }*/

    private void init() {
        String type = getIntent().getStringExtra(Constants.ACTIVITY_NAME);
        hSetCurrentDateTime();


        if (NetworkUtils.isNetworkConnected(this)) {

            viewModel = ViewModelProviders.of(this, new CustomViewModelFactory(this.getApplication(), this)).get(LocationViewModel.class);

        }

        if ("LinkedinActivity".equals(getIntent().getStringExtra(Constants.ACTIVITY_NAME))) {
            email = getIntent().getStringExtra("Email");
            photourl = getIntent().getStringExtra("Photo");
            user_type = getIntent().getStringExtra("userType");
            path = getIntent().getStringExtra("Path");
            activity_type = "LinkedinActivity";
            companyUrl = getIntent().getStringExtra("companyUrl");
            firstname = getIntent().getStringExtra("FirstName");
            surname = getIntent().getStringExtra("SurName");
            about = getIntent().getStringExtra("about");

        } else if (!TextUtils.isEmpty(type) && type.equals(AppConstants.FACEBOOK_LOGIN_TYPE)) {
            email = getIntent().getStringExtra("Email");
            photourl = getIntent().getStringExtra("Photo");
            path = getIntent().getStringExtra("Path");
            user_type = getIntent().getStringExtra("userType");
            activity_type = AppConstants.FACEBOOK_LOGIN_TYPE;
            companyUrl = getIntent().getStringExtra("companyUrl");
            firstname = getIntent().getStringExtra("FirstName");
            surname = getIntent().getStringExtra("SurName");
            about = getIntent().getStringExtra("about");

        } else {
            email = getIntent().getStringExtra("Email");
            activity_type = "normal_type";
            file_name = getIntent().getStringExtra("FilePath");
            user_type = getIntent().getStringExtra("userType");
            firstname = getIntent().getStringExtra("FirstName");
            companyUrl = getIntent().getStringExtra("companyUrl");
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

                Log.d("nextbtn", "Click");

                if (Checkpermission()) {
                    startLocationUpdates();
                    validator.validate();
                } else {
                    requestlocationpermission();
                }
            }
        });





        MySpinnerAdapter adapterlanguages = new MySpinnerAdapter(
                this,
                android.R.layout.simple_spinner_item,
                Arrays.asList(getResources().getStringArray(R.array.languageSpinnerItems))
        );

        adapterlanguages.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        edit_language.setAdapter(adapterlanguages);

        edit_language.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View view, int arg2, long arg3) {

                selected_val = edit_language.getSelectedItem().toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });

        ed_country.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        spinCities.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int position, long id) {
                city = spinCities.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }

        });

        edit_date.setOnClickListener(v -> {
            // TODO Auto-generated method stub


            new DatePickerDialog(CreateAccount_3_Activity.this, R.style.DialogTheme, myDateListener, myCalendar
                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH)).show();
        });

        edit_month.setOnClickListener(v -> {
            // TODO Auto-generated method stub
            new DatePickerDialog(CreateAccount_3_Activity.this, R.style.DialogTheme, myDateListener, myCalendar
                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH)).show();
        });

        edit_year.setOnClickListener(v -> {
            new DatePickerDialog(CreateAccount_3_Activity.this, R.style.DialogTheme, myDateListener, myCalendar
                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH)).show();

        });
        // Added by AliRaza on 07-05-22//
        myDateListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {

                edit_date.setText(String.valueOf(arg3));
                edit_month.setText(String.valueOf(arg2+1));
                edit_year.setText(String.valueOf(arg1));

                // arg1 = year
                // arg2 = month
                // arg3 = day
            }
        };
    }

    private void hSetCurrentDateTime() {
        String date = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        String[] result = date.split("-");
        edit_date.setText(result[0]);
        edit_month.setText(result[1]);
        edit_year.setText(result[2]);
        updateLabel();
    }






    private void updateLabel() {

        dob_str = edit_year.getText().toString() + "-" + edit_month.getText().toString() + "-" + edit_date.getText().toString();

        Log.d("test123", dob_str);
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
        if(isLocationEnabled()){
            startLocationUpdates();
        }


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
            intent.putExtra("companyUrl", companyUrl);
            intent.putExtra("city", city);
            intent.putExtra("country", ed_country.getText().toString());
            intent.putExtra("about", about);
            intent.putExtra("language", selected_val);
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
            intent.putExtra("companyUrl", companyUrl);
            intent.putExtra("city", city);
            intent.putExtra("country", ed_country.getText().toString());
            intent.putExtra("language", selected_val);
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
                intent.putExtra("companyUrl", companyUrl);
                intent.putExtra("city", city);
                intent.putExtra("country", ed_country.getText().toString());
                intent.putExtra("language", selected_val);
                intent.putExtra(Constants.ACTIVITY_NAME, activity_type);
                startActivity(intent);
            } else {
                intent.putExtra("Email", email);
                intent.putExtra("FirstName", firstname);
                intent.putExtra("SurName", surname);
                intent.putExtra("userType", user_type);
                intent.putExtra("about", about);
                intent.putExtra("dob", dob_str);
                intent.putExtra("companyUrl", companyUrl);
                intent.putExtra("city", city);
                intent.putExtra("country", ed_country.getText().toString());
                intent.putExtra("language", selected_val);
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
//        showDialog();
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
                    String countryCode = addresses.get(0).getCountryCode();

                    hCountyCodeFromLocation = countryCode;



                    hSetCountry();

                    addresses.get(0).getAdminArea();

                    String[] splitArray = address.split(",");
                    String new_text = splitArray[3];
                    String street = splitArray[0];
                    String sector = splitArray[1];

                    //  spinCities.setText(city);
                    ed_country.setText(country);
                    hideDialog();

                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private void hSetCountry() {
        showDialog();
        viewModel.hGetAllCities(hCountyCodeFromLocation).observe(this, response -> {

            if (response != null) {
                for (int i = 0; i < response.size(); i++) {
                    hCitiesList.add(response.get(i).name);
                    Log.d("test123", response.get(i).name);
                }
                hSetCitySpinner();

                hideDialog();
            } else {
                Toast.makeText(getBaseContext(), "Some thing went wrong...", Toast.LENGTH_LONG).show();
            }

        });

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
    public void onRequestPermissionsResult(int requestCode, String permissions[],
                                           int[] grantResults) {
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
                this.finish();
                break;

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

    private void hSetCitySpinner() {
        Log.d("citiesSpinner", "hSetCitySpinner: "+hCitiesList.size());
        ArrayAdapter<String> hCitiesAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, hCitiesList);
        hCitiesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        spinCities.setAdapter(hCitiesAdapter);

        spinCities.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int position, long id) {
                city = hCitiesList.get(position);
                spinCities.setSelection(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
    }

}
