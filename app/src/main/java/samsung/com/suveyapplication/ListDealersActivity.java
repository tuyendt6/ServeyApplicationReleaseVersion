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
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
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
import com.samsung.table.tblFrecuenciaVisitas;
import com.samsung.table.tblProvincias;
import com.samsung.table.tblPuntosDeVenta;
import com.samsung.table.tblVendedoresPorPuntosDeVenta;
import com.samsung.table.tblZonas;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by SamSunger on 5/13/2015.
 */
public class ListDealersActivity extends Fragment implements onSimpleSearchActionsListener, onSearchListener {
    private ArrayList<Dealer> mListDealer = new ArrayList<Dealer>();
    private DealerAdapter dealerAdapter;
    private ListView mListView;
    private ImageButton mAddDealer;
    private ImageButton mBack;

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

    private ImageButton mSearch;
    private EditText mTextSearch;

    private ArrayList<Dealer> mListDealerSearch = new ArrayList<Dealer>();
    private MaterialSearchView materialSearchView;
    private WindowManager mWindowManager;
    private boolean mSearchViewAdded = false;

    private ArrayList<String> mListDate = new ArrayList<>();

    private TextView textView1;
    private TextView textView2;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View RootView = inflater.inflate(R.layout.listdealer_layout, container, false);
        mSearch = (ImageButton) RootView.findViewById(R.id.btnsearch);
        mTextSearch = (EditText) RootView.findViewById(R.id.textsearch);

        textView1 = (TextView) RootView.findViewById(R.id.textView10);
        textView2 = (TextView) RootView.findViewById(R.id.textView11);


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


        mSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dealerAdapter.getFilter().filter(mTextSearch.getText().toString().trim());
                dealerAdapter.notifyDataSetChanged();
            }
        });

        mListView = (ListView) RootView.findViewById(R.id.listView);
        dealerAdapter = new DealerAdapter(getActivity().getBaseContext(), R.layout.dealeritem, mListDealer);
        mListView.setAdapter(dealerAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (!flag_click) {
                    Dealer dealer = dealerAdapter.getItem(position);
                    Log.e("ListDealersActivity.px", "tuyen.px " + dealer.toString());
                    Util.DealerSelected = dealer;
                    Intent i = new Intent(getActivity().getBaseContext(), ProfileDearlerActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    i.putExtra("test", "false");
                    startActivity(i);
                } else {
                    Log.e("ListDealersActivity.px", "tuyen.px  call it again");

                    flag_click = false;
                    DrawerLayout drawer = (DrawerLayout) getActivity().findViewById(R.id.drawer_layout);
                    if (drawer != null)
                        drawer.closeDrawer(GravityCompat.START);
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.frame_container, new ListDealersActivity()).commit();
                    Util.String_Date = mListDate.get(position);
                }
            }
        });
        mAddDealer = (ImageButton) RootView.findViewById(R.id.imbadddeler);
        mBack = (ImageButton) RootView.findViewById(R.id.imbexit);
        mAddDealer.setOnClickListener(new View.OnClickListener() {
                                          @Override
                                          public void onClick(View v) {

                                              DrawerLayout drawer = (DrawerLayout) getActivity().findViewById(R.id.drawer_layout);
                                              if (drawer != null)
                                                  drawer.closeDrawer(GravityCompat.START);

                                              FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                              fragmentManager.beginTransaction()
                                                      .replace(R.id.frame_container, new AddDealerAcitivity()).commit();


                                          }
                                      }

        );
        mBack.setOnClickListener(new View.OnClickListener()

                                 {
                                     @Override
                                     public void onClick(View v) {
                                     }
                                 }

        );
        if (mListDealer.size() > 0)

        {
            mListDealer.removeAll(mListDealer);
        }

        SetupView();

        return RootView;
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
        if (!flag_click) {
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
        if (c != null)
            if (c.getCount() == 0) {
                c.close();
                return;
            }

        ArrayList<String> arrayList = getListPDVID();
        if (c != null)
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
                    dealerAdapter.notifyDataSetChanged();
                }
            }
        if (c != null)
            c.close();

        if (mListDealer.size() == 0) {
            showDialog();
            flag_click = true;
            setupViewDate();
        }


    }

    private void setupListDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -1);
        mListDate.add(new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH).format(calendar.getTime().getTime()));
        calendar.add(Calendar.DATE, -1);
        mListDate.add(new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH).format(calendar.getTime().getTime()));
        calendar.add(Calendar.DATE, -1);
        mListDate.add(new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH).format(calendar.getTime().getTime()));
        calendar.add(Calendar.DATE, -1);
        mListDate.add(new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH).format(calendar.getTime().getTime()));
        calendar.add(Calendar.DATE, -1);
        mListDate.add(new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH).format(calendar.getTime().getTime()));
        calendar.add(Calendar.DATE, -1);
        mListDate.add(new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH).format(calendar.getTime().getTime()));
        calendar.add(Calendar.DATE, -1);
        mListDate.add(new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH).format(calendar.getTime().getTime()));
    }


    private void setupViewDate() {
        textView1.setVisibility(View.VISIBLE);
        textView2.setVisibility(View.VISIBLE);
        setupListDate();
        ArrayAdapter arrayAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, mListDate);
        mListView.setAdapter(arrayAdapter);
    }


    private boolean flag_click = false;

    private void showDialog() {
        new AlertDialog.Builder(getActivity())
                .setTitle("Notificación")
                .setMessage("No hay citas coordinadas para hoy. Por favor, llame su Supervisor")
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }


    private String getCurrentDay() {
        String date = "";
        if (Util.String_Date != "") {
            textView1.setVisibility(View.VISIBLE);
            textView1.setText("Encuestas : ");
            textView2.setVisibility(View.VISIBLE);
            textView2.setText(Util.String_Date);
            DateFormat originalFormat = new SimpleDateFormat("dd/MM/yyyy",
                    Locale.ENGLISH);
            DateFormat targetFormat = new SimpleDateFormat("EEEE", Locale.ENGLISH);
            Date dates = null;
            try {
                dates = originalFormat.parse(Util.String_Date);
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            date = targetFormat.format(dates);
            Util.String_Date = "";
        } else {
            textView1.setVisibility(View.GONE);
            textView2.setVisibility(View.GONE);
            SimpleDateFormat sdf = new SimpleDateFormat("EEEE", Locale.ENGLISH);
            Date d = new Date();
            date = sdf.format(d);
        }
        return date;
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
        if (c.getCount() == 0) {
            c.close();
            return mListPDVID;
        }
        mListPKID = c.getString(c.getColumnIndexOrThrow(tblFrecuenciaVisitas.PK_ID));
        // }
        Log.e("tuyenpx ", "tuyenpx _ tblFrecuenciaVisitas.PK_ID : " + mListPKID);

        c.close();
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
