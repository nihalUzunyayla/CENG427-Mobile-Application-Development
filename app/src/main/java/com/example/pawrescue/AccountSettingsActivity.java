package com.example.pawrescue;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import java.util.Locale;

public class AccountSettingsActivity extends AppCompatActivity {

    private static final String PREF_LANGUAGE_KEY = "language_preference";
    private static final String PREF_DARK_MODE_KEY = "dark_mode_preference";
    private static final String DEFAULT_LANGUAGE = "en";
    TextView bottomTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setAppLanguage();
        setContentView(R.layout.activity_account_settings);

        RadioGroup radioGroupLanguage = findViewById(R.id.radioGroupLanguage);
        Switch switchDarkMode = findViewById(R.id.switchDarkMode);
        SeekBar fontSizeSeekBar = findViewById(R.id.fontSizeSeekBar);
        bottomTextView = findViewById(R.id.bottomTextView);

        fontSizeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                switch (progress) {
                    case 0:
                        bottomTextView.setTextSize(16);
                        break;
                    case 1:
                        bottomTextView.setTextSize(24);
                        break;
                    case 2:
                        bottomTextView.setTextSize(32);
                        break;
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // SeekBar değişmeye başladığında yapılacak işlemler
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // SeekBar değişimi durduğunda yapılacak işlemler
            }
        });

        String currentLanguage = getLanguagePreference();
        if (currentLanguage.equals("en")) {
            ((RadioButton) findViewById(R.id.radioButtonEnglish)).setChecked(true);
        } else {
            ((RadioButton) findViewById(R.id.radioButtonTurkish)).setChecked(true);
        }

        radioGroupLanguage.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.radioButtonEnglish) {
                setLanguagePreference("en");
            } else if (checkedId == R.id.radioButtonTurkish) {
                setLanguagePreference("tr");
            }

            recreate();
        });

        switchDarkMode.setChecked(getDarkModePreference());

        switchDarkMode.setOnCheckedChangeListener((buttonView, isChecked) -> {
            setDarkModePreference(isChecked);

            if (isChecked) {
                enableDarkMode();
            } else {
                disableDarkMode();
            }
        });
    }

    private void enableDarkMode() {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
    }

    private void disableDarkMode() {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
    }

    private void setAppLanguage() {
        String languagePreference = getLanguagePreference();
        Locale locale = new Locale(languagePreference);
        Locale.setDefault(locale);

        Configuration config = new Configuration();
        config.locale = locale;

        Resources resources = getResources();
        resources.updateConfiguration(config, resources.getDisplayMetrics());

    }

    private String getLanguagePreference() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        return prefs.getString(PREF_LANGUAGE_KEY, DEFAULT_LANGUAGE);
    }

    private void setLanguagePreference(String language) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(PREF_LANGUAGE_KEY, language);
        editor.apply();
    }
    private void setDarkModePreference(boolean isDarkMode) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(PREF_DARK_MODE_KEY, isDarkMode);
        editor.apply();
    }
    private boolean getDarkModePreference() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        return prefs.getBoolean(PREF_DARK_MODE_KEY, false); // Varsayılan değer false olarak ayarlandı.
    }
}


    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater mif = getMenuInflater();
        mif.inflate(R.menu.bottom_nav_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
}