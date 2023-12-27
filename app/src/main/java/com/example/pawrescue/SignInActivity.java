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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        // Gerekli bileşenleri bağlama
        editTextUsername = findViewById(R.id.editTextUsername);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonSignIn = findViewById(R.id.buttonSignIn);
        buttonSignUp = findViewById(R.id.buttonSignUp);
        textViewSignUpPrompt = findViewById(R.id.textViewSignUpPrompt);
        checkBoxRememberMe = findViewById(R.id.checkBoxRememberMe);
        userDB = new UserDB(this);
        preferencesHelper = new PreferencesHelper(this);

        // Remember me seçeneği işaretli ise kayıtlı kullanıcı adı ve şifreyi doldur
        if (checkBoxRememberMe.isChecked()) {
            editTextUsername.setText(preferencesHelper.getSavedUsername());
            editTextPassword.setText(preferencesHelper.getSavedPassword());
        }

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
    }

    private void signIn() {
        // Kullanıcı adı ve şifreyi al
        String username = editTextUsername.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        // Kullanıcı adı ve şifreyi kontrol et
        if (userDB.checkUser(username, password)) {
            // Remember me seçeneği işaretli ise bilgileri kaydet
            if (checkBoxRememberMe.isChecked()) {
                preferencesHelper.saveCredentials(username, password);
            } else {
                // Remember me seçeneği işaretli değilse daha önce kaydedilmiş bilgileri sil
                preferencesHelper.clearCredentials();
            }

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
