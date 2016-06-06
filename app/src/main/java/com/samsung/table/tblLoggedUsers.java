package com.samsung.table;

import android.database.sqlite.SQLiteDatabase;

/**
 * Created by SamSunger on 5/13/2015.
 */
public class tblLoggedUsers {

    public static final String TBL_NAME = "tbl_LoggedUsers";
    public static final String _ID = "id";//1
    public static final String CREDENTIAL = "Credential";//2
    public static final String VENDEDORID = "VendedorID";//3
    public static final String LOGINTIME = "LoginTime";//4
    private static String createData() {
        StringBuilder sBuiler = new StringBuilder();
        sBuiler.append("create table " + TBL_NAME + " (");
        sBuiler.append(_ID + " integer primary key autoincrement, ");//1
        sBuiler.append(CREDENTIAL + " text, ");//2
        sBuiler.append(VENDEDORID + " text, ");//3
        sBuiler.append(LOGINTIME + " text);");//4
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
