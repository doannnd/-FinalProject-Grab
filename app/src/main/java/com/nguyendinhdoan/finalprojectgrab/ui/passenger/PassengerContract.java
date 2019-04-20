package com.nguyendinhdoan.finalprojectgrab.ui.passenger;

import android.content.Context;
import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

public interface PassengerContract {

    interface PassengerToView {
        void getDeviceLocationSuccess(Location lastKnownLocation);

        void getDeviceLocationFailed(LatLng defaultLocation, String message);

        void showLoading();

        void hideLoading();
    }

    interface PassengerToPresenter {
        void getDeviceLocation(Context context);

        void getLocationPermission(Context context);

        void onRequestPermissionResult(int requestCode, int[] grantResults);

        void locationPickupRequest(Location currentLocation);
    }

    interface PassengerToInteractor {
        void getDeviceLocation(Context context);

        void getLocationPermission(Context context);

        void onRequestPermissionResult(int requestCode, int[] grantResults);

        void locationPickupRequest(Location currentLocation);
    }

    interface OnPassengerListener {
        void getDeviceLocationSuccess(Location lastKnowLocation);

        void getDeviceLocationFailed(LatLng defaultLocation, String message);

        void showLoading();

        void hideLoading();
    }
}
