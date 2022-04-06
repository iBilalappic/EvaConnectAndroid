package com.hypernym.evaconnect.view.ui.fragments;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.CAMERA;
import static com.hypernym.evaconnect.utils.AppUtils.getApplicationContext;

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
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
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
import com.hypernym.evaconnect.listeners.OnOneOffClickListener;
import com.hypernym.evaconnect.models.BaseModel;
import com.hypernym.evaconnect.models.User;
import com.hypernym.evaconnect.repositories.CustomViewModelFactory;
import com.hypernym.evaconnect.utils.Constants;
import com.hypernym.evaconnect.utils.DateUtils;
import com.hypernym.evaconnect.utils.LoginUtils;
import com.hypernym.evaconnect.view.adapters.MySpinnerAdapter;
import com.hypernym.evaconnect.viewmodel.UserViewModel;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;


public class EditProfileLocationFragment extends BaseFragment implements Validator.ValidationListener,
        com.google.android.gms.location.LocationListener, View.OnClickListener {

    private Validator validator;

    @BindView(R.id.btn_next)
    TextView btn_next;


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

    User user = new User();
    User userData = new User();


    String email, photourl, activity_type, user_type, firstname, surname, file_name, path, about, dob, city, companyUrl;
    final Calendar myCalendar = Calendar.getInstance();
    private UserViewModel userViewModel;

    String dob_str = "";
    public static final int RequestPermissionCode = 1;
    LatLng mLastLocation;
    long UPDATE_INTERVAL = 15000;  /* 1 sec */
    long FASTEST_INTERVAL = 501; /* 1/2 sec */
    public static int counter = 0;
    private LocationRequest mLocationRequest;
    private LocationManager locationManager;
    ArrayAdapter<String> adapterUkCities;
    ArrayAdapter<String> adapterPakCities;

    DatePickerDialog.OnDateSetListener date = (view, year, monthOfYear, dayOfMonth) -> {
        // TODO Auto-generated method stub
        myCalendar.set(Calendar.YEAR, year);
        myCalendar.set(Calendar.MONTH, monthOfYear);
        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        updateLabel();
    };
    private String selected_val = "English";
    private String str_address = "";

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        userViewModel = ViewModelProviders.of(this, new CustomViewModelFactory(getActivity().getApplication())).get(UserViewModel.class);
        user = LoginUtils.getLoggedinUser();
        img_backarrow.setOnClickListener(this);
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        isLocationEnabled();
        init();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_proflie_location, container, false);
    }

    private void isLocationEnabled() {
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            return;
        } else {
            buildAlertMessageNoGps();
        }
    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
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

    @Override
    public void onValidationSucceeded() {
        update();

    }


    private void init() {

        adapterUkCities = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_item, getResources()
                .getStringArray(R.array.uk_cities));

        adapterPakCities = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_item, getResources()
                .getStringArray(R.array.pak_cities));


        startLocationUpdates();

        if ((getArguments() != null)) {
            userData = getArguments().getParcelable(Constants.USER);
            /* edit_about_yourself.setText(userData.getBio_data());*/
            ed_country.setText(userData.getCountry());


            if (userData.getDate_of_birth()!=null) {
                setDob(userData.getDate_of_birth());
            }

            Log.v("USER", userData.toString());
        }

        if (userData.getType().equals("user")) {
            layout_date.setVisibility(View.GONE);
            tv_dob.setVisibility(View.GONE);
            title.setText("Date of Birth / Location");
        } else {
            layout_date.setVisibility(View.GONE);
            tv_dob.setVisibility(View.GONE);
            title.setText("Location");
        }

        validator = new Validator(this);
        validator.setValidationListener(this);
        img_backarrow.setOnClickListener(this);

        btn_next.setOnClickListener(new OnOneOffClickListener() {
            @Override
            public void onSingleClick(View v) {

                if (Checkpermission()) {
                    // LaunchGallery();

                    validator.validate();

                } else {
                    requestlocationpermission();
                }
            }
        });



       /* ArrayAdapter<String> adapterlanguages = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, getResources()
                .getStringArray(R.array.languageSpinnerItems));*/


        MySpinnerAdapter adapterlanguages = new MySpinnerAdapter(
                getContext(),
                android.R.layout.simple_spinner_item,
                Arrays.asList(getResources().getStringArray(R.array.languageSpinnerItems))
        );
        adapterUkCities.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapterPakCities.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //
        adapterlanguages.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        edit_language.setAdapter(adapterlanguages);

        if (userData.getLanguage() != null) {
            int spinnerPosition = adapterlanguages.getPosition(userData.getLanguage());
            edit_language.setSelection(spinnerPosition);
        }

        if (userData.getCountry().equalsIgnoreCase("United Kingdom")) {
            spinCities.setAdapter(adapterUkCities);

            if (userData.getCity() != null) {
                int spinnerPosition = adapterUkCities.getPosition(userData.getCity());
                spinCities.setSelection(spinnerPosition);
            }
        } else if (userData.getCountry().equalsIgnoreCase("Pakistan")) {
            spinCities.setAdapter(adapterPakCities);

            if (userData.getCity() != null) {
                int spinnerPosition = adapterPakCities.getPosition(userData.getCity());
                spinCities.setSelection(spinnerPosition);
            }
        }


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
                if (s.toString().equalsIgnoreCase("United Kingdom")) {
                    spinCities.setAdapter(adapterUkCities);

                } else if (s.toString().equalsIgnoreCase("Pakistan")) {
                    spinCities.setAdapter(adapterPakCities);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        spinCities.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int position, long id) {
                city = arg0.getItemAtPosition(position).toString();
                Log.d("city", "" + city);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });


        edit_date.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(getContext(), R.style.DialogTheme, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        edit_month.setOnClickListener(v -> {
            new DatePickerDialog(getContext(), R.style.DialogTheme, date, myCalendar
                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH)).show();
        });

        edit_year.setOnClickListener(v -> {
            new DatePickerDialog(getContext(), R.style.DialogTheme, date, myCalendar
                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH)).show();
        });
    }

    private void update() {
        showDialog();
        userData.setCity(spinCities.getSelectedItem().toString());
        userData.setCountry(ed_country.getText().toString());
        userData.setLanguage(edit_language.getSelectedItem().toString());
        if (!str_address.isEmpty()) {
            userData.setAddress(str_address);
        }

        if (userData.getType().equalsIgnoreCase("user")) {
            userData.setDate_of_birth(dob_str);
        }


        HashMap<String, Object> body = new HashMap<String, Object>();
        body.put("city", userData.getCity());
        body.put("country", userData.getCountry());
        if (user.getType().equalsIgnoreCase("user")) {
            body.put("date_of_birth", userData.getDate_of_birth());
        }

        if (!str_address.isEmpty()) {
            body.put("address", userData.getAddress());
        }
        body.put("language", userData.getLanguage());
        body.put("modified_by_id", userData.getId());
        body.put("modified_datetime", DateUtils.GetCurrentdatetime());
        userViewModel.editProfile(body, userData.getId()).observe(this, new Observer<BaseModel<List<Object>>>() {
            @Override
            public void onChanged(BaseModel<List<Object>> listBaseModel) {
                if (!listBaseModel.isError()) {
                    getActivity().onBackPressed();
                } else {
                    networkResponseDialog(getString(R.string.error), getString(R.string.err_unknown));
                }
                hideDialog();
            }
        });
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error : errors) {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(getContext());
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
                Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
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
        SettingsClient settingsClient = LocationServices.getSettingsClient(getContext());
        settingsClient.checkLocationSettings(locationSettingsRequest);

        // new Google API SDK v11 uses getFusedLocationProviderClient(this)
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        LocationServices.getFusedLocationProviderClient(getContext()).requestLocationUpdates(mLocationRequest, new LocationCallback() {
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
            geocoder = new Geocoder(getContext(), Locale.getDefault());

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
                    str_address = address;
                    //  spinCities.setText(city);
                    ed_country.setText(country);
                    hideDialog();

                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public boolean Checkpermission() {

        int FirstPermissionResult = ContextCompat.checkSelfPermission(getContext(), ACCESS_FINE_LOCATION);
        int CameraResult = ContextCompat.checkSelfPermission(getContext(), CAMERA);


        return FirstPermissionResult == PackageManager.PERMISSION_GRANTED &&
                CameraResult == PackageManager.PERMISSION_GRANTED;
    }

    private void requestlocationpermission() {
        ActivityCompat.requestPermissions(getActivity(), new String[]
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

    private void updateLabel() {
       // String myFormat = "MM/dd/yy"; //In which you need put here
        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        edit_date.setText(String.valueOf(DateFormat.format("dd", myCalendar.getTime())));
        edit_month.setText(String.valueOf(DateFormat.format("MM", myCalendar.getTime())));
        edit_year.setText(String.valueOf(DateFormat.format("yyyy", myCalendar.getTime())));
        dob_str = sdf.format(myCalendar.getTime());

    }

    private void setDob(String date_s)  {

        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        Calendar cal = Calendar.getInstance();
        Date d = null;
        try {
            d = sdf.parse(date_s);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (d != null) {
            cal.setTime(d);
            edit_date.setText(String.valueOf(DateFormat.format("dd", cal.getTime())));
            edit_month.setText(String.valueOf(DateFormat.format("MM", cal.getTime())));
            edit_year.setText(String.valueOf(DateFormat.format("yyyy", cal.getTime())));
            dob_str = sdf.format(cal.getTime());
        }

        //


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_backarrow:
                getActivity().onBackPressed();
                break;

        }
    }

}