package com.muhrizqullah.listcontact;

public class DbConfig {
    public static final String DB_NAME = "contact_db";
    public static final int DB_VERSION = 1;

    public static final String CONTACT_TABLE_NAME = "contact";
    public static final String CONTACT_ID_COL = "id";
    public static final String CONTACT_NAME_COL = "name";
    public static final String CONTACT_PHONE_COL = "phone";

    public static final String CREATE_CONTACT_TABLE = "CREATE TABLE IF NOT EXISTS " + CONTACT_TABLE_NAME + "( "
            + CONTACT_ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + CONTACT_NAME_COL + " TEXT, "
            + CONTACT_PHONE_COL + " TEXT"
            + ");";
}