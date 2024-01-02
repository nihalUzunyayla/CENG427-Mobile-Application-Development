package com.example.pawrescue;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import androidx.viewpager.widget.ViewPager;

import com.example.pawrescue.databinding.ActivityLostFoundPlatformBinding;
import com.example.pawrescue.ui.main.PlaceholderFragment;
import com.example.pawrescue.ui.main.SectionsPagerAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import java.io.IOException;

public class LostFoundPlatformActivity extends AppCompatActivity {
    private static final int REQUEST_CODE = 1;
    private static final int CAMERA_PERMISSION_REQUEST_CODE = 2;

    LostPetDB lostPetDB = new LostPetDB(this);
    FoundPetDB foundPetDB = new FoundPetDB(this);
    private ActivityLostFoundPlatformBinding binding;
    EditText editTextPetName;
    EditText editTextPetType;
    EditText editTextPetAge;
    EditText editTextPetGender;
    EditText editTextLostAddress;
    EditText editTextPetEstimatedAge;
    private ImageView imageViewCenter;
    private Bitmap selectedImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityLostFoundPlatformBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = binding.viewPager;
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = binding.tabs;
        tabs.setupWithViewPager(viewPager);
        FloatingActionButton fab = binding.fab;

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.tabs.getSelectedTabPosition() == 0) {
                    showAddLostPetDialog();
                } else {
                    showAddFoundPetDialog();
                }
            }
        });
    }

    private void showAddLostPetDialog() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.dialog_add_lost_pet, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);
        builder.setTitle("Add Lost Pet");

        editTextPetName = dialogView.findViewById(R.id.editTextPetName);
        editTextPetType = dialogView.findViewById(R.id.editTextPetType);
        editTextPetAge = dialogView.findViewById(R.id.editTextPetAge);
        editTextPetGender = dialogView.findViewById(R.id.editTextPetGender);
        editTextLostAddress = dialogView.findViewById(R.id.editTextLostAddress);
        imageViewCenter = dialogView.findViewById(R.id.imageViewPet);

        builder.setPositiveButton("Add", (dialog, which) -> {
            String petName = editTextPetName.getText().toString();
            String petType = editTextPetType.getText().toString();
            int petAge = Integer.parseInt(editTextPetAge.getText().toString());
            String petGender = editTextPetGender.getText().toString();
            String lostAddress = editTextLostAddress.getText().toString();

            // Check if all fields are filled
            if (checkEditFieldsNotEmpty(editTextPetName, editTextPetType, editTextPetAge, editTextPetGender, editTextLostAddress)) {
                addPetToDatabase(petName, petType, petAge, petGender, selectedImage, lostAddress);

            } else {
                Snackbar.make(binding.getRoot(), "Please fill in all fields", Snackbar.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> {

        });

        imageViewCenter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (ContextCompat.checkSelfPermission(LostFoundPlatformActivity.this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                                ContextCompat.checkSelfPermission(LostFoundPlatformActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                                ContextCompat.checkSelfPermission(LostFoundPlatformActivity.this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(LostFoundPlatformActivity.this, new String[]{android.Manifest.permission.CAMERA, android.Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, CAMERA_PERMISSION_REQUEST_CODE);
                        } else {
                            // Permissions are already granted
                            takePhoto();
                        }
                    } else {
                        // For devices below Android M, no need to request permissions
                        takePhoto();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("LostFoundPlatform", "Error in onClick: " + e.getMessage());
                }
            }
        });



        if (binding.tabs.getSelectedTabPosition() == 0) {
            AlertDialog alertDialog = builder.create();
            alertDialog.show();

        } else {
            
        }
    }

    private void addPetToDatabase(String name, String type, int age, String gender, Bitmap photo, String address) {
        // Adding pet information to the database
        LostPet lostPet = new LostPet(name, type, age, gender, photo, address);
        long id = lostPetDB.addLostPet(lostPet);

        if (id != -1) {
            Toast.makeText(this, getString(R.string.pet_saved), Toast.LENGTH_SHORT).show();
            clearInputFields();
        } else {
            Toast.makeText(this, getString(R.string.pet_do_not_saved), Toast.LENGTH_SHORT).show();
        }
    }

    private void showAddFoundPetDialog() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.dialog_add_found_pet, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);
        builder.setTitle("Add Found Pet");

        editTextPetType = dialogView.findViewById(R.id.editTextPetType);
        editTextPetEstimatedAge = dialogView.findViewById(R.id.editTextPetEstimatedAge);
        editTextPetGender = dialogView.findViewById(R.id.editTextPetGender);
        imageViewCenter = dialogView.findViewById(R.id.imageViewPet);

        builder.setPositiveButton("Add", (dialog, which) -> {
            String petType = editTextPetType.getText().toString();
            int petEstimatedAge = Integer.parseInt(editTextPetEstimatedAge.getText().toString());
            String petGender = editTextPetGender.getText().toString();

            // Check if all fields are filled
            if (checkEditFieldsNotEmpty(editTextPetType, editTextPetEstimatedAge, editTextPetGender)) {
                addFoundPetToDatabase(petType, petEstimatedAge, petGender, selectedImage);

            } else {
                Snackbar.make(binding.getRoot(), "Please fill in all fields", Snackbar.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> {
            // Handle Cancel button action if needed
        });

        imageViewCenter.setOnClickListener(v -> {
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                            ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                            ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA, android.Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, CAMERA_PERMISSION_REQUEST_CODE);
                    } else {
                        // Permissions are already granted
                        takePhoto();
                    }
                } else {
                    // For devices below Android M, no need to request permissions
                    takePhoto();
                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("LostFoundPlatform", "Error in onClick: " + e.getMessage());
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void addFoundPetToDatabase(String type, int estimatedAge, String gender, Bitmap photo) {
        // Adding found pet information to the database
        FoundPet foundPet = new FoundPet(type, gender, estimatedAge, photo);
        long id = foundPetDB.addFoundPet(foundPet);

        if (id != -1) {
            Toast.makeText(this, getString(R.string.pet_saved), Toast.LENGTH_SHORT).show();
            clearInputFields();
        } else {
            Toast.makeText(this, getString(R.string.pet_do_not_saved), Toast.LENGTH_SHORT).show();
        }
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
                    // Check if the result is from the camera
                    if (data.hasExtra(getString(R.string.data))) {
                        Bundle extras = data.getExtras();
                        selectedImage = (Bitmap) extras.get(getString(R.string.data));
                        selectedImage = makeSmaller(selectedImage, 200);
                        imageViewCenter.setImageBitmap(selectedImage);
                    }
                    // Check if the result is from the gallery
                    else if (data.getData() != null) {
                        // Get the selected image URI
                        Uri selectedImageUri = data.getData();

                        // Load the selected image into the ImageView
                        try {
                            selectedImage = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);
                            selectedImage = makeSmaller(selectedImage, 200);
                            imageViewCenter.setImageBitmap(selectedImage);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    private boolean checkEditFieldsNotEmpty(EditText... editTexts) {
        for (EditText editText : editTexts) {
            if (editText.getText().toString().trim().isEmpty()) {
                return false;
            }
        }
        return true;
    }

    public Bitmap makeSmaller(Bitmap image, int maxSize) {

        if (image == null) {
            return null;
        }
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
    private void clearInputFields() {

        editTextPetName.setText("");
        editTextPetType.setText("");
        editTextPetAge.setText("");
        editTextPetGender.setText("");
        editTextLostAddress.setText("");

        imageViewCenter.setImageResource(R.drawable.camera);
        selectedImage = null;
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
}
