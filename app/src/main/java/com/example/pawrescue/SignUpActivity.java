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

public class SignUpActivity extends AppCompatActivity {

    private EditText editTextUsername, editTextEmail, editTextPassword, editTextConfirmPassword;
    private CheckBox checkBoxTerms, checkBoxRememberMe;
    private Button buttonSignUp, buttonSignIn;
    private TextView textViewSignInPrompt;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        // Gerekli bileşenleri bağlama
        editTextUsername = (EditText) findViewById(R.id.editTextUsername);
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        editTextConfirmPassword = (EditText) findViewById(R.id.editTextConfirmPassword);
        checkBoxTerms = (CheckBox) findViewById(R.id.checkBoxTerms);
        checkBoxRememberMe = (CheckBox) findViewById(R.id.checkBoxRememberMe);
        buttonSignUp = (Button) findViewById(R.id.buttonSignUp);
        textViewSignInPrompt = (TextView) findViewById(R.id.textViewSignInPrompt);
        buttonSignIn = (Button) findViewById(R.id.buttonSignIn);
        databaseHelper = new DatabaseHelper(this);

        // "SIGN UP" butonuna tıklanınca
        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUp();
            }
        });

        // "SIGN IN" butonuna tıklanınca
        buttonSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // "SIGN IN" sayfasına yönlendir
                startActivity(new Intent(SignUpActivity.this, SignInActivity.class));
            }
        });
    }

    private void signUp() {
        // Kullanıcı bilgilerini al
        String username = editTextUsername.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String confirmPassword = editTextConfirmPassword.getText().toString().trim();

        // Diğer gerekli kontrolleri yap (şifreler uyuşuyor mu, şartları kabul etmiş mi, vb.)
        if (username.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            // Gerekli alanlar boşsa kullanıcıyı uyar
            Toast.makeText(this, getString(R.string.empty_field), Toast.LENGTH_SHORT).show();
        } else if (!password.equals(confirmPassword)) {
            // Şifreler uyuşmuyorsa kullanıcıyı uyar
            Toast.makeText(this, getString(R.string.wrong_password), Toast.LENGTH_SHORT).show();
        } else if (!checkBoxTerms.isChecked()) {
            // Şartları kabul etmemişse kullanıcıyı uyar
            Toast.makeText(this, getString(R.string.accept_term_condition), Toast.LENGTH_SHORT).show();
        } else {
            // Veritabanına kullanıcıyı ekleyin
            User user = new User();
            user.setUsername(username);
            user.setEmail(email);
            user.setPassword(password);
            databaseHelper.addUser(user);

            // Kullanıcıyı giriş sayfasına yönlendir
            startActivity(new Intent(SignUpActivity.this, SignInActivity.class));
        }
    }

}
