package com.example.project_mobile.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.project_mobile.models.Hike;
import com.example.project_mobile.models.Observation;

public class MyDatabaseHelper extends SQLiteOpenHelper {
    private Context context;
    private static final String DATABASE_NAME = "Coursework.db";
    private static final String TABLE_HIKE = "hikes";
    private static final String TABLE_OBSERVATION = "observations";
    public MyDatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
        this.context = context;
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_HIKE + "(hike_id INTEGER primary key autoincrement, " +
                "hike_name TEXT NOT NULL, " +
                "hike_location TEXT NOT NULL, " +
                "date_hike TEXT NOT NULL, " +
                "parking_available TEXT NOT NULL, " +
                "hike_length INTEGER NOT NULL," +
                "hike_level TEXT NOT NULL," +
                "hike_description TEXT NOT NULL)");
        db.execSQL("CREATE TABLE " + TABLE_OBSERVATION + "(observation_id INTEGER primary key autoincrement," +
                "hike_id INTEGER NOT NULL," +
                "observation_name TEXT NOT NULL," +
                "date_time TEXT NOT NULL," +
                "comments TEXT NOT NULL," +
                "foreign key (hike_id) references " + TABLE_HIKE + "(hike_id))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_HIKE);
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_OBSERVATION);
        onCreate(db);
    }

    public void addNewHike(Hike hike){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("hike_name", hike.getHike_name());
        cv.put("hike_location", hike.getLocation_hike());
        cv.put("date_hike",hike.getDate_hike());
        cv.put("parking_available",hike.getParking_available());
        cv.put("hike_length",hike.getHike_length());
        cv.put("hike_level",hike.getHike_level());
        cv.put("hike_description",hike.getHike_description());
        long result = db.insert(TABLE_HIKE,null,cv);
        if(result == -1){
            Toast.makeText(context, "Failed to Add", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(context, "Successfully to Add", Toast.LENGTH_SHORT).show();
        }
    }

    public Cursor readAllHikes(){
        String query ="SELECT hike_id, hike_name, hike_location, date_hike, parking_available, hike_length, hike_level, hike_description FROM "+ TABLE_HIKE;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = null;
        if(db != null){
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }

    public void updateHike(Hike hike){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("hike_name", hike.getHike_name());
        cv.put("hike_location", hike.getLocation_hike());
        cv.put("date_hike",hike.getDate_hike());
        cv.put("parking_available",hike.getParking_available());
        cv.put("hike_length",hike.getHike_length());
        cv.put("hike_level",hike.getHike_level());
        cv.put("hike_description",hike.getHike_description());
        long result = db.update(TABLE_HIKE, cv, "hike_id=?",new String[]{String.valueOf(hike.getHike_id())});
        if(result == -1){
            Toast.makeText(context, "Failed to Update.", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(context, "Successfully to Update", Toast.LENGTH_SHORT).show();
        }
    }

    public void deleteOneRow(String row_id){
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.delete(TABLE_HIKE, "hike_id=?",new String[]{row_id});
        if(result == -1){
            Toast.makeText(context, "Failed to Delete", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(context, "Successfully to Delete", Toast.LENGTH_SHORT).show();
        }
    }

    public void deleteAll(){
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.delete(TABLE_HIKE, null,null);
        if(result == -1){
            Toast.makeText(context, "Failed to Clear", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(context, "Successfully to Clear", Toast.LENGTH_SHORT).show();
        }
    }

    public void addNewObservation(Observation observation){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("observation_name", observation.getObs_name());
        cv.put("date_time", observation.getObs_time());
        cv.put("comments",observation.getObs_comment());
        cv.put("hike_id",observation.getHike_id());
        long result = db.insert(TABLE_OBSERVATION,null,cv);
        if(result == -1){
            Toast.makeText(context, "Failed to Add", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(context, "Successfully to Add", Toast.LENGTH_SHORT).show();
        }
    }

    public Cursor readAllObservation(String hikeId){
        String query ="SELECT observation_id, hike_id, observation_name, date_time, comments  FROM "+ TABLE_OBSERVATION + " WHERE hike_id = ?";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = null;
        if(db != null){
            cursor = db.rawQuery(query, new String[]{hikeId});
        }
        return cursor;
    }

    public void updateObservation(Observation observation){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("observation_name", observation.getObs_name());
        cv.put("date_time", observation.getObs_time());
        cv.put("comments",observation.getObs_comment());
        long result = db.update(TABLE_OBSERVATION, cv, "hike_id=? and observation_id=?",new String[]{String.valueOf(observation.getHike_id()), String.valueOf(observation.getObs_id())});
        if(result == -1){
            Toast.makeText(context, "Failed to Update.", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(context, "Successfully to Update", Toast.LENGTH_SHORT).show();
        }
    }

    public void deleteOneRowObs(String ob_id){
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.delete(TABLE_OBSERVATION, "hike_id=?",new String[]{ob_id});
        if(result == -1){
            Toast.makeText(context, "Failed to Delete", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(context, "Successfully to Delete", Toast.LENGTH_SHORT).show();
        }
    }
}
