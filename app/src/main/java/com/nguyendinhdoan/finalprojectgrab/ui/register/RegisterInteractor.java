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

public class RegisterInteractor implements RegisterContract.RegisterToInteractor {

    private static final String TAG = "RegisterInteractor";

    private RegisterContract.OnRegisterListener listener;

    public RegisterInteractor(RegisterContract.OnRegisterListener listener) {
        this.listener = listener;
    }

    @Override
    public void performRegisterOperation(final Context context, String fullName, String email, String password) {
        if(TextUtils.isEmpty(fullName)) {
            listener.onFullNameError(context.getString(R.string.error_full_name));
            return;
        }

        if (TextUtils.isEmpty(email) || !isValidEmail(email)) {
            listener.onEmailError(context.getString(R.string.error_email));
            return;
        }

        if (TextUtils.isEmpty(password) || password.length() < 6) {
            listener.onPasswordError(context.getString(R.string.error_password));
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

    private boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

}
