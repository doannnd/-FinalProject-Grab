package com.nguyendinhdoan.finalprojectgrab.ui.register;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.nguyendinhdoan.finalprojectgrab.R;
import com.nguyendinhdoan.finalprojectgrab.util.CommonUtil;

public class RegisterInteractor implements RegisterContract.RegisterToInteractor {

    private static final String TAG = "RegisterInteractor";

    private RegisterContract.OnRegisterListener listener;

    public RegisterInteractor(RegisterContract.OnRegisterListener listener) {
        this.listener = listener;
    }

    @Override
    public void performRegisterOperation(Context context, String fullName, String email, String password) {

        if (!CommonUtil.validateFullName(fullName)) {
            listener.onError(context.getString(R.string.error_full_name).toUpperCase());
            return;
        }

        if (!CommonUtil.validateEmail(email)) {
            listener.onError(context.getString(R.string.error_email).toUpperCase());
            return;
        }

        if (!CommonUtil.validatePassword(password)) {
            listener.onError(context.getString(R.string.error_password).toUpperCase());
            return;
        }

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Log.d(TAG, "LOGIN SUCCESS: " + task.getResult());
                    listener.onRegisterResponse(true);
                } else {
                    Log.e(TAG, "REGISTER FAILED: " + task.getException());
                    listener.onError(task.getException().toString().toUpperCase());
                }
            }
        });
    }
}
