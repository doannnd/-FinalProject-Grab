package com.nguyendinhdoan.finalprojectgrab.ui.register;

import android.content.Context;

public class RegisterPresenter implements RegisterContract.RegisterToPresenter,
        RegisterContract.OnRegisterListener {

    private RegisterContract.RegisterToView view;
    private RegisterContract.RegisterToInteractor model;

    public RegisterPresenter(RegisterContract.RegisterToView view) {
        this.view = view;
        model = new RegisterInteractor(this);
    }

    @Override
    public void register(Context context, String fullName, String email, String password) {
        view.showLoading();
        model.performRegisterOperation(context, fullName, email, password);
        view.hideLoading();
    }

    @Override
    public void onError(String message) {
        view.onError(message);
    }

    @Override
    public void onRegisterResponse(boolean isRegisterSuccess) {
        view.onRegisterResponse(isRegisterSuccess);
    }
}
