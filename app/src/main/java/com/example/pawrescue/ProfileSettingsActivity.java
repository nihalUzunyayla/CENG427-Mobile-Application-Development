package com.example.pawrescue;
import android.Manifest;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

public class ProfileSettingsActivity extends AppCompatActivity {

    private static final int REQUEST_CODE = 1;
    private static final int CAMERA_PERMISSION_REQUEST_CODE = 105;
    private static final int STORAGE_PERMISSION_REQUEST_CODE = 106;

    private ImageView profileImageView;
    private Bitmap selectedImage;
    private User userProfile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_settings);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        TextView profileTextViewName = findViewById(R.id.profileTextViewName);
        TextView profileTextViewEmail = findViewById(R.id.profileTextViewEmail);
        TextView profileTextViewPassword = findViewById(R.id.profileTextViewPassword);

        userProfile = ((SignInActivity) getParent()).userProfile;

        profileTextViewName.setText(userProfile.getUsername());
        profileTextViewEmail.setText(userProfile.getEmail());
        profileTextViewPassword.setText(userProfile.getPassword());

        profileImageView = findViewById(R.id.profileImageView);
        if (userProfile.getPhotoBitmap() == null) {
            profileImageView.setImageResource(R.drawable.user);
        } else {
            profileImageView.setImageBitmap(userProfile.getPhotoBitmap());
        }
        profileImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAndRequestPermissions();
            }
        });

        checkAndRequestPermissions();
    }

    private void takePhoto() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // Check if there is a camera app available
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create an Intent to open the gallery
            Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            galleryIntent.setType("image/*");

            // Create a chooser Intent to allow the user to select between camera and gallery
            Intent chooserIntent = Intent.createChooser(galleryIntent, getString(R.string.select_img));
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{takePictureIntent});

            // Start the activity with the chooser Intent
            startActivityForResult(chooserIntent, REQUEST_CODE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CODE) {
                if (data != null) {
                    if (data.hasExtra(getString(R.string.data))) {
                        Bundle extras = data.getExtras();
                        selectedImage = (Bitmap) extras.get(getString(R.string.data));
                        selectedImage = makeSmaller(selectedImage, 200);
                        profileImageView.setImageBitmap(selectedImage);
                        userProfile.setPhotoBitmap(selectedImage);

                    } else if (data.getData() != null) {
                        Uri selectedImageUri = data.getData();
                        try {
                            selectedImage = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);
                            selectedImage = makeSmaller(selectedImage, 200);
                            profileImageView.setImageBitmap(selectedImage);
                            userProfile.setPhotoBitmap(selectedImage);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    updateProfilePhotoInDatabase(userProfile);
                }
            }
        }
    }
    private void updateProfilePhotoInDatabase(User userProfil) {
        try (UserDB userDB = new UserDB(this)) {
            userDB.updateUser(userProfil);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void checkAndRequestPermissions() {
        profileImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ContextCompat.checkSelfPermission(ProfileSettingsActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                            ContextCompat.checkSelfPermission(ProfileSettingsActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                            ContextCompat.checkSelfPermission(ProfileSettingsActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(ProfileSettingsActivity.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, CAMERA_PERMISSION_REQUEST_CODE);
                    } else {
                        // Permissions are already granted
                        takePhoto();
                    }
                } else {
                    // For devices below Android M, no need to request permissions
                    takePhoto();
                }
            }
        });
    }

    private Bitmap makeSmaller(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float ratio = (float) width / (float) height;

        if (ratio > 1) {
            width = maxSize;
            height = (int) (width / ratio);
        } else {
            height = maxSize;
            width = (int) (height * ratio);
        }

        return Bitmap.createScaledBitmap(image, width, height, true);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            // Check if the camera permission is granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Camera permission granted, proceed to take photo
                takePhoto();
            } else {
                // Camera permission denied
                Toast.makeText(this, getString(R.string.cam_permission), Toast.LENGTH_SHORT).show();
            }
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater mif = getMenuInflater();
        mif.inflate(R.menu.bottom_nav_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
}
