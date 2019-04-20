package com.nguyendinhdoan.finalprojectgrab.ui.passenger;

import android.content.Context;
import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

public class PassengerPresenter implements PassengerContract.PassengerToPresenter, PassengerContract.OnPassengerListener {

    private PassengerContract.PassengerToView view;
    private PassengerContract.PassengerToInteractor model;

    public PassengerPresenter(PassengerContract.PassengerToView view) {
        this.view = view;
        model = new PassengerInteractor(this);
    }

    @Override
    public void getDeviceLocation(Context context) {
        model.getDeviceLocation(context);
    }


    @Override
    public void getLocationPermission(Context context) {
        model.getLocationPermission(context);
    }

    @Override
    public void onRequestPermissionResult(int requestCode, int[] grantResults) {
        model.onRequestPermissionResult(requestCode, grantResults);
    }

    @Override
    public void locationPickupRequest(Location currentLocation) {
        model.locationPickupRequest(currentLocation);
    }

    @Override
    public void getDeviceLocationSuccess(Location lastKnownLocation) {
        view.getDeviceLocationSuccess(lastKnownLocation);
    }

    @Override
    public void getDeviceLocationFailed(LatLng defaultLocation, String message) {
        view.getDeviceLocationFailed(defaultLocation, message);
    }

    @Override
    public void showLoading() {
        view.showLoading();
    }

    @Override
    public void hideLoading() {
        view.hideLoading();
    }
}
