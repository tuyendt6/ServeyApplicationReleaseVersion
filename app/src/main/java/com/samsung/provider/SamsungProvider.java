
package com.samsung.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.util.Log;

import com.samsung.table.tblCorregimientos;
import com.samsung.table.tblCorregimientosPorZona;
import com.samsung.table.tblDistritos;
import com.samsung.table.tblEncuestaDatos;
import com.samsung.table.tblEncuestaDisenos;
import com.samsung.table.tblEncuestaPreguntas;
import com.samsung.table.tblEncuestaRespuestaGrupos;
import com.samsung.table.tblEncuestaRespuestas;
import com.samsung.table.tblFrecuenciaVisitas;
import com.samsung.table.tblLoggedUsers;
import com.samsung.table.tblPDVContactos;
import com.samsung.table.tblProvincias;
import com.samsung.table.tblPuntosDeVenta;
import com.samsung.table.tblSuperAdministrador;
import com.samsung.table.tblVendedores;
import com.samsung.table.tblVendedoresPorPuntosDeVenta;
import com.samsung.table.tblZonas;

/**
 * @author tuyenpx
 *         class nay dung de tao co so du lieu sql su dung content provider
 */

public class SamsungProvider extends ContentProvider {

    public static final String PRO_NAME = "com.samsung.provider";

    // 0
    public static final Uri URI_CONREEGIMIENTOS = Uri.parse("content://" + PRO_NAME + "/"
            + tblCorregimientos.TBL_NAME);

    // 1
    public static final Uri URI_CORREGIMIENTOS_PORZONA = Uri.parse("content://" + PRO_NAME + "/"
            + tblCorregimientosPorZona.TBL_NAME);

    // 2
    public static final Uri URI_DISTRITOS = Uri.parse("content://" + PRO_NAME + "/"
            + tblDistritos.TBL_NAME);

    // 3
    public static final Uri URI_ENCUESTADATOS = Uri.parse("content://" + PRO_NAME + "/"
            + tblEncuestaDatos.TBL_NAME);

    // 3
    public static final Uri URI_ENCUESTA_DISENOS = Uri.parse("content://" + PRO_NAME + "/"
            + tblEncuestaDisenos.TBL_NAME);

    // 4
    public static final Uri URI_ENCUESTA_PREGUNTAS = Uri.parse("content://" + PRO_NAME + "/"
            + tblEncuestaPreguntas.TBL_NAME);

    // 5
    public static final Uri URI_ENCURESTA_RESPUETAGRUPOS = Uri.parse("content://" + PRO_NAME + "/"
            + tblEncuestaRespuestaGrupos.TBL_NAME);

    // 6
    public static final Uri URI_ENCURESTA_RESPUESTAS  = Uri.parse("content://" + PRO_NAME + "/"
            + tblEncuestaRespuestas.TBL_NAME);

    // 7
    public static final Uri URI_FRECUENCIA_VISITAS = Uri.parse("content://" + PRO_NAME + "/"
            + tblFrecuenciaVisitas.TBL_NAME);

    // 8
    public static final Uri URI_LOGGED_USERS = Uri.parse("content://" + PRO_NAME + "/"
            + tblLoggedUsers.TBL_NAME);

    // 9
    public static final Uri URI_PDV_CONTACTOS = Uri.parse("content://" + PRO_NAME + "/"
            + tblPDVContactos.TBL_NAME);

    // 10
    public static final Uri URI_PROVINCIAS = Uri.parse("content://" + PRO_NAME + "/"
            + tblProvincias.TBL_NAME);

    // 11
    public static final Uri URI_PUNTOS_DEVENTA = Uri.parse("content://" + PRO_NAME + "/"
            + tblPuntosDeVenta.TBL_NAME);


    // 12
    public static final Uri URI_SUPER_ADMINISTRADOR = Uri.parse("content://" + PRO_NAME + "/"
            + tblSuperAdministrador.TBL_NAME);

    // 13
    public static final Uri URI_VENDEDORES = Uri.parse("content://" + PRO_NAME + "/"
            + tblVendedores.TBL_NAME);

    //14

    public static final Uri URI_VENDEDORES_POR_PUNTOS_DEVENTA = Uri.parse("content://" + PRO_NAME + "/"
            + tblVendedoresPorPuntosDeVenta.TBL_NAME);
    //15

    public static final Uri URI_ZONAS = Uri.parse("content://" + PRO_NAME + "/"
            + tblZonas.TBL_NAME);

    // 12
    private static final String JoinMatcherOrder = "JOIN_DETAIL_ORDER";

