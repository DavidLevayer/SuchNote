package android.uqacproject.com.suchnote;

import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.uqacproject.com.suchnote.audiofragment.AudioDialogFragment;
import android.uqacproject.com.suchnote.photofragment.PhotoDialogFragment;
import android.uqacproject.com.suchnote.textfragment.TextDialogFragment;
import android.uqacproject.com.suchnote.videofragment.VideoDialogFragment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.HashMap;


public class MainActivity extends ActionBarActivity implements SensorEventListener {

    public final static int AUDIO_FRAGMENT_ID = 0;
    public final static int VIDEO_FRAGMENT_ID = 1;
    public final static int PHOTO_FRAGMENT_ID = 2;
    public final static int TEXT_FRAGMENT_ID = 3;

    private HashMap<Integer,Object> sensorValues;
    private SensorManager mSensorManager;
    private Sensor mLightSensor;
    private WifiManager mWifiManager;
    private LocationManager mLocationManager;
    private LocationListener mLocationListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button b1 = (Button) findViewById(R.id.button1);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchDialogFragment(TEXT_FRAGMENT_ID);
            }
        });

        Button b2 = (Button) findViewById(R.id.button2);
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchDialogFragment(PHOTO_FRAGMENT_ID);
            }
        });

        Button b3 = (Button) findViewById(R.id.button3);
        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchDialogFragment(AUDIO_FRAGMENT_ID);
            }
        });

        Button b4 = (Button) findViewById(R.id.button4);
        b4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchDialogFragment(VIDEO_FRAGMENT_ID);
            }
        });

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mLightSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);

        mWifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);

        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        // Define a listener that responds to location updates
        mLocationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                location.getLatitude();
                ((TextView)findViewById(R.id.sensor_speed)).setText(String.valueOf(location.getSpeed()));
            }
            public void onStatusChanged(String provider, int status, Bundle extras) { }
            public void onProviderEnabled(String provider) { }
            public void onProviderDisabled(String provider) { }
        };


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void launchDialogFragment(int dialogFragmentId){

        FragmentManager fm = getFragmentManager();
        DialogFragment f = null;

        switch(dialogFragmentId) {
            case AUDIO_FRAGMENT_ID:
                f = new AudioDialogFragment();
                break;
            case VIDEO_FRAGMENT_ID:
                f = new VideoDialogFragment();
                break;
            case PHOTO_FRAGMENT_ID:
                f = new PhotoDialogFragment();
                break;
            default:
                f = new TextDialogFragment();
                break;
        }
        f.show(fm, "note_dialog_fragment");
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(sensorValues == null)
            sensorValues = new HashMap<>();

        mSensorManager.registerListener(this,mLightSensor,SensorManager.SENSOR_DELAY_NORMAL);

        WifiInfo mWifiInfo = mWifiManager.getConnectionInfo();
        String ssid = "none";
        if(mWifiInfo != null)
            ssid = mWifiInfo.getSSID();
        ((TextView)findViewById(R.id.sensor_wifi)).setText(ssid);

        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, mLocationListener);
    }

    @Override
    protected void onPause() {
        super.onPause();

        mSensorManager.unregisterListener(this);
        mLocationManager.removeUpdates(mLocationListener);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        switch(event.sensor.getType()){
            case Sensor.TYPE_LIGHT:
                sensorValues.put(Sensor.TYPE_LIGHT,event.values[0]);
                ((TextView)findViewById(R.id.sensor_light)).setText(String.valueOf(event.values[0]));
                break;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) { }
}
