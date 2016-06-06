package samsung.com.suveyapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.samsung.object.Util;

/**
 * Created by SamSunger on 5/19/2015.
 */
public class ApterAddAcitivity extends AppCompatActivity implements View.OnClickListener {

    private TextView mName;
    private TextView mAddress;
    private TextView mCity;

    private Button mProfile;
    private Button mDealers;
    private Button mEncutas;
    private Button mMaps;


    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.after_adddealer_layout);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        mName = (TextView) findViewById(R.id.txtnameDealer);
        mAddress = (TextView) findViewById(R.id.txtAddress);
        mCity = (TextView) findViewById(R.id.txtCity);


        mName.setText(Util.DealerSelected.getDealerName());
        mAddress.setText(Util.DealerSelected.getAdress());
        mCity.setText(Util.DealerSelected.getCity() + "," + Util.DealerSelected.getDistric());


        mProfile = (Button) findViewById(R.id.btnviewprofile);
        mProfile.setOnClickListener(this);

        mDealers = (Button) findViewById(R.id.btndealers);
        mDealers.setOnClickListener(this);


        mEncutas = (Button) findViewById(R.id.btnEncuestas);
        mEncutas.setOnClickListener(this);

        mMaps = (Button) findViewById(R.id.btnmaps);
        mMaps.setOnClickListener(this);


    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnviewprofile:
                break;
            case R.id.btndealers:
                Intent i = new Intent(getBaseContext(), ProfileDearlerActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                break;
            case R.id.btnEncuestas:
                Intent ik = new Intent(getBaseContext(), ListServeyActivity.class);
                ik.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(ik);
                break;
            case R.id.btnmaps:
                Intent ik1 = new Intent(getBaseContext(), MapsActivity.class);
                ik1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(ik1);
                break;
        }

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
