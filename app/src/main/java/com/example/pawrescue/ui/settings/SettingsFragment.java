package com.example.pawrescue.ui.settings;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.SeekBar;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.pawrescue.R;
import com.example.pawrescue.SignUpActivity;

public class SettingsFragment extends Fragment {

    private CheckBox checkBoxNotifications;
    private CheckBox checkBoxDarkMode;
    private SeekBar seekBarFontSize;
    private Button buttonSave;
    private Spinner spinnerLanguage;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_settings, container, false);

        checkBoxNotifications = root.findViewById(R.id.checkBoxNotifications);
        checkBoxDarkMode = root.findViewById(R.id.checkBoxDarkMode);
        seekBarFontSize = root.findViewById(R.id.seekBarFontSize);
        buttonSave = root.findViewById(R.id.buttonSave);
        spinnerLanguage = root.findViewById(R.id.spinnerLanguage);

        String[] languages = {"Türkçe", "English"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, languages);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLanguage.setAdapter(adapter);

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Ayarları kaydetme işlemleri burada yapılabilir (örneğin, SharedPreferences kullanarak)
            }
        });

        // Spinner'dan seçilen dil değiştiğinde yapılacak işlemleri belirt
        spinnerLanguage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // Seçilen dil ile ilgili işlemleri burada yapabilirsiniz
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Bir şey seçilmediğinde yapılacak işlemleri burada yapabilirsiniz
            }
        });

        // "LOG OUT" butonuna tıklandığında SignUpActivity'e geçiş
        Button buttonLogout = root.findViewById(R.id.buttonLogOut);
        buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(requireContext(), SignUpActivity.class);
                startActivity(intent);
            }
        });

        return root;
    }



}
