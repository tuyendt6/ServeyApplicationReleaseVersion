package com.samsung.table;

import android.database.sqlite.SQLiteDatabase;

/**
 * Created by SamSunger on 5/13/2015.
 */
public class tblVendedores {
    public static final String TBL_NAME = "tbl_Vendedores";
    public static final String _ID = "id";//1
    public static final String PK_ID = "PkID";//2
    public static final String NOMBRE_COMPETO = "NombreCompleto";//3
    public static final String NOMBRE_USUARIO = "NombreUsuario";//4
    public static final String CLAVE = "Clave";//5
    public static final String DERECCION = "Direccion";//6
    public static final String DOC_IDENT = "DocIdent";//7
    public static final String TELEFONOS = "Telefonos";//8
    public static final String ACTIVO = "Activo";//9
    public static final String Email1="Email1";
    public static final String Email2="Email2";
    private static String createData() {
        StringBuilder sBuiler = new StringBuilder();
        sBuiler.append("create table " + TBL_NAME + " (");
        sBuiler.append(_ID + " integer primary key autoincrement, ");//1
        sBuiler.append(PK_ID + " text, ");//2
        sBuiler.append(NOMBRE_COMPETO + " text, ");//3
        sBuiler.append(NOMBRE_USUARIO + " text, ");//4
        sBuiler.append(CLAVE + " text, ");//5
        sBuiler.append(DERECCION + " text, ");//6
        sBuiler.append(DOC_IDENT + " text, ");//7
        sBuiler.append(TELEFONOS + " text, ");   //8
        sBuiler.append(Email1 + " text, ");//7
        sBuiler.append(Email2 + " text, ");   //8
        sBuiler.append(ACTIVO + " text);");//9
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
