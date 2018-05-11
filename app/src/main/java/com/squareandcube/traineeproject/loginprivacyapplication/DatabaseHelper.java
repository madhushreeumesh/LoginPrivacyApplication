package com.squareandcube.traineeproject.loginprivacyapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "candidate_db";

    private static final String TABLE_CANDIDATE = "candidate";

    // User Table Columns names
    private static final String COLUMN_CANDIDATE_ID = "candidate_id";
    private static final String COLUMN_FIRST_NAME = "first_name";
    private static final String COLUMN_LAST_NAME = "last_name";
    private static final String COLUMN_DOB = "DOB";
    private static final String COLUMN_EMAIL = "email_id";
    private static final String COLUMN_PASSWORD = "password";
    private static final String COLUMN_PHONENO = "phone_no";
    private static final String COLUMN_CANDIDATE_IMAGE = "candidate_image";

    private String CREATE_USER_TABLE = "CREATE TABLE " + TABLE_CANDIDATE + "("
            + COLUMN_CANDIDATE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_FIRST_NAME + " TEXT,"
            +COLUMN_LAST_NAME+" TEXT,"
            +COLUMN_DOB +" TEXT,"
            + COLUMN_EMAIL + " TEXT NOT NULL UNIQUE," + COLUMN_PASSWORD + " TEXT,"
            +COLUMN_PHONENO+ " INTEGER NOT NULL UNIQUE,"+COLUMN_CANDIDATE_IMAGE+" BLOB"+")";


    private String DROP_USER_TABLE = "DROP TABLE IF EXISTS " + TABLE_CANDIDATE;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(CREATE_USER_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL(DROP_USER_TABLE);

        // Create tables again
        onCreate(db);
    }

    public void addUser(CondidateRegisterModel user) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_FIRST_NAME, user.getFirstName());
        values.put(COLUMN_LAST_NAME, user.getLastName());
        values.put(COLUMN_DOB, user.getDOB());
        values.put(COLUMN_EMAIL, user.getEmail());
        values.put(COLUMN_PASSWORD, user.getPassword());
        values.put(COLUMN_PHONENO, user.getPhoneNo());
        values.put(COLUMN_CANDIDATE_IMAGE, user.getImage());

        // Inserting Row
        db.insert(TABLE_CANDIDATE, null, values);
        db.close();
    }

    public void updateUser(CondidateRegisterModel user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_CANDIDATE_ID,user.getUser_Id());
        values.put(COLUMN_FIRST_NAME, user.getFirstName());
        values.put(COLUMN_LAST_NAME, user.getLastName());
        values.put(COLUMN_DOB, user.getDOB());
        values.put(COLUMN_EMAIL, user.getEmail());
        values.put(COLUMN_PHONENO,user.getPhoneNo());
        values.put(COLUMN_CANDIDATE_IMAGE,user.getImage());
