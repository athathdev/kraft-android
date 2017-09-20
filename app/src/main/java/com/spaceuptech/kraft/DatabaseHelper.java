package com.spaceuptech.kraft;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_LIKED_POSTS = "liked_posts";
    private static final String TABLE_INTERESTED_EVENTS = "interested_events";

    public DatabaseHelper(Context context) {
        super(context, "kraft", null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String liked_posts = "CREATE TABLE " + TABLE_LIKED_POSTS + "(" +
                "id text," +
                "PRIMARY KEY(id)" +
                ");";

        String interested_events = "CREATE TABLE " + TABLE_INTERESTED_EVENTS + "(" +
                "id text," +
                "PRIMARY KEY(id)" +
                ");";

        db.execSQL(liked_posts);
        db.execSQL(interested_events);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LIKED_POSTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_INTERESTED_EVENTS);
        // Create tables again
        onCreate(db);
    }

    public boolean checkIfPostLiked(String postId){
        boolean liked = false;
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "select id from " + TABLE_LIKED_POSTS + " where id = \"" + postId + "\" " ;
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            liked = true;
        }
        cursor.close();
        db.close();
        return liked;
    }

    public void setPostAsLiked(String postId){
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put("id", postId);
        db.insert(TABLE_LIKED_POSTS, null, values);
        db.close();
    }

    public void setPostAsNotLiked(String postId){
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put("id", postId);
        db.delete(TABLE_LIKED_POSTS, "id = ?", new String[]{postId});
        db.close();
    }

    public boolean checkIfInterestedEvent(String eventId){
        boolean interested = false;
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "select id from " + TABLE_INTERESTED_EVENTS + " where id = \"" + eventId + "\" " ;
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            interested = true;
        }
        cursor.close();
        db.close();
        return interested;
    }

    public void setEventAsInterested(String eventId){
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put("id", eventId);
        db.insert(TABLE_INTERESTED_EVENTS, null, values);
        db.close();
    }

    public void setEventAsNotInterested(String eventId){
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put("id", eventId);
        db.delete(TABLE_INTERESTED_EVENTS, "id = ?", new String[]{eventId});
        db.close();
    }

}
