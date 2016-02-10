package ttagawa.proximitychat;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.util.Log;
import android.content.Intent;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MyActivity";
    private EditText ed;
    private LocationManager locationManager;
    private Location location;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        String provider = locationManager.getBestProvider(criteria, true);
        if(provider==null){
            Toast.makeText(this,"no provider",Toast.LENGTH_LONG);
        }else {
            location = locationManager.getLastKnownLocation(provider);
            Log.i(TAG, "is gps enabled:" + locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER));
            Log.i(TAG, "is network enabled:" + locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER));
            Log.i(TAG, "location: " + location.getAccuracy());
        }
        ed = (EditText) findViewById(R.id.editText);
        ed.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Button button = (Button)findViewById(R.id.button);
                if(!ed.getText().toString().trim().equals("")){
                    button.setVisibility(View.VISIBLE);
                }else{
                    button.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    public void startChat(View v){
        Intent intent = new Intent(this, MessageActivity.class);
        startActivity(intent);
    }



}
