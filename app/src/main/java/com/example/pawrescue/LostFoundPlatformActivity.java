package com.example.pawrescue;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LostFoundPlatformActivity extends AppCompatActivity {

    private static final int REQUEST_CODE = 1;
    private static final int CAMERA_PERMISSION_REQUEST_CODE = 2;

    private LostPetDB lostPetDB;

    private EditText editTextPetName;
    private EditText editTextPetType;
    private EditText editTextPetAge;
    private EditText editTextPetGender;
    private EditText editTextLostAddress;
    private ImageView imageViewCenter;
    private Bitmap selectedImage;

    private RecyclerView recyclerViewLostPets;
    private LostPetAdapter lostPetAdapter;
    private ArrayList<LostPet> lostPetList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lost_found_platform);

        lostPetDB = new LostPetDB(this);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> showAddLostPetDialog());

        recyclerViewLostPets = findViewById(R.id.recyclerViewLostPets);
        recyclerViewLostPets.setLayoutManager(new GridLayoutManager(this, 2));

        lostPetList = new ArrayList<>();
        lostPetAdapter = new LostPetAdapter(lostPetList, this);
        recyclerViewLostPets.setAdapter(lostPetAdapter);

        getPetData();

        lostPetAdapter.setOnItemLongClickListener(position -> showFoundDialog(position));
    }

    private void showFoundDialog(int position) {
        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.dialog_found_pet, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);
        builder.setTitle(getString(R.string.found_pet));

        EditText editTextName = dialogView.findViewById(R.id.editTextFoundName);
        EditText editTextSurname = dialogView.findViewById(R.id.editTextFoundSurname);
        EditText editTextPhoneNumber = dialogView.findViewById(R.id.editTextFoundPhoneNumber);

        builder.setPositiveButton("OK", (dialog, which) -> {
            String foundName = editTextName.getText().toString();
            String foundSurname = editTextSurname.getText().toString();
            String foundPhoneNumber = editTextPhoneNumber.getText().toString();

            if (!foundName.isEmpty() && !foundSurname.isEmpty() && !foundPhoneNumber.isEmpty()) {
                showContactMessage(foundPhoneNumber);
            } else {
                Toast.makeText(this, getString(R.string.empty_field), Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton(getString(R.string.cancel), (dialog, which) -> {
            // Handle Cancel button action if needed
        });

        AlertDialog foundDialog = builder.create();
        foundDialog.show();
    }

    private void showContactMessage(String phoneNumber) {
        String message = getString(R.string.communication)+ phoneNumber;

        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG);

        snackbar.setAction(getString(R.string.ok), v -> {
            // Handle the action if needed
        });

        snackbar.show();
    }

    private void getPetData() {
        try {
            SQLiteDatabase database = this.openOrCreateDatabase("LostPetDatabase", MODE_PRIVATE, null);

            String selection = null;
            List<String> selectionArgsList = new ArrayList<>();
            String[] selectionArgs = selectionArgsList.toArray(new String[0]);
            Cursor cursor = database.query("lost_pets", null, selection, selectionArgs, null, null, null);

            int nameIx = cursor.getColumnIndex("name");
            int typeIx = cursor.getColumnIndex("type");
            int ageIx = cursor.getColumnIndex("age");
            int genderIx = cursor.getColumnIndex("gender");
            int photoIx = cursor.getColumnIndex("photo");
            int addressIx = cursor.getColumnIndex("lost_address");

            lostPetList.clear();

            while (cursor.moveToNext()) {
                String name = cursor.getString(nameIx);
                String type = cursor.getString(typeIx);
                int age = cursor.getInt(ageIx);
                String gender = cursor.getString(genderIx);
                String address = cursor.getString(addressIx);

                byte[] byteArray = cursor.getBlob(photoIx);
                Bitmap image = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);

                LostPet lostPet = new LostPet(name, type, age, gender, image, address);
                lostPetList.add(lostPet);
            }

            lostPetAdapter.notifyDataSetChanged();

            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater mif = getMenuInflater();
        mif.inflate(R.menu.bottom_nav_menu, menu);
        return super.onCreateOptionsMenu(menu);
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

        builder.setPositiveButton(getString(R.string.add), null); // Set to null for custom handling

        builder.setNegativeButton(getString(R.string.cancel), (dialog, which) -> {
            // Handle Cancel button action if needed
            dialog.dismiss(); // Dismiss the dialog when Cancel button is clicked
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.setOnShowListener(dialogInterface -> {
            Button positiveButton = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);

            positiveButton.setOnClickListener(view -> {
                String petName = editTextPetName.getText().toString();
                String petType = editTextPetType.getText().toString();
                String petAgeText = editTextPetAge.getText().toString();
                String petGender = editTextPetGender.getText().toString();
                String lostAddress = editTextLostAddress.getText().toString();

                // Check if the selectedImage is null, set it to the default image
                if (selectedImage == null) {
                    selectedImage = BitmapFactory.decodeResource(getResources(), R.drawable.default_pet);
                }

                if (checkEditFieldsNotEmpty(editTextPetName, editTextPetType, editTextPetAge, editTextPetGender, editTextLostAddress)) {
                    try {
                        int petAge = Integer.parseInt(petAgeText);
                        addPetToDatabase(petName, petType, petAge, petGender, selectedImage, lostAddress);
                        alertDialog.dismiss(); // Dismiss the dialog only if all fields are filled
                    } catch (NumberFormatException e) {
                        Toast.makeText(this, getString(R.string.valid_age), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, getString(R.string.empty_field), Toast.LENGTH_SHORT).show();
                    // Dialog will not be dismissed here
                }
            });
        });

        imageViewCenter.setOnClickListener(v -> {
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
        });

        alertDialog.show();
    }



    private void addPetToDatabase(String name, String type, int age, String gender, Bitmap photo, String address) {
        LostPet lostPet = new LostPet(name, type, age, gender, photo, address);
        long id = lostPetDB.addLostPet(lostPet);

        if (id != -1) {
            Toast.makeText(this, getString(R.string.pet_saved), Toast.LENGTH_SHORT).show();
            clearInputFields();
        } else {
            Toast.makeText(this, getString(R.string.pet_do_not_saved), Toast.LENGTH_SHORT).show();
        }
    }

    private void takePhoto() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            galleryIntent.setType("image/*");

            Intent chooserIntent = Intent.createChooser(galleryIntent, getString(R.string.select_img));
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{takePictureIntent});

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
                        imageViewCenter.setImageBitmap(selectedImage);
                    } else if (data.getData() != null) {
                        Uri selectedImageUri = data.getData();

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
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                takePhoto();
            } else {
                Toast.makeText(this, getString(R.string.cam_permission), Toast.LENGTH_SHORT).show();
            }
        }
    }
}
