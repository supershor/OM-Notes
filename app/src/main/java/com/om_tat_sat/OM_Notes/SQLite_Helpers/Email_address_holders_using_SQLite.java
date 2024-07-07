package com.om_tat_sat.OM_Notes.SQLite_Helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.om_tat_sat.OM_Notes.Parameters_for_SQLite.Parameters_for_SQLite_Email_Password;


public class Email_address_holders_using_SQLite extends SQLiteOpenHelper {

    //here I am trying to make a table in SQLite for storing email and password of users and I will be tracking it using sqlite

    public Email_address_holders_using_SQLite(@Nullable Context context) {
        super(context, Parameters_for_SQLite_Email_Password.name, null, Parameters_for_SQLite_Email_Password.version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        //Creating a table named email_password.db with table name email_password and columns email and password and version 1

        String create="CREATE TABLE "+Parameters_for_SQLite_Email_Password.table_name+" ("+Parameters_for_SQLite_Email_Password.column_email+" Text PRIMARY KEY, "+Parameters_for_SQLite_Email_Password.column_password+" Text)";
        db.execSQL(create);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public String login_try(String email, String password){

        //Here I am trying to check if the email and password is correct or not and if user does not Exists then it will return false
        //And will try making him signup using the email and password if user does not exist
        //We are also checking passwords here

        SQLiteDatabase database=getReadableDatabase();
        String query="SELECT * FROM "+Parameters_for_SQLite_Email_Password.table_name +" WHERE "+Parameters_for_SQLite_Email_Password.column_email+" = '"+email+"'";
        Cursor cursor=database.rawQuery(query,null);
        if (cursor.moveToFirst()){
            Log.e("login_try:-----------",cursor.getString(0)+"_-_"+cursor.getString(1));
            if (cursor.getString(1).equals(password)){
                return "Login Successful";
            }else {
                return "Wrong Password";
            }
        }else {
            if (signup_try(email,password)){
                return "Signup Successful";
            }
            else {
                return "User Does Not Exists";
            }
        }


    }
    public boolean signup_try(String email, String password){

        //Here I am trying to signup the user using the email and password
        //Since every email is unique a new data will be created in the table

        SQLiteDatabase database=getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(Parameters_for_SQLite_Email_Password.column_email,email);
        contentValues.put(Parameters_for_SQLite_Email_Password.column_password,password);
        database.insert(Parameters_for_SQLite_Email_Password.table_name,null,contentValues);
        database.close();

        return true;
    }
}
