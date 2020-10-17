package com.example.accelerometer;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.widget.LoginButton;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareButton;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.net.URL;

public class MainActivity extends AppCompatActivity implements SensorEventListener, LocationListener {

    public static final String TAG = "MainActivity";

    public SensorManager sensorManager;
    Sensor accelerometer;
    SmsManager manager;
    LocationManager locationManager;
    TextView message;
    TextView locationText;
    LoginButton loginButton;
    CallbackManager callbackManager;
    ShareButton share;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       setContentView(R.layout.activity_main);

        //loginButton = findViewById(R.id.loginButton);
        callbackManager = CallbackManager.Factory.create();

        message = findViewById(R.id.message);
        locationText = findViewById(R.id.locationText);
        Log.d(TAG, "onCreate: Initializing Sensor Services");
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        manager = SmsManager.getDefault();

        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(MainActivity.this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        Log.d(TAG, "onCreate: Registered accelerometer listner");
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        Log.d(TAG, "onSensorChanged: X" + event.values[0] + "Y: " + event.values[1] + "Z: " + event.values[2]);

        if (event.values[0] > 15) {
            int i = 1;
            while (i != 0) {
                getLocationDetails();
                i--;
            }
        } else {
            message.setText("Shake the phone to send SOS");
        }

    }

    private void getLocationDetails() {
        try {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 500, 5, this);
            Toast.makeText(this, "Location Details is being fetched", Toast.LENGTH_SHORT).show();
        } catch (SecurityException e) {
            e.printStackTrace();
            Toast.makeText(this, "Location Details are facing problem to get Connected Try again later", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onLocationChanged(Location location) {

        locationText.setText("Current Location: " + location.getLatitude() + ", " + location.getLongitude());
        manager.sendTextMessage("+917814400611", "", "Your Friend Is In Danger" + "," + location.getLatitude() + "," + location.getLongitude(), null, null);
        String number = "7890034535";
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel: " + number));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        startActivity(intent);

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Toast.makeText(this, "Please Enable GPS and Internet", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        callbackManager.onActivityResult(requestCode, resultCode, data);
//
//        ShareLinkContent shareLinkContent = new ShareLinkContent.Builder()
//                .setContentUrl(Uri.parse("https://youtu.be/GHnOGZO9gz8"))
//                .build();
//        share.setShareContent(shareLinkContent);
//
//    }

}
