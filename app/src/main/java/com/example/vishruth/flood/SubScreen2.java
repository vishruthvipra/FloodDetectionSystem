package com.example.vishruth.flood;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class SubScreen2 extends Activity {

    int waterlevel2;
    SharedPreferences shared;
    TextView textElement1, textElement2, textElement3, textElement4, textElement5, textElement6;;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_screen2);
        StrictMode.enableDefaults();
        textElement1 = (TextView) findViewById(R.id.textView2);
        textElement2 = (TextView) findViewById(R.id.textView3);
        textElement3 = (TextView) findViewById(R.id.textView4);
        textElement4 = (TextView) findViewById(R.id.textView5);
        textElement5 = (TextView) findViewById(R.id.textView6);
        textElement6 = (TextView) findViewById(R.id.textView);

        shared=getSharedPreferences("app2", Context.MODE_PRIVATE);
        getData();
        SharedPreferences.Editor edit=shared.edit();
        edit.putInt("waterlevel2",waterlevel2);
        edit.commit();


        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("MM-dd HH:mm");
        String formattedDate = df.format(c.getTime());
        textElement5.setText("Last Updated on : " + formattedDate + "        ");

    }

    public void getData() {
        String result = "";
        InputStream inputStream;
        try {
            URL myurl = new URL("http://techpraja.com/getSensor2.php");
            HttpURLConnection urlConnection = (HttpURLConnection) myurl.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setDoInput(true);
            urlConnection.connect();
            inputStream = urlConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
            StringBuilder stringBuilder = new StringBuilder();
            String line = "";
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
            inputStream.close();
            result = stringBuilder.toString();
        } catch (Exception e) {
            Log.e("problem!", "Error" + e);
        }
        try {
            String s = "";
            JSONArray jsonArray = new JSONArray(result);
            /*for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                s = s + "Sensor id: " + jsonObject.getString("sensorid") + "\n" +
                        "Water level: " + jsonObject.getString("waterlevel") + "\n" +
                        "Temperature " + jsonObject.getString("temp") + "\n" +
                        "Humidity: " + jsonObject.getString("humidity") + "\n";
            }*/
            String sensor = "";
            String waterlevel = "";
            String temperature = "";
            String humdity = "";
            JSONObject jsonObject = jsonArray.getJSONObject(0);
            sensor = "Sensor ID [" + jsonObject.getString("sensor_id") + "]:\n";
            waterlevel2 = jsonObject.getInt("water_level");
            waterlevel = "Water level: " + jsonObject.getString("water_level") + "cm\n";
            temperature = "Temperature: " + jsonObject.getString("temperature") + " C\n";
            humdity = "Humidity: " + jsonObject.getString("humidity") + "%\n";
            textElement1.setText(sensor);
            textElement2.setText(waterlevel);
            textElement3.setText(temperature);
            textElement4.setText(humdity);
        } catch (Exception e) {
            Log.e("error", "error parsing data");
        }
    }
}