    public static final Uri URI_JOIN_DETAIL_ORDER = Uri.parse("content://" + PRO_NAME + "/"
            + JoinMatcherOrder);



    private static final int M_CONREEGIMIENTOS = 0;
    private static final int M_CORREGIMIENTOS_PORZONA = 1;
    private static final int M_DISTRITOS = 2;
    private static final int M__ENCUESTADATOS = 3;
    private static final int M_ENCUESTA_PREGUNTAS = 4;
    private static final int M_ENCURESTA_RESPUETAGRUPOS = 5;
    private static final int M_ENCURESTA_RESPUESTAS = 6;
    private static final int M_FRECUENCIA_VISITAS = 7;
    private static final int M_LOGGED_USERS = 8;
    private static final int M_PDV_CONTACTOS = 9;
    private static final int M_PROVINCIAS = 10;
    private static final int M_PUNTOS_DEVENTA = 11;
    private static final int M_SUPER_ADMINISTRADOR = 12;
    private static final int M_VENDEDORES = 13;
    private static final int M_VENDEDORES_POR_PUNTOS_DEVENTA = 14;
    private static final int M_ZONAS = 15;
    private static final int M_ENCUESTA_DISENOS = 16;
    private static final int M_Join = 17;
    public static final UriMatcher uriMatcher;
    private static final String TAG = "BKEProvider";
    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PRO_NAME, tblCorregimientos.TBL_NAME, M_CONREEGIMIENTOS);
        uriMatcher.addURI(PRO_NAME, tblCorregimientosPorZona.TBL_NAME, M_CORREGIMIENTOS_PORZONA);
        uriMatcher.addURI(PRO_NAME, tblDistritos.TBL_NAME, M_DISTRITOS);
        uriMatcher.addURI(PRO_NAME, tblEncuestaDatos.TBL_NAME, M__ENCUESTADATOS);// 1
        uriMatcher.addURI(PRO_NAME, tblEncuestaPreguntas.TBL_NAME, M_ENCUESTA_PREGUNTAS);// 2
        uriMatcher.addURI(PRO_NAME, tblEncuestaRespuestaGrupos.TBL_NAME, M_ENCURESTA_RESPUETAGRUPOS);// 3
        uriMatcher.addURI(PRO_NAME, tblEncuestaRespuestas.TBL_NAME, M_ENCURESTA_RESPUESTAS);// 4
        uriMatcher.addURI(PRO_NAME, tblFrecuenciaVisitas.TBL_NAME, M_FRECUENCIA_VISITAS);// 5
        uriMatcher.addURI(PRO_NAME, tblLoggedUsers.TBL_NAME, M_LOGGED_USERS);// 6
        uriMatcher.addURI(PRO_NAME, tblPDVContactos.TBL_NAME, M_PDV_CONTACTOS);// 7
        uriMatcher.addURI(PRO_NAME, tblProvincias.TBL_NAME, M_PROVINCIAS);// 8
        uriMatcher.addURI(PRO_NAME, tblPuntosDeVenta.TBL_NAME, M_PUNTOS_DEVENTA);// 9
        uriMatcher.addURI(PRO_NAME, tblSuperAdministrador.TBL_NAME, M_SUPER_ADMINISTRADOR);// 10
        uriMatcher.addURI(PRO_NAME, tblVendedores.TBL_NAME, M_VENDEDORES);// 11
        uriMatcher.addURI(PRO_NAME, tblVendedoresPorPuntosDeVenta.TBL_NAME, M_VENDEDORES_POR_PUNTOS_DEVENTA);// 12
        uriMatcher.addURI(PRO_NAME, tblZonas.TBL_NAME, M_ZONAS);// 13
        uriMatcher.addURI(PRO_NAME, tblEncuestaDisenos.TBL_NAME, M_ENCUESTA_DISENOS);// 14
        uriMatcher.addURI(PRO_NAME, JoinMatcherOrder, M_Join);// 14
    }

    private static final String DATABASE_NAME = "Database_BKETool";

    private static final int DATABASE_VERSION = 6;

    private  DatabaseHelper mDbHelper;

    // using DatabaseHelper Class to help manage your database
    private class DatabaseHelper extends SQLiteOpenHelper {
        public DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            tblCorregimientos.onCreate(db); //0
            tblCorregimientosPorZona.onCreate(db);// 1
            tblDistritos.onCreate(db);// 2
            tblEncuestaDatos.onCreate(db);// 3
            tblEncuestaDisenos.onCreate(db);// 4
            tblEncuestaPreguntas.onCreate(db);// 5
            tblEncuestaRespuestaGrupos.onCreate(db);// 6
            tblEncuestaRespuestas.onCreate(db);// 7
            tblFrecuenciaVisitas.onCreate(db);// 8
            tblLoggedUsers.onCreate(db);// 9
            tblPDVContactos.onCreate(db);// 10
            tblProvincias.onCreate(db);//11
            tblPuntosDeVenta.onCreate(db);//12
            tblSuperAdministrador.onCreate(db);//13
            tblVendedores.onCreate(db);//14
            tblVendedoresPorPuntosDeVenta.onCreate(db);//15
            tblZonas.onCreate(db);//16
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            tblCorregimientos.onUpgrade(db, oldVersion, newVersion); //0
            tblCorregimientosPorZona.onUpgrade(db, oldVersion, newVersion); //0
            tblDistritos.onUpgrade(db, oldVersion, newVersion); //0
            tblEncuestaDatos.onUpgrade(db, oldVersion, newVersion); //0
            tblEncuestaDisenos.onUpgrade(db, oldVersion, newVersion); //0
            tblEncuestaPreguntas.onUpgrade(db, oldVersion, newVersion); //0
            tblEncuestaRespuestaGrupos.onUpgrade(db, oldVersion, newVersion); //0
            tblEncuestaRespuestas.onUpgrade(db, oldVersion, newVersion); //0
            tblFrecuenciaVisitas.onUpgrade(db, oldVersion, newVersion); //0
            tblLoggedUsers.onUpgrade(db, oldVersion, newVersion); //0
            tblPDVContactos.onUpgrade(db, oldVersion, newVersion); //0
            tblProvincias.onUpgrade(db, oldVersion, newVersion); //0
            tblPuntosDeVenta.onUpgrade(db, oldVersion, newVersion); //0
            tblSuperAdministrador.onUpgrade(db, oldVersion, newVersion); //0
            tblVendedores.onUpgrade(db, oldVersion, newVersion); //0
            tblVendedoresPorPuntosDeVenta.onUpgrade(db, oldVersion, newVersion); //0
            tblZonas.onUpgrade(db, oldVersion, newVersion); //0
        }
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        String table = "";
        switch (uriMatcher.match(uri)) {
            case M_CONREEGIMIENTOS: //0
                table = tblCorregimientos.TBL_NAME;
                break;
            case M_CORREGIMIENTOS_PORZONA:// 1
                table = tblCorregimientosPorZona.TBL_NAME;
                break;
            case M_DISTRITOS:// 2
                table = tblDistritos.TBL_NAME;
                break;
            case M__ENCUESTADATOS:// 3
                table = tblEncuestaDatos.TBL_NAME;
                break;
            case M_ENCUESTA_DISENOS:// 4
                table = tblEncuestaDisenos.TBL_NAME;
                break;
            case M_ENCUESTA_PREGUNTAS:// 5
                table = tblEncuestaPreguntas.TBL_NAME;
                break;
            case M_ENCURESTA_RESPUETAGRUPOS:// 6
                table = tblEncuestaRespuestaGrupos.TBL_NAME;
                break;
            case M_ENCURESTA_RESPUESTAS:// 7
                table = tblEncuestaRespuestas.TBL_NAME;
                break;
            case M_FRECUENCIA_VISITAS:// 8
                table = tblFrecuenciaVisitas.TBL_NAME;
                break;
            case M_LOGGED_USERS:// 9
                table = tblLoggedUsers.TBL_NAME;
                break;
            case M_PDV_CONTACTOS:// 10
                table = tblPDVContactos.TBL_NAME;
                break;
            case M_PROVINCIAS: //11
                table = tblProvincias.TBL_NAME;
                break;
            case M_PUNTOS_DEVENTA: //12
                table = tblPuntosDeVenta.TBL_NAME;
                break;
            case M_SUPER_ADMINISTRADOR: //13
                table = tblSuperAdministrador.TBL_NAME;
                break;
            case M_VENDEDORES: //13
                table = tblVendedores.TBL_NAME;
                break;
            case M_VENDEDORES_POR_PUNTOS_DEVENTA: //13
                table = tblVendedoresPorPuntosDeVenta.TBL_NAME;
                break;
            case M_ZONAS: //13
                table = tblZonas.TBL_NAME;
                break;

            default:
                break;
        }
        Log.e(TAG, "Xoa table " + table);
        SQLiteDatabase sqlDB = mDbHelper.getWritableDatabase();
        int deleteCount = sqlDB.delete(table, selection, selectionArgs);
        Log.e(TAG, "Tong so dong da xoa = " + deleteCount);

        // Thong bao den cac observer ve su thay doi
        getContext().getContentResolver().notifyChange(uri, null);
        return deleteCount;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {

        int key = uriMatcher.match(uri);
        String table = "";

        switch (key) {
            case M_CONREEGIMIENTOS: //0
                table = tblCorregimientos.TBL_NAME;
                break;
            case M_CORREGIMIENTOS_PORZONA:// 1
                table = tblCorregimientosPorZona.TBL_NAME;
                break;
            case M_DISTRITOS:// 2
                table = tblDistritos.TBL_NAME;
                break;
            case M__ENCUESTADATOS:// 3
                table = tblEncuestaDatos.TBL_NAME;
                break;
            case M_ENCUESTA_DISENOS:// 4
                table = tblEncuestaDisenos.TBL_NAME;
                break;
            case M_ENCUESTA_PREGUNTAS:// 5
                table = tblEncuestaPreguntas.TBL_NAME;
                break;
            case M_ENCURESTA_RESPUETAGRUPOS:// 6
                table = tblEncuestaRespuestaGrupos.TBL_NAME;
                break;
            case M_ENCURESTA_RESPUESTAS:// 7
                table = tblEncuestaRespuestas.TBL_NAME;
                break;
            case M_FRECUENCIA_VISITAS:// 8
                table = tblFrecuenciaVisitas.TBL_NAME;
                break;
            case M_LOGGED_USERS:// 9
                table = tblLoggedUsers.TBL_NAME;
                break;
            case M_PDV_CONTACTOS:// 10
                table = tblPDVContactos.TBL_NAME;
                break;
            case M_PROVINCIAS: //11
                table = tblProvincias.TBL_NAME;
                break;
            case M_PUNTOS_DEVENTA: //12
                table = tblPuntosDeVenta.TBL_NAME;
                break;
            case M_SUPER_ADMINISTRADOR: //13
                table = tblSuperAdministrador.TBL_NAME;
                break;
            case M_VENDEDORES: //13
                table = tblVendedores.TBL_NAME;
                break;
            case M_VENDEDORES_POR_PUNTOS_DEVENTA: //13
                table = tblVendedoresPorPuntosDeVenta.TBL_NAME;
                break;
            case M_ZONAS: //13
                table = tblZonas.TBL_NAME;
                break;

            default:
                break;
        }

        SQLiteDatabase sqlDB = mDbHelper.getWritableDatabase();
        long rowID = sqlDB.insertWithOnConflict(table, "", values, SQLiteDatabase.CONFLICT_REPLACE);
        getContext().getContentResolver().notifyChange(uri, null);

        if (rowID > 0) {
            return Uri.withAppendedPath(uri, String.valueOf(rowID));
        } else {
            return null;

        }
    }

    @Override
    public boolean onCreate() {

        Context context = getContext();
        mDbHelper = new DatabaseHelper(context);
        // addDataUser();
        return false;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
            String sortOrder) {

        int key = uriMatcher.match(uri);
        String table = "";

        switch (key) {
            case M_CONREEGIMIENTOS: //0
                table = tblCorregimientos.TBL_NAME;
                break;
            case M_CORREGIMIENTOS_PORZONA:// 1
                table = tblCorregimientosPorZona.TBL_NAME;
                break;
            case M_DISTRITOS:// 2
                table = tblDistritos.TBL_NAME;
                break;
            case M__ENCUESTADATOS:// 3
                table = tblEncuestaDatos.TBL_NAME;
                break;
            case M_ENCUESTA_DISENOS:// 4
                table = tblEncuestaDisenos.TBL_NAME;
                break;
            case M_ENCUESTA_PREGUNTAS:// 5
                table = tblEncuestaPreguntas.TBL_NAME;
                break;
            case M_ENCURESTA_RESPUETAGRUPOS:// 6
                table = tblEncuestaRespuestaGrupos.TBL_NAME;
                break;
            case M_ENCURESTA_RESPUESTAS:// 7
                table = tblEncuestaRespuestas.TBL_NAME;
                break;
            case M_FRECUENCIA_VISITAS:// 8
                table = tblFrecuenciaVisitas.TBL_NAME;
                break;
            case M_LOGGED_USERS:// 9
                table = tblLoggedUsers.TBL_NAME;
                break;
            case M_PDV_CONTACTOS:// 10
                table = tblPDVContactos.TBL_NAME;
                break;
            case M_PROVINCIAS: //11
                table = tblProvincias.TBL_NAME;
                break;
            case M_PUNTOS_DEVENTA: //12
                table = tblPuntosDeVenta.TBL_NAME;
                break;
            case M_SUPER_ADMINISTRADOR: //13
                table = tblSuperAdministrador.TBL_NAME;
                break;
            case M_VENDEDORES: //13
                table = tblVendedores.TBL_NAME;
                break;
            case M_VENDEDORES_POR_PUNTOS_DEVENTA: //13
                table = tblVendedoresPorPuntosDeVenta.TBL_NAME;
                break;
            case M_ZONAS: //13
                table = tblZonas.TBL_NAME;
                break;
            case M_Join: //13
                table = joinToPuntosDevata();
                break;

            default:
                break;
        }

        SQLiteDatabase sqlDB = mDbHelper.getWritableDatabase();
        SQLiteQueryBuilder sqlBuilder = new SQLiteQueryBuilder();
        sqlBuilder.setTables(table);
        sqlBuilder.setDistinct(true);
        Cursor c = sqlBuilder.query(sqlDB, projection, selection, selectionArgs, null, null,
                sortOrder);
        c.setNotificationUri(getContext().getContentResolver(), uri);
        return c;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        int key = uriMatcher.match(uri);
        String table = "";

        switch (key) {
            case M_CONREEGIMIENTOS: //0
                table = tblCorregimientos.TBL_NAME;
                break;
            case M_CORREGIMIENTOS_PORZONA:// 1
                table = tblCorregimientosPorZona.TBL_NAME;
                break;
            case M_DISTRITOS:// 2
                table = tblDistritos.TBL_NAME;
                break;
            case M__ENCUESTADATOS:// 3
                table = tblEncuestaDatos.TBL_NAME;
                break;
            case M_ENCUESTA_DISENOS:// 4
                table = tblEncuestaDisenos.TBL_NAME;
                break;
            case M_ENCUESTA_PREGUNTAS:// 5
                table = tblEncuestaPreguntas.TBL_NAME;
                break;
            case M_ENCURESTA_RESPUETAGRUPOS:// 6
                table = tblEncuestaRespuestaGrupos.TBL_NAME;
                break;
            case M_ENCURESTA_RESPUESTAS:// 7
                table = tblEncuestaRespuestas.TBL_NAME;
                break;
            case M_FRECUENCIA_VISITAS:// 8
                table = tblFrecuenciaVisitas.TBL_NAME;
                break;
            case M_LOGGED_USERS:// 9
                table = tblLoggedUsers.TBL_NAME;
                break;
            case M_PDV_CONTACTOS:// 10
                table = tblPDVContactos.TBL_NAME;
                break;
            case M_PROVINCIAS: //11
                table = tblProvincias.TBL_NAME;
                break;
            case M_PUNTOS_DEVENTA: //12
                table = tblPuntosDeVenta.TBL_NAME;
                break;
            case M_SUPER_ADMINISTRADOR: //13
                table = tblSuperAdministrador.TBL_NAME;
                break;
            case M_VENDEDORES: //13
                table = tblVendedores.TBL_NAME;
                break;
            case M_VENDEDORES_POR_PUNTOS_DEVENTA: //13
                table = tblVendedoresPorPuntosDeVenta.TBL_NAME;
                break;
            case M_ZONAS: //13
                table = tblZonas.TBL_NAME;
                break;

            default:
                break;
        }

        SQLiteDatabase sqlDB = mDbHelper.getWritableDatabase();
        int rowEffect = sqlDB.update(table, values, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return rowEffect;
    }


    private static String joinToPuntosDevata() {
        StringBuilder sBuiler = new StringBuilder();
        sBuiler.append(tblPuntosDeVenta.TBL_NAME  + " LEFT OUTER JOIN "
                + tblDistritos.TBL_NAME);
        sBuiler.append(" ON " + tblPuntosDeVenta.TBL_NAME  + "."
                + tblPuntosDeVenta.DISTRITOID + "=" + tblDistritos.TBL_NAME + "."
                + tblDistritos.PK_ID);
        sBuiler.append(" LEFT OUTER JOIN " + tblProvincias.TBL_NAME);
        sBuiler.append(" ON " + tblPuntosDeVenta.TBL_NAME + "."
                + tblPuntosDeVenta.PROVINCIAID + "=" + tblProvincias.TBL_NAME + "."
                + tblProvincias.PK_ID);
        sBuiler.append(" LEFT OUTER JOIN " + tblZonas.TBL_NAME);
        sBuiler.append(" ON " + tblPuntosDeVenta.TBL_NAME + "." + tblPuntosDeVenta.ZONAID + "="
                + tblZonas.TBL_NAME + "." + tblZonas.PK_ID);
        return sBuiler.toString();
    }
}
