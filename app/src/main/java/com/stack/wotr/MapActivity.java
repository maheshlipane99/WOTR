package com.stack.wotr;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;// Add an import statement for the client library.
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.maps.android.SphericalUtil;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback, View.OnClickListener {
    private static final String TAG = MapActivity.class.getSimpleName();
    private GoogleMap mMap;
    private static final int REQUEST_CODE = 101;
    Location currentLocation;
    FusedLocationProviderClient fusedLocationProviderClient;
    List<LatLng> mLatLng = new ArrayList<>();
    private Button btnConfirm, btnMyLocation, btnClear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        initView();
        initSearchPlace();
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        fetchLocation();
    }

    private void initView() {
        btnConfirm = findViewById(R.id.btnConfirm);
        btnMyLocation = findViewById(R.id.btnMyLocation);
        btnClear = findViewById(R.id.btnClear);

        btnConfirm.setOnClickListener(this);
        btnMyLocation.setOnClickListener(this);
        btnClear.setOnClickListener(this);
    }

    public void initSearchPlace() {


        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), getString(R.string.map_key));
        }

        // Initialize the AutocompleteSupportFragment.
        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);

        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG));

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.
                Log.i(TAG, "Place: " + place.getName() + ", " + place.getId() + " " + place.getLatLng());
                addMarker(place);
            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.i(TAG, "An error occurred: " + status);
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(mMap.MAP_TYPE_SATELLITE);
        setCurrentLocation();
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng point) {
                addPoint(point);
                Drawable circleDrawable = getResources().getDrawable(R.drawable.ic_circle_outline);
                BitmapDescriptor markerIcon = getMarkerIconFromDrawable(circleDrawable);
                mMap.addMarker(new MarkerOptions()
                        .position(point)
                        .icon(markerIcon)
                );
            }
        });
    }

    private void setCurrentLocation() {
        LatLng latLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
        mMap.addCircle(new CircleOptions()
                .center(latLng)
                .radius(30)
                .strokeColor(Color.argb(60, 0, 0, 255))
                .strokeWidth(1)
                .fillColor(Color.argb(2, 0, 0, 10)));

        mMap.addCircle(new CircleOptions()
                .center(latLng)
                .radius(10)
                .strokeColor(Color.BLUE)
                .strokeWidth(0)
                .fillColor(Color.argb(60, 0, 0, 255)));

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(latLng) // Sets the center of the map
                .tilt(0) // Sets the tilt of the camera to 20 degrees
                .zoom(18) // Sets the zoom
                .bearing(90) // Sets the orientation of the camera to east
                .build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

    }

    private void addPoint(LatLng point) {
        mLatLng.add(point);
        drawPolyline();
    }

    private String getArea() {
        return  new DecimalFormat("#.##").format(SphericalUtil.computeArea(mLatLng));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    fetchLocation();
                }
                break;
        }
    }

    private void fetchLocation() {
        if (ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
            return;
        }
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    currentLocation = location;
                    SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
                    assert supportMapFragment != null;
                    supportMapFragment.getMapAsync(MapActivity.this);
                }
            }
        });
    }

    private void removeAllMap() {
        mMap.clear();
        mLatLng.clear();
    }

    private void removeAllMarkerAndPolygon() {
        mMap.clear();
        mLatLng.clear();
        setCurrentLocation();
    }

    private void drawPolygon() {

        if (mMap != null && mLatLng.size() > 0) {
            Log.i(TAG, "drawPolygon: Size " + mLatLng.size());
            LatLng[] arr = new LatLng[mLatLng.size()];
            // ArrayList to Array Conversion
            for (int i = 0; i < mLatLng.size(); i++)
                arr[i] = mLatLng.get(i);

            PolygonOptions polygonOptions = new PolygonOptions();
            polygonOptions.add(arr);
            polygonOptions.strokeColor(ContextCompat.getColor(this, android.R.color.holo_orange_light));
            polygonOptions.strokeWidth(3);
            // polygonOptions.fillColor(ContextCompat.getColor(this, android.R.color.holo_orange_light));
            mMap.addPolygon(polygonOptions);
            Toast.makeText(this, "Size is : "+getArea()+" square meters", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Please select more than one point", Toast.LENGTH_SHORT).show();
        }
    }



    private void drawPolyline() {

        if (mMap != null && mLatLng.size() > 0) {
            Log.i(TAG, "drawPolygon: Size " + mLatLng.size());
            PolylineOptions options = new PolylineOptions()
                    .width(3)
                    .color(ContextCompat.getColor(this, android.R.color.holo_orange_light)).geodesic(true);
            for (int z = 0; z < mLatLng.size(); z++) {
                LatLng point = mLatLng.get(z);
                options.add(point);
            }
            mMap.addPolyline(options);
        } else {
            Toast.makeText(this, "Please select more than one point", Toast.LENGTH_SHORT).show();
        }
    }

    public void addMarker(Place p) {

        MarkerOptions markerOptions = new MarkerOptions();

        markerOptions.position(p.getLatLng());
        markerOptions.title(p.getName() + "");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
        // mMap.addMarker(markerOptions);

        mMap.moveCamera(CameraUpdateFactory.newLatLng(p.getLatLng()));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(13));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnConfirm: {
                drawPolygon();
                break;
            }
            case R.id.btnMyLocation: {
                fetchLocation();
                break;
            }
            case R.id.btnClear: {
                removeAllMarkerAndPolygon();
                break;
            }
        }
    }

    private BitmapDescriptor getMarkerIconFromDrawable(Drawable drawable) {
        Canvas canvas = new Canvas();
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        canvas.setBitmap(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }
}