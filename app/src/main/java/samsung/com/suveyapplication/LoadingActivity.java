package samsung.com.suveyapplication;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.samsung.provider.SamsungProvider;
import com.samsung.table.tblCorregimientos;
import com.samsung.table.tblDistritos;
import com.samsung.table.tblEncuestaDatos;
import com.samsung.table.tblEncuestaDisenos;
import com.samsung.table.tblEncuestaPreguntas;
import com.samsung.table.tblEncuestaRespuestaGrupos;
import com.samsung.table.tblEncuestaRespuestas;
import com.samsung.table.tblFrecuenciaVisitas;
import com.samsung.table.tblProvincias;
import com.samsung.table.tblPuntosDeVenta;
import com.samsung.table.tblVendedores;
import com.samsung.table.tblVendedoresPorPuntosDeVenta;
import com.samsung.table.tblZonas;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by SamSunger on 5/15/2015.
 */
public class LoadingActivity extends AppCompatActivity {
    private ProgressBar progressBar;
    private TextView mView;


    String flag = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.loading_layout);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        if (!isGpsEnable()) {
            showDialogAuthenFail();
        } else {
            progressBar = (ProgressBar) findViewById(R.id.idprogress);
            mView = (TextView) findViewById(R.id.txtprogress);
            progressBar.setMax(100);
            mView.setText("0 %");
            try {
                flag = getIntent().getStringExtra("flag");
            } catch (Exception e) {

            }
            new Connect2Server().execute(getBaseContext());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10) {
            Log.e("tuyenpx", "tuyenpx da chay vao day");
            if (isGpsEnable()) {

            } else {
                finish();
            }
        }

    }

    private boolean isGpsEnable() {
        LocationManager service = (LocationManager) getSystemService(LOCATION_SERVICE);
        boolean enable = service.isProviderEnabled(LocationManager.GPS_PROVIDER);
        return enable;
    }

    private void showDialogAuthenFail() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Encuestas CWP requiere GPS Activo");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivityForResult(intent, 10);
                finish();
            }
        });
        builder.create().show();
    }

    class Connect2Server extends AsyncTask<Context, Void, String> {

        //List cac ket qua query
        private List<Model2Jason.Vendedores> mListVendedores;

        private List<Model2Jason.PuntosDeVanta> mListPuntosDeVanta;

        private List<Model2Jason.Zonas> mListZonas;

        private List<Model2Jason.Distritos> mListDistritos;

        private List<Model2Jason.Corregimientos> mListCorregimientos;
        private List<Model2Jason.Encutadatos> mListEncuetadatos;


        private List<Model2Jason.Provincias> mListProvincias;

        private List<Model2Jason.EncuestaDisenos> mListEncuestaDisenoses;
        private List<Model2Jason.EncuestasPreguntas> mListEncuestasPreguntases;
        private List<Model2Jason.EncuestasRespuestasGrupos> mListEncuestasRespuestasGruposes;
        private List<Model2Jason.EncuestaRespuestas> mListEncuestaRespuestas;


        private List<Model2Jason.FrecuenciaVisitas> mListFrecuenciaVisitas;
        private List<Model2Jason.VendedoresPorPuntosDeVenta> mListVendedoresPorPuntoDeVenta;


        private Gson mGson;

        private Type mType;

        private String TAG = "Connect2Server";
        private Context mContext;

        @Override
        protected String doInBackground(Context... params) {
            mContext = params[0];
            try {
                queryDataBaseFromServer();
            } catch (Exception e) {
                clearAll();
                Log.e(TAG, "remove data dueto : " + e.toString());
            }
            inSert2Database();
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (TextUtils.isEmpty(flag)) {
                Intent i = new Intent(getBaseContext(), LoginActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
            }
            finish();
        }

        private void clearAll() {
            if (mListDistritos != null) {
                mListDistritos = null;
            }
            if (mListProvincias != null) {
                mListProvincias = null;
            }
            if (mListPuntosDeVanta != null) {
                mListPuntosDeVanta = null;
            }
            if (mListVendedores != null) {
                mListVendedores = null;
            }
            if (mListZonas != null) {
                mListZonas = null;
            }
            if (mListEncuestaDisenoses != null) {
                mListEncuestaDisenoses = null;
            }
            if (mListEncuestasPreguntases != null) {
                mListEncuestasPreguntases = null;
            }
            if (mListEncuestasRespuestasGruposes != null) {
                mListEncuestasRespuestasGruposes = null;
            }
            if (mListEncuestaRespuestas != null) {
                mListEncuestaRespuestas = null;
            }

        }

        int n = 0;

        private void setProgress() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    n = n + 10;
                    progressBar.setProgress(n);
                    mView.setText(n + " % ");
                }
            });
        }


        private void queryDataBaseFromServer() {
            QueryUsertable();
            setProgress();//10
            QueryDealerTable();
            if (mListPuntosDeVanta != null) {
                mContext.getContentResolver().delete(SamsungProvider.URI_PUNTOS_DEVENTA, null, null);
            }
            setProgress();//20
            QueryDistrito();
            setProgress();//30
            if (mListDistritos != null) {
                mContext.getContentResolver().delete(SamsungProvider.URI_DISTRITOS, null, null);
            }

            QueryCorregimientos();

            if (mListCorregimientos != null) {
                mContext.getContentResolver().delete(SamsungProvider.URI_CONREEGIMIENTOS, null, null);
            }


            QueryProvincias();
            setProgress();//40
            if (mListProvincias != null) {
                mContext.getContentResolver().delete(SamsungProvider.URI_PROVINCIAS, null, null);
            }
            QueryZonas();
            setProgress();//50
            if (mListZonas != null) {
                mContext.getContentResolver().delete(SamsungProvider.URI_ZONAS, null, null);
            }
            QueryEncuestaDisenoses();
            if (mListEncuestaDisenoses != null) {
                mContext.getContentResolver().delete(SamsungProvider.URI_ENCUESTA_DISENOS, null, null);
            }
            setProgress();//60
            QueryEncuestasPreguntases();
            if (mListEncuestasPreguntases != null) {
                mContext.getContentResolver().delete(SamsungProvider.URI_ENCUESTA_PREGUNTAS, null, null);
            }

            QueryFrecuenciaVisitas();
            if (mListFrecuenciaVisitas != null) {
                mContext.getContentResolver().delete(SamsungProvider.URI_FRECUENCIA_VISITAS, null, null);
            }
            QueryVendedoresPorPuntosDeVenta();
            if (mListVendedoresPorPuntoDeVenta != null) {
                mContext.getContentResolver().delete(SamsungProvider.URI_VENDEDORES_POR_PUNTOS_DEVENTA, null, null);
            }
            setProgress();//70
            QueryEncuestasRespuestasGruposes();
            if (mListEncuestasRespuestasGruposes != null) {
                mContext.getContentResolver().delete(SamsungProvider.URI_ENCURESTA_RESPUETAGRUPOS, null, null);
            }

            QueryEncuestaRespuestas();
            if (mListEncuestaRespuestas != null) {
                mContext.getContentResolver().delete(SamsungProvider.URI_ENCURESTA_RESPUESTAS, null, null);
            }
            QueryEncuestaDatos();
            if (mListEncuestaRespuestas != null) {
                mContext.getContentResolver().delete(SamsungProvider.URI_ENCUESTADATOS, tblEncuestaDatos.SYS + " =?", new String[]{"true"});
            }
        }

        private void inSert2Database() {
            insertEncuestaDatos(mListEncuetadatos);
            setProgress();//80
            insertVendedores(mListVendedores);
            inserDistritos(mListDistritos);
            inserCorregimientos(mListCorregimientos);
            insertDealer(mListPuntosDeVanta);
            setProgress();//90
            insertEncuestaDisenoses(mListEncuestaDisenoses);
            insertEncuestasPreguntases(mListEncuestasPreguntases);
            insertEncuestasRespuestasGruposes(mListEncuestasRespuestasGruposes);
            inserZonas(mListZonas);
            inserProvincias(mListProvincias);
            insertFrecuenciaVisitas(mListFrecuenciaVisitas);
            insertVendedoresPorPuntosDeVenta(mListVendedoresPorPuntoDeVenta);
            setProgress();//100
            insertEncuestaRespuestas(mListEncuestaRespuestas);
        }

        private void QueryEncuestaRespuestas() {
            mGson = new Gson();
            mType = new TypeToken<List<Model2Jason.EncuestaRespuestas>>() {
            }.getType();
            mListEncuestaRespuestas = mGson.fromJson(queryServerTable("http://www.tagzone.io/Ode/WebService.asmx/getAllEncuestaRespuestas"), mType);
        }

        private void insertEncuestaRespuestas(List<Model2Jason.EncuestaRespuestas> list) {
            if (list == null) {
                Log.e("tuyen.px", "Insert table Vendedores ignored");
                return;
            }
            for (Model2Jason.EncuestaRespuestas va : list) {
                ContentValues values = new ContentValues();
                values.put(tblEncuestaRespuestas.PK_ID, va.PK_ID);// 1
                values.put(tblEncuestaRespuestas.CODIGO, va.CODIGO);// 2
                values.put(tblEncuestaRespuestas.DESCRIPCION, va.DESCRIPCION);// 3
                values.put(tblEncuestaRespuestas.GRUPO_RESQUEST_AS_ID, va.GRUPO_RESQUEST_AS_ID);// 4
                mContext.getContentResolver().insert(SamsungProvider.URI_ENCURESTA_RESPUESTAS, values);
            }
        }


        // query and insert FrecuenciaVisitas :
        private void QueryFrecuenciaVisitas() {
            mGson = new Gson();
            mType = new TypeToken<List<Model2Jason.FrecuenciaVisitas>>() {
            }.getType();
            mListFrecuenciaVisitas = mGson.fromJson(queryServerTable("http://www.tagzone.io/Ode/WebService.asmx/getFrecuenciaVisita"), mType);
        }

        private void insertFrecuenciaVisitas(List<Model2Jason.FrecuenciaVisitas> list) {
            if (list == null) {
                Log.e("tuyen.px", "Insert table FrecuenciaVisitas  ignored");
                return;
            }
            for (Model2Jason.FrecuenciaVisitas va : list) {
                ContentValues values = new ContentValues();
                values.put(tblFrecuenciaVisitas.PK_ID, va.PK_ID);// 1
                values.put(tblFrecuenciaVisitas.CODIGO, va.CODIGO);// 2
                values.put(tblFrecuenciaVisitas.DESCRIPCION, va.DESCRIPCION);//
                mContext.getContentResolver().insert(SamsungProvider.URI_FRECUENCIA_VISITAS, values);
            }
        }


        // query and insert VendedoresPorPuntosDeVenta

        private void QueryVendedoresPorPuntosDeVenta() {
            mGson = new Gson();
            mType = new TypeToken<List<Model2Jason.VendedoresPorPuntosDeVenta>>() {
            }.getType();
            mListVendedoresPorPuntoDeVenta = mGson.fromJson(queryServerTable("http://www.tagzone.io/Ode/WebService.asmx/getAllVendedoresPorPuntosDeVenta"), mType);
        }

        private void insertVendedoresPorPuntosDeVenta(List<Model2Jason.VendedoresPorPuntosDeVenta> list) {
            if (list == null) {
                Log.e("tuyen.px", "Insert table VendedoresPorPuntosDeVenta ignored");
                return;
            }
            for (Model2Jason.VendedoresPorPuntosDeVenta va : list) {
                ContentValues values = new ContentValues();
                values.put(tblVendedoresPorPuntosDeVenta.PK_ID, va.PK_ID);// 1
                values.put(tblVendedoresPorPuntosDeVenta.VENDEDOR_ID, va.VENDEDOR_ID);// 2
                values.put(tblVendedoresPorPuntosDeVenta.PDVID, va.PDVID);// 3
                values.put(tblVendedoresPorPuntosDeVenta.FRECUENCIA_VISITA_ID, va.FRECUENCIA_VISITA_ID);// 4
                mContext.getContentResolver().insert(SamsungProvider.URI_VENDEDORES_POR_PUNTOS_DEVENTA, values);
            }
        }


        private void QueryEncuestaDisenoses() {
            mGson = new Gson();
            mType = new TypeToken<List<Model2Jason.EncuestaDisenos>>() {
            }.getType();
            mListEncuestaDisenoses = mGson.fromJson(queryServerTable("http://www.tagzone.io/Ode/WebService.asmx/getAllEncuestaDisenos"), mType);
        }

        private void insertEncuestaDisenoses(List<Model2Jason.EncuestaDisenos> list) {
            if (list == null) {
                Log.e("tuyen.px", "Insert table EncuestaDisenoses ignored");
                return;
            }
            for (Model2Jason.EncuestaDisenos va : list) {
                ContentValues values = new ContentValues();
                values.put(tblEncuestaDisenos.PK_ID, va.PK_ID);// 1
                values.put(tblEncuestaDisenos.ACTIVO, va.ACTIVO);// 2
                values.put(tblEncuestaDisenos.DESCRIPCION, va.DESCRIPCION);// 3
                values.put(tblEncuestaDisenos.NOMBRE, va.NOMBRE);// 5
                mContext.getContentResolver().insert(SamsungProvider.URI_ENCUESTA_DISENOS, values);
            }
        }

        private void QueryEncuestasPreguntases() {
            mGson = new Gson();
            mType = new TypeToken<List<Model2Jason.EncuestasPreguntas>>() {
            }.getType();
            mListEncuestasPreguntases = mGson.fromJson(queryServerTable("http://www.tagzone.io/Ode/WebService.asmx/getAllEncuestasPreguntas"), mType);
        }

        private void insertEncuestasPreguntases(List<Model2Jason.EncuestasPreguntas> list) {
            if (list == null) {
                Log.e("tuyen.px", "Insert table EncuestasPreguntases ignored");
                return;
            }
            for (Model2Jason.EncuestasPreguntas va : list) {
                ContentValues values = new ContentValues();
                values.put(tblEncuestaPreguntas.PK_ID, va.PK_ID);// 1
                values.put(tblEncuestaPreguntas.DISENO_ID, va.DISENO_ID);// 2
                values.put(tblEncuestaPreguntas.NOMBRE_COLUMNA, va.NOMBRE_COLUMNA);// 3
                values.put(tblEncuestaPreguntas.POSICION_COLUMA, va.POSICION_COLUMA);// 4
                values.put(tblEncuestaPreguntas.Q_TYPE, va.Q_TYPE);// 5
                values.put(tblEncuestaPreguntas.GRUPO_RESPUESTAS_ID, va.GRUPO_RESPUESTAS_ID);// 4
                values.put(tblEncuestaPreguntas.TEXTO_PREGUNTA, va.TEXTO_PREGUNTA);// 5
                mContext.getContentResolver().insert(SamsungProvider.URI_ENCUESTA_PREGUNTAS, values);
            }
        }

        private void QueryEncuestasRespuestasGruposes() {
            mGson = new Gson();
            mType = new TypeToken<List<Model2Jason.EncuestasRespuestasGrupos>>() {
            }.getType();
            mListEncuestasRespuestasGruposes = mGson.fromJson(queryServerTable("http://www.tagzone.io/Ode/WebService.asmx/getAllEncuestaRespuestaGrupos"), mType);
        }

        private void insertEncuestasRespuestasGruposes(List<Model2Jason.EncuestasRespuestasGrupos> list) {
            if (list == null) {
                Log.e("tuyen.px", "Insert table EncuestasRespuestasGruposes ignored");
                return;
            }
            for (Model2Jason.EncuestasRespuestasGrupos va : list) {
                ContentValues values = new ContentValues();
                values.put(tblEncuestaRespuestaGrupos.PK_ID, va.PK_ID);// 1
                values.put(tblEncuestaRespuestaGrupos.DESCRIPCION, va.DESCRIPCION);// 2
                values.put(tblEncuestaRespuestaGrupos.NOMBRE, va.NOMBRE);// 3
                mContext.getContentResolver().insert(SamsungProvider.URI_ENCURESTA_RESPUETAGRUPOS, values);
            }
        }

        private void QueryUsertable() {
            mGson = new Gson();
            mType = new TypeToken<List<Model2Jason.Vendedores>>() {
            }.getType();
            mListVendedores = mGson.fromJson(queryServerTable("http://www.tagzone.io/Ode/WebService.asmx/getAllVendedores"), mType);
        }


        private void insertVendedores(List<Model2Jason.Vendedores> list) {
            if (list == null) {
                Log.e("tuyen.px", "Insert table Vendedores ignored");
                return;
            }
            for (Model2Jason.Vendedores va : list) {
                ContentValues values = new ContentValues();
                values.put(tblVendedores.PK_ID, va.PK_ID);// 1
                values.put(tblVendedores.NOMBRE_USUARIO, va.NOMBRE_USUARIO);// 2
                values.put(tblVendedores.NOMBRE_COMPETO, va.NOMBRE_COMPETO);// 3
                values.put(tblVendedores.ACTIVO, va.ACTIVO);// 4
                values.put(tblVendedores.CLAVE, va.CLAVE);// 5
                values.put(tblVendedores.DERECCION, va.DERECCION);// 6
                values.put(tblVendedores.DOC_IDENT, va.DOC_IDENT);// 7
                values.put(tblVendedores.TELEFONOS, va.TELEFONOS);// 8
                values.put(tblVendedores.Email1, va.Email1);// 7
                values.put(tblVendedores.Email2, va.Email2);// 8
                mContext.getContentResolver().insert(SamsungProvider.URI_VENDEDORES, values);
            }
        }

        private void QueryDealerTable() {
            mGson = new Gson();
            mType = new TypeToken<List<Model2Jason.PuntosDeVanta>>() {
            }.getType();
            mListPuntosDeVanta = mGson.fromJson(queryServerTable("http://www.tagzone.io/Ode/WebService.asmx/getAllPuntos"), mType);
        }

        private void insertDealer(List<Model2Jason.PuntosDeVanta> list) {
            if (list == null) {
                Log.e("tuyen.px", "Insert table PuntosDeVanta ignored");
                return;
            }
            for (Model2Jason.PuntosDeVanta va : list) {
                ContentValues values = new ContentValues();
                values.put(tblPuntosDeVenta.PK_ID, va.PK_ID);// 1
                values.put(tblPuntosDeVenta.ACTIVO, va.ACTIVO);// 2
                values.put(tblPuntosDeVenta.DESCCION_ADICIONALES, va.DESCCION_ADICIONALES);// 3
                values.put(tblPuntosDeVenta.DIRECCION, va.DIRECCION);// 4
                values.put(tblPuntosDeVenta.DISTRITOID, va.DISTRITOID);// 5
                values.put(tblPuntosDeVenta.FACT_DEFT_ID, va.FACT_DEFT_ID);// 6
                values.put(tblPuntosDeVenta.NOMBRE, va.NOMBRE);// 7
                values.put(tblPuntosDeVenta.POSION_LAT, va.POSION_LAT);// 8
                values.put(tblPuntosDeVenta.POSION_LON, va.POSION_LON);// 9
                values.put(tblPuntosDeVenta.PROVINCIAID, va.PROVINCIAID);// 10
                values.put(tblPuntosDeVenta.TELEFONO, va.TELEFONO);// 11
                values.put(tblPuntosDeVenta.ZONAID, va.ZONAID);// 12
                mContext.getContentResolver().insert(SamsungProvider.URI_PUNTOS_DEVENTA, values);
            }
        }

        private void QueryDistrito() {
            mGson = new Gson();
            mType = new TypeToken<List<Model2Jason.Distritos>>() {
            }.getType();
            mListDistritos = mGson.fromJson(queryServerTable("http://www.tagzone.io/Ode/WebService.asmx/getAllDistritos"), mType);
        }

        private void inserDistritos(List<Model2Jason.Distritos> list) {
            if (list == null) {
                Log.e("tuyen.px", "Insert table Distritos ignored");
                return;
            }
            for (Model2Jason.Distritos va : list) {
                ContentValues values = new ContentValues();
                values.put(tblDistritos.PK_ID, va.PK_ID);// 1
                values.put(tblDistritos.NOMBRE, va.NOMBRE);// 2
                values.put(tblDistritos.PROVINCIA_ID, va.PROVINCIA_ID);//3
                mContext.getContentResolver().insert(SamsungProvider.URI_DISTRITOS, values);
            }
        }


        private void QueryCorregimientos() {
            mGson = new Gson();
            mType = new TypeToken<List<Model2Jason.Corregimientos>>() {
            }.getType();
            mListCorregimientos = mGson.fromJson(queryServerTable("http://www.tagzone.io/Ode/WebService.asmx/getAllCorregimientos"), mType);
        }


        private void inserCorregimientos(List<Model2Jason.Corregimientos> list) {
            if (list == null) {
                Log.e("tuyen.px", "Insert table Corregimientos ignored");
                return;
            }
            for (Model2Jason.Corregimientos va : list) {
                ContentValues values = new ContentValues();
                values.put(tblCorregimientos.PK_ID, va.PK_ID);// 1
                values.put(tblCorregimientos.NOMBRE, va.NOMBRE);// 2
                values.put(tblCorregimientos.DISTRITOID, va.DISTRITOID);//3
                mContext.getContentResolver().insert(SamsungProvider.URI_CONREEGIMIENTOS, values);
            }
        }


        private void QueryEncuestaDatos() {
            mGson = new Gson();
            mType = new TypeToken<List<Model2Jason.Encutadatos>>() {
            }.getType();
            mListEncuetadatos = mGson.fromJson(queryServerTable("http://www.tagzone.io/Ode/WebService.asmx/getAllEncuetadatos"), mType);
        }

        private void insertEncuestaDatos(List<Model2Jason.Encutadatos> list) {
            if (list == null) {
                Log.e("tuyen.px", "Insert table EncuestaDatos ignored");
                return;
            }
            for (Model2Jason.Encutadatos va : list) {
                ContentValues values = new ContentValues();

                values.put(tblEncuestaDatos.PK_ID, va.PK_ID);// 1
                values.put(tblEncuestaDatos.DISENO_ID, va.DISENO_ID);// 2
                values.put(tblEncuestaDatos.FECHA_HORA_REGISTRO, va.FECHA_HORA_REGISTRO);//3

                values.put(tblEncuestaDatos.PDV_ID, va.PDV_ID);// 1
                values.put(tblEncuestaDatos.FECHAHORA_ENCUESTA, va.FECHAHORA_ENCUESTA);// 2
                values.put(tblEncuestaDatos.POSICION_REGISTROLAT, va.POSICION_REGISTROLAT);//3
                values.put(tblEncuestaDatos.POSICIONENCUESTA_LAT, va.POSICIONENCUESTA_LAT);//3
                values.put(tblEncuestaDatos.POSICIONENCUESTA_LON, va.POSICIONENCUESTA_LON);//3
                values.put(tblEncuestaDatos.SYS, "true");//3

                values.put(tblEncuestaDatos.POSOCION_REGISTRO_LON, va.POSOCION_REGISTRO_LON);// 1
                values.put(tblEncuestaDatos.VENDEDOR_ID, va.VENDEDOR_ID);// 2


                values.put(tblEncuestaDatos.PREGUNTA_01, va.PREGUNTA_01);//3

                values.put(tblEncuestaDatos.PREGUNTA_02, va.PREGUNTA_02);// 1
                values.put(tblEncuestaDatos.PREGUNTA_03, va.PREGUNTA_03);// 2
                values.put(tblEncuestaDatos.PREGUNTA_04, va.PREGUNTA_04);//3
                values.put(tblEncuestaDatos.PREGUNTA_05, va.PREGUNTA_05);// 1
                values.put(tblEncuestaDatos.PREGUNTA_06, va.PREGUNTA_06);// 2

                values.put(tblEncuestaDatos.PREGUNTA_07, va.PREGUNTA_07);//3
                values.put(tblEncuestaDatos.PREGUNTA_08, va.PREGUNTA_08);//3
                values.put(tblEncuestaDatos.PREGUNTA_09, va.PREGUNTA_09);//3
                values.put(tblEncuestaDatos.PREGUNTA_10, va.PREGUNTA_10);//3
                mContext.getContentResolver().insert(SamsungProvider.URI_ENCUESTADATOS, values);
            }
        }


        private void QueryProvincias() {
            mGson = new Gson();
            mType = new TypeToken<List<Model2Jason.Provincias>>() {
            }.getType();
            mListProvincias = mGson.fromJson(queryServerTable("http://www.tagzone.io/Ode/WebService.asmx/getAllProvincias"), mType);
        }


        private void inserProvincias(List<Model2Jason.Provincias> list) {
            if (list == null) {
                Log.e("tuyen.px", "Insert table Provincias ignored");
                return;
            }
            for (Model2Jason.Provincias va : list) {
                ContentValues values = new ContentValues();
                values.put(tblProvincias.PK_ID, va.PK_ID);// 1
                values.put(tblProvincias.NOMBRE, va.NOMBRE);// 2
                mContext.getContentResolver().insert(SamsungProvider.URI_PROVINCIAS, values);
            }
        }


        private void QueryZonas() {
            mGson = new Gson();
            mType = new TypeToken<List<Model2Jason.Zonas>>() {
            }.getType();
            mListZonas = mGson.fromJson(queryServerTable("http://www.tagzone.io/Ode/WebService.asmx/getAllZonas"), mType);
        }

        private void inserZonas(List<Model2Jason.Zonas> list) {
            if (list == null) {
                Log.e("tuyen.px", "Insert table Zonas ignored");
                return;
            }
            for (Model2Jason.Zonas va : list) {
                ContentValues values = new ContentValues();
                values.put(tblZonas.PK_ID, va.PK_ID);// 1
                values.put(tblZonas.NOMBRE, va.NOMBRE);// 2
                mContext.getContentResolver().insert(SamsungProvider.URI_ZONAS, values);
            }
        }

        private String queryServerTable(String tableAddress) {
            StringBuilder sBuiler = new StringBuilder();
            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(tableAddress);
                List<NameValuePair> listParams = new ArrayList<NameValuePair>();
                httppost.setEntity(new UrlEncodedFormEntity(listParams));
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity entity = response.getEntity();
                BufferedReader bfr = new BufferedReader(new InputStreamReader(entity.getContent()));
                String line;
                while ((line = bfr.readLine()) != null) {
                    sBuiler.append(line);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            String result = chuanHoaKetQua(sBuiler.toString());
            Log.e("tuyen.px", "tuyen.px" + result);
            return result;
        }

        private String chuanHoaKetQua(String result) {
            result = result.replace(
                    "<?xml version=\"1.0\" encoding=\"utf-8\"?><string xmlns=\"http://tempuri.org/\">",
                    "");
            result = result.replace("</string>", "");
            return result;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
