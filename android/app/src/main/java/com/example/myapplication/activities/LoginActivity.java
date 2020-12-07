package com.example.myapplication.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.example.myapplication.R;
import com.example.myapplication.models.LoginRequest;
import com.example.myapplication.models.LoginResponse;
import com.example.myapplication.network.NetworkService;
import com.example.myapplication.network.SessionManager;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    private final String LOGIN_KEY = "login";
    private final String PASSWORD_KEY = "password";

    private SessionManager sessionManager;
    private TextInputEditText loginEditText;
    private TextInputLayout loginLayout;
    private TextInputEditText passwordEditText;
    private TextInputLayout passwordLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sessionManager = new SessionManager(this);
        findViews();
        setEmptyListeners();
    }

    public void onClickLogin(View view) {
        if (validateEmptyFields()) {
            NetworkService
                .getInstance()
                .getJSONApi()
                .login(new LoginRequest(loginEditText.getText().toString(),
                        passwordEditText.getText().toString()))
                .enqueue(new Callback<LoginResponse>() {
                    @Override
                    public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                        if (response.isSuccessful()) {
                            LoginResponse loginResponse = response.body();
                            sessionManager.saveToken(loginResponse.getToken());
                            makeIntent(MainActivity.class);
                        } else if (response.code() == 400) {
                            passwordLayout.setError(getString(R.string.login_password_incorrect));
                        }
                    }

                    @Override
                    public void onFailure(Call<LoginResponse> call, Throwable t) {
                        Log.d("ERROR", Objects.requireNonNull(t.getMessage()));
                    }
                });
        }
    }

    public void onClickRegister(View view) {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    private void findViews() {
        loginEditText = findViewById(R.id.username_et);
        loginLayout = findViewById(R.id.username_il);
        passwordEditText = findViewById(R.id.password_et);
        passwordLayout = findViewById(R.id.password_il);
    }

    private void makeIntent(Class<?> destinationClass) {
        Intent intent = new Intent(this, destinationClass);
        startActivity(intent);
        finishAffinity();
    }

    private void setEmptyListeners() {
        createEmptyTextListener(this, loginEditText, loginLayout);
        createEmptyTextListener(this, passwordEditText, passwordLayout);
    }

    private boolean validateEmptyFields() {
        boolean result = true;
        if (loginEditText.getText().length() == 0) {
            loginLayout.setError(getString(R.string.field_is_required));
            result = false;
        }
        if (passwordEditText.getText().length() == 0) {
            passwordLayout.setError(getString(R.string.field_is_required));
            result = false;
        }
        return result;
    }

    public static void createEmptyTextListener(final Context context, EditText editText, final TextInputLayout inputLayout) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0) {
                    inputLayout.setError(context.getString(R.string.field_is_required));
                } else {
                    inputLayout.setErrorEnabled(false);
                }
            }
        });
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        loginEditText.setText(savedInstanceState.getString(LOGIN_KEY));
        passwordEditText.setText(savedInstanceState.getString(PASSWORD_KEY));
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(LOGIN_KEY, loginEditText.getText().toString());
        outState.putString(PASSWORD_KEY, passwordEditText.getText().toString());
    }
}