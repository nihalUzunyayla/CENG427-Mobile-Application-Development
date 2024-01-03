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

public class LostPetDB extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "LostPetDatabase";
    private static final String TABLE_LOST_PETS = "lost_pets";
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_TYPE = "type";
    private static final String KEY_AGE = "age";
    private static final String KEY_GENDER = "gender";
    private static final String KEY_PHOTO = "photo";
    private static final String KEY_LOST_ADDRESS = "lost_address";

    public LostPetDB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_LOST_PETS_TABLE = "CREATE TABLE " + TABLE_LOST_PETS + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"
                + KEY_TYPE + " TEXT," + KEY_AGE + " INTEGER,"
                + KEY_GENDER + " TEXT," + KEY_PHOTO + " BLOB,"
                + KEY_LOST_ADDRESS + " TEXT" + ")";
        db.execSQL(CREATE_LOST_PETS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOST_PETS);
        onCreate(db);
    }

    public long addLostPet(LostPet lostPet) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, lostPet.getName());
        values.put(KEY_TYPE, lostPet.getType());
        values.put(KEY_AGE, lostPet.getAge());
        values.put(KEY_GENDER, lostPet.getGender());
        values.put(KEY_PHOTO, convertBitmapToByteArray(lostPet.getPhotoBitmap()));
        values.put(KEY_LOST_ADDRESS, lostPet.getLostAddress());
        long id = db.insert(TABLE_LOST_PETS, null, values);
        db.close();
        return id;
    }

    public LostPet getLostPet(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_LOST_PETS, new String[]{KEY_ID,
                        KEY_NAME, KEY_TYPE, KEY_AGE, KEY_GENDER, KEY_PHOTO, KEY_LOST_ADDRESS},
                KEY_ID + "=?", new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
        LostPet lostPet = cursorToLostPet(cursor);
        cursor.close();
        db.close();
        return lostPet;
    }

    public ArrayList<LostPet> getAllLostPets() {
        ArrayList<LostPet> lostPetList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_LOST_PETS, null);

        if (cursor.moveToFirst()) {
            do {
                LostPet lostPet = cursorToLostPet(cursor);
                lostPetList.add(lostPet);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return lostPetList;
    }

    public void logDatabaseContents() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_LOST_PETS, null);

        if (cursor.moveToFirst()) {
            do {
                if (cursor != null && cursor.moveToFirst()) {
                    int idIndex = cursor.getColumnIndex(KEY_ID);
                    int nameIndex = cursor.getColumnIndex(KEY_NAME);
                    int typeIndex = cursor.getColumnIndex(KEY_TYPE);
                    int ageIndex = cursor.getColumnIndex(KEY_AGE);
                    int genderIndex = cursor.getColumnIndex(KEY_GENDER);
                    int lostAddressIndex = cursor.getColumnIndex(KEY_LOST_ADDRESS);

                    if (idIndex != -1 && nameIndex != -1 && typeIndex != -1 && ageIndex != -1 &&
                            genderIndex != -1 && lostAddressIndex != -1) {

                        Log.d("LostPetDatabase", "Name: " + nameIndex + ", Type: " + typeIndex + ", Age: " + ageIndex + ", Gender: " + genderIndex + ", Address: " + lostAddressIndex);
                    }
                }
            } while (cursor.moveToNext());
        }

        cursor.close();
    }


    public LostPet cursorToLostPet(Cursor cursor) {
        LostPet lostPet = null;
        try {
            if (cursor != null && cursor.moveToFirst()) {
                int idIndex = cursor.getColumnIndex(KEY_ID);
                int nameIndex = cursor.getColumnIndex(KEY_NAME);
                int typeIndex = cursor.getColumnIndex(KEY_TYPE);
                int ageIndex = cursor.getColumnIndex(KEY_AGE);
                int genderIndex = cursor.getColumnIndex(KEY_GENDER);
                int photoIndex = cursor.getColumnIndex(KEY_PHOTO);
                int lostAddressIndex = cursor.getColumnIndex(KEY_LOST_ADDRESS);

                if (idIndex != -1 && nameIndex != -1 && typeIndex != -1 && ageIndex != -1 &&
                        genderIndex != -1 && photoIndex != -1 && lostAddressIndex != -1) {

                    int id = cursor.getInt(idIndex);
                    String name = cursor.getString(nameIndex);
                    String type = cursor.getString(typeIndex);
                    int age = cursor.getInt(ageIndex);
                    String gender = cursor.getString(genderIndex);
                    byte[] photoBytes = cursor.getBlob(photoIndex);
                    Bitmap photoBitmap = convertByteArrayToBitmap(photoBytes);
                    String lostAddress = cursor.getString(lostAddressIndex);

                    // Add these log statements
                    Log.d("LostPetDB", "Photo bytes length: " + (photoBytes != null ? photoBytes.length : 0));
                    Log.d("LostPetDB", "Photo bitmap: " + (photoBitmap != null ? "Not null" : "Null"));

                    lostPet = new LostPet(name, type, age, gender, photoBitmap, lostAddress);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("LostPetDB", "Error in cursorToLostPet: " + e.getMessage());
        }
        return lostPet;
    }

    public int updateLostPet(LostPet lostPet) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, lostPet.getName());
        values.put(KEY_TYPE, lostPet.getType());
        values.put(KEY_AGE, lostPet.getAge());
        values.put(KEY_GENDER, lostPet.getGender());
        values.put(KEY_PHOTO, convertBitmapToByteArray(lostPet.getPhotoBitmap()));
        values.put(KEY_LOST_ADDRESS, lostPet.getLostAddress()); // Update the lost address

        int lostPetId = getLostPetId(lostPet);
        return db.update(TABLE_LOST_PETS, values, KEY_ID + " = ?",
                new String[]{String.valueOf(lostPetId)});
    }

    public void deleteLostPet(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_LOST_PETS, KEY_ID + " = ?",
                new String[]{String.valueOf(id)});
        db.close();
    }

    public int getLostPetId(LostPet lostPet) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_LOST_PETS, new String[]{KEY_ID},
                KEY_NAME + "=? AND " + KEY_TYPE + "=? AND " + KEY_AGE + "=? AND " +
                        KEY_GENDER + "=? AND " + KEY_LOST_ADDRESS + "=?",
                new String[]{lostPet.getName(), lostPet.getType(), String.valueOf(lostPet.getAge()),
                        lostPet.getGender(), lostPet.getLostAddress()},
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

    public List<LostPet> displayAllLostPets() {
        List<LostPet> lostPetList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_LOST_PETS, null);

        if (cursor.moveToFirst()) {
            do {
                LostPet lostPet = cursorToLostPet(cursor);
                lostPetList.add(lostPet);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return lostPetList;
    }

    private byte[] convertBitmapToByteArray(Bitmap bitmap) {
        try {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            return stream.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("LostPetDB", "Error in convertBitmapToByteArray: " + e.getMessage());
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