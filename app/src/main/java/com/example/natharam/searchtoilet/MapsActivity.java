package com.example.natharam.searchtoilet;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Camera;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, DrawerLayout.DrawerListener,
        NavigationView.OnNavigationItemSelectedListener, LocationListener, GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks ,GoogleMap.OnInfoWindowClickListener,View.OnClickListener
{
    private static final Location TODO = null;
    MapFragment myMapFragment;
    private static final String TAG_MYMAPFRAGMENT = "TAG_MyMapFragment";
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    public static final int REQUEST_SETTING_LOCATION = 0x1;
    public static boolean LOCATION_TURN = false;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private static final int DEFAULT_ZOOM = 15;

    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;//Code used in requesting runtime permissions.
    private static final int REQUEST_CHECK_SETTINGS = 0x1;//Constant used in the location settings dialog.
    DatabaseReference mDatabaseReference;
    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationClient;//Provides access to the Fused Location Provider API.

    private SettingsClient mSettingsClient;//Provides access to the Location Settings API.
    private LocationRequest mLocationRequest;//Stores parameters for requests to the FusedLocationProviderApi.
    private LocationSettingsRequest mLocationSettingsRequest;//Stores the types of location services the client is interested in using.
    private LocationCallback mLocationCallback;

    private Camera mCameraPosition;

    private static final String KEY_CAMERA_POSITION = "camera_position";

    // Keys for storing activity state in the Bundle.
    private final static String KEY_REQUESTING_LOCATION_UPDATES = "requesting-location-updates";
    private final static String KEY_LOCATION = "location";
    private final static String KEY_LAST_UPDATED_TIME_STRING = "last-updated-time-string";

    private boolean mLocationPermissionGranted;
    private Location mLastKnownLocation;

    private final LatLng mDefaultLocation = new LatLng(23.224886, 72.646234);

    public DrawerLayout mDrawerLayout;
    public NavigationView mNavigationView;
    //  List<String> toiletRetData;
    LocationManager locationManager;
    //private static final String TAG = MainActivity.class.getSimpleName();
    ArrayList<LatLng> MarkerPoints;


    /**
     * The desired interval for location updates. Inexact. Updates may be more or less frequent.
     */
    private static final long UPDATE_INTERVAL_IN_MILLISECONDS = 10000;


    // flag for GPS Status
    boolean isGPSEnabled = false;

    // flag for network status
    boolean isNetworkEnabled = false;

    // flag for GPS Tracking is enabled
    boolean isGPSTrackingEnabled = false;

    Location location;
    static double latitude;
    static double longitude;

    // How many Geocoder should return our GPSTracker
    int geocoderMaxResults = 1;

    // The minimum distance to change updates in meters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters

    // The minimum time between updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60; // 1 minute

    // Declaring a Location Manager


    // Store LocationManager.GPS_PROVIDER or LocationManager.NETWORK_PROVIDER information
    private String provider_info;

    /**
     * The fastest rate for active location updates. Exact. Updates will never be more frequent
     * than this value.
     */
    private static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS =
            UPDATE_INTERVAL_IN_MILLISECONDS / 2;
    private String mLastUpdateTime;
    private boolean mRequestingLocationUpdates;
    private Button mStartUpdatesButton;
    private Button mStopUpdatesButton;
    ArrayList<ToiletRetData> toiletRetDatassd;
    static ToiletRetData data;
    public static MapsActivity mMapsActivity;
    //   ToiletRetData toiletRetDataa;
    Button go;
    Marker mCurrLocationMarker;
    LocationRequest mLocationReque;
    GoogleApiClient mGoogleApiClient;

    static ArrayList<String> stringArrayList;
    static ArrayList<Double> stringsDistance;
    ArrayList<AddressClass> addressClassArrayList;
    //For Storag
    private StorageReference mStorageRef;
    static int rindex=0;
    static double avarRating=0.0;
    static String city="Gandhi Nagar";
    EditText whereGo;
    static boolean starta;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        Toolbar toolbar = findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        whereGo = findViewById(R.id.findtext);
        MarkerPoints = new ArrayList();
        mDrawerLayout = findViewById(R.id.drawer_bar);
        mDrawerLayout.addDrawerListener(this);
        mNavigationView = findViewById(R.id.nav_view);
        mNavigationView.setNavigationItemSelectedListener(MapsActivity.this);
        stringsDistance=new ArrayList<>();
        toiletRetDatassd = new ArrayList<>();
        stringArrayList=new ArrayList<>();
        addressClassArrayList=new ArrayList<>();
        mMapsActivity = this;
        mRequestingLocationUpdates = false;
        mLastUpdateTime = "";
        data = new ToiletRetData();

        starta =true;

        //  toiletRetData=new ArrayList<>();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        mDatabaseReference = database.getReference("Messesg");
        mStorageRef = FirebaseStorage.getInstance().getReference("Messesg");

        try
        {

            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, mDrawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

            mDrawerLayout.addDrawerListener(toggle);

            toggle.syncState();

            android.app.FragmentManager myFragmentManager = getFragmentManager();


            myMapFragment = (MapFragment) myFragmentManager.findFragmentByTag(TAG_MYMAPFRAGMENT);
            if (!isGooglePlayServicesAvailable())
            {
                Toast.makeText(MapsActivity.this,"Open Google Map And Chack Current Location",Toast.LENGTH_LONG).show();

            }

            else
            {

                SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
                mapFragment.getMapAsync(this);

                mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
                mSettingsClient = LocationServices.getSettingsClient(this);
            }

        }

        catch (Exception e)
        {


        }

        go = findViewById(R.id.go);
        go.setOnClickListener(this);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.


    }


    public static MapsActivity getmMapsActivity() {


        return mMapsActivity;
    }



    private boolean isGooglePlayServicesAvailable() {
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (ConnectionResult.SUCCESS == status) {
            return true;
        } else {
            GooglePlayServicesUtil.getErrorDialog(status, this, 0).show();
            return false;
        }
    }


    public void getAverStar(){



        DatabaseReference databaseReferences = FirebaseDatabase.getInstance().getReference(toiletRetDatassd.get(rindex).getIdChild());
        databaseReferences.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Toast.makeText(MapsActivity.this, "DataSanap" +dataSnapshot, Toast.LENGTH_LONG).show();
                int count=0;
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){

                    CommentAdd commentAdd=snapshot.getValue(CommentAdd.class);
                    count++;
                    double a=Double.parseDouble(commentAdd.getRating());
                    avarRating=avarRating+a;




                }
                avarRating=avarRating/count;

                DecimalFormat decimalFormat=new DecimalFormat("#.#");
                String s=decimalFormat.format(avarRating);
                stringArrayList.add(rindex,s);


                rindex++;
                if(rindex<toiletRetDatassd.size()){
                    avarRating=0.0;
                    getAverStar();
                }
                else {
                    mNavigationView.setNavigationItemSelectedListener(MapsActivity.this);
                    rindex=0; avarRating=0.0;
                    // Toast.makeText(MapsActivity.this, "DataSanap"+stringArrayList, Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    @Override
    protected void onStart() {
        super.onStart();
        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                toiletRetDatassd.clear();

                int count=0;
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    data = dataSnapshot1.getValue(ToiletRetData.class);
                    toiletRetDatassd.add(data);
                    double latb = data.getLat();
                    double lngb = data.getLng();
                    AddressClass addressClass=new AddressClass(data.getNameOfToilet(),data.getAddressLocat(),latb,lngb);

                    addressClassArrayList.add(addressClass);


                    goToThisLocation(count,latb, lngb, 10);
                    // Toast.makeText(MapsActivity.this, "DataSanap" +toiletRetDatassd, Toast.LENGTH_LONG).show();
                    count++;
                }
                getAverStar();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }



    public void writeNewUser(String a, String b, String c, String d, double lat, double lng,String rating) {
        //User user = new User(name, email);






        String idChild = mDatabaseReference.push().getKey();
        getCommen(idChild,rating);
        ToiletRetData toiletRetDataa = new ToiletRetData(a, b, c, d, lat, lng, rating,idChild);
        mDatabaseReference.child(idChild).setValue(toiletRetDataa);





        Toast.makeText(MapsActivity.this, "Place Successfully Add!", Toast.LENGTH_SHORT).show();
    }




    public void getCommen(String idChild,String rating){
        Calendar calendar=Calendar.getInstance();
        SimpleDateFormat simpleDateFormat =new SimpleDateFormat(" dd-MM-yyyy HH:mm ");

        String timestamp=simpleDateFormat.format(calendar.getTime());
        CommentAdd commentAdd=new CommentAdd("Nice Toilet",rating,timestamp);
        DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference(idChild);
        String id = databaseReference.push().getKey();

        databaseReference.child(id).setValue(commentAdd);


    }



    public void upLoadFile(){


        Uri file = Uri.fromFile(new File("path/to/images/rivers.jpg"));
        StorageReference riversRef = mStorageRef.child("images/rivers.jpg");

        riversRef.putFile(file)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Get a URL to the uploaded content
                        //  Uri downloadUrl = taskSnapshot.getDownloadUrl();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        // ...
                    }
                });


    }







    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (mMap != null) {
            // outState.putParcelable(KEY_CAMERA_POSITION, mMap.getCameraPosition());
            //  outState.putParcelable(KEY_LOCATION, mLastKnownLocation);
            // outState.putBoolean(KEY_REQUESTING_LOCATION_UPDATES, mRequestingLocationUpdates);
            //  outState.putParcelable(KEY_LOCATION, mLastKnownLocation);
            //  outState.putString(KEY_LAST_UPDATED_TIME_STRING, mLastUpdateTime);
            super.onSaveInstanceState(outState);

        }
    }




    @Override
    public void onClick(View view) {

        try {
            goSearchFind();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void jet(){ Toast.makeText(this, "Lyasfasff", Toast.LENGTH_SHORT).show();}


    public void onMapReady(GoogleMap map) {
        mMap = map;
        mMap.setOnInfoWindowClickListener(this);
        // Do other setup activities here too, as described elsewhere in this tutorial.
        // Use a custom info window adapter to handle multiple lines of text in the
        // info window contents.
        mapClient();


        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

            @Override
            // Return null here, so that getInfoContents() is called next.
            public View getInfoWindow(Marker arg0) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {
                // Inflate the layouts for the info window, title and snippet.
                String a;
                View infoWindow = getLayoutInflater().inflate(R.layout.custum_info_contents,
                        (FrameLayout) findViewById(R.id.map), false);

                if(MarkerPoints.get(0)==marker.getPosition()){

                    TextView title = ((TextView) infoWindow.findViewById(R.id.title));
                    title.setText(marker.getTitle());

                    Toast.makeText(MapsActivity.this, "permisson10", Toast.LENGTH_SHORT).show();
                    TextView snippet = ((TextView) infoWindow.findViewById(R.id.snippet));
                    snippet.setText(marker.getSnippet());
                }
                else {

                    TextView title = ((TextView) infoWindow.findViewById(R.id.title));
                    title.setText(marker.getTitle());

                    RatingBar ratingBar=infoWindow.findViewById(R.id.ratingBar3);
                    try {
                        String asd=marker.getSnippet();
                        int kept = Integer.parseInt(asd.substring( 0, asd.indexOf("/")));
                        ratingBar.setRating(Float.parseFloat(stringArrayList.get(kept)));
                        // Toast.makeText(MapsActivity.this, "permisson10"+kept, Toast.LENGTH_SHORT).show();

                    }catch (Exception e){

                    }



                    TextView snippet = ((TextView) infoWindow.findViewById(R.id.snippet));
                    // snippet.setText(marker.getSnippet());
                }

                // MarkerPoints.remove(1);



                return infoWindow;
            }
        });

        mRequestingLocationUpdates = checkLocationPermission();
        // Turn on the My Location layer and the related control on the map.


        // Get the current location of the device and set the position of the map.
        //  getDeviceLocation();
        //goToThisLocation(23.224886, 72.646234,7);

    }



    public void mapClient(){


        try{ if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
                mMap.setMyLocationEnabled(true);
            }
        } else {
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
        }}catch(Exception e){}



    }




    public void goToThisLocation(int count,double lat, double lng, float zom) {
        LatLng ll = new LatLng(lat, lng);


        if (zom == 10) {

            // String rating= getDataStarRating(index,data.getIdChild());
              stringsDistance.add(count, distance(data.getLat(),data.getLng(),mLastKnownLocation.getLatitude()
                      ,mLastKnownLocation.getLongitude()));
            Marker m = mMap.addMarker(new MarkerOptions()
                    .position(ll)
                    .title(String.valueOf(data.getNameOfToilet()))
                    .snippet(count+"/"+data.getIdChild())
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
            //  Toast.makeText(MapsActivity.this," asd"+rating,Toast.LENGTH_SHORT);



        } else if (zom == 15) {

            if(count==1){
                mMap.moveCamera(CameraUpdateFactory.newLatLng(ll));
                mMap.animateCamera(CameraUpdateFactory.zoomTo(12));

            }
            else {
                mMap.moveCamera(CameraUpdateFactory.newLatLng(ll));
                mMap.animateCamera(CameraUpdateFactory.zoomTo(10));

            }


        }


    }


    public void textCheang(){



    }
    public void goSearchFind() throws IOException {

        int i=0;
        String findLocation = whereGo.getText().toString();
        try {

            // city=findLocation.substring(findLocation.lastIndexOf(","));

            // Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
            if(true){

                for (int j =0;j<addressClassArrayList.size();j++){

                    if(findLocation.equals(addressClassArrayList.get(j).getNameOfPlace())){


                        goToThisLocation(1,addressClassArrayList.get(j).getLatti(),addressClassArrayList.get(j).getLngti(),15);
                        return;
                    }

                }




            }

            Geocoder geocoder = new Geocoder(this);
            List<Address> addressList = geocoder.getFromLocationName(findLocation, 1);

            Address address;
            if (addressList != null) {
                address = addressList.get(0);
                String locality = address.getLocality();
                Toast.makeText(this, "Locality", Toast.LENGTH_SHORT).show();
                double lat = address.getLatitude();
                double lng = address.getLongitude();
                goToThisLocation(0,lat, lng, 15);
            } else {
                goToThisLocation(0,23.224886, 72.646234, 15);


            }

        } catch (Exception e) {

        }


    }


    @Override
    protected void onResume() {
        super.onResume();

    }


    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopLocationUpdates();
    }


    protected void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(
                mGoogleApiClient, this);
        Log.d("TEG", "Location update stopped .......................");
    }







    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)

                .addConnectionCallbacks( MapsActivity.this)
                .addOnConnectionFailedListener(MapsActivity.this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(Bundle bundle) {



        if(mGoogleApiClient!=null) {

            int permissionLocation = ContextCompat.checkSelfPermission(MapsActivity.this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION);
            if (permissionLocation == PackageManager.PERMISSION_GRANTED) {
                mLastKnownLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                mLocationRequest = new LocationRequest();
                mLocationRequest.setInterval(3000);
                mLocationRequest.setFastestInterval(3000);
                mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                        .addLocationRequest( mLocationRequest);
                builder.setAlwaysShow(true);
                LocationServices.FusedLocationApi
                        .requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
                PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build());
                result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
                    @Override
                    public void onResult(LocationSettingsResult result) {
                        final Status status = result.getStatus();
                        switch (status.getStatusCode()) {
                            case LocationSettingsStatusCodes.SUCCESS:
                                Log.i("tag", "All location settings are satisfied.");
                                // You can initialize location requests here.
                                int permissionLocation = ContextCompat
                                        .checkSelfPermission(MapsActivity.this,
                                                android.Manifest.permission.ACCESS_FINE_LOCATION);
                                if (permissionLocation == PackageManager.PERMISSION_GRANTED) {
                                    mLastKnownLocation= LocationServices.FusedLocationApi
                                            .getLastLocation(mGoogleApiClient);
                                }

                                break;
                            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                                Log.i("tag", "Location settings are not satisfied. Show the user a dialog to upgrade location settings ");

                                try {
                                    // Show the dialog by calling startResolutionForResult(), and check the result
                                    // in onActivityResult().
                                    status.startResolutionForResult(MapsActivity.this, MY_PERMISSIONS_REQUEST_LOCATION);

                                } catch (IntentSender.SendIntentException e) {

                                }
                                break;
                            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                Log.i("tag", "Location settings are inadequate, and cannot be fixed here. Dialog not created.");

                                break;
                        }
                    }
                });
            }

        }
        else if(mGoogleApiClient==null){

            Toast.makeText(this, "enable Google Play Service", Toast.LENGTH_LONG).show();

        }

        else {
            getMyLocation();

        }
    }








    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case  MY_PERMISSIONS_REQUEST_LOCATION:
                switch (resultCode) {
                    case Activity.RESULT_OK:

                        getMyLocation();
                        break;
                    case Activity.RESULT_CANCELED:
                        finish();
                        break;
                }
                break;
        }
    }

    public void getMyLocation(){

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (location == null) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
        mLastKnownLocation=location;

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(3000);
        mLocationRequest.setFastestInterval(3000);;
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }


    }

    @Override
    public void onConnectionSuspended(int i) {

    }


    @Override
    public void onLocationChanged(Location location) {

        mLastKnownLocation = location;
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }

        //Place current location marker
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Current Position");
        markerOptions.snippet("0.0");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
        mCurrLocationMarker = mMap.addMarker(markerOptions);
        MarkerPoints.clear();
        MarkerPoints.add(latLng);
        if(starta){
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(13));
            starta=false;


        }
        else {


        }




    }


    private double distance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1))
                * Math.sin(deg2rad(lat2))
                + Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        return (dist);
    }

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }

    public boolean checkLocationPermission(){
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Asking user if explanation is needed
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                //Prompt the user once explanation has been shown
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted. Do the
                    // contacts-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            android.Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        mMap.setMyLocationEnabled(true);
                    }

                } else {

                    // Permission denied, Disable the functionality that depends on this permission.
                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }

            // other 'case' lines to check for other permissions this app might request.
            // You can add here other case statements according to your requirement.
        }
    }

    @Override
    public void onDrawerSlide(View drawerView, float slideOffset) {

    }

    @Override
    public void onDrawerOpened(View drawerView) {

    }

    @Override
    public void onDrawerClosed(View drawerView) {

    }



    @Override
    public void onDrawerStateChanged(int newState) {

    }


    @SuppressLint("ResourceAsColor")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment = null;
        Class fragmentClass = null;

        switch(item.getItemId()){
            case R.id.nav_map:
                Toast.makeText(this,"map",Toast.LENGTH_SHORT).show();
                map map=new map();
                fragmentClass=map.getClass();
                try {
                    fragment = (Fragment) fragmentClass.newInstance();
                    mNavigationView.setCheckedItem(R.id.nav_map);

                } catch (Exception e) {
                    e.printStackTrace();
                }

                go.setVisibility(View.VISIBLE);
                FragmentManager fragmentManager=getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.mapsa,fragment).commit();
                mDrawerLayout.closeDrawers();
                break;
            case  R.id.nav_list:
                go.setVisibility(View.INVISIBLE);
                getSupportFragmentManager().beginTransaction().replace(R.id.mapsa,
                        new ListVi(MapsActivity.this,toiletRetDatassd,stringArrayList,stringsDistance)).commit();
                mNavigationView.setCheckedItem(R.id.nav_list);
                mDrawerLayout.closeDrawers();
                Toast.makeText(this,"list view",Toast.LENGTH_SHORT).show();

                break;
            case R.id.nav_add_toilet:

                toiletRetDatassd.clear();

                Intent intent =new Intent(MapsActivity.this,MainActivity.class);
                startActivity(intent);
                mNavigationView.setCheckedItem(R.id.nav_add_toilet);
                //finish();

                mDrawerLayout.closeDrawers();
                go.setVisibility(View.VISIBLE);

                Toast.makeText(this,"add toilet",Toast.LENGTH_SHORT).show();
                break;
            default: ;

        }
        return false;
    }
















    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    @Override
    public void onInfoWindowClick(Marker marker) {

        LatLng la=marker.getPosition();
        double lat=la.latitude;
        double lng =la.longitude;

        String asd=marker.getSnippet();
        String idChild=asd.substring(asd.lastIndexOf("/")+1);

        Intent intent=new Intent(MapsActivity.this,ReViewActivity.class);
        intent.putExtra("lat",lat);
        intent.putExtra("lng",lng);
        intent.putExtra("titel",marker.getTitle());
        intent.putExtra("ChildId",idChild);
        startActivity(intent);


    }





}