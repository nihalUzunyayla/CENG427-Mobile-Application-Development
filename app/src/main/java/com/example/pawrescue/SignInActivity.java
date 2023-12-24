package com.example.pawrescue;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class SignInActivity extends AppCompatActivity {

    private EditText editTextUsername, editTextPassword;
    private Button buttonSignIn, buttonSignUp;
    private TextView textViewSignUpPrompt;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        // Gerekli bileşenleri bağlama
        editTextUsername = (EditText) findViewById(R.id.editTextUsername);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        buttonSignIn = (Button) findViewById(R.id.buttonSignIn);
        buttonSignUp = (Button) findViewById(R.id.buttonSignUp);
        textViewSignUpPrompt = (TextView) findViewById(R.id.textViewSignUpPrompt);
        databaseHelper = new DatabaseHelper(this);

        // "SIGN IN" butonuna tıklanınca
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

        // "Don't have an account?" metnine tıklanınca
        textViewSignUpPrompt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // "SIGN UP" sayfasına yönlendir
                startActivity(new Intent(SignInActivity.this, SignUpActivity.class));
            }
        });
    }

    private void signIn() {
        // Kullanıcı adı ve şifreyi al
        String username = editTextUsername.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        // Kullanıcı adı ve şifreyi kontrol et
        if (databaseHelper.checkUser(username, password)) {
            // Giriş başarılıysa, Main activity'e yönlendir
            startActivity(new Intent(SignInActivity.this, MainActivity.class));
            finish(); // Bu aktiviteyi kapat
        } else {
            // Giriş başarısızsa kullanıcıyı uyar
            Toast.makeText(this, getString(R.string.no_account), Toast.LENGTH_SHORT).show();
        }
    }

    private void signUp() {
        // "SIGN UP" sayfasına yönlendir
        startActivity(new Intent(SignInActivity.this, SignUpActivity.class));
    }
}
