package com.example.mobile.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.lifecycle.MutableLiveData;

import com.example.mobile.Constant;
import com.example.mobile.model.TripEntity;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TripDatabaseAssessObject {
    private SQLiteDatabase db;
    private SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy HH:mm:ss aaa");

    public MutableLiveData<List<TripEntity>> tripList;
    public MutableLiveData<TripEntity> trip;


    public TripDatabaseAssessObject(Context context) {
        dbHandler database = new dbHandler(context);
        this.db = database.getWritableDatabase();

        tripList = new MutableLiveData<List<TripEntity>>();
        {
            getTrips();
        }
        trip = new MutableLiveData<TripEntity>();
    }

    public void getTrips(){
        tripList.setValue(getAll());
    }

    public List<TripEntity> get(String sql, String ...selectArgs){
        List<TripEntity> list = new ArrayList<>();
        Cursor cursor = db.rawQuery(sql, selectArgs);
        while (cursor.moveToNext()){
            TripEntity trip = new TripEntity();
            trip.setId(cursor.getInt(cursor.getColumnIndexOrThrow("Id")));
            trip.setName(cursor.getString(cursor.getColumnIndexOrThrow("name")));
            trip.setDestination(cursor.getString(cursor.getColumnIndexOrThrow("destination")));
            Date date = sdf.parse(cursor.getString(cursor.getColumnIndexOrThrow("date")), new ParsePosition(0));
            trip.setDate(date);
            trip.setRisk(cursor.getInt(cursor.getColumnIndexOrThrow("risk")));
            trip.setDescription(cursor.getString(cursor.getColumnIndexOrThrow("description")));

            list.add(trip);
        }
        return list;
    }

    public List<TripEntity> getAll(){
        String sql = "SELECT * FROM trip";
        return get(sql);
    }

    public TripEntity getById(String id){
        String sql = "SELECT * FROM trip WHERE id=?";

        List<TripEntity> list = get(sql, id);
        trip.setValue(list.get(0));
        return list.get(0);
    }

    public void getTripByID(String id){
        if(id == null || id.equals(String.valueOf(Constant.NEW_ENTITY_ID))){
            trip.setValue(new TripEntity());
            return;
        }
        getById(id);
    }

    public long insert(TripEntity trip){
        ContentValues values = new ContentValues();
        values.put("name", trip.getName());
        values.put("destination", trip.getDestination());
        values.put("date", sdf.format(trip.getDate()));
        values.put("risk", trip.isRisk());
        values.put("description", trip.getDescription());

        return db.insert("trip", null, values);
    }

    public long update(TripEntity trip){
        ContentValues values = new ContentValues();
        values.put("name", trip.getName());
        values.put("destination", trip.getDestination());
        values.put("date", sdf.format(trip.getDate()));
        values.put("risk", trip.isRisk());
        values.put("description", trip.getDescription());

        return db.update("trip", values, "id=?", new String[]{String.valueOf(trip.getId())});
    }

    public int delete(String id){
        return db.delete("trip", "id=?", new String[]{id});
    }

    public MutableLiveData<List<TripEntity>> getList(){
        return tripList;
    }

    public void search(String newText) {
        String query = "Select * from trip WHERE name LIKE '%" + newText + "%' OR destination LIKE '%" + newText + "%'";
        tripList.setValue(get(query));
    }
}
