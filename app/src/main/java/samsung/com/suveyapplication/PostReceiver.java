package samsung.com.suveyapplication;

import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.samsung.provider.SamsungProvider;
import com.samsung.table.tblEncuestaDatos;
import com.samsung.table.tblPuntosDeVenta;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.util.ArrayList;
import java.util.List;

public class PostReceiver extends BroadcastReceiver {
    //chuoi kiem tra ket qua tra ve dong bo voi server
    private final String IS_SYS = "false";
    private Context mContext;

    private String chuanHoaKetQua(String result) {
        result = result.replace("<?xml version=\"1.0\" encoding=\"utf-8\"?>", "");
        result = result.replace("<string xmlns=\"http://tempuri.org/\" />", "");
        return result;
    }

    private Location mLocation;
    private Double lat = 0.0;
    private Double Lang = 0.0;
    private String provider;
    LocationListener locationListener;
    LocationManager locationManager;

    @Override
    public void onReceive(Context context, Intent intent) {
        mContext = context;
        if (isNetworkOnline()) {
            locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

            Criteria criteria = new Criteria();
            criteria.setPowerRequirement(Criteria.POWER_LOW); // Chose your desired power consumption level.
            criteria.setAccuracy(Criteria.ACCURACY_FINE); // Choose your accuracy requirement.
            criteria.setSpeedRequired(true); // Chose if speed for first location fix is required.
            criteria.setAltitudeRequired(false); // Choose if you use altitude.
            criteria.setBearingRequired(false); // Choose if you use bearing.
            criteria.setCostAllowed(false); // Choose if this provider can waste money :-)
            new PostDealerData().execute();

            provider = locationManager.getBestProvider(criteria, false);
            mLocation = locationManager.getLastKnownLocation(provider);
            if (mLocation != null) {
                lat = mLocation.getLatitude();
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
    }


    void doWorkWithNewLocation(Location location) {
        if (mLocation == null) {
            mLocation = location;
            return;
        }
        if (isBetterLocation(mLocation, location)) {
            mLocation = location;
        }
        if (mLocation != null) {
            lat = mLocation.getLatitude();
            Lang = mLocation.getLongitude();
            new PostDealerDatas().execute();
        }
    }

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

    public String getProviderName() {
        LocationManager locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);

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

    public void pauseRev() {
        locationManager.removeUpdates(locationListener);
    }

    public void resumeRev() {
        locationManager.requestLocationUpdates(provider, 400, 1,
                locationListener);
    }

    private boolean isNetworkOnline() {
        boolean status = false;
        try {
            ConnectivityManager cm = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getNetworkInfo(0);
            if (netInfo != null && netInfo.getState() == NetworkInfo.State.CONNECTED) {
                status = true;
            } else {
                netInfo = cm.getNetworkInfo(1);
                if (netInfo != null && netInfo.getState() == NetworkInfo.State.CONNECTED)
                    status = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return status;
    }


    class PostDealerData extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void[] params) {


            Cursor c = mContext.getContentResolver().query(SamsungProvider.URI_PUNTOS_DEVENTA, null, tblPuntosDeVenta.ISYS + "=?", new String[]{"false"}, null);

            while (c.moveToNext()) {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(
                        "http://www.tagzone.io/Ode/WebService.asmx/addNewPuntosDeVenta");
                try {
                    List<NameValuePair> nameValuePair = new ArrayList<>(0);
                    nameValuePair.add(new BasicNameValuePair(tblPuntosDeVenta.FACT_DEFT_ID, c.getString(c.getColumnIndexOrThrow(tblPuntosDeVenta.FACT_DEFT_ID))));
                    nameValuePair.add(new BasicNameValuePair(tblPuntosDeVenta.NOMBRE, c.getString(c.getColumnIndexOrThrow(tblPuntosDeVenta.NOMBRE))));
                    nameValuePair.add(new BasicNameValuePair(tblPuntosDeVenta.DIRECCION, c.getString(c.getColumnIndexOrThrow(tblPuntosDeVenta.DIRECCION))));
                    nameValuePair.add(new BasicNameValuePair(tblPuntosDeVenta.PROVINCIAID, c.getString(c.getColumnIndexOrThrow(tblPuntosDeVenta.PROVINCIAID))));
                    nameValuePair.add(new BasicNameValuePair(tblPuntosDeVenta.DISTRITOID, c.getString(c.getColumnIndexOrThrow(tblPuntosDeVenta.DISTRITOID))));
                    nameValuePair.add(new BasicNameValuePair(tblPuntosDeVenta.CORREGIMIENTOID, c.getString(c.getColumnIndexOrThrow(tblPuntosDeVenta.CORREGIMIENTOID))));
                    nameValuePair.add(new BasicNameValuePair(tblPuntosDeVenta.ZONAID, c.getString(c.getColumnIndexOrThrow(tblPuntosDeVenta.ZONAID))));
                    nameValuePair.add(new BasicNameValuePair(tblPuntosDeVenta.DESCCION_ADICIONALES, c.getString(c.getColumnIndexOrThrow(tblPuntosDeVenta.DESCCION_ADICIONALES))));
                    nameValuePair.add(new BasicNameValuePair(tblPuntosDeVenta.TELEFONO, c.getString(c.getColumnIndexOrThrow(tblPuntosDeVenta.TELEFONO))));
                    nameValuePair.add(new BasicNameValuePair(tblPuntosDeVenta.ACTIVO, c.getString(c.getColumnIndexOrThrow(tblPuntosDeVenta.ACTIVO))));
                    nameValuePair.add(new BasicNameValuePair(tblPuntosDeVenta.EMAIL1, c.getString(c.getColumnIndexOrThrow(tblPuntosDeVenta.EMAIL1))));
                    nameValuePair.add(new BasicNameValuePair("Email2", ""));
                    nameValuePair.add(new BasicNameValuePair(tblPuntosDeVenta.POSION_LAT, c.getString(c.getColumnIndexOrThrow(tblPuntosDeVenta.POSION_LAT))));
                    nameValuePair.add(new BasicNameValuePair(tblPuntosDeVenta.POSION_LON, c.getString(c.getColumnIndexOrThrow(tblPuntosDeVenta.POSION_LON))));


                    httppost.setEntity(new UrlEncodedFormEntity(nameValuePair, "UTF-8"));
                    // Execute HTTP Post Request
                    HttpResponse response = httpclient.execute(httppost);
                    response.getEntity();
                    String resp = EntityUtils.toString(response.getEntity());
                    if (resp.trim().contains("true")) {
                        ContentValues values = new ContentValues();
                        values.put(tblPuntosDeVenta.ISYS, "");// 12
                        mContext.getContentResolver().update(SamsungProvider.URI_PUNTOS_DEVENTA, values,
                                tblPuntosDeVenta.PK_ID + "=?", new String[]{
                                        c.getString(c.getColumnIndexOrThrow(tblPuntosDeVenta.PK_ID))});
                        Log.e("AddDealer", "upload offline" + resp);
                    }


                } catch (Exception e) {
                    Log.e("AddDealer", "upload fail" + e.toString());
                }
            }
            c.close();
            return null;
        }
    }

    class PostDealerDatas extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void[] params) {
            Cursor c = mContext.getContentResolver().query(SamsungProvider.URI_ENCUESTADATOS, null, tblEncuestaDatos.SYS + "=?", new String[]{"false"}, null);
            Log.e("tuyen.px", "lat and long : " + lat + " : " + Lang);
            while (c.moveToNext()) {

                // Create a new HttpClient and Post Header
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(
                        "http://www.tagzone.io/Ode/WebService.asmx/addNewEncuestaDatos");
                try {
                    List<NameValuePair> nameValuePair = new ArrayList<>(0);
                    nameValuePair.add(new BasicNameValuePair(tblEncuestaDatos.DISENO_ID, c.getString(c.getColumnIndexOrThrow(tblEncuestaDatos.DISENO_ID))));
                    nameValuePair.add(new BasicNameValuePair(tblEncuestaDatos.VENDEDOR_ID, c.getString(c.getColumnIndexOrThrow(tblEncuestaDatos.VENDEDOR_ID))));
                    nameValuePair.add(new BasicNameValuePair(tblEncuestaDatos.PDV_ID, c.getString(c.getColumnIndexOrThrow(tblEncuestaDatos.PDV_ID))));
                    nameValuePair.add(new BasicNameValuePair(tblEncuestaDatos.FECHAHORA_ENCUESTA, c.getString(c.getColumnIndexOrThrow(tblEncuestaDatos.FECHAHORA_ENCUESTA))));//update date vao day
                    if (lat == 0.0 || Lang == 0.0) {
                        lat = 8.982861;
                        Lang = -79.526903;
                    }
                    nameValuePair.add(new BasicNameValuePair(tblEncuestaDatos.POSICIONENCUESTA_LON, Lang + ""));
                    nameValuePair.add(new BasicNameValuePair(tblEncuestaDatos.POSICIONENCUESTA_LAT, lat + ""));
                    nameValuePair.add(new BasicNameValuePair(tblEncuestaDatos.FECHA_HORA_REGISTRO, c.getString(c.getColumnIndexOrThrow(tblEncuestaDatos.FECHA_HORA_REGISTRO))));//dont understand what mean ?
                    nameValuePair.add(new BasicNameValuePair(tblEncuestaDatos.POSICION_REGISTROLAT, lat + ""));
                    nameValuePair.add(new BasicNameValuePair(tblEncuestaDatos.POSOCION_REGISTRO_LON, Lang + ""));
                    nameValuePair.add(new BasicNameValuePair(tblEncuestaDatos.PREGUNTA_01, c.getString(c.getColumnIndexOrThrow(tblEncuestaDatos.PREGUNTA_01))));
                    nameValuePair.add(new BasicNameValuePair(tblEncuestaDatos.PREGUNTA_02, c.getString(c.getColumnIndexOrThrow(tblEncuestaDatos.PREGUNTA_02))));
                    nameValuePair.add(new BasicNameValuePair(tblEncuestaDatos.PREGUNTA_03, c.getString(c.getColumnIndexOrThrow(tblEncuestaDatos.PREGUNTA_03))));
                    nameValuePair.add(new BasicNameValuePair(tblEncuestaDatos.PREGUNTA_04, c.getString(c.getColumnIndexOrThrow(tblEncuestaDatos.PREGUNTA_04))));
                    nameValuePair.add(new BasicNameValuePair(tblEncuestaDatos.PREGUNTA_05, c.getString(c.getColumnIndexOrThrow(tblEncuestaDatos.PREGUNTA_05))));
                    nameValuePair.add(new BasicNameValuePair(tblEncuestaDatos.PREGUNTA_06, c.getString(c.getColumnIndexOrThrow(tblEncuestaDatos.PREGUNTA_06))));
                    nameValuePair.add(new BasicNameValuePair(tblEncuestaDatos.PREGUNTA_07, c.getString(c.getColumnIndexOrThrow(tblEncuestaDatos.PREGUNTA_07))));
                    nameValuePair.add(new BasicNameValuePair(tblEncuestaDatos.PREGUNTA_08, c.getString(c.getColumnIndexOrThrow(tblEncuestaDatos.PREGUNTA_08))));
                    nameValuePair.add(new BasicNameValuePair(tblEncuestaDatos.PREGUNTA_09, c.getString(c.getColumnIndexOrThrow(tblEncuestaDatos.PREGUNTA_09))));
                    nameValuePair.add(new BasicNameValuePair(tblEncuestaDatos.PREGUNTA_10, c.getString(c.getColumnIndexOrThrow(tblEncuestaDatos.PREGUNTA_10))));
                    httppost.setEntity(new UrlEncodedFormEntity(nameValuePair, "UTF-8"));
                    // Execute HTTP Post Request
                    HttpResponse response = httpclient.execute(httppost);
                    response.getEntity();
                    String resp = EntityUtils.toString(response.getEntity());
                    if (resp.trim().contains("true")) {
                        Log.e("AddDealer", "upload offline" + resp);
                        ContentValues contentValues = new ContentValues();
                        contentValues.put(tblEncuestaDatos.SYS, "true");
                        mContext.getContentResolver().update(SamsungProvider.URI_ENCUESTADATOS, contentValues, tblEncuestaDatos.DISENO_ID + "=?", new String[]{c.getString(c.getColumnIndexOrThrow(tblEncuestaDatos.DISENO_ID))});
                    }
                    Log.e("AddDealer", "upload offline" + resp);
                } catch (Exception e) {
                    Log.e("AddDealer", "upload fail" + e.toString());
                }
            }
            return null;
        }
    }
}