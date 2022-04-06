package com.hypernym.evaconnect.view.ui.fragments;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.CAMERA;
import static com.hypernym.evaconnect.utils.AppUtils.getApplicationContext;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
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
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
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
import com.hypernym.evaconnect.models.User;
import com.hypernym.evaconnect.repositories.CustomViewModelFactory;
import com.hypernym.evaconnect.utils.LoginUtils;
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


public class LanguageFragment extends BaseFragment implements Validator.ValidationListener,
        com.google.android.gms.location.LocationListener, View.OnClickListener {

    private Validator validator;
    @BindView(R.id.btn_submit)
    TextView btn_submit;


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


    String email, photourl, activity_type, user_type, firstname, surname, file_name, path, about, dob, city, companyUrl;
    final Calendar myCalendar = Calendar.getInstance();
    User user = new User();
    String dob_str = "";
    public static final int RequestPermissionCode = 1;
    LatLng mLastLocation;
    long UPDATE_INTERVAL = 15000;  /* 1 sec */
    long FASTEST_INTERVAL = 501; /* 1/2 sec */
    public static int counter = 0;
    private LocationRequest mLocationRequest;
    private LocationManager locationManager;
    User userData;
    private LocationViewModel viewModel;
    private String hCountyCodeFromLocation = "";
    List<String> hCitiesList = new ArrayList<String>();

    DatePickerDialog.OnDateSetListener date = (view, year, monthOfYear, dayOfMonth) -> {
        // TODO Auto-generated method stub
        myCalendar.set(Calendar.YEAR, year);
        myCalendar.set(Calendar.MONTH, monthOfYear);
        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        updateLabel();
    };
    private String selected_lang = "English";

    public LanguageFragment(User userData1) {
        userData = userData1;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        locationManager = (LocationManager) requireActivity().getSystemService(Context.LOCATION_SERVICE);

        user = LoginUtils.getUser();
        init();

        if (NetworkUtils.isNetworkConnected(requireContext())) {
            showDialog();
            isLocationEnabled();
            startLocationUpdates();
        } else {
            hideDialog();
            Toast.makeText(requireContext(), "Check Your Internet Connection", Toast.LENGTH_LONG).show();
            requireActivity().onBackPressed();
        }


    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        locationManager = (LocationManager) requireActivity().getSystemService(Context.LOCATION_SERVICE);

        viewModel = ViewModelProviders.of(this, new CustomViewModelFactory(requireActivity().getApplication(), getActivity())).get(LocationViewModel.class);


        return inflater.inflate(R.layout.fragment_language, container, false);
    }

    private void isLocationEnabled() {
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Log.d("fragmentlanguage", "isLocationEnabled: ");
            return;
        } else {
            Toast.makeText(requireContext(), "Turn ON your location ", Toast.LENGTH_LONG).show();
        }
    }

    /*  private void hSearchCountryCode() {
          ed_country.setText(userData.getCountry());
          showDialog();

          if (hCountyCodeFromLocation.equals("")) {
              hCountyCodeFromLocation = "PK";
          }



      }

      private void buildAlertMessageNoGps() {
          final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
          builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                  .setCancelable(false)
                  .setPositiveButton("Enable GPS", (dialog, id) -> startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS)))
                  .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
          final AlertDialog alert = builder.create();
          alert.setCancelable(false);
          alert.setIcon(R.drawable.ic_location_disabled_black_24dp);
          alert.setCanceledOnTouchOutside(false);
          alert.show();
      }

      */


    private void init() {
        hSetCurrentDateTime();

        if (user.getType().equalsIgnoreCase("user")) {
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
        img_cross.setOnClickListener(this);

        btn_submit.setOnClickListener(new OnOneOffClickListener() {
            @Override
            public void onSingleClick(View v) {

                if (Checkpermission()) {
                    startLocationUpdates();
                    validator.validate();
                } else {
                    requestlocationpermission();
                }
            }
        });


        MySpinnerAdapter adapterlanguages = new MySpinnerAdapter(
                getActivity(),
                android.R.layout.simple_spinner_item,
                Arrays.asList(getResources().getStringArray(R.array.languageSpinnerItems))
        );
        //
        adapterlanguages.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        edit_language.setAdapter(adapterlanguages);
        edit_language.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?>arg0, View view, int arg2, long arg3) {

                selected_lang = edit_language.getSelectedItem().toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });



        edit_date.setOnClickListener(v -> {
            // TODO Auto-generated method stub
            new DatePickerDialog(getContext(), R.style.DialogTheme, date, myCalendar
                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH)).show();
        });

        edit_month.setOnClickListener(v -> {
            // TODO Auto-generated method stub
            new DatePickerDialog(getContext(), R.style.DialogTheme, date, myCalendar
                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH)).show();
        });

        edit_year.setOnClickListener(v -> {
            // TODO Auto-generated method stub
            new DatePickerDialog(getContext(), R.style.DialogTheme, date, myCalendar
                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH)).show();
        });
    }

    private void updateLabel() {
        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        edit_date.setText(String.valueOf(DateFormat.format("dd",   myCalendar.getTime())));
        edit_month.setText(String.valueOf(DateFormat.format("MM",   myCalendar.getTime())));
        edit_year.setText(String.valueOf(DateFormat.format("yyyy", myCalendar.getTime())));
        dob_str = sdf.format(myCalendar.getTime());

      /*  String dayOfTheWeek = (String) DateFormat.format("EEEE", myCalendar.getTime()); // Thursday
        String day          = (String) DateFormat.format("dd",   myCalendar.getTime()); // 20
        String monthString  = (String) DateFormat.format("MMM",  myCalendar.getTime()); // Jun
        String monthNumber  = (String) DateFormat.format("MM",   myCalendar.getTime()); // 06
        String year         = (String) DateFormat.format("YY", myCalendar.getTime()); // 2013*/
        /* edit_dob.setText(sdf.format(myCalendar.getTime()));*/
    }

    @Override
    public void onResume() {
        super.onResume();

        Log.d("fragmentlanguage", "onResume: ");

        startLocationUpdates();
    }

    @Override
    public void onValidationSucceeded() {
        String country = ed_country.getText().toString();
        String language = edit_language.getSelectedItem().toString();
        String city = spinCities.getSelectedItem().toString();

        userData.setCountry(country);
        userData.setLanguage(language);
        userData.setCity(city);
        userData.setId(LoginUtils.getUser().getId());

        showDialog();
        viewModel.hUpdateUserLocationData(userData.getId(), userData).observe(requireActivity(), object -> {
            hideDialog();
            Toast.makeText(requireActivity(), "Location UPDATED Successfully.", Toast.LENGTH_SHORT).show();
            requireActivity().onBackPressed();
        });
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error : errors) {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(getActivity());
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
                Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
            }
        }
    }

    @SuppressLint("RestrictedApi")
    protected void startLocationUpdates() {

        Log.d("fragmentlanguage", "startLocationUpdates: ");

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
        SettingsClient settingsClient = LocationServices.getSettingsClient(requireContext());
        settingsClient.checkLocationSettings(locationSettingsRequest);

        // new Google API SDK v11 uses getFusedLocationProviderClient(this)
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        LocationServices.getFusedLocationProviderClient(requireContext()).requestLocationUpdates(mLocationRequest, new LocationCallback() {
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
        setAddress(location.getLatitude(), location.getLongitude());
    }

    private void setAddress(Double latitude, Double longitude) {


        if (latitude != null && longitude != null) {
            showDialog();
            Geocoder geocoder;


            List<Address> addresses = null;
            geocoder = new Geocoder(requireContext(), Locale.getDefault());

            if (geocoder != null) {
                try {
                    addresses = geocoder.getFromLocation(latitude, longitude, 2); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                    Log.d("fragmentlanguage", "Address :" + addresses.get(0).toString());

                } catch (IOException e) {

                    e.printStackTrace();

                }
            }

            try {
                if (addresses.size() > 0) {
                    Log.d("fragmentlanguage", " " + addresses.get(0).getMaxAddressLineIndex());

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
                    Log.d("fragmentlanguage", address);

                    String[] splitArray = address.split(",");
                    String new_text = splitArray[3];
                    String street = splitArray[0];
                    String sector = splitArray[1];

                    hCountyCodeFromLocation = addresses.get(0).getCountryCode();
                    hSetCountry();

                    Log.d("fragmentlanguage", hCountyCodeFromLocation);
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
        if (NetworkUtils.isNetworkConnected(requireContext())) {
            viewModel.hGetAllCities(hCountyCodeFromLocation).observe(requireActivity(), response -> {

                spinCities.setSelection(0);

                if (response != null) {
                    for (int i = 0; i < response.size(); i++) {
                        hCitiesList.add(response.get(i).name);
                    }
                    hideDialog();

                    hSetCitySpinner();

                } else {
                    Toast.makeText(requireContext(), "Some thing went wrong...", Toast.LENGTH_LONG).show();
                    hideDialog();

                }

            });
        }

    }


    public boolean Checkpermission() {

        int FirstPermissionResult = ContextCompat.checkSelfPermission(requireContext(), ACCESS_FINE_LOCATION);
        int CameraResult = ContextCompat.checkSelfPermission(requireContext(), CAMERA);


        return FirstPermissionResult == PackageManager.PERMISSION_GRANTED &&
                CameraResult == PackageManager.PERMISSION_GRANTED;
    }


    private void requestlocationpermission() {
        ActivityCompat.requestPermissions(requireActivity(), new String[]
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

        switch (v.getId()) {
            case R.id.img_backarrow:
                getActivity().onBackPressed();
                break;


            case R.id.edit_month:
            case R.id.edit_year:
            case R.id.edit_date:
                new DatePickerDialog(getContext(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                break;
        }
    }


    private void hSetCurrentDateTime() {
        String date = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        String[] result = date.split("-");
        edit_date.setText(result[0]);
        edit_month.setText(result[1]);
        edit_year.setText(result[2]);
        updateLabel();

    }


    private void hSetCitySpinner() {
        ArrayAdapter<String> hCitiesAdapter = new ArrayAdapter<String>(getActivity(),
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