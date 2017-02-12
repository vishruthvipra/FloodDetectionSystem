package com.example.vishruth.flood;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MainScreen extends FragmentActivity implements OnMapReadyCallback {

    SharedPreferences shared1, shared2;
    Button button1, button2, button;
    private GoogleMap mMap;
    double latitude1 = 13.107500, longitude1 = 77.600280, latitude2 = 13.014539, longitude2 = 77.583891;
    LocationManager lm;
    float radius = 100000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final Context context = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(context, WebViewActivity.class);
                startActivity(intent);
            }
        });

        button1 = (Button) findViewById(R.id.button1);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(MainScreen.this, SubScreen1.class);
                startActivity(intent1);
            }
        });
        button2 = (Button) findViewById(R.id.button2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(MainScreen.this, SubScreen2.class);
                startActivity(intent2);
            }
        });
        shared1 = getSharedPreferences("app1",Context.MODE_PRIVATE);
        shared2 = getSharedPreferences("app2",Context.MODE_PRIVATE);
        setNotificationAlarm();
        IntentFilter intentFilter = new IntentFilter("com.example.vishruth.flood");
        registerReceiver(new ProximityReceiver(), intentFilter);

        /*Intent intent = new Intent(this, MyService.class);
        startService(intent);*/
    }

    private void setNotificationAlarm() {
        String locs = Context.LOCATION_SERVICE;
        lm = (LocationManager) getSystemService(locs);
        Intent intent = new Intent("com.example.vishruth.flood");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        int waterlevel1 = shared1.getInt("waterlevel1", 0);
        if(waterlevel1 >=10) {

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                lm.addProximityAlert(latitude1, longitude1, radius, -1, pendingIntent);

            }
        }
        int waterlevel2 = shared2.getInt("waterlevel2", 0);
        if(waterlevel2 >=10) {

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                lm.addProximityAlert(latitude2, longitude2, radius, -1, pendingIntent);

            }
        }
    }
    //public void startService (View view) {

     //   startService(intent);
   // }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            mMap.setMyLocationEnabled(true);

        }

        int waterlevel1 = shared1.getInt("waterlevel1", 0);
        int waterlevel2 = shared2.getInt("waterlevel2", 0);

        LatLng mark1 = new LatLng(latitude1, longitude1);
        LatLng mark2 = new LatLng(latitude2, longitude2);
        if(waterlevel1 >= 10)
            mMap.addMarker(new MarkerOptions().position(mark1).icon(BitmapDescriptorFactory.fromResource(R.drawable.red)).title("Yelahanka"));
        else
            mMap.addMarker(new MarkerOptions().position(mark1).icon(BitmapDescriptorFactory.fromResource(R.drawable.alpha2)).title("Yelahanka"));
        if(waterlevel2 >= 10)
            mMap.addMarker(new MarkerOptions().position(mark2).icon(BitmapDescriptorFactory.fromResource(R.drawable.red)).title("Mekhri Circle"));
        else
            mMap.addMarker(new MarkerOptions().position(mark2).icon(BitmapDescriptorFactory.fromResource(R.drawable.alpha2)).title("Mekhri Circle"));

        mMap.moveCamera(CameraUpdateFactory.newLatLng(mark1));

    }


}
