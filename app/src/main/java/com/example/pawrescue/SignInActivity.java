package com.example.pawrescue;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class SignInActivity extends AppCompatActivity {

    private EditText editTextUsername, editTextPassword;
    private Button buttonSignIn, buttonSignUp;
    private TextView textViewSignUpPrompt;
    private CheckBox checkBoxRememberMe;
    private UserDB userDB;
    private PreferencesHelper preferencesHelper;
    public static User userProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        editTextUsername = findViewById(R.id.editTextUsername);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonSignIn = findViewById(R.id.buttonSignIn);
        buttonSignUp = findViewById(R.id.buttonSignUp);
        textViewSignUpPrompt = findViewById(R.id.textViewSignUpPrompt);
        checkBoxRememberMe = findViewById(R.id.checkBoxRememberMe);
        userDB = new UserDB(this);
        preferencesHelper = new PreferencesHelper(this);

        // Check if "Remember Me" is selected and user is logged in
        if (checkBoxRememberMe.isChecked() && preferencesHelper.isUserLoggedIn()) {
            startMainActivity();
            return;
        }

        buttonSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });

        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUp();
            }
        });

        if (preferencesHelper.isUserLoggedIn()) {
            startMainActivity();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        String savedUsername = preferencesHelper.getSavedUsername();
        String savedPassword = preferencesHelper.getSavedPassword();

        if (!savedUsername.isEmpty() && !savedPassword.isEmpty()) {
            startMainActivity();
        } else if (checkBoxRememberMe.isChecked()) {
            // If "Remember Me" is checked, populate the EditText fields
            editTextUsername.setText(savedUsername);
            editTextPassword.setText(savedPassword);
        }
    }

    private void signIn() {

        String username = editTextUsername.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        userProfile = userDB.getUser(username);

        if (userDB.checkUser(username, password)) {
            if (checkBoxRememberMe.isChecked()) {
                preferencesHelper.saveCredentials(username, password);
            } else {
                preferencesHelper.clearCredentials();
            }

            startMainActivity();
        } else {

            Toast.makeText(this, getString(R.string.no_account), Toast.LENGTH_SHORT).show();
        }
    }

    private void startMainActivity() {
        startActivity(new Intent(SignInActivity.this, MainActivity.class));
        finish();
    }

    private void signUp() {
        startActivity(new Intent(SignInActivity.this, SignUpActivity.class));
    }
}
