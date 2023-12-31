package com.example.pawrescue;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class EmergencyActivity extends AppCompatActivity {

    private static final int CALL_PERMISSION_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency);

        ImageButton emergencyCallButton = (ImageButton) findViewById(R.id.emergencyCallButton);

        emergencyCallButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkCallPermission();
            }
        });

        if (getClass() == EmergencyActivity.class) {
            // ActionBar'ı gizle
            if (getSupportActionBar() != null) {
                getSupportActionBar().hide();
            }
            // StatusBar'ı gizle
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
    }

    private void checkCallPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            // İzin zaten verildiyse, telefon aramasını başlat
            startEmergencyCall();
        } else {
            // İzin verilmediyse, kullanıcıdan izin iste
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, CALL_PERMISSION_REQUEST_CODE);
        }
    }

    private void startEmergencyCall() {
        Intent emergencyCallIntent = new Intent(Intent.ACTION_DIAL);
        emergencyCallIntent.setData(Uri.parse("tel:05452554793"));
        startActivity(emergencyCallIntent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CALL_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // İzin verildiyse, telefon aramasını başlat
                startEmergencyCall();
            } else {
                // İzin verilmediyse, kullanıcıya bir bilgi mesajı gösterilebilir
            }
        }
    }

}
