package com.example.natharam.searchtoilet;

import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Camera;
import android.location.Location;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
//import com.google.firebase.iid.FirebaseInstanceId;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback,GoogleMap.OnMapClickListener,
        GoogleMap.OnMapLongClickListener,LocationListener,GoogleMap.OnMarkerDragListener ,GoogleMap.OnMyLocationChangeListener, Button.OnClickListener,
        GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks
{
    MapFragment myMapFragment;
    private static final String TAG_MYMAPFRAGMENT = "TAG_MyMapFragment";
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
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private final LatLng mDefaultLocation = new LatLng(23.224886, 72.646234);
    private static final int DEFAULT_ZOOM = 15;
    LocationManager locationManager;
    GoogleMap mMap;
    Location location;
    Button submit;
    EditText id;

    EditText type;
    EditText cost;
    EditText locationaCity;
    EditText contact;
    static   String a;
    static String b;
    static   String c;
    static   String d;
    Button addToilet;
    static   double lan;
    static double lng;
    static String rating="5.0";
    private FusedLocationProviderClient mFusedLocationClient;
    // DatabaseReference ref;
    // DatabaseReference pushedPostRef;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    public static boolean LOCATION_TRUN_ON=false;
    boolean makerAdded=false;
    private static final long UPDATE_INTERVAL_IN_MILLISECONDS = 10000;
    static  LatLng mlatlog;

    // flag for GPS Status
    boolean isGPSEnabled = false;

    // flag for network status
    boolean isNetworkEnabled = false;

    // flag for GPS Tracking is enabled
    boolean isGPSTrackingEnabled = false;


    static   double latitud;
    static double longitud;

    // How many Geocoder should return our GPSTracker
    int geocoderMaxResults = 1;

    // The minimum distance to change updates in meters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters

    // The minimum time between updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60; // 1 minute

    // Declaring a Location Manager

    private boolean mRequestingLocationUpdates;
    // Store LocationManager.GPS_PROVIDER or LocationManager.NETWORK_PROVIDER information
    private String provider_info;
    public  static Marker mMarker=null;
    /**
     * The fastest rate for active location updates. Exact. Updates will never be more frequent
     * than this value.
     */
    private static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = UPDATE_INTERVAL_IN_MILLISECONDS / 2;

    Marker mCurrLocationMarker;
    LocationRequest mLocationReque;
    GoogleApiClient mGoogleApiClient;
    static   boolean chck=false;
    ArrayList<ToiletRetData> toiletRetDatalist;
    MapsActivity mapsActivity;
    RatingBar ratingBar;
    DatabaseReference mDatabaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        mDatabaseReference = database.getReference("Messesg");
        // Log.e("TAG", refreshedToken + "");
        ratingBar=(RatingBar)findViewById(R.id.ratingBar);
        id = (EditText) findViewById(R.id.id);
        // address = (EditText) findViewById(R.id.address);
        type = (EditText) findViewById(R.id.type);
        cost= (EditText) findViewById(R.id.textView3);
        locationaCity = findViewById(R.id.location);
        //  contact = findViewById(R.id.contact);

        toiletRetDatalist=new ArrayList<>();

        mapsActivity=MapsActivity.getmMapsActivity();





        android.app.FragmentManager myFragmentManager = getFragmentManager();


        myMapFragment = (MapFragment) myFragmentManager.findFragmentByTag(TAG_MYMAPFRAGMENT);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapsubmit);
        mapFragment.getMapAsync(this);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        // mSettingsClient = LocationServices.getSettingsClient(this);


    }

    public void onMapReady(GoogleMap map) {
        mMap = map;

        // Do other setup activities here too, as described elsewhere in this tutorial.
        // Use a custom info window adapter to handle multiple lines of text in the
        // info window contents.
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
                mMap.setMyLocationEnabled(true);
            }
        } else {
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
        }

        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

            @Override
            // Return null here, so that getInfoContents() is called next.
            public View getInfoWindow(Marker arg0) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {
                // Inflate the layouts for the info window, title and snippet.
                View infoWindow = getLayoutInflater().inflate(R.layout.custum_info_contents,
                        (FrameLayout) findViewById(R.id.map), false);

                TextView title = ((TextView) infoWindow.findViewById(R.id.title));
                title.setText(marker.getTitle());


                Toast.makeText(MainActivity.this, "permisson10", Toast.LENGTH_SHORT).show();
                TextView snippet = ((TextView) infoWindow.findViewById(R.id.snippet));
                snippet.setText(marker.getSnippet());

                //mMap.moveCamera(CameraUpdateFactory.newLatLng(origin));
                //  mMap.animateCamera(CameraUpdateFactory.zoomTo(11));



                return infoWindow;
            }
        });
        mRequestingLocationUpdates = checkLocationPermission();
        // Turn on the My Location layer and the related control on the map.

        mMap.setOnMapClickListener(this);
        mMap.setOnMapLongClickListener(this);

    }



    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest,  this);
        }
        mLastKnownLocation=location;

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(2000);
        mLocationRequest.setFastestInterval(2000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest,  this);
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
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
        mCurrLocationMarker = mMap.addMarker(markerOptions);

        //move map camera
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(9));

        //stop location updates
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient,  this);
        }


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

        }
    }
    @Override
    protected void onStart()
    {
        super.onStart();
        Toast.makeText(MainActivity.this, "Long Press to Add Marker", Toast.LENGTH_LONG).show();

    }

    @Override
    public void onMapLongClick(LatLng latLng)
    {

        Toast.makeText(MainActivity.this, "Hold Marker TO Move(Drag And Drop)", Toast.LENGTH_LONG).show();
        mlatlog=latLng;
        if(!makerAdded)
        {

            Marker m = mMap.addMarker(new MarkerOptions()
                    .position(latLng)
                    .title("").draggable(true));
            mMarker=m;
            makerAdded=true;
        }
        mMap.setOnMarkerDragListener(MainActivity.this);

    }

    @Override
    public void onMapClick(LatLng latLng)
    {
        Toast.makeText(MainActivity.this, "Long Press to Add Marker", Toast.LENGTH_SHORT).show();


    }

    @Override
    public void onMarkerDragStart(Marker marker)
    {

        mMarker=marker;

    }

    @Override
    public void onMarkerDrag(Marker marker)
    {

    }

    @Override
    public void onMarkerDragEnd(Marker marker)
    {
        mMarker=marker;

    }

    @Override
    public void onMyLocationChange(Location location)
    {

    }

    @Override
    public void onClick(View v)
    {
        rating=String.valueOf(ratingBar.getRating());
        a = id.getText().toString();
        b = locationaCity.getText().toString();
        c = type.getText().toString();
        d=cost.getText().toString();



        if (R.id.add_toilet==v.getId() && makerAdded && !TextUtils.isEmpty(b) && !TextUtils.isEmpty(c) && !TextUtils.isEmpty(d) && !TextUtils.isEmpty(d) )
        {
            mlatlog=mMarker.getPosition();
            latitud=mlatlog.latitude;
            longitud=mlatlog.longitude;
            if(mMarker!=null)
            {
                mMarker.remove();
                makerAdded=false;
                //Toast.makeText(MainActivity.this, "Button_click"+mlatlog, Toast.LENGTH_SHORT).show();

                mMarker.remove();
                setSubmit();
            }
            // Toast.makeText(MainActivity.this, "Button_click"+rating, Toast.LENGTH_SHORT).show();


        }

    }

    public  void setSubmit()
    {

        lan=latitud;
        lng=longitud;

        //mapsActivity.writeNewUser(a,b,c,d,lan,lng,rating);
        String idChild = mDatabaseReference.push().getKey();

        ToiletRetData toiletRetDataa = new ToiletRetData(a, b, c, d, lan, lng, rating,idChild);
        mDatabaseReference.child(idChild).setValue(toiletRetDataa);
        getCommen(idChild,rating);



    }

    public void getCommen(String idChild,String rating){
        Calendar calendar=Calendar.getInstance();
        SimpleDateFormat simpleDateFormat =new SimpleDateFormat(" dd-MM-yyyy HH:mm ");

        String timestamp=simpleDateFormat.format(calendar.getTime());
        CommentAdd commentAdd=new CommentAdd("Nice Toilet",rating,timestamp);
        DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference(idChild);
        String id = databaseReference.push().getKey();

        databaseReference.child(id).setValue(commentAdd);


        finish();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.main,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.action_android:

                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }

    }



    @Override
    public void onBackPressed()
    {
        super.onBackPressed();

        boolean chck=false;
    }
}
