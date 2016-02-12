package ttagawa.proximitychat;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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
    public static Location loc;
    private boolean enable;
    private boolean accurate = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        String provider = locationManager.getBestProvider(criteria, false);
        enable = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        Log.i(TAG,"enabled:"+enable);
        Button b = (Button) findViewById(R.id.GPSbutton);
        if(!enable){
            b.setText("Enable GPS");
        }else{
            b.setText("Disable GPS");
        }
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED){
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 10, locationListener);
            Log.i(TAG, "is gps enabled:" + locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER));
            Log.i(TAG, "is network enabled:" + locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER));
            if(loc!=null)
                Log.i(TAG, "location: " + loc.getAccuracy());
        }else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                Log.i(TAG, "please allow to use your location");

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        1);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = settings.edit();
        String userId = settings.getString("user_id", "");
        if(userId.equals("")){
            SecureRandomString ran = new SecureRandomString();
            userId = ran.nextString();
            editor.putString("user_id",userId);
            editor.commit();
        }
        String nickname = settings.getString("nickname", "");
        ed = (EditText) findViewById(R.id.editText);
        ed.setText(nickname);
        final Button button = (Button)findViewById(R.id.button);
        ed.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                //should also check if bool accurate is true
                if(accurate&&!ed.getText().toString().trim().equals("")){
                    button.setVisibility(View.VISIBLE);
                }else{
                    button.setVisibility(View.INVISIBLE);
                }
            }
        });
        if(accurate&&!ed.getText().toString().trim().equals("")){
            button.setVisibility(View.VISIBLE);
        }else{
            button.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onResume() {
        Log.i(TAG, "resuming");
        Button b = (Button) findViewById(R.id.GPSbutton);
        enable = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (!enable) {
            b.setText("Enable GPS");
        } else {
            b.setText("Disable GPS");
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 10, locationListener);
        }
        super.onResume();
    }
  /*  @Override
    public void onDestroy(){
        super.onDestroy();
    }*/

    LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            Log.i(TAG,"accuracy:"+location.getAccuracy());
            if(location.getAccuracy()<=50){
                accurate = true;
                loc=location;
                Button button = (Button)findViewById(R.id.button);
                if(accurate&&!ed.getText().toString().trim().equals("")){
                    button.setVisibility(View.VISIBLE);
                }
            }
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };

    public void startChat(View v){
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("nickname",ed.getText().toString().trim());
        editor.commit();
        Log.i(TAG,"nickname:"+settings.getString("nickname",""));
        Intent intent = new Intent(this, MessageActivity.class);
        startActivity(intent);
    }

    public void toggleGPS(View v){
        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        startActivity(intent);
        Button button = (Button) findViewById(R.id.GPSbutton);
        enable = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if(enable){
            button.setText("Disable GPS");
        }else{
            button.setText("Enable GPS");
        }
    }



}
