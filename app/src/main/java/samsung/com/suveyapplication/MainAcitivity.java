package samsung.com.suveyapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;

import com.samsung.object.Util;

/**
 * Created by SamSunger on 5/13/2015.
 */

public class MainAcitivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar= (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        if (drawer != null)
            drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        if (navigationView != null)
            navigationView.setNavigationItemSelectedListener(this);
        if (drawer != null)
            drawer.closeDrawer(GravityCompat.START);

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.frame_container, new MapsActivity()).commit();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        String flag = "";
        try {
            flag = intent.getStringExtra("add");
        } catch (Exception e) {

        }
        if (!TextUtils.isEmpty(flag)) {
            if (flag.equals("add")) {
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                if (drawer != null)
                    drawer.closeDrawer(GravityCompat.START);

                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.frame_container, new AddDealerAcitivity()).commit();
            } else if (flag.equals("list")) {
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                if (drawer != null)
                    drawer.closeDrawer(GravityCompat.START);

                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.frame_container, new ListDealersActivity()).commit();
            }
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer != null)
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            } else {
                super.onBackPressed();
            }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        Fragment fragment = null;

        int id = item.getItemId();

        if (id == R.id.list_all_dealers) {
            fragment = new AllDealerActivity();
            toolbar.setTitle("Listar Puntos de Venta");
        } else if (id == R.id.add_new_dealer) {
            fragment = new AddDealerAcitivity();
            toolbar.setTitle("Añadir Nuevo Punto de Venta");
        } else if (id == R.id.start_servey) {
            toolbar.setTitle("Iniciar Nueva Encuesta");
            fragment = new ListDealersActivity();
        } else if (id == R.id.sys_database) {
            Intent i = new Intent(getBaseContext(), LoadingActivity.class);
            i.putExtra("flag", "yes");
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
        } else if (id == R.id.general_map_view) {
            toolbar.setTitle("Mapa General");
            fragment = new MapsActivity();
        } else if (id == R.id.exit) {
            finish();
        } else if (id == R.id.report_servey) {
            toolbar.setTitle("Informes");
            fragment = new ListDateServeyFragement();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer != null)
            drawer.closeDrawer(GravityCompat.START);

        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.frame_container, fragment).commit();
        } else {
            Log.e("MainActivity", "Error in creating fragment");
        }
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Util.String_Date = "";
    }
}
