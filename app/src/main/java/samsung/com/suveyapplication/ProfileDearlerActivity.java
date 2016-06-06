package samsung.com.suveyapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.samsung.object.Dealer;
import com.samsung.object.Util;
import com.samsung.provider.SamsungProvider;
import com.samsung.table.tblEncuestaDatos;
import com.samsung.table.tblEncuestaDisenos;
import com.samsung.table.tblEncuestaPreguntas;
import com.samsung.table.tblEncuestaRespuestas;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * Created by SamSunger on 5/15/2015.
 */
public class ProfileDearlerActivity extends AppCompatActivity implements View.OnClickListener {
    private GoogleMap mMap; // Might be null if Google Play services APK is not available.

    private Dealer dealer;
    private TextView mName;
    private TextView mAddress;
    private TextView mCity;
    private Button mStartSerVey;
    private ImageButton mExit;
    private ImageButton mDealer;
    private String flag_check = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profiledealer);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Empezar Encuesta");

        mName = (TextView) findViewById(R.id.txtnameDealer);
        mAddress = (TextView) findViewById(R.id.txtAddress);
        mCity = (TextView) findViewById(R.id.txtCity);
        mStartSerVey = (Button) findViewById(R.id.btnstartServey);
        mStartSerVey.setOnClickListener(this);
        mExit = (ImageButton) findViewById(R.id.imbexit);
        mExit.setOnClickListener(this);
        mDealer = (ImageButton) findViewById(R.id.imbadddeler);
        mDealer.setOnClickListener(this);
        flag_check = getIntent().getStringExtra("test");
        Log.e("tuyenpx", "ss onCreate flag_check = " + flag_check);
        try {
            String s = getIntent().getStringExtra("alldealer");
            if (s.trim().equals("true")) {
                mStartSerVey.setVisibility(View.GONE);
            } else {
                mStartSerVey.setVisibility(View.VISIBLE);
            }
        } catch (Exception e) {
            Log.e("tuyenox", e.toString());
        }
        //setUpMapIfNeeded();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        try {
            String s = intent.getStringExtra("alldealer");
            if (s.trim().equals("true")) {
                mStartSerVey.setVisibility(View.GONE);
            } else {
                mStartSerVey.setVisibility(View.VISIBLE);
            }
        } catch (Exception e) {
            Log.e("tuyenox", e.toString());
        }

        try {
            flag_check = intent.getStringExtra("test");
        } catch (Exception e) {
            Log.e("tuyenox", e.toString());
        }
        Log.e("tuyenpx", "ss onNewIntent flag_check = " + flag_check);
    }

    private String ConverString(String date_date) {

        DateFormat originalFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss a",
                Locale.ENGLISH);
        DateFormat targetFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = null;
        try {
            date = originalFormat.parse(date_date);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        if (date == null) {
            DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.000");
            try {
                date = sdf.parse(date_date);
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        if (date == null) {
            return "";
        }


        return targetFormat.format(date);
    }


    private void checkTakedServey() {

        Log.e("tuyenpx", "tuyenpx called here checkTakedServey " + Util.DealerSelected.getPKID());

        String pkID = Util.DealerSelected.getPKID();
        SharedPreferences sharedPreferences = getSharedPreferences("question", Context.MODE_PRIVATE);
        String ID = sharedPreferences.getString("venderID", "1");

        Cursor c = getContentResolver().query(SamsungProvider.URI_ENCUESTADATOS, null, tblEncuestaDatos.PDV_ID + "=? AND " + tblEncuestaDatos.VENDEDOR_ID + " =?", new String[]{pkID, ID}, null);
        Log.e("tuyenpx", "ss flag_check = " + flag_check);
        Log.e("tuyenpx", "ss c.getcount = " + c.getCount());
        if (c != null & c.getCount() > 0 && flag_check.trim().equals("true")) {
            while (c.moveToNext()) {
                String Date = c.getString(c.getColumnIndexOrThrow(tblEncuestaDatos.FECHAHORA_ENCUESTA));
                Log.e("tuyenpx ", "getPKID pkID = " + pkID);
                Log.e("tuyenpx ", "compare Date = " + Date + "Util.String_Date_Servey.trim() " + Util.String_Date_Servey.trim());
                if (Util.String_Date_Servey.trim().equals(ConverString(Date).trim())) {

                    mStartSerVey.setVisibility(View.GONE);
                    mAddress.setText(getServeyName(c.getString(c.getColumnIndex(tblEncuestaDatos.DISENO_ID))));
                    insertQuestion(c.getString(c.getColumnIndex(tblEncuestaDatos.DISENO_ID)));
                    Log.e("tuyenpx", "mlistquestion size = " + arrayList.size());

                    for (int i = 0; i < arrayList.size(); i++) {

                        String value = c.getString(c.getColumnIndex("Pregunta0" + (i + 1)));
                        Log.e("tuyenpx", "colum_name = " + "Pregunta0" + (i + 1));
                        Log.e("tuyenpx", "value = " + value);

                        arrayList.get(i).Answer = getAnserName(value);
                    }
                    StringBuilder stringBuilder = new StringBuilder();

                    for (int i = 0; i < arrayList.size(); i++) {
                        stringBuilder.append(arrayList.get(i).Question + " : " + arrayList.get(i).Answer + "\n");
                    }
                    String value = stringBuilder.toString();
                    Log.e("tuyenpx", "value" + value);
                    mCity.setText(value);
                    Log.e("tuyenpx", "mCity = " + mCity.getText().toString());
                }

            }
        }
        c.close();


    }


    private String getAnserName(String id) {
        String date = "";
        Cursor d = getContentResolver().query(SamsungProvider.URI_ENCURESTA_RESPUESTAS, null, tblEncuestaRespuestas.PK_ID + "=?", new String[]{id}, null);
        if (d != null && d.getCount() > 0) {
            d.moveToFirst();
            date = d.getString(d.getColumnIndexOrThrow(tblEncuestaRespuestas.CODIGO));
            d.getCount();
        }
        return date;
    }


    private void insertQuestion(String densitos) {
        if (arrayList.size() > 0)
            arrayList.removeAll(arrayList);
        Cursor d = getContentResolver().query(SamsungProvider.URI_ENCUESTA_PREGUNTAS, null, tblEncuestaPreguntas.DISENO_ID + " =? ", new String[]{densitos}, null);
        Log.e("tuyenpx", "insertQuestion size = " + d.getCount() + "densitos = " + densitos);
        if (d != null && d.getCount() > 0)
            while (d.moveToNext()) {
                AnswerQuestion answerQuestion = new AnswerQuestion();
                answerQuestion.Question = d.getString(d.getColumnIndex(tblEncuestaPreguntas.TEXTO_PREGUNTA));
                arrayList.add(answerQuestion);
            }

        d.close();
    }


    ArrayList<AnswerQuestion> arrayList = new ArrayList();


    class AnswerQuestion {
        public String Question;
        public String Answer;

        public AnswerQuestion() {
        }
    }


    String getServeyName(String densitos) {
        String date = "";
        Cursor c = getContentResolver().query(SamsungProvider.URI_ENCUESTA_DISENOS, null, tblEncuestaDisenos.PK_ID + "=?", new String[]{densitos}, null);
        if (c != null & c.getCount() > 0) {
            c.moveToFirst();
            date = c.getString(c.getColumnIndex(tblEncuestaDisenos.NOMBRE));
            c.close();
        }

        return date;

    }

    @Override
    protected void onResume() {
        super.onResume();
        dealer = Util.DealerSelected;
        setUpMapIfNeeded();
        SetupView();
        checkTakedServey();
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        LocationListener listener = new LocationListener() {

            @Override
            public void onStatusChanged(String provider, int status,
                                        Bundle extras) {
            }

            @Override
            public void onProviderEnabled(String provider) {
            }

            @Override
            public void onProviderDisabled(String provider) {

            }

            @Override
            public void onLocationChanged(Location location) {
                Util.currentLat = location.getLatitude() + "";
                Util.currentLong = location.getLongitude() + "";
                //   Log.e("tuyenpx : lat and long", Util.currentLat+ Util.currentLong);
            }
        };
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                35, 0, listener);
    }

    private void SetupView() {
        mName.setText(dealer.getDealerName());
        mAddress.setText(dealer.getAdress());
        mCity.setText(dealer.getCity() + "," + dealer.getDistric());
    }

    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                mMap.setMyLocationEnabled(true);
                setUpMap();
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {

        Log.e("ProfileDealerActivity ", dealer.toString());
        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(Double.parseDouble(dealer.getLatitud().trim()), Double.parseDouble(dealer.getLongGitude().trim())))
                .title(dealer.getDealerName())
                .snippet(dealer.getAdress())
                .anchor(0.5f, 1)).showInfoWindow();
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(Double.parseDouble(dealer.getLatitud().trim()), Double.parseDouble(dealer.getLongGitude().trim())), 6);

// Zoom in, animating the camera.
        mMap.animateCamera(CameraUpdateFactory.zoomIn());

// Zoom out to zoom level 10, animating with a duration of 2 seconds.
        mMap.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);

// Construct a CameraPosition focusing on Panama City and animate the camera to that position.
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(Double.parseDouble(dealer.getLatitud().trim()), Double.parseDouble(dealer.getLongGitude().trim())))
                .zoom(17)                   // Sets the zoom
                .bearing(90)                // Sets the orientation of the camera to east
                .tilt(30)                   // Sets the tilt of the camera to 30 degrees
                .build();                   // Creates a CameraPosition from the builder
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imbadddeler:
                Intent i = new Intent(getBaseContext(), MainAcitivity.class);
                i.putExtra("add", "add");
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                break;
            case R.id.imbexit:
                finish();
                break;
            case R.id.btnstartServey:
                Intent ik = new Intent(getBaseContext(), ListServeyActivity.class);
                ik.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(ik);
                break;
            default:
                break;
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_add, menu);
        MenuItem menuItem = menu.findItem(R.id.add);
        menuItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent i = new Intent(getBaseContext(), MainAcitivity.class);
                i.putExtra("add", "add");
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                return true;
            }
        });

        return true;
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
