package com.nguyendinhdoan.finalprojectgrab.ui.passenger;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.nguyendinhdoan.finalprojectgrab.R;
import com.nguyendinhdoan.finalprojectgrab.ui.login.LoginActivity;
import com.nguyendinhdoan.finalprojectgrab.ui.widget.BottomSheetPassengerFragment;
import com.nguyendinhdoan.finalprojectgrab.ui.widget.CustomInfoWindow;
import com.wang.avi.AVLoadingIndicatorView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PassengerActivity extends AppCompatActivity implements OnMapReadyCallback,
        PassengerContract.PassengerToView, View.OnClickListener {

    public static final String TAG = "PASSENGER_ACTIVITY";
    public static final String BOTTOM_SHEET_TAG = "BOTTOM_SHEET_TAG";
    public static final int DEFAULT_ZOOM = 14;
    public static final double CIRCLE_RADIUS = 100.0;
    public static final float CIRCLE_STROKE_WIDTH = 1f;

    @BindView(R.id.activity_main_toolbar)
    Toolbar toolbar;
    @BindView(R.id.activity_passenger_avl_loading)
    AVLoadingIndicatorView avLoadingIndicatorView;
    @BindView(R.id.activity_main_iv_expandable)
    ImageView ivExpandable;
    @BindView(R.id.activity_main_btn_request_pickup)
    Button btnPickupRequest;

    private GoogleMap mGoogleMap;
    private PassengerContract.PassengerToPresenter presenter;
    private Location currentLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passenger);
        ButterKnife.bind(this);

        setupUi();
        presenter = new PassengerPresenter(this);
        presenter.getDeviceLocation(this);
    }

    private void setupUi() {
        setupToolbar();
        setupGoogleMap();
        addEvents();
    }

    private void addEvents() {
        ivExpandable.setOnClickListener(this);
    }

    private void setupGoogleMap() {
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.activity_passenger_map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int menuId = item.getItemId();
        switch (menuId) {
            case R.id.action_profile: {
                break;
            }
            case R.id.action_logout: {
                FirebaseAuth.getInstance().signOut();
                launchLogin();
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void launchLogin() {
        Intent intentLogin = new Intent(this, LoginActivity.class);
        intentLogin.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intentLogin);
        finish();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;

        // get location permission of the device
        presenter.getLocationPermission(this);
        // update ui
        updateLocationUI();
        // Get the current location of the device and set the position of the map.
        presenter.getDeviceLocation(this);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        presenter.onRequestPermissionResult(requestCode, grantResults);
        updateLocationUI();
    }

    private void updateLocationUI() {
        if (mGoogleMap == null) {
            return;
        }
        try {
            if (PassengerInteractor.mLocationPermissionGranted) {
                mGoogleMap.setMyLocationEnabled(true);
                mGoogleMap.getUiSettings().setMyLocationButtonEnabled(true);
            } else {
                mGoogleMap.setMyLocationEnabled(false);
                mGoogleMap.getUiSettings().setMyLocationButtonEnabled(false);
                presenter.getLocationPermission(this);
            }
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage());
        }
    }


    @Override
    public void getDeviceLocationSuccess(final Location mLastKnownLocation) {
        if (mLastKnownLocation != null) {
            LatLng coordinateCurrentLocation = new LatLng(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude());
            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(coordinateCurrentLocation, DEFAULT_ZOOM));
           /* // add circle for current location
            mGoogleMap.addCircle(new CircleOptions().center(coordinateCurrentLocation)
                    .radius(CIRCLE_RADIUS).strokeWidth(CIRCLE_STROKE_WIDTH)
                    .strokeColor(Color.BLUE).fillColor(Color.argb(30,9, 131, 247)));
            // add marker and title
            mGoogleMap.addMarker(new MarkerOptions().position(coordinateCurrentLocation).title("You"));*/
            currentLocation = mLastKnownLocation;
        }
    }

    @Override
    public void getDeviceLocationFailed(LatLng mDefaultLocation, String message) {
        if (mDefaultLocation != null && message != null) {
            mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(mDefaultLocation, DEFAULT_ZOOM));
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void showLoading() {
        avLoadingIndicatorView.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        avLoadingIndicatorView.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.activity_main_iv_expandable) {
            setupBottomSheetPassenger();
        }
    }

    private void setupBottomSheetPassenger() {
        BottomSheetPassengerFragment bottomSheetPassengerFragment = BottomSheetPassengerFragment.newInstance(BOTTOM_SHEET_TAG);
        bottomSheetPassengerFragment.show(getSupportFragmentManager(), bottomSheetPassengerFragment.getTag());
    }

    @OnClick(R.id.activity_main_btn_request_pickup)
    void handlePickupRequest() {
        presenter.locationPickupRequest(currentLocation);
        btnPickupRequest.setText(getString(R.string.getting_your_driver));
    }

}
