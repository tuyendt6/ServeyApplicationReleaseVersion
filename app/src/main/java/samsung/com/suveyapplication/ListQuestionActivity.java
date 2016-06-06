package samsung.com.suveyapplication;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.samsung.adapter.QuestionAdapter;
import com.samsung.object.QuestionObject;
import com.samsung.object.Util;
import com.samsung.provider.SamsungProvider;
import com.samsung.table.tblEncuestaDatos;
import com.samsung.table.tblEncuestaPreguntas;
import com.samsung.table.tblEncuestaRespuestaGrupos;
import com.samsung.table.tblEncuestaRespuestas;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by SamSunger on 5/13/2015.
 */
public class ListQuestionActivity extends AppCompatActivity {
    private ArrayList<QuestionObject> mListQuestionOjbect = new ArrayList<QuestionObject>();
    private QuestionAdapter questionAdapter;
    private ListView mListView;
    private ImageButton mNewServey;
    private ImageButton mBack;

    private TextView mServeyName;
    private TextView mDescription;
    private LinearLayout linerFooter;
    private Button Save;
    private Button Cancel;


    private Location mLocation;
    private Double Lat = 0.0;
    private Double Lang = 0.0;
    private String provider;
    LocationListener locationListener;
    LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.question_layout);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Encuestas");

        mListView = (ListView) findViewById(R.id.listservey);
        mServeyName = (TextView) findViewById(R.id.tvQuestion);
        mDescription = (TextView) findViewById(R.id.tvdescription);
        mServeyName.setText(Util.ServeySelected.getNOMBRE());

        questionAdapter = new QuestionAdapter(getBaseContext(), R.layout.awser_item, mListQuestionOjbect);


        LayoutInflater inflater = getLayoutInflater();
        linerFooter = (LinearLayout) inflater.inflate(R.layout.food_layout, null);
        Save = (Button) linerFooter.findViewById(R.id.btnsave);
        Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insert_table_question();
                finish();
                new PostDealerData().execute();
            }

        });
        Cancel = (Button) linerFooter.findViewById(R.id.btncancel);
        Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mListView.addFooterView(linerFooter);

        mListView.setAdapter(questionAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            }
        });
        mNewServey = (ImageButton) findViewById(R.id.imbaddnewservey);
        mBack = (ImageButton) findViewById(R.id.imbexit);
        mNewServey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getBaseContext(), AddDealerAcitivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                finish();
            }
        });
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        Criteria criteria = new Criteria();
        criteria.setPowerRequirement(Criteria.POWER_LOW); // Chose your desired power consumption level.
        criteria.setAccuracy(Criteria.ACCURACY_FINE); // Choose your accuracy requirement.
        criteria.setSpeedRequired(true); // Chose if speed for first location fix is required.
        criteria.setAltitudeRequired(false); // Choose if you use altitude.
        criteria.setBearingRequired(false); // Choose if you use bearing.
        criteria.setCostAllowed(false); // Choose if this provider can waste money :-)


        provider = locationManager.getBestProvider(criteria, false);
        mLocation = locationManager.getLastKnownLocation(provider);
        if (mLocation != null) {
            Lat = mLocation.getLatitude();
            Lang = mLocation.getLongitude();
        }

        locationListener = new LocationListener() {

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            @Override
            public void onProviderEnabled(String provider) {
            }

            @Override
            public void onProviderDisabled(String provider) {
            }

            @Override
            public void onLocationChanged(Location location) {
                doWorkWithNewLocation(location);
            }
        };

        long minTime = 5 * 1000; // Minimum time interval for update in seconds, i.e. 5 seconds.
        long minDistance = 10; // Minimum distance change for update in meters, i.e. 10 meters.

        locationManager.requestLocationUpdates(getProviderName(), minTime,
                minDistance, locationListener);
    }

    static boolean postdata = false;

    static final int TIME_DIFFERENCE_THRESHOLD = 1 * 60 * 1000;

    boolean isBetterLocation(Location oldLocation, Location newLocation) {
        // If there is no old location, of course the new location is better.
        if (oldLocation == null) {
            return true;
        }

        // Check if new location is newer in time.
        boolean isNewer = newLocation.getTime() > oldLocation.getTime();

        // Check if new location more accurate. Accuracy is radius in meters, so less is better.
        boolean isMoreAccurate = newLocation.getAccuracy() < oldLocation.getAccuracy();
        if (isMoreAccurate && isNewer) {
            // More accurate and newer is always better.
            return true;
        } else if (isMoreAccurate && !isNewer) {
            // More accurate but not newer can lead to bad fix because of user movement.
            // Let us set a threshold for the maximum tolerance of time difference.
            long timeDifference = newLocation.getTime() - oldLocation.getTime();

            // If time difference is not greater then allowed threshold we accept it.
            if (timeDifference > -TIME_DIFFERENCE_THRESHOLD) {
                return true;
            }
        }

        return false;
    }


    void doWorkWithNewLocation(Location location) {
        if (mLocation == null) {
            mLocation = location;
        }
        if (isBetterLocation(mLocation, location)) {
            mLocation = location;
        }
        Log.e("tuyenpx", "da chay vao day : " + mLocation.getLongitude() + " post: " + postdata);

        if (mLocation != null) {
            Lat = mLocation.getLatitude();
            Lang = mLocation.getLongitude();
        }
    }


    public String getProviderName() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        Criteria criteria = new Criteria();
        criteria.setPowerRequirement(Criteria.POWER_LOW); // Chose your desired power consumption level.
        criteria.setAccuracy(Criteria.ACCURACY_FINE); // Choose your accuracy requirement.
        criteria.setSpeedRequired(true); // Chose if speed for first location fix is required.
        criteria.setAltitudeRequired(false); // Choose if you use altitude.
        criteria.setBearingRequired(false); // Choose if you use bearing.
        criteria.setCostAllowed(false); // Choose if this provider can waste money :-)

        // Provide your criteria and flag enabledOnly that tells
        // LocationManager only to return active providers.
        return locationManager.getBestProvider(criteria, true);
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (mListQuestionOjbect.size() == 0) {
            SetupView();
        }
    }

    private void SetupView() {
        // (String PK_ID, String NOMBRE, String DESCRIPCION, String ACTIVO, String TIMEDONE, String questionType, String questionTypeDescription, String questionAnswer)

        Cursor c = getContentResolver().query(SamsungProvider.URI_ENCUESTA_PREGUNTAS, null, tblEncuestaPreguntas.DISENO_ID + " =? ", new String[]{Util.ServeySelected.getPK_ID()}, null);
        if (c.getCount() == 0) {
            return;
        }
        while (c.moveToNext()) {
            //String PK_ID, String DISENO_ID, String POSICION_COLUMA, String NOMBRE_COLUMNA, String TEXTO_PREGUNTA, String q_TYPE, String questionType, String questionTypeDescription, String questionAnswer, int selected)
            QuestionObject questionObject = new QuestionObject(c.getString(c.getColumnIndexOrThrow(tblEncuestaPreguntas.PK_ID)),
                    c.getString(c.getColumnIndexOrThrow(tblEncuestaPreguntas.DISENO_ID)), c.getString(c.getColumnIndexOrThrow(tblEncuestaPreguntas.POSICION_COLUMA)),
                    c.getString(c.getColumnIndexOrThrow(tblEncuestaPreguntas.NOMBRE_COLUMNA)), c.getString(c.getColumnIndexOrThrow(tblEncuestaPreguntas.TEXTO_PREGUNTA)),
                    c.getString(c.getColumnIndexOrThrow(tblEncuestaPreguntas.Q_TYPE)), getQuestionType(c.getString(c.getColumnIndexOrThrow(tblEncuestaRespuestas.GRUPO_RESQUEST_AS_ID))), getQuestionTypeDes(c.getString(c.getColumnIndexOrThrow(tblEncuestaRespuestas.GRUPO_RESQUEST_AS_ID))),
                    "0", 1, c.getString(c.getColumnIndexOrThrow(tblEncuestaRespuestas.GRUPO_RESQUEST_AS_ID)));
            mDescription.setText(questionObject.getQuestionTypeDescription());
            mListQuestionOjbect.add(questionObject);
            questionAdapter.notifyDataSetChanged();
        }
        c.close();
    }


    private String getQuestionType(String ID) {
        Cursor d = getContentResolver().query(SamsungProvider.URI_ENCURESTA_RESPUESTAS, null, tblEncuestaRespuestas.GRUPO_RESQUEST_AS_ID + "=?", new String[]{ID}, null);
        String result = "";
        while (d.moveToNext()) {
            result = result + d.getString(d.getColumnIndexOrThrow(tblEncuestaRespuestas.DESCRIPCION)) + ";" + d.getString(d.getColumnIndexOrThrow(tblEncuestaRespuestas.PK_ID)) + "@@";
        }
        Log.e("tuyenpx","quesstion type = "+ result);
        d.close();
        result = result.substring(0, result.length() - 2);
        return result;
    }

    private String getQuestionTypeDes(String ID) {
        Cursor d = getContentResolver().query(SamsungProvider.URI_ENCURESTA_RESPUETAGRUPOS, null, tblEncuestaRespuestaGrupos.PK_ID + "=?", new String[]{ID}, null);
        String result = "";
        while (d.moveToNext()) {
            result = d.getString(d.getColumnIndexOrThrow(tblEncuestaRespuestaGrupos.DESCRIPCION));
        }
        d.close();
        return result;

    }

    boolean flag = false;

    class PostDealerData extends AsyncTask<Void, Void, Void> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void[] params) {
            SharedPreferences sharedPreferences = getSharedPreferences("question", Context.MODE_PRIVATE);
            String ID = sharedPreferences.getString("venderID", "1");
            // Create a new HttpClient and Post Header
            HttpClient httpclient = new DefaultHttpClient();

            Log.e("tuyenpx", " online int i = lat lang : " + Lat + Lang);
            HttpPost httppost = new HttpPost(
                    "http://www.tagzone.io/Ode/WebService.asmx/addNewEncuestaDatos");
            try {
                List<NameValuePair> nameValuePair = new ArrayList<>(0);
                Log.e("tuyenpx", "Util.ServeySelected.getPK_ID()" + Util.ServeySelected.getPK_ID());
                nameValuePair.add(new BasicNameValuePair(tblEncuestaDatos.DISENO_ID, Util.ServeySelected.getPK_ID()));
                nameValuePair.add(new BasicNameValuePair(tblEncuestaDatos.VENDEDOR_ID, ID));
                nameValuePair.add(new BasicNameValuePair(tblEncuestaDatos.PDV_ID, Util.DealerSelected.getPKID()));
                nameValuePair.add(new BasicNameValuePair(tblEncuestaDatos.FECHAHORA_ENCUESTA, getCurrentDateAndTime()));//update date vao day

                if (Lat == 0.0 || Lang == 0.0) {
                    Lat = 8.982861;
                    Lang = -79.526903;
                }
                nameValuePair.add(new BasicNameValuePair(tblEncuestaDatos.POSICIONENCUESTA_LON, Lang + ""));
                nameValuePair.add(new BasicNameValuePair(tblEncuestaDatos.POSICIONENCUESTA_LAT, Lat + ""));
                nameValuePair.add(new BasicNameValuePair(tblEncuestaDatos.FECHA_HORA_REGISTRO, getCurrentDateAndTime()));//dont understand what mean ?
                nameValuePair.add(new BasicNameValuePair(tblEncuestaDatos.POSICION_REGISTROLAT, ""));
                nameValuePair.add(new BasicNameValuePair(tblEncuestaDatos.POSOCION_REGISTRO_LON, ""));
                int i = mListQuestionOjbect.size();
                Log.e("tuyenpx", "int i = mListQuestionOjbect.get(0).getQuestionAnswer() " + mListQuestionOjbect.get(0).getQuestionAnswer());
                Log.e("tuyenpx", "int i = Util.DealerSelected.getPKID() " + Util.DealerSelected.getPKID());
                switch (i) {
                    case 0:
                        nameValuePair.add(new BasicNameValuePair(tblEncuestaDatos.PREGUNTA_01, ""));
                        nameValuePair.add(new BasicNameValuePair(tblEncuestaDatos.PREGUNTA_02, ""));
                        nameValuePair.add(new BasicNameValuePair(tblEncuestaDatos.PREGUNTA_03, ""));
                        nameValuePair.add(new BasicNameValuePair(tblEncuestaDatos.PREGUNTA_04, ""));
                        nameValuePair.add(new BasicNameValuePair(tblEncuestaDatos.PREGUNTA_05, ""));
                        nameValuePair.add(new BasicNameValuePair(tblEncuestaDatos.PREGUNTA_06, ""));
                        nameValuePair.add(new BasicNameValuePair(tblEncuestaDatos.PREGUNTA_07, ""));
                        nameValuePair.add(new BasicNameValuePair(tblEncuestaDatos.PREGUNTA_08, ""));
                        nameValuePair.add(new BasicNameValuePair(tblEncuestaDatos.PREGUNTA_09, ""));
                        nameValuePair.add(new BasicNameValuePair(tblEncuestaDatos.PREGUNTA_10, ""));
                        break;
                    case 1:
                        nameValuePair.add(new BasicNameValuePair(tblEncuestaDatos.PREGUNTA_01, mListQuestionOjbect.get(0).getQuestionAnswer()));
                        nameValuePair.add(new BasicNameValuePair(tblEncuestaDatos.PREGUNTA_02, ""));
                        nameValuePair.add(new BasicNameValuePair(tblEncuestaDatos.PREGUNTA_03, ""));
                        nameValuePair.add(new BasicNameValuePair(tblEncuestaDatos.PREGUNTA_04, ""));
                        nameValuePair.add(new BasicNameValuePair(tblEncuestaDatos.PREGUNTA_05, ""));
                        nameValuePair.add(new BasicNameValuePair(tblEncuestaDatos.PREGUNTA_06, ""));
                        nameValuePair.add(new BasicNameValuePair(tblEncuestaDatos.PREGUNTA_07, ""));
                        nameValuePair.add(new BasicNameValuePair(tblEncuestaDatos.PREGUNTA_08, ""));
                        nameValuePair.add(new BasicNameValuePair(tblEncuestaDatos.PREGUNTA_09, ""));
                        nameValuePair.add(new BasicNameValuePair(tblEncuestaDatos.PREGUNTA_10, ""));
                        break;
                    case 2:
                        nameValuePair.add(new BasicNameValuePair(tblEncuestaDatos.PREGUNTA_01, mListQuestionOjbect.get(0).getQuestionAnswer()));
                        nameValuePair.add(new BasicNameValuePair(tblEncuestaDatos.PREGUNTA_02, mListQuestionOjbect.get(1).getQuestionAnswer()));
                        nameValuePair.add(new BasicNameValuePair(tblEncuestaDatos.PREGUNTA_03, ""));
                        nameValuePair.add(new BasicNameValuePair(tblEncuestaDatos.PREGUNTA_04, ""));
                        nameValuePair.add(new BasicNameValuePair(tblEncuestaDatos.PREGUNTA_05, ""));
                        nameValuePair.add(new BasicNameValuePair(tblEncuestaDatos.PREGUNTA_06, ""));
                        nameValuePair.add(new BasicNameValuePair(tblEncuestaDatos.PREGUNTA_07, ""));
                        nameValuePair.add(new BasicNameValuePair(tblEncuestaDatos.PREGUNTA_08, ""));
                        nameValuePair.add(new BasicNameValuePair(tblEncuestaDatos.PREGUNTA_09, ""));
                        nameValuePair.add(new BasicNameValuePair(tblEncuestaDatos.PREGUNTA_10, ""));
                        break;
                    case 3:
                        nameValuePair.add(new BasicNameValuePair(tblEncuestaDatos.PREGUNTA_01, mListQuestionOjbect.get(0).getQuestionAnswer()));
                        nameValuePair.add(new BasicNameValuePair(tblEncuestaDatos.PREGUNTA_02, mListQuestionOjbect.get(1).getQuestionAnswer()));
                        nameValuePair.add(new BasicNameValuePair(tblEncuestaDatos.PREGUNTA_03, mListQuestionOjbect.get(2).getQuestionAnswer()));
                        nameValuePair.add(new BasicNameValuePair(tblEncuestaDatos.PREGUNTA_04, ""));
                        nameValuePair.add(new BasicNameValuePair(tblEncuestaDatos.PREGUNTA_05, ""));
                        nameValuePair.add(new BasicNameValuePair(tblEncuestaDatos.PREGUNTA_06, ""));
                        nameValuePair.add(new BasicNameValuePair(tblEncuestaDatos.PREGUNTA_07, ""));
                        nameValuePair.add(new BasicNameValuePair(tblEncuestaDatos.PREGUNTA_08, ""));
                        nameValuePair.add(new BasicNameValuePair(tblEncuestaDatos.PREGUNTA_09, ""));
                        nameValuePair.add(new BasicNameValuePair(tblEncuestaDatos.PREGUNTA_10, ""));
                        break;
                    case 4:
                        nameValuePair.add(new BasicNameValuePair(tblEncuestaDatos.PREGUNTA_01, mListQuestionOjbect.get(0).getQuestionAnswer()));
                        nameValuePair.add(new BasicNameValuePair(tblEncuestaDatos.PREGUNTA_02, mListQuestionOjbect.get(1).getQuestionAnswer()));
                        nameValuePair.add(new BasicNameValuePair(tblEncuestaDatos.PREGUNTA_03, mListQuestionOjbect.get(2).getQuestionAnswer()));
                        nameValuePair.add(new BasicNameValuePair(tblEncuestaDatos.PREGUNTA_04, mListQuestionOjbect.get(3).getQuestionAnswer()));
                        nameValuePair.add(new BasicNameValuePair(tblEncuestaDatos.PREGUNTA_05, ""));
                        nameValuePair.add(new BasicNameValuePair(tblEncuestaDatos.PREGUNTA_06, ""));
                        nameValuePair.add(new BasicNameValuePair(tblEncuestaDatos.PREGUNTA_07, ""));
                        nameValuePair.add(new BasicNameValuePair(tblEncuestaDatos.PREGUNTA_08, ""));
                        nameValuePair.add(new BasicNameValuePair(tblEncuestaDatos.PREGUNTA_09, ""));
                        nameValuePair.add(new BasicNameValuePair(tblEncuestaDatos.PREGUNTA_10, ""));
                        break;
                    case 5:
                        nameValuePair.add(new BasicNameValuePair(tblEncuestaDatos.PREGUNTA_01, mListQuestionOjbect.get(0).getQuestionAnswer()));
                        nameValuePair.add(new BasicNameValuePair(tblEncuestaDatos.PREGUNTA_02, mListQuestionOjbect.get(1).getQuestionAnswer()));
                        nameValuePair.add(new BasicNameValuePair(tblEncuestaDatos.PREGUNTA_03, mListQuestionOjbect.get(2).getQuestionAnswer()));
                        nameValuePair.add(new BasicNameValuePair(tblEncuestaDatos.PREGUNTA_04, mListQuestionOjbect.get(3).getQuestionAnswer()));
                        nameValuePair.add(new BasicNameValuePair(tblEncuestaDatos.PREGUNTA_05, mListQuestionOjbect.get(4).getQuestionAnswer()));
                        nameValuePair.add(new BasicNameValuePair(tblEncuestaDatos.PREGUNTA_06, ""));
                        nameValuePair.add(new BasicNameValuePair(tblEncuestaDatos.PREGUNTA_07, ""));
                        nameValuePair.add(new BasicNameValuePair(tblEncuestaDatos.PREGUNTA_08, ""));
                        nameValuePair.add(new BasicNameValuePair(tblEncuestaDatos.PREGUNTA_09, ""));
                        nameValuePair.add(new BasicNameValuePair(tblEncuestaDatos.PREGUNTA_10, ""));
                        break;
                    case 6:
                        nameValuePair.add(new BasicNameValuePair(tblEncuestaDatos.PREGUNTA_01, mListQuestionOjbect.get(0).getQuestionAnswer()));
                        nameValuePair.add(new BasicNameValuePair(tblEncuestaDatos.PREGUNTA_02, mListQuestionOjbect.get(1).getQuestionAnswer()));
                        nameValuePair.add(new BasicNameValuePair(tblEncuestaDatos.PREGUNTA_03, mListQuestionOjbect.get(2).getQuestionAnswer()));
                        nameValuePair.add(new BasicNameValuePair(tblEncuestaDatos.PREGUNTA_04, mListQuestionOjbect.get(3).getQuestionAnswer()));
                        nameValuePair.add(new BasicNameValuePair(tblEncuestaDatos.PREGUNTA_05, mListQuestionOjbect.get(4).getQuestionAnswer()));
                        nameValuePair.add(new BasicNameValuePair(tblEncuestaDatos.PREGUNTA_06, mListQuestionOjbect.get(5).getQuestionAnswer()));
                        nameValuePair.add(new BasicNameValuePair(tblEncuestaDatos.PREGUNTA_07, ""));
                        nameValuePair.add(new BasicNameValuePair(tblEncuestaDatos.PREGUNTA_08, ""));
                        nameValuePair.add(new BasicNameValuePair(tblEncuestaDatos.PREGUNTA_09, ""));
                        nameValuePair.add(new BasicNameValuePair(tblEncuestaDatos.PREGUNTA_10, ""));
                        break;
                    case 7:
                        nameValuePair.add(new BasicNameValuePair(tblEncuestaDatos.PREGUNTA_01, mListQuestionOjbect.get(0).getQuestionAnswer()));
                        nameValuePair.add(new BasicNameValuePair(tblEncuestaDatos.PREGUNTA_02, mListQuestionOjbect.get(1).getQuestionAnswer()));
                        nameValuePair.add(new BasicNameValuePair(tblEncuestaDatos.PREGUNTA_03, mListQuestionOjbect.get(2).getQuestionAnswer()));
                        nameValuePair.add(new BasicNameValuePair(tblEncuestaDatos.PREGUNTA_04, mListQuestionOjbect.get(3).getQuestionAnswer()));
                        nameValuePair.add(new BasicNameValuePair(tblEncuestaDatos.PREGUNTA_05, mListQuestionOjbect.get(4).getQuestionAnswer()));
                        nameValuePair.add(new BasicNameValuePair(tblEncuestaDatos.PREGUNTA_06, mListQuestionOjbect.get(5).getQuestionAnswer()));
                        nameValuePair.add(new BasicNameValuePair(tblEncuestaDatos.PREGUNTA_07, mListQuestionOjbect.get(6).getQuestionAnswer()));
                        nameValuePair.add(new BasicNameValuePair(tblEncuestaDatos.PREGUNTA_08, ""));
                        nameValuePair.add(new BasicNameValuePair(tblEncuestaDatos.PREGUNTA_09, ""));
                        nameValuePair.add(new BasicNameValuePair(tblEncuestaDatos.PREGUNTA_10, ""));
                        break;
                    case 8:
                        nameValuePair.add(new BasicNameValuePair(tblEncuestaDatos.PREGUNTA_01, mListQuestionOjbect.get(0).getQuestionAnswer()));
                        nameValuePair.add(new BasicNameValuePair(tblEncuestaDatos.PREGUNTA_02, mListQuestionOjbect.get(1).getQuestionAnswer()));
                        nameValuePair.add(new BasicNameValuePair(tblEncuestaDatos.PREGUNTA_03, mListQuestionOjbect.get(2).getQuestionAnswer()));
                        nameValuePair.add(new BasicNameValuePair(tblEncuestaDatos.PREGUNTA_04, mListQuestionOjbect.get(3).getQuestionAnswer()));
                        nameValuePair.add(new BasicNameValuePair(tblEncuestaDatos.PREGUNTA_05, mListQuestionOjbect.get(4).getQuestionAnswer()));
                        nameValuePair.add(new BasicNameValuePair(tblEncuestaDatos.PREGUNTA_06, mListQuestionOjbect.get(5).getQuestionAnswer()));
                        nameValuePair.add(new BasicNameValuePair(tblEncuestaDatos.PREGUNTA_07, mListQuestionOjbect.get(6).getQuestionAnswer()));
                        nameValuePair.add(new BasicNameValuePair(tblEncuestaDatos.PREGUNTA_08, mListQuestionOjbect.get(7).getQuestionAnswer()));
                        nameValuePair.add(new BasicNameValuePair(tblEncuestaDatos.PREGUNTA_09, ""));
                        nameValuePair.add(new BasicNameValuePair(tblEncuestaDatos.PREGUNTA_10, ""));
                        break;
                    case 9:
                        nameValuePair.add(new BasicNameValuePair(tblEncuestaDatos.PREGUNTA_01, mListQuestionOjbect.get(0).getQuestionAnswer()));
                        nameValuePair.add(new BasicNameValuePair(tblEncuestaDatos.PREGUNTA_02, mListQuestionOjbect.get(1).getQuestionAnswer()));
                        nameValuePair.add(new BasicNameValuePair(tblEncuestaDatos.PREGUNTA_03, mListQuestionOjbect.get(2).getQuestionAnswer()));
                        nameValuePair.add(new BasicNameValuePair(tblEncuestaDatos.PREGUNTA_04, mListQuestionOjbect.get(3).getQuestionAnswer()));
                        nameValuePair.add(new BasicNameValuePair(tblEncuestaDatos.PREGUNTA_05, mListQuestionOjbect.get(4).getQuestionAnswer()));
                        nameValuePair.add(new BasicNameValuePair(tblEncuestaDatos.PREGUNTA_06, mListQuestionOjbect.get(5).getQuestionAnswer()));
                        nameValuePair.add(new BasicNameValuePair(tblEncuestaDatos.PREGUNTA_07, mListQuestionOjbect.get(6).getQuestionAnswer()));
                        nameValuePair.add(new BasicNameValuePair(tblEncuestaDatos.PREGUNTA_08, mListQuestionOjbect.get(7).getQuestionAnswer()));
                        nameValuePair.add(new BasicNameValuePair(tblEncuestaDatos.PREGUNTA_09, mListQuestionOjbect.get(8).getQuestionAnswer()));
                        nameValuePair.add(new BasicNameValuePair(tblEncuestaDatos.PREGUNTA_10, ""));
                        break;
                    case 10:
                    default:
                        nameValuePair.add(new BasicNameValuePair(tblEncuestaDatos.PREGUNTA_01, mListQuestionOjbect.get(0).getQuestionAnswer()));
                        nameValuePair.add(new BasicNameValuePair(tblEncuestaDatos.PREGUNTA_02, mListQuestionOjbect.get(1).getQuestionAnswer()));
                        nameValuePair.add(new BasicNameValuePair(tblEncuestaDatos.PREGUNTA_03, mListQuestionOjbect.get(2).getQuestionAnswer()));
                        nameValuePair.add(new BasicNameValuePair(tblEncuestaDatos.PREGUNTA_04, mListQuestionOjbect.get(3).getQuestionAnswer()));
                        nameValuePair.add(new BasicNameValuePair(tblEncuestaDatos.PREGUNTA_05, mListQuestionOjbect.get(4).getQuestionAnswer()));
                        nameValuePair.add(new BasicNameValuePair(tblEncuestaDatos.PREGUNTA_06, mListQuestionOjbect.get(5).getQuestionAnswer()));
                        nameValuePair.add(new BasicNameValuePair(tblEncuestaDatos.PREGUNTA_07, mListQuestionOjbect.get(6).getQuestionAnswer()));
                        nameValuePair.add(new BasicNameValuePair(tblEncuestaDatos.PREGUNTA_08, mListQuestionOjbect.get(7).getQuestionAnswer()));
                        nameValuePair.add(new BasicNameValuePair(tblEncuestaDatos.PREGUNTA_09, mListQuestionOjbect.get(8).getQuestionAnswer()));
                        nameValuePair.add(new BasicNameValuePair(tblEncuestaDatos.PREGUNTA_10, mListQuestionOjbect.get(9).getQuestionAnswer()));
                        break;
                }

                httppost.setEntity(new UrlEncodedFormEntity(nameValuePair, "UTF-8"));
                // Execute HTTP Post Request
                HttpResponse response = httpclient.execute(httppost);
                response.getEntity();
                String resp = EntityUtils.toString(response.getEntity());
                if (resp.trim().contains("true")) {
                    Log.e("AddDealer", "upload succes" + resp);
                    flag = true;
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(tblEncuestaDatos.SYS, "true");
                    getContentResolver().update(SamsungProvider.URI_ENCUESTADATOS,contentValues,tblEncuestaDatos.DISENO_ID + "=?", new String[]{Util.ServeySelected.getPK_ID()});


                //    getContentResolver().update(SamsungProvider.URI_ENCUESTADATOS, tblEncuestaDatos.DISENO_ID + "=?", new String[]{Util.ServeySelected.getPK_ID()});
                } else {
                    Log.e("AddDealer", "upload fail" + resp);
                }
            } catch (Exception e) {
                Log.e("AddDealer", "upload fail" + e.toString());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (flag == true) {
                Intent i = new Intent(getBaseContext(), SuccesAcitivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                finish();
            } else {
                Intent i = new Intent(getBaseContext(), CancelActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                finish();
            }
        }
    }

    private String chuanHoaKetQua(String result) {
        result = result.replace("<?xml version=\"1.0\" encoding=\"utf-8\"?>", "");
        result = result.replace("<string xmlns=\"http://tempuri.org/\" />", "");
        return result;
    }

    //2015-05-15 00:00:00.000
    private String getCurrentDateAndTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.000");
        String currentDateandTime = sdf.format(new Date());
        return currentDateandTime;

    }

    private void insert_table_question() {
        SharedPreferences sharedPreferences = getSharedPreferences("question", Context.MODE_PRIVATE);
        String ID = sharedPreferences.getString("venderID", "1");

        ContentValues values = new ContentValues();
        values.put(tblEncuestaDatos.DISENO_ID, Util.ServeySelected.getPK_ID());
        values.put(tblEncuestaDatos.VENDEDOR_ID, ID);
        values.put(tblEncuestaDatos.PDV_ID, Util.DealerSelected.getPKID());
        values.put(tblEncuestaDatos.FECHAHORA_ENCUESTA, getCurrentDateAndTime());//update date vao day
        values.put(tblEncuestaDatos.POSICIONENCUESTA_LON, Util.currentLong);
        values.put(tblEncuestaDatos.POSICIONENCUESTA_LAT, Util.currentLat);
        values.put(tblEncuestaDatos.FECHA_HORA_REGISTRO, getCurrentDateAndTime());//dont understand what mean ?
        values.put(tblEncuestaDatos.POSICION_REGISTROLAT, "");
        values.put(tblEncuestaDatos.POSOCION_REGISTRO_LON, "");
        values.put(tblEncuestaDatos.SYS, "false");

        int i = mListQuestionOjbect.size();
        switch (i) {
            case 0:
                values.put(tblEncuestaDatos.PREGUNTA_01, "");
                values.put(tblEncuestaDatos.PREGUNTA_02, "");
                values.put(tblEncuestaDatos.PREGUNTA_03, "");
                values.put(tblEncuestaDatos.PREGUNTA_04, "");
                values.put(tblEncuestaDatos.PREGUNTA_05, "");
                values.put(tblEncuestaDatos.PREGUNTA_06, "");
                values.put(tblEncuestaDatos.PREGUNTA_07, "");
                values.put(tblEncuestaDatos.PREGUNTA_08, "");
                values.put(tblEncuestaDatos.PREGUNTA_09, "");
                values.put(tblEncuestaDatos.PREGUNTA_10, "");
                break;
            case 1:
                values.put(tblEncuestaDatos.PREGUNTA_01, mListQuestionOjbect.get(0).getQuestionAnswer());
                values.put(tblEncuestaDatos.PREGUNTA_02, "");
                values.put(tblEncuestaDatos.PREGUNTA_03, "");
                values.put(tblEncuestaDatos.PREGUNTA_04, "");
                values.put(tblEncuestaDatos.PREGUNTA_05, "");
                values.put(tblEncuestaDatos.PREGUNTA_06, "");
                values.put(tblEncuestaDatos.PREGUNTA_07, "");
                values.put(tblEncuestaDatos.PREGUNTA_08, "");
                values.put(tblEncuestaDatos.PREGUNTA_09, "");
                values.put(tblEncuestaDatos.PREGUNTA_10, "");
                break;
            case 2:
                values.put(tblEncuestaDatos.PREGUNTA_01, mListQuestionOjbect.get(0).getQuestionAnswer());
                values.put(tblEncuestaDatos.PREGUNTA_02, mListQuestionOjbect.get(1).getQuestionAnswer());
                values.put(tblEncuestaDatos.PREGUNTA_03, "");
                values.put(tblEncuestaDatos.PREGUNTA_04, "");
                values.put(tblEncuestaDatos.PREGUNTA_05, "");
                values.put(tblEncuestaDatos.PREGUNTA_06, "");
                values.put(tblEncuestaDatos.PREGUNTA_07, "");
                values.put(tblEncuestaDatos.PREGUNTA_08, "");
                values.put(tblEncuestaDatos.PREGUNTA_09, "");
                values.put(tblEncuestaDatos.PREGUNTA_10, "");
                break;
            case 3:
                values.put(tblEncuestaDatos.PREGUNTA_01, mListQuestionOjbect.get(0).getQuestionAnswer());
                values.put(tblEncuestaDatos.PREGUNTA_02, mListQuestionOjbect.get(1).getQuestionAnswer());
                values.put(tblEncuestaDatos.PREGUNTA_03, mListQuestionOjbect.get(2).getQuestionAnswer());
                values.put(tblEncuestaDatos.PREGUNTA_04, "");
                values.put(tblEncuestaDatos.PREGUNTA_05, "");
                values.put(tblEncuestaDatos.PREGUNTA_06, "");
                values.put(tblEncuestaDatos.PREGUNTA_07, "");
                values.put(tblEncuestaDatos.PREGUNTA_08, "");
                values.put(tblEncuestaDatos.PREGUNTA_09, "");
                values.put(tblEncuestaDatos.PREGUNTA_10, "");
                break;
            case 4:
                values.put(tblEncuestaDatos.PREGUNTA_01, mListQuestionOjbect.get(0).getQuestionAnswer());
                values.put(tblEncuestaDatos.PREGUNTA_02, mListQuestionOjbect.get(1).getQuestionAnswer());
                values.put(tblEncuestaDatos.PREGUNTA_03, mListQuestionOjbect.get(2).getQuestionAnswer());
                values.put(tblEncuestaDatos.PREGUNTA_04, mListQuestionOjbect.get(3).getQuestionAnswer());
                values.put(tblEncuestaDatos.PREGUNTA_05, "");
                values.put(tblEncuestaDatos.PREGUNTA_06, "");
                values.put(tblEncuestaDatos.PREGUNTA_07, "");
                values.put(tblEncuestaDatos.PREGUNTA_08, "");
                values.put(tblEncuestaDatos.PREGUNTA_09, "");
                values.put(tblEncuestaDatos.PREGUNTA_10, "");
                break;
            case 5:
                values.put(tblEncuestaDatos.PREGUNTA_01, mListQuestionOjbect.get(0).getQuestionAnswer());
                values.put(tblEncuestaDatos.PREGUNTA_02, mListQuestionOjbect.get(1).getQuestionAnswer());
                values.put(tblEncuestaDatos.PREGUNTA_03, mListQuestionOjbect.get(2).getQuestionAnswer());
                values.put(tblEncuestaDatos.PREGUNTA_04, mListQuestionOjbect.get(3).getQuestionAnswer());
                values.put(tblEncuestaDatos.PREGUNTA_05, mListQuestionOjbect.get(4).getQuestionAnswer());
                values.put(tblEncuestaDatos.PREGUNTA_06, "");
                values.put(tblEncuestaDatos.PREGUNTA_07, "");
                values.put(tblEncuestaDatos.PREGUNTA_08, "");
                values.put(tblEncuestaDatos.PREGUNTA_09, "");
                values.put(tblEncuestaDatos.PREGUNTA_10, "");
                break;
            case 6:
                values.put(tblEncuestaDatos.PREGUNTA_01, mListQuestionOjbect.get(0).getQuestionAnswer());
                values.put(tblEncuestaDatos.PREGUNTA_02, mListQuestionOjbect.get(1).getQuestionAnswer());
                values.put(tblEncuestaDatos.PREGUNTA_03, mListQuestionOjbect.get(2).getQuestionAnswer());
                values.put(tblEncuestaDatos.PREGUNTA_04, mListQuestionOjbect.get(3).getQuestionAnswer());
                values.put(tblEncuestaDatos.PREGUNTA_05, mListQuestionOjbect.get(4).getQuestionAnswer());
                values.put(tblEncuestaDatos.PREGUNTA_06, mListQuestionOjbect.get(5).getQuestionAnswer());
                values.put(tblEncuestaDatos.PREGUNTA_07, "");
                values.put(tblEncuestaDatos.PREGUNTA_08, "");
                values.put(tblEncuestaDatos.PREGUNTA_09, "");
                values.put(tblEncuestaDatos.PREGUNTA_10, "");
                break;
            case 7:
                values.put(tblEncuestaDatos.PREGUNTA_01, mListQuestionOjbect.get(0).getQuestionAnswer());
                values.put(tblEncuestaDatos.PREGUNTA_02, mListQuestionOjbect.get(1).getQuestionAnswer());
                values.put(tblEncuestaDatos.PREGUNTA_03, mListQuestionOjbect.get(2).getQuestionAnswer());
                values.put(tblEncuestaDatos.PREGUNTA_04, mListQuestionOjbect.get(3).getQuestionAnswer());
                values.put(tblEncuestaDatos.PREGUNTA_05, mListQuestionOjbect.get(4).getQuestionAnswer());
                values.put(tblEncuestaDatos.PREGUNTA_06, mListQuestionOjbect.get(5).getQuestionAnswer());
                values.put(tblEncuestaDatos.PREGUNTA_07, mListQuestionOjbect.get(6).getQuestionAnswer());
                values.put(tblEncuestaDatos.PREGUNTA_08, "");
                values.put(tblEncuestaDatos.PREGUNTA_09, "");
                values.put(tblEncuestaDatos.PREGUNTA_10, "");
                break;
            case 8:
                values.put(tblEncuestaDatos.PREGUNTA_01, mListQuestionOjbect.get(0).getQuestionAnswer());
                values.put(tblEncuestaDatos.PREGUNTA_02, mListQuestionOjbect.get(1).getQuestionAnswer());
                values.put(tblEncuestaDatos.PREGUNTA_03, mListQuestionOjbect.get(2).getQuestionAnswer());
                values.put(tblEncuestaDatos.PREGUNTA_04, mListQuestionOjbect.get(3).getQuestionAnswer());
                values.put(tblEncuestaDatos.PREGUNTA_05, mListQuestionOjbect.get(4).getQuestionAnswer());
                values.put(tblEncuestaDatos.PREGUNTA_06, mListQuestionOjbect.get(5).getQuestionAnswer());
                values.put(tblEncuestaDatos.PREGUNTA_07, mListQuestionOjbect.get(6).getQuestionAnswer());
                values.put(tblEncuestaDatos.PREGUNTA_08, mListQuestionOjbect.get(7).getQuestionAnswer());
                values.put(tblEncuestaDatos.PREGUNTA_09, "");
                values.put(tblEncuestaDatos.PREGUNTA_10, "");
                break;
            case 9:
                values.put(tblEncuestaDatos.PREGUNTA_01, mListQuestionOjbect.get(0).getQuestionAnswer());
                values.put(tblEncuestaDatos.PREGUNTA_02, mListQuestionOjbect.get(1).getQuestionAnswer());
                values.put(tblEncuestaDatos.PREGUNTA_03, mListQuestionOjbect.get(2).getQuestionAnswer());
                values.put(tblEncuestaDatos.PREGUNTA_04, mListQuestionOjbect.get(3).getQuestionAnswer());
                values.put(tblEncuestaDatos.PREGUNTA_05, mListQuestionOjbect.get(4).getQuestionAnswer());
                values.put(tblEncuestaDatos.PREGUNTA_06, mListQuestionOjbect.get(5).getQuestionAnswer());
                values.put(tblEncuestaDatos.PREGUNTA_07, mListQuestionOjbect.get(6).getQuestionAnswer());
                values.put(tblEncuestaDatos.PREGUNTA_08, mListQuestionOjbect.get(7).getQuestionAnswer());
                values.put(tblEncuestaDatos.PREGUNTA_09, mListQuestionOjbect.get(8).getQuestionAnswer());
                values.put(tblEncuestaDatos.PREGUNTA_10, "");
                break;
            case 10:
            default:
                values.put(tblEncuestaDatos.PREGUNTA_01, mListQuestionOjbect.get(0).getQuestionAnswer());
                values.put(tblEncuestaDatos.PREGUNTA_02, mListQuestionOjbect.get(1).getQuestionAnswer());
                values.put(tblEncuestaDatos.PREGUNTA_03, mListQuestionOjbect.get(2).getQuestionAnswer());
                values.put(tblEncuestaDatos.PREGUNTA_04, mListQuestionOjbect.get(3).getQuestionAnswer());
                values.put(tblEncuestaDatos.PREGUNTA_05, mListQuestionOjbect.get(4).getQuestionAnswer());
                values.put(tblEncuestaDatos.PREGUNTA_06, mListQuestionOjbect.get(5).getQuestionAnswer());
                values.put(tblEncuestaDatos.PREGUNTA_07, mListQuestionOjbect.get(6).getQuestionAnswer());
                values.put(tblEncuestaDatos.PREGUNTA_08, mListQuestionOjbect.get(7).getQuestionAnswer());
                values.put(tblEncuestaDatos.PREGUNTA_09, mListQuestionOjbect.get(8).getQuestionAnswer());
                values.put(tblEncuestaDatos.PREGUNTA_10, mListQuestionOjbect.get(9).getQuestionAnswer());
                break;
        }
        getContentResolver().insert(SamsungProvider.URI_ENCUESTADATOS, values);
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
