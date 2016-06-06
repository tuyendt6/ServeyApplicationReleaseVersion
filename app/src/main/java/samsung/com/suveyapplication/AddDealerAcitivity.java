package samsung.com.suveyapplication;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.samsung.object.Corregimientos;
import com.samsung.object.Dealer;
import com.samsung.object.Distrito;
import com.samsung.object.Util;
import com.samsung.provider.SamsungProvider;
import com.samsung.table.tblCorregimientos;
import com.samsung.table.tblDistritos;
import com.samsung.table.tblProvincias;
import com.samsung.table.tblPuntosDeVenta;
import com.samsung.table.tblVendedoresPorPuntosDeVenta;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by SamSunger on 5/13/2015.
 */
public class AddDealerAcitivity extends Fragment implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private EditText mName;
    private EditText mAdress;
    private EditText mEmail;
    private EditText mPhone;
    private ImageButton mAddDealer;
    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private LatLng mLatLng = null;
    private GoogleApiClient mGoogleApiClient;


    @Override
    public void onConnected(Bundle bundle) {
        Log.e("tuyenpx", "onConnected");

        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if (mLastLocation != null && mMap != null) {
            setUpMap();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.e("tuyenpx", "onConnectionSuspended");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.e("tuyenpx", "onConnectionFailed");
    }

    private Spinner mDistrito;
    private Spinner mCorregimientos;
    private ArrayList<Distrito> mListDistrito = new ArrayList<>();
    private ArrayAdapter<Distrito> arrayAdapter;


    private ArrayList<Corregimientos> mListCorregimientos = new ArrayList<>();
    private ArrayAdapter<Corregimientos> arrayAdapterCorregimientos;


    private Distrito mDistritos = null;
    private Corregimientos mCorregimientosItem = null;
    String email = "";
    private Location mLastLocation = null;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View RootView = inflater.inflate(R.layout.add_dealer_layout, container, false);
        setUpMapIfNeeded();
        setHasOptionsMenu(true);
        mName = (EditText) RootView.findViewById(R.id.txtaddname);
        mAdress = (EditText) RootView.findViewById(R.id.txtaddAddress);
        mEmail = (EditText) RootView.findViewById(R.id.txtaddemail);
        mPhone = (EditText) RootView.findViewById(R.id.txtaddphone);
        mAddDealer = (ImageButton) RootView.findViewById(R.id.imbadddeler);
        mDistrito = (Spinner) RootView.findViewById(R.id.spinner);
        mCorregimientos = (Spinner) RootView.findViewById(R.id.spinner2);
        mDistrito.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mDistritos = mListDistrito.get(position);
                setupCorregimi(mDistritos.getDistritoID());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        mCorregimientos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                mCorregimientosItem = mListCorregimientos.get(position);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        mAddDealer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String NameDealer = mName.getText().toString().trim();
                String Address = mAdress.getText().toString().trim();
                String Email = mEmail.getText().toString().trim();
                String Phone = mPhone.getText().toString().trim();
                if (NameDealer.equals("")) {
                    Toast.makeText(getActivity().getBaseContext(), "Name Can not null", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (Address.equals("")) {
                    Toast.makeText(getActivity().getBaseContext(), "Address Can not null", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (Email.equals("")) {
                    Toast.makeText(getActivity().getBaseContext(), "Email Can not null", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (Phone.equals("")) {
                    Toast.makeText(getActivity().getBaseContext(), "Phone Can not null", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (mLatLng == null) {
                    Toast.makeText(getActivity().getBaseContext(), "Press on map for setting location of Dealer", Toast.LENGTH_SHORT).show();
                    return;
                }

                Log.e("tuyenpx test Phone", Phone);
                Log.e("tuyenpx test Email", Email);
                Log.e("tuyenpx test ", "Description" + Address);
                email = Email;

                if (mDistritos == null) {
                    mDistritos = mListDistrito.get(0);
                }

                if (mCorregimientosItem == null) {
                    mCorregimientosItem = mListCorregimientos.get(0);
                }

                Dealer dealer = new Dealer("0", NameDealer, mDistritos.getDistritoName(), getProvice(mDistritos.getProvincialID()), Address, mLatLng.latitude + "", mLatLng.longitude + "", "True", "", Phone);
                Log.e("AddDealer : ", dealer.toString());
                //check dealer location :
                boolean flag = false;
                Cursor c = getActivity().getContentResolver().query(SamsungProvider.URI_PUNTOS_DEVENTA, new String[]{tblPuntosDeVenta.POSION_LAT, tblPuntosDeVenta.POSION_LON}, null, null, null);
                while (c.moveToNext()) {
                    String lat = c.getString(c.getColumnIndexOrThrow(tblPuntosDeVenta.POSION_LAT));
                    String Lang = c.getString(c.getColumnIndexOrThrow(tblPuntosDeVenta.POSION_LON));
                    if (dealer.getLatitud().trim().equals(lat.trim()) && (dealer.getLongGitude().equals(Lang.trim()))) {
                        flag = true;
                        break;
                    }
                }
                c.close();

                Log.e("AddDealer : ", "gia tri flag" + flag);

                if (flag == false) {
                    Util.DealerSelected = dealer;
                    DrawerLayout drawer = (DrawerLayout) getActivity().findViewById(R.id.drawer_layout);
                    if (drawer != null)
                        drawer.closeDrawer(GravityCompat.START);

                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.frame_container, new ListDealersActivity()).commit();


                    //add to database :
                    ContentValues values = new ContentValues();
                    values.put(tblPuntosDeVenta.ACTIVO, dealer.getStatus());// 2
                    values.put(tblPuntosDeVenta.DESCCION_ADICIONALES, "");// 3
                    values.put(tblPuntosDeVenta.DIRECCION, dealer.getAdress());// 4
                    values.put(tblPuntosDeVenta.DISTRITOID, mDistritos.getDistritoID());// 5
                    values.put(tblPuntosDeVenta.CORREGIMIENTOID, mCorregimientosItem.getCorregimientosID());
                    values.put(tblPuntosDeVenta.FACT_DEFT_ID, "");// 6
                    values.put(tblPuntosDeVenta.EMAIL1, email);// 6
                    values.put(tblPuntosDeVenta.NOMBRE, dealer.getDealerName());// 7
                    values.put(tblPuntosDeVenta.POSION_LAT, dealer.getLatitud());// 8
                    values.put(tblPuntosDeVenta.POSION_LON, dealer.getLongGitude());// 9
                    values.put(tblPuntosDeVenta.PROVINCIAID, mDistritos.getProvincialID());// 10
                    values.put(tblPuntosDeVenta.TELEFONO, dealer.getPhoneNumber());// 11
                    values.put(tblPuntosDeVenta.ISYS, "false");// 12
                    getActivity().getContentResolver().insert(SamsungProvider.URI_PUNTOS_DEVENTA, values);
                    new PostDealerData().execute(dealer);
                }
            }
        });

        if (mListDistrito.size() == 0) {
            setupPrinner();
        }
        if (mGoogleApiClient != null)
            mGoogleApiClient.connect();
        return RootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_add, menu);
        MenuItem searchItem = menu.findItem(R.id.add);
        searchItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                String NameDealer = mName.getText().toString().trim();
                String Address = mAdress.getText().toString().trim();
                String Email = mEmail.getText().toString().trim();
                String Phone = mPhone.getText().toString().trim();
                if (NameDealer.equals("")) {
                    Toast.makeText(getActivity().getBaseContext(), "Name Can not null", Toast.LENGTH_SHORT).show();
                    return false;
                }
                if (Address.equals("")) {
                    Toast.makeText(getActivity().getBaseContext(), "Address Can not null", Toast.LENGTH_SHORT).show();
                    return false;
                }
                if (Email.equals("")) {
                    Toast.makeText(getActivity().getBaseContext(), "Email Can not null", Toast.LENGTH_SHORT).show();
                    return false;
                }
                if (Phone.equals("")) {
                    Toast.makeText(getActivity().getBaseContext(), "Phone Can not null", Toast.LENGTH_SHORT).show();
                    return false;
                }
                if (mLatLng == null) {
                    Toast.makeText(getActivity().getBaseContext(), "Press on map for setting location of Dealer", Toast.LENGTH_SHORT).show();
                    return false;
                }

                Log.e("tuyenpx test Phone", Phone);
                Log.e("tuyenpx test Email", Email);
                Log.e("tuyenpx test ", "Description" + Address);
                email = Email;

                if (mDistritos == null) {
                    mDistritos = mListDistrito.get(0);
                }

                if (mCorregimientosItem == null) {
                    mCorregimientosItem = mListCorregimientos.get(0);
                }

                Dealer dealer = new Dealer("0", NameDealer, mDistritos.getDistritoName(), getProvice(mDistritos.getProvincialID()), Address, mLatLng.latitude + "", mLatLng.longitude + "", "True", "", Phone);
                Log.e("AddDealer : ", dealer.toString());
                //check dealer location :
                boolean flag = false;
                Cursor c = getActivity().getContentResolver().query(SamsungProvider.URI_PUNTOS_DEVENTA, new String[]{tblPuntosDeVenta.POSION_LAT, tblPuntosDeVenta.POSION_LON}, null, null, null);
                while (c.moveToNext()) {
                    String lat = c.getString(c.getColumnIndexOrThrow(tblPuntosDeVenta.POSION_LAT));
                    String Lang = c.getString(c.getColumnIndexOrThrow(tblPuntosDeVenta.POSION_LON));
                    if (dealer.getLatitud().trim().equals(lat.trim()) && (dealer.getLongGitude().equals(Lang.trim()))) {
                        flag = true;
                        break;
                    }
                }
                c.close();

                Log.e("AddDealer : ", "gia tri flag" + flag);

                if (flag == false) {
                    Util.DealerSelected = dealer;
                    //add to database :
                    ContentValues values = new ContentValues();
                    values.put(tblPuntosDeVenta.PK_ID, dealer.getPKID());// 2
                    values.put(tblPuntosDeVenta.ACTIVO, dealer.getStatus());// 2
                    values.put(tblPuntosDeVenta.DESCCION_ADICIONALES, "");// 3
                    values.put(tblPuntosDeVenta.DIRECCION, dealer.getAdress());// 4
                    values.put(tblPuntosDeVenta.DISTRITOID, mDistritos.getDistritoID());// 5
                    values.put(tblPuntosDeVenta.CORREGIMIENTOID, mCorregimientosItem.getCorregimientosID());
                    values.put(tblPuntosDeVenta.FACT_DEFT_ID, "");// 6
                    values.put(tblPuntosDeVenta.EMAIL1, email);// 6
                    values.put(tblPuntosDeVenta.NOMBRE, dealer.getDealerName());// 7
                    values.put(tblPuntosDeVenta.POSION_LAT, dealer.getLatitud());// 8
                    values.put(tblPuntosDeVenta.POSION_LON, dealer.getLongGitude());// 9
                    values.put(tblPuntosDeVenta.PROVINCIAID, mDistritos.getProvincialID());// 10
                    values.put(tblPuntosDeVenta.TELEFONO, dealer.getPhoneNumber());// 11
                    values.put(tblPuntosDeVenta.ISYS, "false");// 12
                    getActivity().getContentResolver().insert(SamsungProvider.URI_PUNTOS_DEVENTA, values);
                    new PostDealerData().execute(dealer);
                }
                return true;
            }
        });
    }


    private void setUpMap() {
        Log.e("tuyen.px", "set up map");
//        mMap.addMarker(new MarkerOptions()
//                .position(new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude()))
//                .anchor(0.5f, 1));
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude()), 6);

