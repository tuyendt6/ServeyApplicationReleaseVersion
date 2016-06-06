package com.samsung.table;

import android.database.sqlite.SQLiteDatabase;

/**
 * Created by SamSunger on 5/13/2015.
 */
public class tblDistritos {

    public static final String TBL_NAME = "tbl_Distritos";
    public static final String _ID = "id";//1
    public static final String PK_ID = "PkID";//2
    public static final String PROVINCIA_ID = "ProvinciaID";//3
    public static final String NOMBRE = "Nombre";//4

    private static String createData() {
        StringBuilder sBuiler = new StringBuilder();
        sBuiler.append("create table " + TBL_NAME + " (");
        sBuiler.append(_ID + " integer primary key autoincrement, ");//1
        sBuiler.append(PK_ID + " text, ");//2
        sBuiler.append(PROVINCIA_ID + " text, ");//3
        sBuiler.append(NOMBRE + " text);");//4
        return sBuiler.toString();
    }

    public static void onCreate(SQLiteDatabase database) {
        database.execSQL(createData());
    }

    public static void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        database.execSQL("DROP TABLE IF EXISTS " + TBL_NAME);
        onCreate(database);
    }
}
