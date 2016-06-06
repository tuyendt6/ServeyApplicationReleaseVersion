package samsung.com.suveyapplication;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.samsung.object.Util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;


/**
 * Created by Computer on 5/31/2016.
 */


public class ListDateServeyFragement extends Fragment {

    private ListView mListView;
    private TextView textView1;
    private TextView textView2;
    private ArrayList<String> mListDate = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View RootView = inflater.inflate(R.layout.listdealer_layout, container, false);

        textView1 = (TextView) RootView.findViewById(R.id.textView10);
        textView1.setVisibility(View.VISIBLE);
        textView1.setText("Informe de Encuestas");

        textView2 = (TextView) RootView.findViewById(R.id.textView11);
        textView2.setVisibility(View.VISIBLE);
        textView2.setText("Selecione la Fecha");

        mListView = (ListView) RootView.findViewById(R.id.listView);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                DrawerLayout drawer = (DrawerLayout) getActivity().findViewById(R.id.drawer_layout);
                if (drawer != null)
                    drawer.closeDrawer(GravityCompat.START);
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.frame_container, new AllDealerServey()).commit();
                Util.String_Date_Servey = mListDate.get(position);

            }
        });

        setupViewDate();
        return RootView;
    }

    private void setupListDate() {
        Calendar calendar = Calendar.getInstance();
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

}
