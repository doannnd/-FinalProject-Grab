package com.nguyendinhdoan.finalprojectgrab.util;

import android.text.TextUtils;
import android.util.Patterns;

import org.w3c.dom.Text;

public class CommonUtil {

    public static boolean validateFullName(String fullName) {
        return !TextUtils.isEmpty(fullName);
    }

    public static boolean validateEmail(String email) {
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public static boolean validatePassword(String password) {
        return !TextUtils.isEmpty(password) && password.length() > 6;

    }
}
