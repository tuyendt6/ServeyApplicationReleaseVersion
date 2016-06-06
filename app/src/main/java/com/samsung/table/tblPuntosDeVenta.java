package com.samsung.table;

import android.database.sqlite.SQLiteDatabase;

/**
 * Created by SamSunger on 5/13/2015.
 */
public class tblPuntosDeVenta {
    public static final String TBL_NAME = "tbl_PuntosDeVenta";
    public static final String _ID = "id";//1
    public static final String PK_ID = "PkID";//2
    public static final String FACT_DEFT_ID = "FactDeptID";//3
    public static final String NOMBRE = "Nombre";//4
    public static final String DIRECCION = "Direccion";//5
    public static final String PROVINCIAID = "ProvinciaID";//6
    public static final String DISTRITOID = "DistritoID";//7
    public static final String CORREGIMIENTOID = "CorregimientoID";//4

    public static final String ZONAID = "ZonaID";//8
    public static final String EMAIL1 = "Email1";//8
    public static final String DESCCION_ADICIONALES = "DireccionesAdicionales";//9
    public static final String TELEFONO = "Telefonos";//6
    public static final String ACTIVO = "Activo";//7
    public static final String POSION_LAT = "PosicionLat";//8
    public static final String ISYS = "is_sys";//8
    public static final String POSION_LON = "PosicionLon";//9
    private static String createData() {
        StringBuilder sBuiler = new StringBuilder();
        sBuiler.append("create table " + TBL_NAME + " (");
        sBuiler.append(_ID + " integer primary key autoincrement, ");//1
        sBuiler.append(PK_ID + " text, ");//2
        sBuiler.append(FACT_DEFT_ID + " text, ");//3
        sBuiler.append(NOMBRE + " text, ");//4
        sBuiler.append(DIRECCION + " text, ");//5
        sBuiler.append(PROVINCIAID + " text, ");//6
        sBuiler.append(CORREGIMIENTOID + " text, ");//6
        sBuiler.append(DISTRITOID + " text, ");//7
        sBuiler.append(ZONAID + " text, ");   //8
        sBuiler.append(DESCCION_ADICIONALES + " text, ");//5
        sBuiler.append(TELEFONO + " text, ");//6
        sBuiler.append(EMAIL1 + " text, ");//6
        sBuiler.append(ACTIVO + " text, ");//7
        sBuiler.append(ISYS + " text, ");//7
        sBuiler.append(POSION_LAT + " text, ");   //8
        sBuiler.append(POSION_LON + " text);");//9
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
