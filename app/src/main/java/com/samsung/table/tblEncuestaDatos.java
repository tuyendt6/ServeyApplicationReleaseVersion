package com.samsung.table;

import android.database.sqlite.SQLiteDatabase;
/**
 * Created by SamSunger on 5/13/2015.
 */
public class tblEncuestaDatos {
    public static final String TBL_NAME = "tbl_EncuestaDatos";
    public static final String _ID = "id";//1
    public static final String PK_ID = "PkID";//2
    public static final String DISENO_ID = "DisenoID";//3
    public static final String VENDEDOR_ID = "VendedorID";//4
    public static final String PDV_ID = "PDVID";//5
    public static final String SYS = "SYS";//5
    public static final String FECHAHORA_ENCUESTA = "FechaHoraEncuesta";//6
    public static final String POSICIONENCUESTA_LAT = "PosicionEncuestaLat";//7
    public static final String POSICIONENCUESTA_LON = "PosicionEncuestaLon";//8
    public static final String FECHA_HORA_REGISTRO = "FechaHoraRegistro";//9
    public static final String POSICION_REGISTROLAT = "PosicionRegistroLat";//10
    public static final String POSOCION_REGISTRO_LON = "PosicionRegistroLon";//11
    public static final String PREGUNTA_01 = "Pregunta01";//12
    public static final String PREGUNTA_02 = "Pregunta02";//13
    public static final String PREGUNTA_03 = "Pregunta03";//14
    public static final String PREGUNTA_04 = "Pregunta04";//15
    public static final String PREGUNTA_05 = "Pregunta05";//16
    public static final String PREGUNTA_06 = "Pregunta06";//17
    public static final String PREGUNTA_07 = "Pregunta07";//18
    public static final String PREGUNTA_08 = "Pregunta08";//19
    public static final String PREGUNTA_09 = "Pregunta09";//20
    public static final String PREGUNTA_10 = "Pregunta10";//21
    private static String createData() {
        StringBuilder sBuiler = new StringBuilder();
        sBuiler.append("create table " + TBL_NAME + " (");
        sBuiler.append(_ID + " integer primary key autoincrement, ");//1
        sBuiler.append(PK_ID + " text, ");//2
        sBuiler.append(DISENO_ID + " text, ");//3
        sBuiler.append(VENDEDOR_ID + " text, ");//4
        sBuiler.append(PDV_ID + " text, ");//5
        sBuiler.append(SYS + " text, ");//5
        sBuiler.append(FECHAHORA_ENCUESTA + " text, ");//6
        sBuiler.append(POSICIONENCUESTA_LAT + " text, ");//7
        sBuiler.append(POSICIONENCUESTA_LON + " text, ");   //8
        sBuiler.append(FECHA_HORA_REGISTRO + " text, ");//9
        sBuiler.append(POSICION_REGISTROLAT + " text, ");//10
        sBuiler.append(POSOCION_REGISTRO_LON + " text, ");//11
        sBuiler.append(PREGUNTA_01 + " text, ");//12
        sBuiler.append(PREGUNTA_02 + " text, ");//13
        sBuiler.append(PREGUNTA_03 + " text, ");//14
        sBuiler.append(PREGUNTA_04 + " text, ");   //15
        sBuiler.append(PREGUNTA_05 + " text, ");//16
        sBuiler.append(PREGUNTA_06 + " text, ");//17
        sBuiler.append(PREGUNTA_07 + " text, ");//18
        sBuiler.append(PREGUNTA_08 + " text, ");//19
        sBuiler.append(PREGUNTA_09 + " text, ");//20
        sBuiler.append(PREGUNTA_10 + " text);");//21
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
