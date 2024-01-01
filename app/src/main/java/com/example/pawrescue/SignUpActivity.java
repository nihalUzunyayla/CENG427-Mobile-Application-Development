package com.example.pawrescue;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUpActivity extends AppCompatActivity {

    private EditText editTextUsername, editTextEmail, editTextPassword, editTextConfirmPassword;
    private CheckBox checkBoxTerms, checkBoxRememberMe;
    private Button buttonSignUp, buttonSignIn;
    private TextView textViewSignInPrompt;
    private UserDB userDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        editTextUsername = (EditText) findViewById(R.id.editTextUsername);
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        editTextConfirmPassword = (EditText) findViewById(R.id.editTextConfirmPassword);
        checkBoxTerms = (CheckBox) findViewById(R.id.checkBoxTerms);
        checkBoxRememberMe = (CheckBox) findViewById(R.id.checkBoxRememberMe);
        buttonSignUp = (Button) findViewById(R.id.buttonSignUp);
        textViewSignInPrompt = (TextView) findViewById(R.id.textViewSignInPrompt);
        buttonSignIn = (Button) findViewById(R.id.buttonSignIn);
        userDB = new UserDB(this);
        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUp();
            }
        });

        buttonSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignUpActivity.this, SignInActivity.class));
            }
        });

        editTextPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int before, int count) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String inputText = editable.toString();
                if (!isValidPassword(inputText)) {
                    editTextPassword.setError(getString(R.string.valid_password));
                } else {
                    editTextPassword.setError(null);
                }
            }
        });

        editTextConfirmPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int before, int count) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String inputText = editable.toString();
                if (!isValidPassword(inputText)) {
                    editTextConfirmPassword.setError(getString(R.string.valid_password));
                } else {
                    editTextConfirmPassword.setError(null);
                }
            }
        });

    }


    private void signUp() {
        String username = editTextUsername.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String confirmPassword = editTextConfirmPassword.getText().toString().trim();

        if (username.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            // Gerekli alanlar boşsa kullanıcıyı uyar
            Toast.makeText(this, getString(R.string.empty_field), Toast.LENGTH_SHORT).show();
        } else if (!password.equals(confirmPassword)) {
            // Şifreler uyuşmuyorsa kullanıcıyı uyar
            Toast.makeText(this, getString(R.string.wrong_password), Toast.LENGTH_SHORT).show();
        } else if (!checkBoxTerms.isChecked()) {
            // Şartları kabul etmemişse kullanıcıyı uyar
            Toast.makeText(this, getString(R.string.accept_term_condition), Toast.LENGTH_SHORT).show();
        } else if (userDB.checkUser(username, password)) {
            // Kullanıcı zaten kayıtlıysa kullanıcıyı uyar
            Toast.makeText(this, getString(R.string.account_already_exists), Toast.LENGTH_SHORT).show();
        }else {
            // Veritabanına ekle
            User user = new User();
            user.setUsername(username);
            user.setEmail(email);
            user.setPassword(password);
            userDB.addUser(user);

            // SignIn'e yönlendir
            startActivity(new Intent(SignUpActivity.this, SignInActivity.class));
        }
    }

    private boolean isValidPassword(String password) {
        String regex = "\\d{4}"; // 4 haneli olacak
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }

    private boolean validateFields(){
        String password = editTextPassword.getText().toString();
        String confirmPassword = editTextConfirmPassword.getText().toString();

        if (!isValidPassword(password)) {
            editTextPassword.setError(getString(R.string.valid_password));
            return false;
        } else {
            editTextPassword.setError(null);
        }

        if (!isValidPassword(confirmPassword)) {
            editTextConfirmPassword.setError(getString(R.string.valid_password));
            return false;
        } else {
            editTextConfirmPassword.setError(null);
        }

        return !password.isEmpty() && !confirmPassword.isEmpty();
    }

}
