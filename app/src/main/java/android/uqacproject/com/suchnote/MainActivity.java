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
import android.media.AudioManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.uqacproject.com.suchnote.audiofragment.AudioDialogFragment;
import android.uqacproject.com.suchnote.photofragment.PhotoDialogFragment;
import android.uqacproject.com.suchnote.textfragment.TextDialogFragment;
import android.uqacproject.com.suchnote.videofragment.Note;
import android.uqacproject.com.suchnote.videofragment.VideoDialogFragment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;


public class MainActivity extends Activity implements SensorEventListener {

    public final static int MODE_NUMBER = 4;

    public final static int TEXT_NOTE = 0;
    public final static int AUDIO_NOTE = 1;
    public final static int PHOTO_NOTE = 2;
    public final static int VIDEO_NOTE = 3;

    public final static String SENSOR_VALUES = "sensor_values";

    public final static int SENSOR_NUMBER = 3;

    public final static int LIGHT_SENSOR = 0;
    public final static int SOUND_SENSOR = 1;
    public final static int SPEED_SENSOR = 2;


    private Context mContext;

    private ViewPager mViewPager;
    private SlidingTabLayout mSlidingTabLayout;

    private HashMap<Integer, Object> sensorValues;
    private SensorManager mSensorManager;
    private Sensor mLightSensor;
    private WifiManager mWifiManager;
    private AudioManager mAudioManager;
    private LocationManager mLocationManager;
    private LocationListener mLocationListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;

        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        mViewPager.setAdapter(new NotePagerAdapter());
        mSlidingTabLayout = (SlidingTabLayout) findViewById(R.id.sliding_tabs);
        mSlidingTabLayout.setViewPager(mViewPager);

        FloatingActionButton addButton = (FloatingActionButton) findViewById(R.id.fab_1);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getFragmentManager();
                DialogFragment f = new SelectorDialogFragment();

                float[] fakeValues = new float[SENSOR_NUMBER];
                fakeValues[LIGHT_SENSOR] = 10f;
                fakeValues[SOUND_SENSOR] = 0f;
                fakeValues[SPEED_SENSOR] = 5f;

                Bundle b = new Bundle();
                b.putFloatArray(SENSOR_VALUES, fakeValues);
                f.setArguments(b);

                f.show(fm,"selector_fragment");
            }
        });

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

    @Deprecated
    public void launchDialogFragment(int dialogFragmentId) {

        FragmentManager fm = getFragmentManager();
        DialogFragment f = null;

        switch (dialogFragmentId) {
            case AUDIO_NOTE:
                f = new AudioDialogFragment();
                break;
            case VIDEO_NOTE:
                f = new VideoDialogFragment();
                break;
            case PHOTO_NOTE:
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

        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
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

    class NotePagerAdapter extends PagerAdapter {

        private ListView mListView;
        private NoteArrayAdapter mNoteAdapter;

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
                case TEXT_NOTE:
                    return tabs[0];
                case AUDIO_NOTE:
                    return tabs[1];
                case PHOTO_NOTE:
                    return tabs[2];
                case VIDEO_NOTE:
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
            mNoteAdapter = new NoteArrayAdapter(mContext, new ArrayList<Note>());
            mListView.setAdapter(mNoteAdapter);

            // TODO Add element to the list
            loadNotes(position);

            // Add the newly created View to the ViewPager
            container.addView(view);

            // Return the View
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        private void loadNotes(int position){

            Note[] mNotes = null;
            switch(position){
                // Texte
                case TEXT_NOTE:
                    mNotes = FileManager.getNotes(TEXT_NOTE);
                    break;
                // Audio
                case AUDIO_NOTE:
                    mNotes = FileManager.getNotes(AUDIO_NOTE);
                    break;
                // Photo
                case PHOTO_NOTE:
                    mNotes = FileManager.getNotes(PHOTO_NOTE);
                    break;
                // Vidéo
                case VIDEO_NOTE:
                    mNotes = FileManager.getNotes(VIDEO_NOTE);
                    break;
            }

            if(mNotes != null){
                for(Note n : mNotes)
                    mNoteAdapter.add(n);
            }
        }
    }
}
