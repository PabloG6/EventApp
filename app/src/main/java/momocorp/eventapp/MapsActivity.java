package momocorp.eventapp;

import android.*;
import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;

import java.util.Locale;

public class MapsActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener{

    // NON-PUBLIC, NON-STATIC FIELDS START WITH M;
    TextView mLatitude;
    TextView mLongitude;
    Button mButton;
    LocationRequest mLocationRequest;
    Location mLocation;
    LinearLayout mLinearLayout;
    boolean mLocationUpdateStatus;


    //PRIVATE STATIC VARIABLES
    private static final int REQUEST_COARSE_LOCATION = 0;
    public static final String LOCATION_TAG = "LocationTest";

    //PUBLIC STATIC VARIABLES
    public static final int UPDATE_LOCATION_INTERVALS = 1000;
    public static final int MAXIMUM_UPDATE_INTERVAL = UPDATE_LOCATION_INTERVALS/2;

    //PRIVATE VARIABLES;

    private GoogleApiClient googleApiClient;
    private  Context context;
    private Activity activity;






    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        context = this;
        mLatitude = (TextView) findViewById(R.id.latitude);
        mLongitude = (TextView) findViewById(R.id.longitude);
        mButton = (Button) findViewById(R.id.get_location);
        mLinearLayout = (LinearLayout) findViewById(R.id.main_linear_layout);
        activity = this;
        buildGoogleApiClient();
    }

    @Override
    protected void onStart() {
        super.onStart();
        googleApiClient.connect();


    }

    protected void onStop(){
        super.onStop();
        googleApiClient.disconnect();
    }

    //please create api client
    public synchronized void buildGoogleApiClient(){
        Log.i(LOCATION_TAG, "Building Google Api Client");
        googleApiClient = new GoogleApiClient.Builder(this).
                addConnectionCallbacks(this).
                addOnConnectionFailedListener(this).
                addApi(LocationServices.API).
                build();
        createLocationRequest();



    }


    public void createLocationRequest(){
        //instantiate location object;
        mLocationRequest = new LocationRequest();

        //set the interval
        mLocationRequest.setInterval(UPDATE_LOCATION_INTERVALS);
        //set the maximum interval
        mLocationRequest.setFastestInterval(MAXIMUM_UPDATE_INTERVAL);
        //set the priority
        mLocationRequest.setPriority(LocationRequest.PRIORITY_LOW_POWER);

    }

    public void startLocationUpdates(){
        ActivityCompat.requestPermissions(activity,
                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_COARSE_LOCATION);
        //start the location update.
        if(ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION)==PackageManager.PERMISSION_GRANTED)
            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, mLocationRequest, this);
        else
            Snackbar.make(mLinearLayout, "We don't have access to your location", Snackbar.LENGTH_SHORT);


    }

    public void snackBar(){
        Snackbar.make(mLinearLayout, "We don't have access to your location", Snackbar.LENGTH_SHORT);

    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if(mLocation==null){
            if(ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION)
                    ==PackageManager.PERMISSION_GRANTED) {
                mLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
            }
            else
                snackBar();
        }

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(Location location) {
        mLocation = location;
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLatitude.setText(mLocation.getLatitude()+"");
                mLongitude.setText(mLocation.getLongitude()+"");

            }
        });

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }






}


