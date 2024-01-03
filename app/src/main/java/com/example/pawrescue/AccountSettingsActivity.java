
package com.example.pawrescue;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

public class AccountSettingsActivity extends AppCompatActivity {

    private static final String PREF_LANGUAGE_KEY = "language_preference";
    private static final String DEFAULT_LANGUAGE = "en";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setAppLanguage();
        setContentView(R.layout.activity_account_settings);

        RadioGroup radioGroupLanguage = findViewById(R.id.radioGroupLanguage);

        // Dil tercihini kontrol et
        String currentLanguage = getLanguagePreference();
        if (currentLanguage.equals("en")) {
            ((RadioButton) findViewById(R.id.radioButtonEnglish)).setChecked(true);
        } else {
            ((RadioButton) findViewById(R.id.radioButtonTurkish)).setChecked(true);
        }

        // Dil seçimi değiştiğinde tetiklenecek olay dinleyicisi
        radioGroupLanguage.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.radioButtonEnglish) {
                setLanguagePreference("en");
            } else if (checkedId == R.id.radioButtonTurkish) {
                setLanguagePreference("tr");
            }

            // Uygulamayı tekrar başlatarak dil değişikliklerini uygula
            recreate();
        });
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

    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater mif = getMenuInflater();
        mif.inflate(R.menu.bottom_nav_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
}