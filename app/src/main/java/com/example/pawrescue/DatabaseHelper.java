package com.example.pawrescue;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "paw_rescue_db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_USERS = "users";
    private static final String TABLE_PETS = "pets";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_USERNAME = "username";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_PASSWORD = "password";
    private static final String COLUMN_PET_NAME = "pet_name";
    private static final String COLUMN_PET_TYPE = "pet_type";
    private static final String COLUMN_PET_AGE = "pet_age";
    private static final String COLUMN_PET_GENDER = "pet_gender";
    private static final String COLUMN_PET_PHOTO = "pet_photo";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USERS +
                "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_USERNAME + " TEXT," +
                COLUMN_EMAIL + " TEXT," +
                COLUMN_PASSWORD + " TEXT" +
                ")";
        db.execSQL(CREATE_USERS_TABLE);

        String CREATE_PETS_TABLE = "CREATE TABLE " + TABLE_PETS +
                "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_USERNAME + " TEXT," +
                COLUMN_PET_NAME + " TEXT," +
                COLUMN_PET_TYPE + " TEXT," +
                COLUMN_PET_AGE + " INTEGER," +
                COLUMN_PET_GENDER + " TEXT," +
                COLUMN_PET_PHOTO + " TEXT" +
                ")";
        db.execSQL(CREATE_PETS_TABLE);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PETS);
        onCreate(db);
    }
    public void addUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USERNAME, user.getUsername());
        values.put(COLUMN_EMAIL, user.getEmail());
        values.put(COLUMN_PASSWORD, user.getPassword());
        db.insert(TABLE_USERS, null, values);
        db.close();
    }
    public boolean checkUser(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {COLUMN_ID};
        String selection = COLUMN_USERNAME + " = ?" + " AND " + COLUMN_PASSWORD + " = ?";
        String[] selectionArgs = {username, password};

        Cursor cursor = db.query(TABLE_USERS, columns, selection, selectionArgs,
                null, null, null);

        int cursorCount = cursor.getCount();
        cursor.close();
        db.close();

        return cursorCount > 0;
    }
    public void deleteUser(String username) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_USERS, COLUMN_USERNAME + " = ?", new String[]{username});
        db.close();
    }
    public void addPet(Pet pet, String username) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USERNAME, username);
        values.put(COLUMN_PET_NAME, pet.getName());
        values.put(COLUMN_PET_TYPE, pet.getType());
        values.put(COLUMN_PET_AGE, pet.getAge());
        values.put(COLUMN_PET_GENDER, pet.getGender());
        values.put(COLUMN_PET_PHOTO, pet.getPhoto());
        db.insert(TABLE_PETS, null, values);
        db.close();
    }
    public boolean checkPet(String username, String petName) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {COLUMN_ID};
        String selection = COLUMN_USERNAME + " = ?" + " AND " + COLUMN_PET_NAME + " = ?";
        String[] selectionArgs = {username, petName};

        Cursor cursor = db.query(TABLE_PETS, columns, selection, selectionArgs,
                null, null, null);

        int cursorCount = cursor.getCount();
        cursor.close();
        db.close();

        return cursorCount > 0;
    }
    public void deletePet(String username, String petName) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_PETS, COLUMN_USERNAME + " = ?" + " AND " + COLUMN_PET_NAME + " = ?",
                new String[]{username, petName});
        db.close();
    }
}
