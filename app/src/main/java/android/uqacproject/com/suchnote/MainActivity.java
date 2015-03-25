package android.uqacproject.com.suchnote;

import android.app.Activity;
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
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.uqacproject.com.suchnote.audiofragment.AudioDialogFragment;
import android.uqacproject.com.suchnote.photofragment.PhotoDialogFragment;
import android.uqacproject.com.suchnote.textfragment.TextDialogFragment;
import android.uqacproject.com.suchnote.videofragment.VideoDialogFragment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.HashMap;


public class MainActivity extends Activity implements SensorEventListener {

    public final static int AUDIO_FRAGMENT_ID = 0;
    public final static int VIDEO_FRAGMENT_ID = 1;
    public final static int PHOTO_FRAGMENT_ID = 2;
    public final static int TEXT_FRAGMENT_ID = 3;

    private ViewPager mViewPager;
    private SlidingTabLayout mSlidingTabLayout;

    private HashMap<Integer, Object> sensorValues;
    private SensorManager mSensorManager;
    private Sensor mLightSensor;
    private WifiManager mWifiManager;
    private LocationManager mLocationManager;
    private LocationListener mLocationListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        mViewPager.setAdapter(new SamplePagerAdapter());
        mSlidingTabLayout = (SlidingTabLayout) findViewById(R.id.sliding_tabs);
        mSlidingTabLayout.setViewPager(mViewPager);

        //final ActionBar mActionBar = getActionBar();
        //mActionBar.setDisplayShowTitleEnabled(false);
        //mActionBar.setDisplayHomeAsUpEnabled(true);
        //mActionBar.setHomeButtonEnabled(true);

        initSensors();
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

    public void launchDialogFragment(int dialogFragmentId) {

        FragmentManager fm = getFragmentManager();
        DialogFragment f = null;

        switch (dialogFragmentId) {
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

        if (sensorValues == null)
            sensorValues = new HashMap<>();

        mSensorManager.registerListener(this, mLightSensor, SensorManager.SENSOR_DELAY_NORMAL);

        WifiInfo mWifiInfo = mWifiManager.getConnectionInfo();
        String ssid = "none";
        if (mWifiInfo != null)
            ssid = mWifiInfo.getSSID();
        // TODO save l'id wifi

        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, mLocationListener);
    }

    @Override
    protected void onPause() {
        super.onPause();

        mSensorManager.unregisterListener(this);
        mLocationManager.removeUpdates(mLocationListener);
    }

    private void initSensors() {
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mLightSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);

        mWifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);

        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        // Define a listener that responds to location updates
        mLocationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                location.getLatitude();
                // TODO save la vitesse
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            public void onProviderEnabled(String provider) {
            }

            public void onProviderDisabled(String provider) {
            }
        };
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        switch (event.sensor.getType()) {
            case Sensor.TYPE_LIGHT:
                sensorValues.put(Sensor.TYPE_LIGHT, event.values[0]);
                // TODO save la luminosité
                break;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }


    class SamplePagerAdapter extends PagerAdapter {

        private ListView mListView;
        //private NewsArrayAdapter mListViewAdapter;

        @Override
        public int getCount() {
            return 4;
        }

        @Override
        public boolean isViewFromObject(View view, Object o) {
            return o == view;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            String[] tabs = getResources().getStringArray(R.array.activity_tabs);
            switch (position) {
                case TEXT_FRAGMENT_ID:
                    return tabs[0];
                case AUDIO_FRAGMENT_ID:
                    return tabs[1];
                case PHOTO_FRAGMENT_ID:
                    return tabs[2];
                case VIDEO_FRAGMENT_ID:
                    return tabs[3];
            }
            return "noName";
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            // Inflate a new layout from our resources
            View view = getLayoutInflater().inflate(R.layout.activity_main_list,
                    container, false);

            mListView = (ListView) view.findViewById(R.id.note_listview);
            //mListViewAdapter = new NewsArrayAdapter(mContext, new ArrayList<News>());
            //mListView.setAdapter(mListViewAdapter);
            //mListView.setOnItemClickListener(HomeFragment.this);

            // TODO Add element to the list

            // Add the newly created View to the ViewPager
            container.addView(view);

            // Return the View
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
}
