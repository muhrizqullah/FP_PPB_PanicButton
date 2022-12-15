package com.muhrizqullah.listcontact;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class ContactRepository extends SQLiteOpenHelper {
    public ContactRepository(@Nullable Context context) {
        super(context, DbConfig.DB_NAME, null, DbConfig.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(DbConfig.CREATE_CONTACT_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        // upgrade table if there is any db change
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DbConfig.CONTACT_TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    public long insert(Contact contact) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DbConfig.CONTACT_NAME_COL, contact.getName());
        contentValues.put(DbConfig.CONTACT_PHONE_COL, contact.getPhone());
        long id = db.insert(DbConfig.CONTACT_TABLE_NAME, null, contentValues);
        db.close();
        return id;
    }

    public ArrayList<Contact> getAll() {
        ArrayList<Contact> contacts = new ArrayList<Contact>();
        String query = "SELECT * FROM " + DbConfig.CONTACT_TABLE_NAME;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if(cursor.moveToFirst()) {
            do {
                Contact contact = new Contact(
                        Integer.toString(cursor.getInt(cursor.getColumnIndexOrThrow(DbConfig.CONTACT_ID_COL))),
                        cursor.getString(cursor.getColumnIndexOrThrow(DbConfig.CONTACT_NAME_COL)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DbConfig.CONTACT_PHONE_COL))
                );
                contacts.add(contact);
            } while(cursor.moveToNext());
        }
        db.close();
        return contacts;
    }

    public Contact getById(String id) {
        String query = "SELECT * FROM " + DbConfig.CONTACT_TABLE_NAME + " WHERE " + DbConfig.CONTACT_ID_COL + "=\"" + id + "\"";
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        Contact contact = new Contact("", "", "");
        if(cursor.moveToFirst()) {
            do {
                contact = new Contact(
                        Integer.toString(cursor.getInt(cursor.getColumnIndexOrThrow(DbConfig.CONTACT_ID_COL))),
                        cursor.getString(cursor.getColumnIndexOrThrow(DbConfig.CONTACT_NAME_COL)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DbConfig.CONTACT_PHONE_COL))
                );
            } while(cursor.moveToNext());
        }
        db.close();
        return contact;
    }

    public void update(String id, String name, String phone) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DbConfig.CONTACT_NAME_COL, name);
        contentValues.put(DbConfig.CONTACT_PHONE_COL, phone);
        db.update(DbConfig.CONTACT_TABLE_NAME, contentValues, DbConfig.CONTACT_ID_COL + "=?", new String[]{id});
        db.close();
    }

    public void delete(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(DbConfig.CONTACT_TABLE_NAME, DbConfig.CONTACT_ID_COL + "=?", new String[]{id});
        db.close();
    }

    public ArrayList<Contact> getByName(String term) {
        ArrayList<Contact> contacts = new ArrayList<Contact>();
        String query = "SELECT * FROM " + DbConfig.CONTACT_TABLE_NAME + " WHERE " + DbConfig.CONTACT_NAME_COL + " LIKE '%" + term + "%'";
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if(cursor.moveToFirst()) {
            do {
                Contact contact = new Contact(
                        Integer.toString(cursor.getInt(cursor.getColumnIndexOrThrow(DbConfig.CONTACT_ID_COL))),
                        cursor.getString(cursor.getColumnIndexOrThrow(DbConfig.CONTACT_NAME_COL)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DbConfig.CONTACT_PHONE_COL))
                );
                contacts.add(contact);
            } while(cursor.moveToNext());
        }
        db.close();
        return contacts;
    }
}
