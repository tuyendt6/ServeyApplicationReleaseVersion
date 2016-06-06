package com.samsung.table;

import android.database.sqlite.SQLiteDatabase;

/**
 * Created by SamSunger on 5/13/2015.
 */
public class tblSuperAdministrador {

    public static final String TBL_NAME = "tbl_SuperAdministrador";
    public static final String _ID = "id";//1
    public static final String SUPER_ADMINISTRADOR_ID = "SuperAdministradorId";//2
    public static final String NOMBRE = "Nombre";//3
    public static final String APELLIDO = "apellido";//4
    public static final String DIRECCION = "direccion";//5
    public static final String TELEFONO = "telefono";//6
    public static final String EMAIL = "email";//7
    public static final String CLAVE = "Clave";//8
    public static final String NOMBRE_USUARIO = "NombreUsuario";//9
    private static String createData() {
        StringBuilder sBuiler = new StringBuilder();
        sBuiler.append("create table " + TBL_NAME + " (");
        sBuiler.append(_ID + " integer primary key autoincrement, ");//1
        sBuiler.append(SUPER_ADMINISTRADOR_ID + " text, ");//2
        sBuiler.append(NOMBRE + " text, ");//3
        sBuiler.append(APELLIDO + " text, ");//4
        sBuiler.append(DIRECCION + " text, ");//5
        sBuiler.append(TELEFONO + " text, ");//6
        sBuiler.append(EMAIL + " text, ");//7
        sBuiler.append(CLAVE + " text, ");   //8
        sBuiler.append(NOMBRE_USUARIO + " text);");//9
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
