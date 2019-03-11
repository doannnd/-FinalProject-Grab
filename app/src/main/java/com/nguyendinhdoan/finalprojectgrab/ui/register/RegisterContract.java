package com.nguyendinhdoan.finalprojectgrab.ui.register;

import android.content.Context;

public interface RegisterContract {

    interface RegisterToPresenter {
        void register(Context context, String fullName, String email, String password);
    }

    interface RegisterToView {
        void showLoading();

        void hideLoading();

        void onRegisterResponse(boolean isRegisterSuccess);

        void onError(String message);

        void onFullNameError(String message);

        void onEmailError(String message);

        void onPasswordError(String message);

    }

    interface  RegisterToInteractor {
        void performRegisterOperation(Context context, String fullName, String email, String password);
    }

    interface  OnRegisterListener {
        void onError(String message);

        void onRegisterResponse(boolean isRegisterSuccess);

        void onFullNameError(String message);

        void onEmailError(String message);

        void onPasswordError(String message);
    }
}
