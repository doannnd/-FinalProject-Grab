package com.nguyendinhdoan.finalprojectgrab.ui.register;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.nguyendinhdoan.finalprojectgrab.R;
import com.nguyendinhdoan.finalprojectgrab.ui.login.LoginActivity;
import com.nguyendinhdoan.finalprojectgrab.ui.main.MainActivity;
import com.wang.avi.AVLoadingIndicatorView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegisterActivity extends AppCompatActivity implements RegisterContract.RegisterToView{

    @BindView(R.id.layout_register)
    ConstraintLayout layoutRegister;
    @BindView(R.id.activity_register_layout_full_name)
    TextInputLayout layoutFullName;
    @BindView(R.id.activity_register_layout_email)
    TextInputLayout layoutEmail;
    @BindView(R.id.activity_register_layout_password)
    TextInputLayout layoutPassword;
    @BindView(R.id.activity_register_et_full_name)
    TextInputEditText etFullName;
    @BindView(R.id.activity_register_et_email)
    TextInputEditText etEmail;
    @BindView(R.id.activity_register_et_password)
    TextInputEditText etPassword;
    @BindView(R.id.activity_register_avl_loading)
    AVLoadingIndicatorView avlLoading;

    private RegisterContract.RegisterToPresenter registerPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);

        registerPresenter = new RegisterPresenter(this);
    }



    @OnClick(R.id.activity_register_btn_register)
    void handleRegister() {
        String fullName = etFullName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        registerPresenter.register(this, fullName, email, password);
    }

    @OnClick(R.id.activity_register_btn_launch_login)
    void launchLogin() {
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    @Override
    public void showLoading() {
        avlLoading.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        avlLoading.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onRegisterResponse(boolean isRegisterSuccess) {
        if (isRegisterSuccess) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
    }

    @Override
    public void onError(String message) { Snackbar.make(layoutRegister, message, Snackbar.LENGTH_LONG); }

    @Override
    public void onFullNameError(String message) {
        layoutFullName.setError(message);
    }

    @Override
    public void onEmailError(String message) {
        layoutEmail.setError(message);
    }

    @Override
    public void onPasswordError(String message) {
        layoutPassword.setError(message);
    }
}
