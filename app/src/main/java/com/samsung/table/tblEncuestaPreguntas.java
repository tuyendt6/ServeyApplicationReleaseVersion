package com.samsung.table;

import android.database.sqlite.SQLiteDatabase;

/**
 * Created by SamSunger on 5/13/2015.
 */
public class tblEncuestaPreguntas {

    public static final String TBL_NAME = "tbl_EncuestaPreguntas";
    public static final String _ID = "id";//1
    public static final String PK_ID = "PkID";//2
    public static final String DISENO_ID = "DisenoID";//3
    public static final String POSICION_COLUMA = "PosicionColumna";//4
    public static final String NOMBRE_COLUMNA = "NombreColumna";//5
    public static final String TEXTO_PREGUNTA = "TextoPregunta";//6
    public static final String Q_TYPE = "QType";//7
    public static final String GRUPO_RESPUESTAS_ID = "GrupoRespuestasID";//6

    private static String createData() {
        StringBuilder sBuiler = new StringBuilder();
        sBuiler.append("create table " + TBL_NAME + " (");
        sBuiler.append(_ID + " integer primary key autoincrement, ");//1
        sBuiler.append(PK_ID + " text, ");//2
        sBuiler.append(DISENO_ID + " text, ");//3
        sBuiler.append(POSICION_COLUMA + " text, ");//4
        sBuiler.append(NOMBRE_COLUMNA + " text, ");//5
        sBuiler.append(TEXTO_PREGUNTA + " text, ");//6
        sBuiler.append(GRUPO_RESPUESTAS_ID + " text, ");//6
        sBuiler.append(Q_TYPE + " text);");//9
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
