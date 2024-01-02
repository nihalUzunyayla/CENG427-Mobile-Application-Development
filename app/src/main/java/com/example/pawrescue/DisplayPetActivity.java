package com.example.pawrescue;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class DisplayPetActivity extends AppCompatActivity implements PetModalDetails.PetDBListener{

    private ArrayList<Pet> petList;
    private DisplayPetAdapter petAdapter;
    String selectedSpecies,selectedAge,selectedGender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setContentView(R.layout.activity_display_pet);

        Spinner spinnerSpecies = findViewById(R.id.spinnerSpecies);
        Spinner spinnerAge = findViewById(R.id.spinnerAge);
        Spinner spinnerGender = findViewById(R.id.spinnerGender);

        ArrayAdapter<CharSequence> speciesAdapter = ArrayAdapter.createFromResource(
                this, R.array.filter_species, android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence> ageAdapter = ArrayAdapter.createFromResource(
                this, R.array.filter_age, android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence> genderAdapter = ArrayAdapter.createFromResource(
                this, R.array.filter_gender, android.R.layout.simple_spinner_item);

        speciesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ageAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerSpecies.setAdapter(speciesAdapter);
        spinnerAge.setAdapter(ageAdapter);
        spinnerGender.setAdapter(genderAdapter);

        petList = new ArrayList<>();
        petAdapter = new DisplayPetAdapter(petList, this);
        RecyclerView recyclerViewAnimals = findViewById(R.id.recyclerViewPets);
        recyclerViewAnimals.setLayoutManager(new GridLayoutManager(this,2));
        recyclerViewAnimals.setAdapter(petAdapter);

        getPetData();


        spinnerSpecies.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
         @Override
         public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
             selectedSpecies = spinnerSpecies.getSelectedItem().toString();
             getPetData();
         }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });
        spinnerAge.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                selectedAge = spinnerAge.getSelectedItem().toString();
                getPetData();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });
        spinnerGender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                selectedGender = spinnerGender.getSelectedItem().toString();
                getPetData();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });

        petAdapter.setOnItemLongClickListener(new DisplayPetAdapter.OnItemLongClickListener() {
            @Override
            public void onItemLongClick(int position) {
                showItemDetailsModal(position);
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater mif = getMenuInflater();
        mif.inflate(R.menu.bottom_nav_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
    private void showItemDetailsModal(int position) {
        PetModalDetails petDetailsDialog = new PetModalDetails(this, petList.get(position), new PetDB(this), position);
        petDetailsDialog.initialize();
        Pet selectedPet = petList.get(position);
        petDetailsDialog.setPetDetails(
                selectedPet.getName(),
                selectedPet.getType(),
                selectedPet.getAge(),
                selectedPet.getGender(),
                selectedPet.getState(),
                selectedPet.getPhotoBitmap()
        );

        petDetailsDialog.setPetDBListener(this);
        petDetailsDialog.show();

    }

    @Override
    public void onPetDeleted(int position) {
        petList.remove(position);
        petAdapter.notifyItemRemoved(position);
    }
   private void getPetData() {
       try {
           SQLiteDatabase database = this.openOrCreateDatabase("PetDatabase", MODE_PRIVATE, null);

           String selection = null;
           List<String> selectionArgsList = new ArrayList<>();
           if (!selectedSpecies.equals(getString(R.string.All))) {
               selection = "type=?";
               selectionArgsList.add(selectedSpecies);
           }
           if (!selectedAge.equals("0-20")) {
               String[] ageRange = selectedAge.split("-");
               int startAge = Integer.parseInt(ageRange[0]);
               int endAge = Integer.parseInt(ageRange[1]);

               if (selection == null) {
                   selection = "age BETWEEN ? AND ?";
               } else {
                   selection += " AND age BETWEEN ? AND ?";
               }
               selectionArgsList.add(String.valueOf(startAge));
               selectionArgsList.add(String.valueOf(endAge));
           }
           if (!selectedGender.equals(getString(R.string.All))) {
               if (selection == null) {
                   selection = "gender=?";
               } else {
                   selection += " AND gender=?";
               }
               selectionArgsList.add(selectedGender);
           }
           String[] selectionArgs = selectionArgsList.toArray(new String[0]);
           Cursor cursor = database.query("pets", null, selection, selectionArgs, null, null, null);
           petList.clear();

           int nameIx = cursor.getColumnIndex("name");
           int typeIx = cursor.getColumnIndex("type");
           int ageIx = cursor.getColumnIndex("age");
           int genderIx = cursor.getColumnIndex("gender");
           int stateIx = cursor.getColumnIndex("state");
           int photoIx = cursor.getColumnIndex("photo");

           petList.clear();

           while (cursor.moveToNext()) {
               String name = cursor.getString(nameIx);
               String type = cursor.getString(typeIx);
               int age = cursor.getInt(ageIx);
               String gender = cursor.getString(genderIx);
               String state = cursor.getString(stateIx);

               byte[] byteArray = cursor.getBlob(photoIx);
               Bitmap image = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);

               Pet pet = new Pet(name, type, age, gender, image, state);
               petList.add(pet);
           }

           petAdapter.notifyDataSetChanged();
           cursor.close();
       } catch (Exception e) {
           e.printStackTrace();
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
}
