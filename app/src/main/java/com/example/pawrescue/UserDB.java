package com.example.pawrescue;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class UserDB extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "user_db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_NAME = "users";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_USERNAME = "username";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_PASSWORD = "password";
    private static final String COLUMN_CREDIT_CARD_NUMBER = "credit_card_number";
    private static final String COLUMN_EXPIRATION_DATE = "expiration_date";
    private static final String COLUMN_CVV = "cvv";

    public UserDB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableQuery = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_USERNAME + " TEXT, " +
                COLUMN_EMAIL + " TEXT, " +
                COLUMN_PASSWORD + " TEXT, " +
                COLUMN_CREDIT_CARD_NUMBER + " TEXT, " +
                COLUMN_EXPIRATION_DATE + " TEXT, " +
                COLUMN_CVV + " TEXT)";

        db.execSQL(createTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public long addUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USERNAME, user.getUsername());
        values.put(COLUMN_EMAIL, user.getEmail());
        values.put(COLUMN_PASSWORD, user.getPassword());
        values.put(COLUMN_CREDIT_CARD_NUMBER, user.getCreditCardNumber());
        values.put(COLUMN_EXPIRATION_DATE, user.getExpirationDate());
        values.put(COLUMN_CVV, user.getCvv());

        long result = db.insert(TABLE_NAME, null, values);
        db.close();
        return result;
    }

    public User getUser(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {COLUMN_ID, COLUMN_USERNAME, COLUMN_EMAIL, COLUMN_PASSWORD,
                COLUMN_CREDIT_CARD_NUMBER, COLUMN_EXPIRATION_DATE, COLUMN_CVV};
        String selection = COLUMN_USERNAME + "=?";
        String[] selectionArgs = {username};

        Cursor cursor = db.query(TABLE_NAME, columns, selection, selectionArgs,
                null, null, null, null);

        User user = null;
        if (cursor != null && cursor.moveToFirst()) {
            user = new User();
            int usernameIndex = cursor.getColumnIndex(COLUMN_USERNAME);
            int emailIndex = cursor.getColumnIndex(COLUMN_EMAIL);
            int passwordIndex = cursor.getColumnIndex(COLUMN_PASSWORD);
            int creditCardNumberIndex = cursor.getColumnIndex(COLUMN_CREDIT_CARD_NUMBER);
            int expirationDateIndex = cursor.getColumnIndex(COLUMN_EXPIRATION_DATE);
            int cvvIndex = cursor.getColumnIndex(COLUMN_CVV);

            if (usernameIndex >= 0) {
                user.setUsername(cursor.getString(usernameIndex));
            }

            if (emailIndex >= 0) {
                user.setEmail(cursor.getString(emailIndex));
            }

            if (passwordIndex >= 0) {
                user.setPassword(cursor.getString(passwordIndex));
            }

            if (creditCardNumberIndex >= 0) {
                user.setCreditCardNumber(cursor.getString(creditCardNumberIndex));
            }

            if (expirationDateIndex >= 0) {
                user.setExpirationDate(cursor.getString(expirationDateIndex));
            }

            if (cvvIndex >= 0) {
                user.setCvv(cursor.getString(cvvIndex));
            }

            cursor.close();
        }
        db.close();
        return user;
    }


    public boolean checkUser(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {COLUMN_USERNAME};
        String selection = COLUMN_USERNAME + " = ? AND " + COLUMN_PASSWORD + " = ?";
        String[] selectionArgs = {username, password};

        Cursor cursor = db.query(TABLE_NAME, columns, selection, selectionArgs,
                null, null, null);

        int count = cursor.getCount();
        cursor.close();
        db.close();

        return count > 0;
    }

    public int deleteUser(String username) {
        SQLiteDatabase db = this.getWritableDatabase();
        String whereClause = COLUMN_USERNAME + "=?";
        String[] whereArgs = {username};

        int result = db.delete(TABLE_NAME, whereClause, whereArgs);
        db.close();
        return result;
    }
}
