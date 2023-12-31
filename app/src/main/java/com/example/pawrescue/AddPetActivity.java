
package com.example.pawrescue;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.IOException;

public class AddPetActivity extends AppCompatActivity {

    private static final int REQUEST_CODE = 1;
    private static final int CAMERA_PERMISSION_REQUEST_CODE = 103;
    private static final int STORAGE_PERMISSION_REQUEST_CODE = 104;

    PetDB petDB = new PetDB(this);

    private ImageView imageViewCenter;
    private Bitmap selectedImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_pet);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        petDB.writeAllPets();

        imageViewCenter = findViewById(R.id.imageViewCenter);

        // ImageView click to initiate the photo-taking process
        imageViewCenter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check and request permissions when imageViewCenter is clicked
                checkAndRequestPermissions();
            }
        });

        EditText editTextPetName = findViewById(R.id.editTextPetName);
        EditText editTextPetType = findViewById(R.id.editTextPetType);
        EditText editTextPetAge = findViewById(R.id.editTextPetAge);
        EditText editTextPetGender = findViewById(R.id.editTextPetGender);
        EditText editTextPetState = findViewById(R.id.editTextPetState);

        Button buttonAddPet = findViewById(R.id.buttonAddPet);
        buttonAddPet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check if all fields are filled
                if (checkFieldsNotEmpty(editTextPetName, editTextPetType, editTextPetAge, editTextPetGender, editTextPetState)) {
                    // Check if an image is selected
                    if (selectedImage != null) {
                        addPetToDatabase(
                                editTextPetName.getText().toString(),
                                editTextPetType.getText().toString(),
                                Integer.parseInt(editTextPetAge.getText().toString()),
                                editTextPetGender.getText().toString(),
                                selectedImage,
                                editTextPetState.getText().toString()
                        );
                    } else {
                        Toast.makeText(AddPetActivity.this, getString(R.string.empty_image), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(AddPetActivity.this, getString(R.string.empty_field), Toast.LENGTH_SHORT).show();
                }
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

    private void addPetToDatabase(String name, String type, int age, String gender, Bitmap photo, String state) {
        // Adding pet information to the database
        Pet newPet = new Pet(name, type, age, gender, photo, state);
        long id = petDB.addPet(newPet);

        if (id != -1) {
            Toast.makeText(this, getString(R.string.pet_saved), Toast.LENGTH_SHORT).show();
            clearInputFields();
        } else {
            Toast.makeText(this, getString(R.string.pet_do_not_saved), Toast.LENGTH_SHORT).show();
        }
    }

    private boolean checkFieldsNotEmpty(EditText... editTexts) {
        for (EditText editText : editTexts) {
            if (editText.getText().toString().trim().isEmpty()) {
                return false;
            }
        }
        return true;
    }

    public Bitmap makeSmaller(Bitmap image, int maxSize) {
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
        EditText editTextPetName = findViewById(R.id.editTextPetName);
        EditText editTextPetType = findViewById(R.id.editTextPetType);
        EditText editTextPetAge = findViewById(R.id.editTextPetAge);
        EditText editTextPetGender = findViewById(R.id.editTextPetGender);
        EditText editTextPetState = findViewById(R.id.editTextPetState);

        editTextPetName.setText("");
        editTextPetType.setText("");
        editTextPetAge.setText("");
        editTextPetGender.setText("");
        editTextPetState.setText("");

        // Reset imageViewCenter
        imageViewCenter.setImageResource(R.drawable.camera);
        selectedImage = null;
    }

    private void checkAndRequestPermissions() {
        // Move the setOnClickListener outside of the onClick method
        imageViewCenter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ContextCompat.checkSelfPermission(AddPetActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                            ContextCompat.checkSelfPermission(AddPetActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                            ContextCompat.checkSelfPermission(AddPetActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(AddPetActivity.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, CAMERA_PERMISSION_REQUEST_CODE);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater mif = getMenuInflater();
        mif.inflate(R.menu.bottom_nav_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
}