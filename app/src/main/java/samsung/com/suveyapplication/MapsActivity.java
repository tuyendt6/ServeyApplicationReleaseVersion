package samsung.com.suveyapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.ui.IconGenerator;
import com.samsung.object.Dealer;
import com.samsung.object.Util;
import com.samsung.provider.SamsungProvider;
import com.samsung.table.tblDistritos;
import com.samsung.table.tblFrecuenciaVisitas;
import com.samsung.table.tblProvincias;
import com.samsung.table.tblPuntosDeVenta;
import com.samsung.table.tblVendedoresPorPuntosDeVenta;
import com.samsung.table.tblZonas;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class MapsActivity extends Fragment {

    private ArrayList<Dealer> mListDealer = new ArrayList<Dealer>();
    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private static final LatLng PANAMA = new LatLng(8.982861, -79.526903);
    private static final LatLng PTY_VIEW = new LatLng(8.982861, -79.526903);


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View RootView = inflater.inflate(R.layout.activity_maps, container, false);
        SetupView();
        setUpMapIfNeeded();
        return RootView;
    }


    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p/>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = (((SupportMapFragment) getChildFragmentManager()
                    .findFragmentById(R.id.map)).getMap());
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                mMap.setMyLocationEnabled(true);

                // Obtain the map from a MapFragment or MapView.

// Move the camera instantly to PANAMA with a zoom of 6.
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(PANAMA, 6));

// Zoom in, animating the camera.
                mMap.animateCamera(CameraUpdateFactory.zoomIn());

// Zoom out to zoom level 10, animating with a duration of 2 seconds.
                mMap.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);

