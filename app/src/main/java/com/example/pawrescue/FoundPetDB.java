package com.example.pawrescue;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class FoundPetDB extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "FoundPetDatabase";
    private static final String TABLE_FOUND_PETS = "found_pets";
    private static final String KEY_ID = "id";
    private static final String KEY_TYPE = "type";
    private static final String KEY_GENDER = "gender";
    private static final String KEY_ESTIMATED_AGE = "estimated_age";
    private static final String KEY_PHOTO = "photo";

    public FoundPetDB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_FOUND_PETS_TABLE = "CREATE TABLE " + TABLE_FOUND_PETS + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_TYPE + " TEXT,"
                + KEY_GENDER + " TEXT," + KEY_ESTIMATED_AGE + " INTEGER,"
                + KEY_PHOTO + " BLOB" + ")";
        db.execSQL(CREATE_FOUND_PETS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FOUND_PETS);
        onCreate(db);
    }

    public long addFoundPet(FoundPet foundPet) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_TYPE, foundPet.getType());
        values.put(KEY_GENDER, foundPet.getGender());
        values.put(KEY_ESTIMATED_AGE, foundPet.getEstimatedAge());
        values.put(KEY_PHOTO, convertBitmapToByteArray(foundPet.getPhotoBitmap()));
        long id = db.insert(TABLE_FOUND_PETS, null, values);
        db.close();
        return id;
    }

    public FoundPet getFoundPet(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_FOUND_PETS, new String[]{KEY_ID,
                        KEY_TYPE, KEY_GENDER, KEY_ESTIMATED_AGE, KEY_PHOTO},
                KEY_ID + "=?", new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
        FoundPet foundPet = cursorToFoundPet(cursor);
        cursor.close();
        db.close();
        return foundPet;
    }

    public ArrayList<FoundPet> getAllFoundPets() {
        ArrayList<FoundPet> foundPetList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_FOUND_PETS, null);

        if (cursor.moveToFirst()) {
            do {
                FoundPet foundPet = cursorToFoundPet(cursor);
                foundPetList.add(foundPet);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return foundPetList;
    }

    public FoundPet cursorToFoundPet(Cursor cursor) {
        FoundPet foundPet = null;
        try {
            if (cursor != null && cursor.moveToFirst()) {
                int idIndex = cursor.getColumnIndex(KEY_ID);
                int typeIndex = cursor.getColumnIndex(KEY_TYPE);
                int genderIndex = cursor.getColumnIndex(KEY_GENDER);
                int estimatedAgeIndex = cursor.getColumnIndex(KEY_ESTIMATED_AGE);
                int photoIndex = cursor.getColumnIndex(KEY_PHOTO);

                if (idIndex != -1 && typeIndex != -1 && genderIndex != -1 &&
                        estimatedAgeIndex != -1 && photoIndex != -1) {

                    int id = cursor.getInt(idIndex);
                    String type = cursor.getString(typeIndex);
                    String gender = cursor.getString(genderIndex);
                    int estimatedAge = cursor.getInt(estimatedAgeIndex);
                    byte[] photoBytes = cursor.getBlob(photoIndex);
                    Bitmap photoBitmap = convertByteArrayToBitmap(photoBytes);

                    // Add these log statements
                    Log.d("FoundPetDB", "Photo bytes length: " + (photoBytes != null ? photoBytes.length : 0));
                    Log.d("FoundPetDB", "Photo bitmap: " + (photoBitmap != null ? "Not null" : "Null"));

                    foundPet = new FoundPet(type, gender, estimatedAge, photoBitmap);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("FoundPetDB", "Error in cursorToFoundPet: " + e.getMessage());
        }
        return foundPet;
    }

    public int updateFoundPet(FoundPet foundPet) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_TYPE, foundPet.getType());
        values.put(KEY_GENDER, foundPet.getGender());
        values.put(KEY_ESTIMATED_AGE, foundPet.getEstimatedAge());
        values.put(KEY_PHOTO, convertBitmapToByteArray(foundPet.getPhotoBitmap()));

        int foundPetId = getFoundPetId(foundPet);
        return db.update(TABLE_FOUND_PETS, values, KEY_ID + " = ?",
                new String[]{String.valueOf(foundPetId)});
    }

    public void deleteFoundPet(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_FOUND_PETS, KEY_ID + " = ?",
                new String[]{String.valueOf(id)});
        db.close();
    }

    public int getFoundPetId(FoundPet foundPet) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_FOUND_PETS, new String[]{KEY_ID},
                KEY_TYPE + "=? AND " + KEY_GENDER + "=? AND " + KEY_ESTIMATED_AGE + "=?",
                new String[]{foundPet.getType(), foundPet.getGender(),
                        String.valueOf(foundPet.getEstimatedAge())},
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

    public List<FoundPet> displayAllFoundPets() {
        List<FoundPet> foundPetList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_FOUND_PETS, null);

        if (cursor.moveToFirst()) {
            do {
                FoundPet foundPet = cursorToFoundPet(cursor);
                foundPetList.add(foundPet);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return foundPetList;
    }

    private byte[] convertBitmapToByteArray(Bitmap bitmap) {
        try {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            return stream.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("FoundPetDB", "Error in convertBitmapToByteArray: " + e.getMessage());
            return null;
        }
    }

    private Bitmap convertByteArrayToBitmap(byte[] byteArray) {
        if (byteArray == null) {
            return null;
        }
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
    }
}
