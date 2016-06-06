package samsung.com.suveyapplication;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.samsung.provider.SamsungProvider;
import com.samsung.table.tblVendedores;

/**
 * Created by SamSunger on 5/13/2015.
 */
public class LoginFailAcitivity extends AppCompatActivity {
    private EditText txtEmail;
    private Button btnOK;
    private Button btnCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_fail);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        txtEmail = (EditText) findViewById(R.id.txtemail);
        btnOK = (Button) findViewById(R.id.btnok);
        btnCancel = (Button) findViewById(R.id.btncancel);
        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txtEmail.getText().toString().equals("")) {
                    Toast.makeText(getBaseContext(), "Email is not null ! ", Toast.LENGTH_SHORT).show();
                } else {
                    boolean flag = checkEmail(txtEmail.getText().toString());
                    if (flag) {
                        Intent i = new Intent(getBaseContext(), MainAcitivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i);
                        finish();
                    } else {
                        txtEmail.setText("");
                        Toast.makeText(getBaseContext(), "Email is not correct ! ", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private boolean checkEmail(String email) {
        Cursor c = getContentResolver().query(SamsungProvider.URI_VENDEDORES, null, null, null, null);

        if (c.getCount() == 0) {
            return false;
        }

        while (c.moveToNext()) {
            String Email = c.getString(c.getColumnIndexOrThrow(tblVendedores.Email1));
            if (Email.equals(email)) {
                return true;
            }
        }
        return false;
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
