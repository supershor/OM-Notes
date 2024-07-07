package com.om_tat_sat.OM_Notes.SQLite_Helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;


import com.om_tat_sat.OM_Notes.DataHolders.Notes_title_description_id_holder;
import com.om_tat_sat.OM_Notes.Parameters_for_SQLite.Parameters_for_SQLite_Email_Password;
import com.om_tat_sat.OM_Notes.Parameters_for_SQLite.Parameters_for_SQLite_notes_and_description;

import java.util.ArrayList;

public class Notes_description_holders_using_SQLite extends SQLiteOpenHelper {
    public Notes_description_holders_using_SQLite(@Nullable Context context) {
        super(context, Parameters_for_SQLite_notes_and_description.name, null, Parameters_for_SQLite_notes_and_description.version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        //Creating a sql table to save notes of users with columns Email, unique id, notes, and its descriptions
        //unique id is INTEGER PRIMARY KEY AUTOINCREMENT for better reliability in sql tables and so that notes with same title can be made again and again

        String create="CREATE TABLE "+Parameters_for_SQLite_notes_and_description.table_name+" ( "+ Parameters_for_SQLite_Email_Password.column_email+" Text," + Parameters_for_SQLite_notes_and_description.column_id +" INTEGER PRIMARY KEY AUTOINCREMENT," +Parameters_for_SQLite_notes_and_description.column_title+" Text,"+Parameters_for_SQLite_notes_and_description.column_description+" Text )";
        db.execSQL(create);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void add_new_note(String title,String description,String email){

        //Here I am trying to save the data in table when user add a new note the parameters used are email, notes title and notes description
        //The unique id will get auto incremented

        SQLiteDatabase database=getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(Parameters_for_SQLite_Email_Password.column_email,email);
        contentValues.put(Parameters_for_SQLite_notes_and_description.column_title,title);
        contentValues.put(Parameters_for_SQLite_notes_and_description.column_description,description);
        database.insert(Parameters_for_SQLite_notes_and_description.table_name,null,contentValues);
    }

    public void update_note(String new_title, String new_description, String email, Integer id){

        //Here I am trying to update the data in table when user changes notes title and notes description
        //The unique id will be used here as where query

        SQLiteDatabase database=getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(Parameters_for_SQLite_Email_Password.column_email,email);
        contentValues.put(Parameters_for_SQLite_notes_and_description.column_title,new_title);
        contentValues.put(Parameters_for_SQLite_notes_and_description.column_description,new_description);
        String where_query="WHERE "+Parameters_for_SQLite_notes_and_description.column_id+" = "+id;
        database.update(Parameters_for_SQLite_notes_and_description.table_name,contentValues,where_query,null);
    }
    public void delete_note(Integer id){

        //Here I am trying to delete the data in table when user taps on delete button
        //The unique id will be used here as where query

        SQLiteDatabase database=getWritableDatabase();
        String where_query="WHERE "+Parameters_for_SQLite_notes_and_description.column_id+" = "+id;
        database.delete(Parameters_for_SQLite_notes_and_description.table_name,where_query,null);
    }

    public ArrayList<Notes_title_description_id_holder> fetch_note(String email){

        //Here I am fetching all the data's for current user
        //The email id address will be used here as where query
        //I will also be returning a arraylist of notes and description for recyclerview

        ArrayList<Notes_title_description_id_holder>notes_description_arraylist=new ArrayList();

        SQLiteDatabase database=getReadableDatabase();
        String where_query="SELECT * FROM "+Parameters_for_SQLite_notes_and_description.table_name+" WHERE "+Parameters_for_SQLite_Email_Password.column_email+" = "+email;
        Cursor cursor=database.rawQuery(where_query,null);
        if (cursor.moveToFirst()){
            do {
                notes_description_arraylist.add(new Notes_title_description_id_holder(cursor.getString(3),Integer.parseInt(cursor.getString(1)),cursor.getString(2)));
            }while (cursor.moveToNext());
        }
        return notes_description_arraylist;
    }
}
