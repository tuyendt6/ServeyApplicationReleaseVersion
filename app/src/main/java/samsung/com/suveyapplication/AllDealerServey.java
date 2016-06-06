package samsung.com.suveyapplication;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
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
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.samsung.adapter.DealerAdapter;
import com.samsung.customview.interfaces.onSearchListener;
import com.samsung.customview.interfaces.onSimpleSearchActionsListener;
import com.samsung.customview.widgets.MaterialSearchView;
import com.samsung.object.Dealer;
import com.samsung.object.Util;
import com.samsung.provider.SamsungProvider;
import com.samsung.table.tblDistritos;
import com.samsung.table.tblEncuestaDatos;
import com.samsung.table.tblProvincias;
import com.samsung.table.tblPuntosDeVenta;
import com.samsung.table.tblZonas;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Computer on 4/2/2016.
 */
public class AllDealerServey extends Fragment implements onSimpleSearchActionsListener, onSearchListener {

    private ArrayList<Dealer> mListDealer = new ArrayList<Dealer>();
    private DealerAdapter dealerAdapter;
    private ListView mListView;

    @Override
    public void onSearch(String query) {
        dealerAdapter.getFilter().filter(query.trim());
        dealerAdapter.notifyDataSetChanged();
    }

    @Override
    public void searchViewOpened() {

    }

    @Override
    public void searchViewClosed() {

    }

    @Override
    public void onCancelSearch() {
        materialSearchView.hide();
    }

    @Override
    public void onItemClicked(String item) {

    }

    @Override
    public void onScroll() {

    }

    @Override
    public void error(String localizedMessage) {

    }

    private MaterialSearchView materialSearchView;
    private WindowManager mWindowManager;
    private boolean mSearchViewAdded = false;
    private TextView textView1;
    private TextView textView2;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View RooView = inflater.inflate(R.layout.all_dealer_layout, container, false);

        textView1 = (TextView) RooView.findViewById(R.id.textView10);
        textView1.setVisibility(View.VISIBLE);
        textView1.setText("Encuestas : ");

        textView2 = (TextView) RooView.findViewById(R.id.textView11);
        textView2.setText(Util.String_Date_Servey);
        textView2.setVisibility(View.VISIBLE);

        setHasOptionsMenu(true);

        materialSearchView = new MaterialSearchView(getActivity());
        materialSearchView.setOnSearchListener(this);
        materialSearchView.setSearchResultsListener(this);
        materialSearchView.setHintText("Buscar");
        mWindowManager = (WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE);


        if (!mSearchViewAdded && mWindowManager != null) {
            mWindowManager.addView(materialSearchView,
                    MaterialSearchView.getSearchViewLayoutParams(getActivity()));
            mSearchViewAdded = true;
            Log.e("tuyenpx ", "mSearchViewAdded = materialSearchView added");


        }

        mListView = (ListView) RooView.findViewById(R.id.list_all_dealers);
        dealerAdapter = new DealerAdapter(getActivity().getBaseContext(), R.layout.dealeritem, mListDealer);
        mListView.setAdapter(dealerAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Dealer dealer = dealerAdapter.getItem(position);
                Log.e("ListDealersActivity.px", "tuyen.px " + dealer.toString());
                Util.DealerSelected = dealer;
                Intent i = new Intent(getActivity().getBaseContext(), ProfileDearlerActivity.class);
                i.putExtra("test", "true");
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
            }
        });
        if (mListDealer.size() > 0) {
            mListDealer.removeAll(mListDealer);
        }
        SetupView();


        return RooView;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mWindowManager != null) {
            mWindowManager = null;
        }
    }

    private boolean searchActive = false;
    private MenuItem searchItem;

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_home, menu);
        searchItem = menu.findItem(R.id.search);
        searchItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                materialSearchView.post(new Runnable() {
                    @Override
                    public void run() {
                        materialSearchView.display();
                        openKeyboard();
                    }
                });
                return true;
            }
        });
        if (searchActive)
            materialSearchView.display();

    }

    private String ConverString(String date_date) {

        Log.e("tuyenpx", "date_date = " + date_date);

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

    private void openKeyboard() {
        new Handler().postDelayed(new Runnable() {
            public void run() {
                materialSearchView.getSearchView().dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_DOWN, 0, 0, 0));
                materialSearchView.getSearchView().dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_UP, 0, 0, 0));
            }
        }, 200);
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
        while (c.moveToNext()) {
            String PK_ID = c.getString(c.getColumnIndexOrThrow(tblPuntosDeVenta.PK_ID));
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
            if (checkTakedServey(PK_ID)) {
                mListDealer.add(dealer);
                dealerAdapter.notifyDataSetChanged();
            }
        }
        c.close();

        if (mListDealer.size() == 0) {
            showDialog();
        }
    }

    private void showDialog() {
        new AlertDialog.Builder(getActivity())
                .setTitle("Notificación")
                .setMessage("No hay informes en el sistema")
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        DrawerLayout drawer = (DrawerLayout) getActivity().findViewById(R.id.drawer_layout);
                        if (drawer != null)
                            drawer.closeDrawer(GravityCompat.START);
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        fragmentManager.beginTransaction()
                                .replace(R.id.frame_container, new ListDateServeyFragement()).commit();
                        dialog.cancel();

                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    private boolean checkTakedServey(String pkID) {

        Log.e("tuyenpx", "PKID = " + pkID);

        boolean flag = false;
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("question", Context.MODE_PRIVATE);
        String ID = sharedPreferences.getString("venderID", "1");

        Cursor c = getActivity().getContentResolver().query(SamsungProvider.URI_ENCUESTADATOS, null, tblEncuestaDatos.PDV_ID + "=? AND " + tblEncuestaDatos.VENDEDOR_ID + " =?", new String[]{pkID, ID}, null);

        if (c != null & c.getCount() > 0) {
            while (c.moveToNext()) {
                String Date = c.getString(c.getColumnIndexOrThrow(tblEncuestaDatos.FECHAHORA_ENCUESTA));
                Log.e("tuyenpx ", "getPKID pkID = " + pkID);
                Log.e("tuyenpx ", "compare Date = " + Date + "Util.String_Date_Servey.trim() " + Util.String_Date_Servey.trim());
                if (Util.String_Date_Servey.trim().equals(ConverString(Date).trim())) {
                    flag = true;
                    break;
                }
            }
        }
        c.close();
        return flag;
    }
}