//        values.put(COLUMN_PASSWORD, user.getPassword());

        // updating row
        db.update(TABLE_CANDIDATE, values, COLUMN_CANDIDATE_ID + " = ?",
                new String[]{String.valueOf(user.getUser_Id())});
        db.close();
    }
    public List<CondidateRegisterModel> getUserDetail(String email)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        List<CondidateRegisterModel> userList = new ArrayList<CondidateRegisterModel>();
      Cursor cursor=  db.rawQuery("SELECT * FROM "+TABLE_CANDIDATE+" WHERE "+COLUMN_EMAIL+" ='"+email+"'",null);

        if (cursor.moveToFirst()) {
            do {
                CondidateRegisterModel user = new CondidateRegisterModel();
                user.setUser_Id(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_CANDIDATE_ID))));
                user.setFirstName(cursor.getString(cursor.getColumnIndex(COLUMN_FIRST_NAME)));
                user.setLastName(cursor.getString(cursor.getColumnIndex(COLUMN_LAST_NAME)));
                user.setDOB(cursor.getString(cursor.getColumnIndex(COLUMN_DOB)));
                user.setEmail(cursor.getString(cursor.getColumnIndex(COLUMN_EMAIL)));
                user.setPassword(cursor.getString(cursor.getColumnIndex(COLUMN_PASSWORD)));
                user.setPhoneNo(cursor.getString(cursor.getColumnIndex(COLUMN_PHONENO)));
                user.setImage(cursor.getString(cursor.getColumnIndex(COLUMN_CANDIDATE_IMAGE)));
                // Adding user record to list
                userList.add(user);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return userList;

    }
    public List<CondidateRegisterModel> getAllUser() {
        // array of columns to fetch
        String[] columns = {
                COLUMN_CANDIDATE_ID,
                COLUMN_EMAIL,
                COLUMN_FIRST_NAME,
                COLUMN_LAST_NAME,
                COLUMN_DOB,
                COLUMN_PHONENO,
                COLUMN_PASSWORD,
                COLUMN_CANDIDATE_IMAGE
        };
        // sorting orders
        String sortOrder =
                COLUMN_FIRST_NAME +" ASC";
        List<CondidateRegisterModel> userList = new ArrayList<CondidateRegisterModel>();

        SQLiteDatabase db = this.getReadableDatabase();

        // query the user table
        /**
         * Here query function is used to fetch records from user table this function works like we use sql query.
         * SQL query equivalent to this query function is
         * SELECT user_id,user_name,user_email,user_password FROM user ORDER BY user_name;
         */
        Cursor cursor = db.query(TABLE_CANDIDATE, //Table to query
                columns,    //columns to return
                null,        //columns for the WHERE clause
                null,        //The values for the WHERE clause
                null,       //group the rows
                null,       //filter by row groups
                sortOrder); //The sort order


        // Traversing through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                CondidateRegisterModel user = new CondidateRegisterModel();
                user.setUser_Id(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_CANDIDATE_ID))));
                user.setFirstName(cursor.getString(cursor.getColumnIndex(COLUMN_FIRST_NAME)));
                user.setLastName(cursor.getString(cursor.getColumnIndex(COLUMN_LAST_NAME)));
                user.setDOB(cursor.getString(cursor.getColumnIndex(COLUMN_DOB)));
                user.setEmail(cursor.getString(cursor.getColumnIndex(COLUMN_EMAIL)));
                user.setPassword(cursor.getString(cursor.getColumnIndex(COLUMN_PASSWORD)));
                user.setPhoneNo(cursor.getString(cursor.getColumnIndex(COLUMN_PHONENO)));
                user.setImage(cursor.getString(cursor.getColumnIndex(COLUMN_CANDIDATE_IMAGE)));
                // Adding user record to list
                userList.add(user);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        // return user list
        return userList;
    }

    public boolean checkUser(String email) {

        // array of columns to fetch
        String[] columns = {
                COLUMN_CANDIDATE_ID
        };
        SQLiteDatabase db = this.getReadableDatabase();

        // selection criteria
        String selection = COLUMN_EMAIL + " = ?";

        // selection argument
        String[] selectionArgs = {email};

        // query user table with condition
        /**
         * Here query function is used to fetch records from user table this function works like we use sql query.
         * SQL query equivalent to this query function is
         * SELECT user_id FROM user WHERE user_email = 'jack@androidtutorialshub.com';
         */
        Cursor cursor = db.query(TABLE_CANDIDATE, //Table to query
                columns,                    //columns to return
                selection,                  //columns for the WHERE clause
                selectionArgs,              //The values for the WHERE clause
                null,                       //group the rows
                null,                      //filter by row groups
                null);                      //The sort order
        int cursorCount = cursor.getCount();
        cursor.close();
        db.close();

        if (cursorCount > 0)
        {
            return true;
        }

        return false;
    }

    /**
     * This method to check user exist or not
     *
     * @param email
     * @param password
     * @return true/false
     */
    public boolean checkUser(String email, String password) {

        // array of columns to fetch
        String[] columns = {
                COLUMN_CANDIDATE_ID
        };
        SQLiteDatabase db = this.getReadableDatabase();
        // selection criteria
        String selection = COLUMN_EMAIL + " = ?" + " AND " + COLUMN_PASSWORD + " = ?";

        // selection arguments
        String[] selectionArgs = {email, password};

        // query user table with conditions
        /**
         * Here query function is used to fetch records from user table this function works like we use sql query.
         * SQL query equivalent to this query function is
         * SELECT user_id FROM user WHERE user_email = 'jack@androidtutorialshub.com' AND user_password = 'qwerty';
         */
        Cursor cursor = db.query(TABLE_CANDIDATE, //Table to query
                columns,                    //columns to return
                selection,                  //columns for the WHERE clause
                selectionArgs,              //The values for the WHERE clause
                null,                       //group the rows
                null,                       //filter by row groups
                null);                      //The sort order

        int cursorCount = cursor.getCount();

        cursor.close();
        db.close();
        if (cursorCount > 0) {
            return true;
        }

        return false;
    }
}