// Construct a CameraPosition focusing on Panama City and animate the camera to that position.
                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(PTY_VIEW)           // Sets the center of the map to Panama City
                        .zoom(17)                   // Sets the zoom
                        .bearing(90)                // Sets the orientation of the camera to east
                        .tilt(30)                   // Sets the tilt of the camera to 30 degrees
                        .build();                   // Creates a CameraPosition from the builder
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                setUpMap();
                mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker) {

                        for (Dealer dealer : mListDealer) {
                            if (Double.parseDouble(dealer.getLatitud()) == marker.getPosition().latitude && Double.parseDouble(dealer.getLongGitude()) == marker.getPosition().longitude) {
                                Dealer dealer1 = dealer;
                                Util.DealerSelected = dealer1;
                                Intent i = new Intent(getActivity().getBaseContext(), ProfileDearlerActivity.class);
                                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                i.putExtra("test","false");
                                startActivity(i);
                                break;
                            }
                        }
                        return false;
                    }
                });
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
        for (Dealer dealer : mListDealer) {
            Log.e("tuyen.px", "dealer.getLatitud().trim()" + dealer.getLatitud().trim() + "dealer.getLongGitude().trim()" + dealer.getLongGitude().trim() + "dealer.getDealerName()" + dealer.getDealerName() + "dealer.getAdress()" + dealer.getAdress());

            TextView textView = new TextView(getActivity());
            textView.setText(Html.fromHtml("<b>" + dealer.getDealerName() + "</b> <br>" + dealer.getAdress()));
            textView.setTextColor(Color.WHITE);
            textView.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
            textView.setBackgroundColor(getActivity().getResources().getColor(R.color.colorPrimary));
            IconGenerator factory = new IconGenerator(getActivity());
            factory.setContentView(textView);
            Bitmap icon = factory.makeIcon();

            mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(Double.parseDouble(dealer.getLatitud().trim()), Double.parseDouble(dealer.getLongGitude().trim())))
                    .anchor(0.5f, 1).icon(BitmapDescriptorFactory.fromBitmap(icon)));
        }

        //  mMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));
    }

    private void SetupView() {
        String[] projections = new String[]{tblPuntosDeVenta.TBL_NAME + "." + tblPuntosDeVenta.PK_ID,
                tblPuntosDeVenta.TBL_NAME + "." + tblPuntosDeVenta.NOMBRE + " AS Name", tblDistritos.TBL_NAME + "." + tblDistritos.NOMBRE + " AS Dictrict", tblProvincias.TBL_NAME + "." + tblProvincias.NOMBRE + " AS Province",
                tblPuntosDeVenta.TBL_NAME + "." + tblPuntosDeVenta.DIRECCION, tblPuntosDeVenta.TBL_NAME + "." + tblPuntosDeVenta.POSION_LAT, tblPuntosDeVenta.TBL_NAME + "." + tblPuntosDeVenta.POSION_LON,
                tblPuntosDeVenta.TBL_NAME + "." + tblPuntosDeVenta.ACTIVO, tblZonas.TBL_NAME + "." + tblZonas.NOMBRE + " AS Zone", tblPuntosDeVenta.TBL_NAME + "." + tblPuntosDeVenta.TELEFONO
        };
        Cursor c = getActivity().getContentResolver().query(SamsungProvider.URI_JOIN_DETAIL_ORDER, projections, null, null, tblPuntosDeVenta.TBL_NAME + "." + tblPuntosDeVenta.NOMBRE);
        if (c.getCount() == 0) {
            return;
        }

        ArrayList<String> arrayList = getListPDVID();

        while (c.moveToNext()) {
            String PK_ID = c.getString(c.getColumnIndexOrThrow(tblPuntosDeVenta.PK_ID));
            Log.e("tuyenpx ", "tuyenpx _ check dealer : " + PK_ID);
            if (arrayList.contains(PK_ID)) {
                Dealer dealer = new Dealer(PK_ID, c.getString(c.getColumnIndexOrThrow("Name")),
                        c.getString(c.getColumnIndexOrThrow("Dictrict")),
                        c.getString(c.getColumnIndexOrThrow("Province")),
                        c.getString(c.getColumnIndexOrThrow(tblPuntosDeVenta.DIRECCION)),
                        c.getString(c.getColumnIndexOrThrow(tblPuntosDeVenta.POSION_LAT)),
                        c.getString(c.getColumnIndexOrThrow(tblPuntosDeVenta.POSION_LON)),
                        c.getString(c.getColumnIndexOrThrow(tblPuntosDeVenta.ACTIVO)),
                        c.getString(c.getColumnIndexOrThrow("Zone")),
                        c.getString(c.getColumnIndexOrThrow(tblPuntosDeVenta.TELEFONO)));
                Log.e("ListDealersActivity ", dealer.toString());
                mListDealer.add(dealer);
            }
        }
        c.close();
    }

    private String getCurrentDay() {
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE", Locale.ENGLISH);
        Date d = new Date();
        return sdf.format(d);
    }

    private int getCurrentID() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("question", Context.MODE_PRIVATE);
        String id = sharedPreferences.getString("venderID", "1");
        return Integer.parseInt(id.trim());
    }

    private ArrayList<String> getListPDVID() {

        ArrayList<String> mListPDVID = new ArrayList<>();

        String mListPKID = "";
        Log.e("tuyenpx ", "tuyenpx _getCurrentDay() : " + getCurrentDay());
        Cursor c = getActivity().getContentResolver().query(SamsungProvider.URI_FRECUENCIA_VISITAS, null, tblFrecuenciaVisitas.CODIGO + " =?", new String[]{getCurrentDay()}, null);
        c.moveToFirst();
        mListPKID = c.getString(c.getColumnIndexOrThrow(tblFrecuenciaVisitas.PK_ID));
        if (c.getCount() == 0) {
            c.close();
            return mListPDVID;
        }
        // }
        Log.e("tuyenpx ", "tuyenpx _ tblFrecuenciaVisitas.PK_ID : " + mListPKID);

        Cursor d = getActivity().getContentResolver().query(SamsungProvider.URI_VENDEDORES_POR_PUNTOS_DEVENTA, null, tblVendedoresPorPuntosDeVenta.VENDEDOR_ID + " =?", new String[]{getCurrentID() + ""}, null);
        while (d.moveToNext()) {
            String PKID = d.getString(d.getColumnIndexOrThrow(tblVendedoresPorPuntosDeVenta.FRECUENCIA_VISITA_ID));
            String PDVID = d.getString(d.getColumnIndexOrThrow(tblVendedoresPorPuntosDeVenta.PDVID));

            String[] listday = PKID.split(",");
            for (String day : listday) {
                Log.e("tuyenpx ", "tuyenpx _ day  : " + mListPKID);

                if (day.trim().equalsIgnoreCase(mListPKID.trim())) {
                    mListPDVID.add(PDVID);
                    Log.e("tuyenpx ", "tuyenpx _add PDVID  : " + PDVID);
                    break;
                }
            }
        }
        d.close();
        return mListPDVID;
    }
}