// Zoom in, animating the camera.
        mMap.animateCamera(CameraUpdateFactory.zoomIn());

// Zoom out to zoom level 10, animating with a duration of 2 seconds.
        mMap.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);

// Construct a CameraPosition focusing on Panama City and animate the camera to that position.
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude()))
                .zoom(17)                   // Sets the zoom
                .bearing(90)                // Sets the orientation of the camera to east
                .tilt(30)                   // Sets the tilt of the camera to 30 degrees
                .build();                   // Creates a CameraPosition from the builder
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    private void setUpMapIfNeeded() {
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getChildFragmentManager()
                    .findFragmentById(R.id.map)).getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                mMap.setMyLocationEnabled(true);
                mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(LatLng latLng) {
                        mLatLng = latLng;
                        mMap.clear();
                        mMap.addMarker(new MarkerOptions()
                                .position(latLng)
                                .anchor(0.5f, 1));
                    }
                });

                if (mLastLocation != null) {
                    setUpMap();
                }

            }
        }
    }

    private int getConreginmientosID(String distritoID) {
        int result = 0;
        Cursor c = getActivity().getContentResolver().query(SamsungProvider.URI_CONREEGIMIENTOS, null, tblDistritos._ID + " =?", new String[]{distritoID + ""}, null);

        while (c.moveToNext()) {
            result = c.getInt(c.getColumnIndex(tblCorregimientos._ID));
        }
        return result;
    }


    class PostDealerData extends AsyncTask<Dealer, Void, Void> {
        @Override
        protected Void doInBackground(Dealer[] params) {
            Dealer dealer = params[0];
            Log.e("Adddealer : ", mDistritos.toString());
            // Create a new HttpClient and Post Header
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(
                    "http://www.tagzone.io/Ode/WebService.asmx/addNewPuntosDeVenta");
            try {
                List<NameValuePair> nameValuePair = new ArrayList<>(0);
                nameValuePair.add(new BasicNameValuePair(tblPuntosDeVenta.FACT_DEFT_ID, ""));
                nameValuePair.add(new BasicNameValuePair(tblPuntosDeVenta.NOMBRE, dealer.getDealerName()));
                nameValuePair.add(new BasicNameValuePair(tblPuntosDeVenta.DIRECCION, dealer.getAdress()));
                nameValuePair.add(new BasicNameValuePair(tblPuntosDeVenta.PROVINCIAID, mDistritos.getProvincialID()));
                nameValuePair.add(new BasicNameValuePair(tblPuntosDeVenta.DISTRITOID, mDistritos.getDistritoID()));
                nameValuePair.add(new BasicNameValuePair(tblPuntosDeVenta.CORREGIMIENTOID, mCorregimientosItem.getCorregimientosID()));
                nameValuePair.add(new BasicNameValuePair(tblPuntosDeVenta.ZONAID, ""));
                nameValuePair.add(new BasicNameValuePair(tblPuntosDeVenta.EMAIL1, email));
                nameValuePair.add(new BasicNameValuePair("Email2", ""));
                nameValuePair.add(new BasicNameValuePair(tblPuntosDeVenta.DESCCION_ADICIONALES, ""));
                nameValuePair.add(new BasicNameValuePair(tblPuntosDeVenta.TELEFONO, dealer.getPhoneNumber()));
                nameValuePair.add(new BasicNameValuePair(tblPuntosDeVenta.ACTIVO, dealer.getStatus()));
                nameValuePair.add(new BasicNameValuePair(tblPuntosDeVenta.POSION_LAT, dealer.getLatitud()));
                nameValuePair.add(new BasicNameValuePair(tblPuntosDeVenta.POSION_LON, dealer.getLongGitude()));


                httppost.setEntity(new UrlEncodedFormEntity(nameValuePair, "UTF-8"));
                // Execute HTTP Post Request
                HttpResponse response = httpclient.execute(httppost);
                response.getEntity();
                String resp = EntityUtils.toString(response.getEntity());
                if (resp.trim().contains("true")) {
                    ContentValues values = new ContentValues();
                    values.put(tblPuntosDeVenta.ISYS, "");// 12
                    getActivity().getContentResolver().update(SamsungProvider.URI_PUNTOS_DEVENTA, values,
                            tblPuntosDeVenta.NOMBRE + " =?", new String[]{
                                    dealer.getDealerName()});
                    Log.e("AddDealer", "upload susscess" + resp + dealer.toString());
                } else {
                    Log.e("AddDealer", "upload fail" + resp);
                }
            } catch (UnsupportedEncodingException e) {
                Log.e("AddDealer", "UnsupportedEncodingException fail" + e.toString());
            } catch (IOException e) {
                Log.e("AddDealer", "IOException fail" + e.toString());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            DrawerLayout drawer = (DrawerLayout) getActivity().findViewById(R.id.drawer_layout);
            if (drawer != null)
                drawer.closeDrawer(GravityCompat.START);

            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.frame_container, new AllDealerActivity()).commit();

        }
    }


    private String chuanHoaKetQua(String result) {
        result = result.replace("<?xml version=\"1.0\" encoding=\"utf-8\"?>", "");
        result = result.replace("<string xmlns=\"http://tempuri.org/\" />", "");
        return result;
    }


    private void setupPrinner() {

        if (mListDistrito != null) {
            mListDistrito.removeAll(mListDistrito);
        }

        arrayAdapter = new ArrayAdapter<Distrito>(getActivity().getBaseContext(), android.R.layout.simple_spinner_item, mListDistrito);
        mDistrito.setAdapter(arrayAdapter);
        Cursor c = getActivity().getContentResolver().query(SamsungProvider.URI_DISTRITOS, null, null, null, tblDistritos.NOMBRE);
        while (c.moveToNext()) {
            Distrito distrito = new Distrito(c.getString(c.getColumnIndexOrThrow(tblDistritos.PK_ID)), c.getString(c.getColumnIndexOrThrow(tblDistritos.PROVINCIA_ID)), c.getString(c.getColumnIndexOrThrow(tblDistritos.NOMBRE)));
            if (!mListDistrito.contains(distrito)) {
                Log.e("Adddelaer : ", distrito.toString() + "DistroID" + distrito.getDistritoID() + "ProvinID" + distrito.getProvincialID());
                mListDistrito.add(distrito);
            }
        }
        arrayAdapter.notifyDataSetChanged();
        c.close();
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        String DistritoID = getDistritFromSeler();

        for (int i = 0; i < mListDistrito.size(); i++) {
            if (mListDistrito.get(i).getDistritoID().trim().equals(DistritoID.trim())) {
                Log.e("setSelection(i)", mListDistrito.get(i).toString());
                mDistrito.setSelection(i);
                break;
            }
        }
        Log.e("setupPrinner", "DistritoID = " + DistritoID);
        setupCorregimi(DistritoID);
    }


    private void setupCorregimi(String DistritoID) {

        if (mListCorregimientos != null) {
            mListCorregimientos.removeAll(mListCorregimientos);
        }

        arrayAdapterCorregimientos = new ArrayAdapter<>(getActivity().getBaseContext(), android.R.layout.simple_spinner_item, mListCorregimientos);
        mCorregimientos.setAdapter(arrayAdapterCorregimientos);
        Cursor c = getActivity().getContentResolver().query(SamsungProvider.URI_CONREEGIMIENTOS, null, tblCorregimientos.DISTRITOID + " =? ", new String[]{DistritoID}, tblDistritos.NOMBRE);
        while (c.moveToNext()) {
            Corregimientos distrito = new Corregimientos(c.getString(c.getColumnIndexOrThrow(tblCorregimientos.PK_ID)), c.getString(c.getColumnIndexOrThrow(tblCorregimientos.DISTRITOID)), c.getString(c.getColumnIndexOrThrow(tblCorregimientos.NOMBRE)));
            if (!mListCorregimientos.contains(distrito)) {
                Log.e("Adddelaer : ", distrito.toString() + " DistroID = " + distrito.getDistritoID() + " CorregimientosID = " + distrito.getCorregimientosID());
                mListCorregimientos.add(distrito);
            }
        }
        arrayAdapterCorregimientos.notifyDataSetChanged();
        c.close();
        arrayAdapterCorregimientos.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
    }


    private String getProvice(String ProvinceID) {
        String name = "";
        Cursor c = getActivity().getContentResolver().query(SamsungProvider.URI_PROVINCIAS, null, tblProvincias.PK_ID + "=?", new String[]{ProvinceID}, null);
        while (c.moveToNext()) {
            name = c.getString(c.getColumnIndexOrThrow(tblProvincias.NOMBRE));
        }
        c.close();
        return name;

    }


    private String getDistritFromSeler() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("question", Context.MODE_PRIVATE);
        String ID = sharedPreferences.getString("venderID", "1");
        String PDVID = "1";
        Cursor c = getActivity().getContentResolver().query(SamsungProvider.URI_VENDEDORES_POR_PUNTOS_DEVENTA, null, tblVendedoresPorPuntosDeVenta.VENDEDOR_ID + "=?", new String[]{ID}, null);
        while (c.moveToNext()) {
            PDVID = c.getString(c.getColumnIndex(tblVendedoresPorPuntosDeVenta.PDVID));
        }
        c.close();
        Log.e("getDistritFromSeler", "PDVID = " + PDVID);
        String DistritoID = "0";

        Cursor d = getActivity().getContentResolver().query(SamsungProvider.URI_PUNTOS_DEVENTA, null, tblPuntosDeVenta.PK_ID + " = " + Integer.parseInt(PDVID), null, null);
        Log.e("getDistritFromSeler", "cusor count d = " + d.getCount());
        while (d.moveToNext()) {
            DistritoID = d.getString(d.getColumnIndex(tblPuntosDeVenta.DISTRITOID));
        }
        d.close();
        return DistritoID;
    }

}
