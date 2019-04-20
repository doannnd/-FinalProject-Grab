package com.nguyendinhdoan.finalprojectgrab.ui.passenger;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class PassengerInteractor implements PassengerContract.PassengerToInteractor {

    private static final String TAG = "PassengerInteractor";

    private PassengerContract.OnPassengerListener listener;
    public static boolean mLocationPermissionGranted;
    private final LatLng mDefaultLocation = new LatLng(21.0055546,105.8434628);
    public static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;

    public PassengerInteractor(PassengerContract.OnPassengerListener listener) {
        this.listener = listener;
    }

    @Override
    public void getDeviceLocation(Context context) {
        try {
            if (mLocationPermissionGranted) {
                listener.showLoading();
                // get most recent location of the device
                Task locationResult = LocationServices.getFusedLocationProviderClient(context).getLastLocation();
                if (locationResult != null) {
                    locationResult.addOnCompleteListener(new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task) {
                            if (task.isSuccessful()) {
                                // current location of you
                                Location lastKnownLocation = (Location) task.getResult();
                                if (lastKnownLocation != null) {
                                    saveCurrentLocationInDatabase(lastKnownLocation);
                                }
                                listener.getDeviceLocationSuccess(lastKnownLocation);
                            } else {
                                // current location is null. set default location
                                String message = Objects.requireNonNull(task.getException()).getMessage();
                                listener.getDeviceLocationFailed(mDefaultLocation, message);
                                Log.e(TAG, message);
                            }
                            listener.hideLoading();
                        }
                    });
                }
            }
        } catch (SecurityException e) {
            e.printStackTrace();
            Log.e(TAG, e.getMessage());
        }
    }

    @Override
    public void getLocationPermission(Context context) {
        if (ContextCompat.checkSelfPermission(context.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions((Activity) context,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    @Override
    public void onRequestPermissionResult(int requestCode, int[] grantResults) {
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                }
            }
        }
    }

    @Override
    public void locationPickupRequest(Location currentLocation) {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Log.d(TAG, "userId: " + userId);
        DatabaseReference pickupRequest = FirebaseDatabase.getInstance().getReference().child("PickupRequest");
        GeoFire geoFire = new GeoFire(pickupRequest);

        // get latitude and longitude
        double latitude = currentLocation.getLatitude();
        double longitude = currentLocation.getLongitude();
        Log.d(TAG, "latitude: " + latitude);
        Log.d(TAG, "longitude: " + longitude);
        geoFire.setLocation(userId, new GeoLocation(latitude, longitude), new GeoFire.CompletionListener() {
            @Override
            public void onComplete(String key, DatabaseError error) {
                if (error != null) {
                    Log.d(TAG, "success");
                } else {
                    Log.d(TAG, "failed");
                }
            }
        });
    }

    private void saveCurrentLocationInDatabase(Location lastKnownLocation) {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference users = FirebaseDatabase.getInstance().getReference("users/" + userId);
        GeoFire geoFire = new GeoFire(users);

        // get latitude and longitude
        double latitude = lastKnownLocation.getLatitude();
        double longitude = lastKnownLocation.getLongitude();
        // save into database
        geoFire.setLocation("location", new GeoLocation(latitude, longitude), new GeoFire.CompletionListener() {
            @Override
            public void onComplete(String key, DatabaseError error) {
                if (error == null) {
                    Log.d(TAG, "save current location of user successful");
                } else {
                    Log.e(TAG, "Error" + error);
                }
            }
        });
    }
}
