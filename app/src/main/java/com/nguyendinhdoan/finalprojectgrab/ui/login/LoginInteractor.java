package com.nguyendinhdoan.finalprojectgrab.ui.login;

import android.content.Context;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.nguyendinhdoan.finalprojectgrab.R;
import com.nguyendinhdoan.finalprojectgrab.util.CommonUtil;

public class LoginInteractor implements LoginContract.LoginToInteractor {

    private static final String TAG = "PhoneInteractor";

    private LoginContract.OnLoginListener listener;

    public LoginInteractor(LoginContract.OnLoginListener listener) {
        this.listener = listener;
    }

    @Override
    public void validateLoginFields(Context context, String email, String phone, String fullName) {

        if (!CommonUtil.validateFullName(fullName)) {
            listener.onValidateError(context.getString(R.string.error_full_name));
            return;
        }

        if (!CommonUtil.validateEmail(email)) {
            listener.onValidateError(context.getString(R.string.error_email));
            return;
        }

        if (!CommonUtil.validatePhone(phone)) {
            listener.onValidateError(context.getString(R.string.error_phone));
            return;
        }

        listener.onValidateResponse(true);
    }

    @Override
    public void isLoggedIn() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            listener.isLoggedIn(true);
        }
    }

}
