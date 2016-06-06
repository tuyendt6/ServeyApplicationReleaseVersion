package samsung.com.suveyapplication;

import android.content.Context;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
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

import com.samsung.adapter.DealerAdapter;
import com.samsung.customview.interfaces.onSearchListener;
import com.samsung.customview.interfaces.onSimpleSearchActionsListener;
import com.samsung.customview.widgets.MaterialSearchView;
import com.samsung.object.Dealer;
import com.samsung.object.Util;
import com.samsung.provider.SamsungProvider;
import com.samsung.table.tblDistritos;
import com.samsung.table.tblProvincias;
import com.samsung.table.tblPuntosDeVenta;
import com.samsung.table.tblZonas;

import java.util.ArrayList;

/**
 * Created by Computer on 4/2/2016.
 */
public class AllDealerActivity extends Fragment implements onSimpleSearchActionsListener, onSearchListener {

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


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View RooView = inflater.inflate(R.layout.all_dealer_layout, container, false);

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
                Dealer dealer = (Dealer) dealerAdapter.getItem(position);
                Log.e("ListDealersActivity.px", "tuyen.px " + dealer.toString());
                Util.DealerSelected = dealer;
                Intent i = new Intent(getActivity().getBaseContext(), ProfileDearlerActivity.class);
                i.putExtra("alldealer", "true");
                i.putExtra("test","false");
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
            mListDealer.add(dealer);
            dealerAdapter.notifyDataSetChanged();
        }
        c.close();
    }
}
