package com.example.tagalong;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class User extends SQLiteOpenHelper {

    public User(Context context)
    {
        super(context, "user.db", null, 1);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE User" + "(" +
                "userid INTEGER PRIMARY KEY, first_name TEXT, last_name TEXT, email_address TEXT, age INTEGER, rating TEXT, filters TEXT, description TEXT, average_comment_value TEXT"
                +")");

        db.execSQL("CREATE TABLE User" + "(" +
                "paymentid INTEGER PRIMARY KEY, senderID TEXT, rcieverID TEXT, amount TEXT, reason INTEGER, status TEXT, dateCompleted TEXT"
                +")");

        db.execSQL("CREATE TABLE Bookings" + "(" +
                "bookingid INTEGER PRIMARY KEY, senderID TEXT, rcieverID TEXT, amount TEXT, reason INTEGER, status TEXT, dateCompleted TEXT"
                +")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
