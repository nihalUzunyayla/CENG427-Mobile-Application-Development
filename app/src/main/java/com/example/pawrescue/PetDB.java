package com.example.pawrescue;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class PetDB extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "PetDatabase";
    private static final String TABLE_PETS = "pets";
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_TYPE = "type";
    private static final String KEY_AGE = "age";
    private static final String KEY_GENDER = "gender";
    private static final String KEY_PHOTO = "photo";
    private static final String KEY_STATE = "state";

    public PetDB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_PETS_TABLE = "CREATE TABLE " + TABLE_PETS + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"
                + KEY_TYPE + " TEXT," + KEY_AGE + " INTEGER,"
                + KEY_GENDER + " TEXT," + KEY_PHOTO + " BLOB,"
                + KEY_STATE + " TEXT" + ")"; // Include the new column in the table
        db.execSQL(CREATE_PETS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PETS);
        onCreate(db);
    }

    public long addPet(Pet pet) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, pet.getName());
        values.put(KEY_TYPE, pet.getType());
        values.put(KEY_AGE, pet.getAge());
        values.put(KEY_GENDER, pet.getGender());
        values.put(KEY_PHOTO, convertBitmapToByteArray(pet.getPhotoBitmap()));
        values.put(KEY_STATE, pet.getState()); // Save the state
        long id = db.insert(TABLE_PETS, null, values);
        db.close();
        return id;
    }

    public Pet getPet(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_PETS, new String[]{KEY_ID,
                        KEY_NAME, KEY_TYPE, KEY_AGE, KEY_GENDER, KEY_PHOTO, KEY_STATE},
                KEY_ID + "=?", new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
        Pet pet = cursorToPet(cursor);
        cursor.close();
        db.close();
        return pet;
    }

    private Pet cursorToPet(Cursor cursor) {
        Pet pet = null;

        if (cursor != null && cursor.getCount() > 0) {
            int idIndex = cursor.getColumnIndex(KEY_ID);
            int nameIndex = cursor.getColumnIndex(KEY_NAME);
            int typeIndex = cursor.getColumnIndex(KEY_TYPE);
            int ageIndex = cursor.getColumnIndex(KEY_AGE);
            int genderIndex = cursor.getColumnIndex(KEY_GENDER);
            int photoIndex = cursor.getColumnIndex(KEY_PHOTO);
            int stateIndex = cursor.getColumnIndex(KEY_STATE);

            if (idIndex != -1 && nameIndex != -1 && typeIndex != -1 && ageIndex != -1 &&
                    genderIndex != -1 && photoIndex != -1 && stateIndex != -1) {
                cursor.moveToFirst();

                int id = cursor.getInt(idIndex);
                String name = cursor.getString(nameIndex);
                String type = cursor.getString(typeIndex);
                int age = cursor.getInt(ageIndex);
                String gender = cursor.getString(genderIndex);
                byte[] photoBytes = cursor.getBlob(photoIndex);
                Bitmap photoBitmap = convertByteArrayToBitmap(photoBytes);
                String state = cursor.getString(stateIndex);

                pet = new Pet(name, type, age, gender, photoBitmap, state);
            }
        }

        return pet;
    }

    public int updatePet(Pet pet) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, pet.getName());
        values.put(KEY_TYPE, pet.getType());
        values.put(KEY_AGE, pet.getAge());
        values.put(KEY_GENDER, pet.getGender());
        values.put(KEY_PHOTO, convertBitmapToByteArray(pet.getPhotoBitmap()));
        values.put(KEY_STATE, pet.getState()); // Update the state

        int petId = getPetId(pet);
        return db.update(TABLE_PETS, values, KEY_ID + " = ?",
                new String[]{String.valueOf(petId)});
    }

    public void deletePet(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_PETS, KEY_ID + " = ?",
                new String[]{String.valueOf(id)});
        db.close();
    }

    private int getPetId(Pet pet) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_PETS, new String[]{KEY_ID},
                KEY_NAME + "=? AND " + KEY_TYPE + "=? AND " + KEY_AGE + "=? AND " +
                        KEY_GENDER + "=? AND " + KEY_STATE + "=?",
                new String[]{pet.getName(), pet.getType(), String.valueOf(pet.getAge()),
                        pet.getGender(), pet.getState()},
                null, null, null, null);

        int id = -1;
        int idIndex = cursor.getColumnIndex(KEY_ID);
        if (cursor != null && idIndex != -1) {
            cursor.moveToFirst();
            id = cursor.getInt(idIndex);
        }

        cursor.close();
        db.close();

        return id;
    }

    public List<Pet> displayAllPets() {
        List<Pet> petList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_PETS, null);

        if (cursor.moveToFirst()) {
            do {
                Pet pet = cursorToPet(cursor);
                petList.add(pet);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return petList;
    }


    private byte[] convertBitmapToByteArray(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }

    private Bitmap convertByteArrayToBitmap(byte[] byteArray) {
        if (byteArray == null) {
            return null;
        }
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
    }

    //terminale yazdır
    public void writeAllPets() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_PETS, new String[]{KEY_ID,
                        KEY_NAME, KEY_TYPE, KEY_AGE, KEY_GENDER, KEY_PHOTO, KEY_STATE},
                null, null, null, null, null);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                int idIndex = cursor.getColumnIndex(KEY_ID);
                int nameIndex = cursor.getColumnIndex(KEY_NAME);
                int typeIndex = cursor.getColumnIndex(KEY_TYPE);
                int ageIndex = cursor.getColumnIndex(KEY_AGE);
                int genderIndex = cursor.getColumnIndex(KEY_GENDER);
                int photoIndex = cursor.getColumnIndex(KEY_PHOTO);
                int stateIndex = cursor.getColumnIndex(KEY_STATE);

                if (idIndex != -1 && nameIndex != -1 && typeIndex != -1 && ageIndex != -1 &&
                        genderIndex != -1 && photoIndex != -1 && stateIndex != -1) {

                    int id = cursor.getInt(idIndex);
                    String name = cursor.getString(nameIndex);
                    String type = cursor.getString(typeIndex);
                    int age = cursor.getInt(ageIndex);
                    String gender = cursor.getString(genderIndex);
                    byte[] photoBytes = cursor.getBlob(photoIndex);
                    Bitmap photoBitmap = convertByteArrayToBitmap(photoBytes);
                    String state = cursor.getString(stateIndex);

                    // Print the pet information
                    System.out.println("Pet ID: " + id);
                    System.out.println("Name: " + name);
                    System.out.println("Type: " + type);
                    System.out.println("Age: " + age);
                    System.out.println("Gender: " + gender);
                    System.out.println("State: " + state);

                }
            }

            cursor.close();
            db.close();
        }
    }
}
