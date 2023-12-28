package com.example.pawrescue;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.DrawableRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class AddPetActivity extends AppCompatActivity {

    private static final int REQUEST_CODE = 1;
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
                takePhoto();
            }
        });

        // Other UI elements
        EditText editTextPetName = findViewById(R.id.editTextPetName);
        EditText editTextPetType = findViewById(R.id.editTextPetType);
        EditText editTextPetAge = findViewById(R.id.editTextPetAge);
        EditText editTextPetGender = findViewById(R.id.editTextPetGender);
        EditText editTextPetState = findViewById(R.id.editTextPetState);

        Button buttonAddPet = findViewById(R.id.buttonAddPet);

        // Add Pet button click to add information to the database
        buttonAddPet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check if all fields are filled
                if (checkFieldsNotEmpty(editTextPetName, editTextPetType, editTextPetAge, editTextPetGender, editTextPetState)) {
                    addPetToDatabase(
                            editTextPetName.getText().toString(),
                            editTextPetType.getText().toString(),
                            Integer.parseInt(editTextPetAge.getText().toString()),
                            editTextPetGender.getText().toString(),
                            selectedImage,
                            editTextPetState.getText().toString()
                    );
                } else {
                    // Display a toast message if any field is empty
                    Toast.makeText(AddPetActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.bottom_nav_menu, menu);
        return true;
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

    private void takePhoto() {
        // Photo-taking process
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_CODE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        // Return process after taking a photo
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            selectedImage = (Bitmap) extras.get("data");
            selectedImage = makeSmaller(selectedImage, 200);
            imageViewCenter.setImageBitmap(selectedImage);
        }
    }

    private void addPetToDatabase(String name, String type, int age, String gender, Bitmap photo, String state) {
        // Adding pet information to the database

        Pet newPet = new Pet(name, type, age, gender, photo, state);
        long id = petDB.addPet(newPet);

        // Display toast message based on the success of the save operation
        if (id != -1) {
            Toast.makeText(this, "Pet information saved successfully", Toast.LENGTH_SHORT).show();
            clearInputFields();
        } else {
            Toast.makeText(this, "Failed to save pet information", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean checkFieldsNotEmpty(EditText... editTexts) {
        // Check if all EditText fields are not empty
        for (EditText editText : editTexts) {
            if (editText.getText().toString().trim().isEmpty()) {
                return false;
            }
        }
        return true;
    }

    public Bitmap makeSmaller(Bitmap image, int maxSize) {
        // Resize the image to a smaller size
        int width = image.getWidth();
        int height = image.getHeight();

        float ratio = (float) width / (float) height;

        if (ratio > 1) {
            //landscape
            width = maxSize;
            height = (int) (width / ratio);
        } else {
            //portrait
            height = maxSize;
            width = (int) (height * ratio);
        }

        return Bitmap.createScaledBitmap(image, width, height, true);
    }

    private void clearInputFields() {
        // Clear all input fields
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
}
