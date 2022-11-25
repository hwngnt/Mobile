package com.example.mobile.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class dbHandler extends SQLiteOpenHelper {

    public static final String DB_NAME = "M_EXPENSE";
    public static final int DB_VERSION = 2;

    public dbHandler(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String sqlCreateExpenseTable = "CREATE TABLE  expense(Id INTEGER PRIMARY KEY AUTOINCREMENT, type text not null , " +
                " amount real not null , date text not null, comment text, tripID int not null, FOREIGN KEY(tripID) REFERENCES trip(Id)) ";
        sqLiteDatabase.execSQL(sqlCreateExpenseTable);

        String sqlCreateTripTable = "CREATE TABLE  trip(Id INTEGER PRIMARY KEY AUTOINCREMENT, name text not null, " +
                " destination text not null , date text not null, risk boolean not null, description text, transportation text not null, participant int not null)";
        sqLiteDatabase.execSQL(sqlCreateTripTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        String sqlDropExpense = "DROP TABLE IF EXISTS expense";
        String sqlDropTrip = "DROP TABLE IF EXISTS trip";
        sqLiteDatabase.execSQL(sqlDropExpense);
        sqLiteDatabase.execSQL(sqlDropTrip);
        onCreate(sqLiteDatabase);
    }
}
