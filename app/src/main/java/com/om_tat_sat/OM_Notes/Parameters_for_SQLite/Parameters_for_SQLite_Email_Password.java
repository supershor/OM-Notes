package com.om_tat_sat.OM_Notes.Parameters_for_SQLite;

public class Parameters_for_SQLite_Email_Password {
    //Here I am trying to make a table in SQLite for storing email and password of users and I will be tracking it using sqlite

    public static final String name = "email_password.db";
    public static final int version = 1;

    public static final String table_name = "email_password";
    public static final String column_email = "email";
    public static final String column_password = "password";
}
