package com.example.myapplication;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
public class AdminSQLiteOpenHelper extends SQLiteOpenHelper {
    public AdminSQLiteOpenHelper(Context context, String nombreBaseDatos,
                                 SQLiteDatabase.CursorFactory factory, int version) {
        super(context, nombreBaseDatos, factory, version);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        //aquí creamos la tabla de usuario (dni, nombre, ciudad, numero)
        db.execSQL("create table usuario(dni integer primary key, nombre text, ciudad text, numero integer)");
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int version1, int version2) {

    }
}