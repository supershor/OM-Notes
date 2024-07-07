package com.om_tat_sat.OM_Notes.Parameters_for_SQLite;

public class Parameters_for_SQLite_notes_and_description {
    //here I am trying to make a table in SQLite for storing notes and description of users and I will be tracking it using sqlite

    public static final String name = "notes_and_description.db";
    public static final int version = 1;

    public static final String table_name = "notes_and_description";
    public static final String column_title = "title";
    public static final String column_id = "id";
    public static final String column_description = "description";
}
