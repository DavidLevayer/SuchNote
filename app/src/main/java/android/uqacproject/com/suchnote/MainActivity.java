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
import android.uqacproject.com.suchnote.audiofragment.DisplayAudioNoteDialogFragment;
import android.uqacproject.com.suchnote.database.DatabaseManager;
import android.uqacproject.com.suchnote.database.NoteInformation;
import android.uqacproject.com.suchnote.database.WifiInformation;
import android.uqacproject.com.suchnote.photofragment.DisplayPhotoNoteDialogFragment;
import android.uqacproject.com.suchnote.photofragment.PhotoDialogFragment;
import android.uqacproject.com.suchnote.textfragment.DisplayTextNoteDialogFragment;
import android.uqacproject.com.suchnote.textfragment.TextDialogFragment;
import android.uqacproject.com.suchnote.videofragment.DisplayVideoNoteDialogFragment;
import android.uqacproject.com.suchnote.videofragment.VideoDialogFragment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.io.File;
import java.util.ArrayList;


public class MainActivity extends Activity implements SensorEventListener, AdapterView.OnItemClickListener {

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

    public final static String WIFI_SSID = "ssidbundlekey";

    private Context mContext;

    private ViewPager mViewPager;
    private SlidingTabLayout mSlidingTabLayout;

    private SensorManager mSensorManager;
    private Sensor mLightSensor;
    private WifiManager mWifiManager;
    private AudioManager mAudioManager;
    private LocationManager mLocationManager;
    private LocationListener mLocationListener;

    private DatabaseManager mDatabaseManager;

    private float[] sensorValues;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;

        sensorValues = new float[SENSOR_NUMBER];

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

                /*
                float[] fakeValues = new float[SENSOR_NUMBER];
                fakeValues[LIGHT_SENSOR] = 10f;
                fakeValues[SOUND_SENSOR] = 0f;
                fakeValues[SPEED_SENSOR] = 5f;
                */
                // On récupère le niveau du volume
                float l1 = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
                float l2 = mAudioManager.getStreamVolume(AudioManager.STREAM_RING);
                sensorValues[SOUND_SENSOR] = Math.max(l1,l2);

                Bundle b = new Bundle();
                b.putFloatArray(SENSOR_VALUES, sensorValues);
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

        mSensorManager.registerListener(this, mLightSensor, SensorManager.SENSOR_DELAY_NORMAL);

        WifiInfo mWifiInfo = mWifiManager.getConnectionInfo();
        if (mWifiInfo != null)
            checkWifiInfo(mWifiInfo.getSSID());

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
                sensorValues[SPEED_SENSOR] = location.getSpeed();
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            public void onProviderEnabled(String provider) {
            }

            public void onProviderDisabled(String provider) {
            }
        };
    }

    private void checkWifiInfo(final String ssid){

        mDatabaseManager = new DatabaseManager(this);

        mDatabaseManager.open();
        WifiInformation infos = mDatabaseManager.getWifiInformation(ssid);
        mDatabaseManager.close();

        // Si le réseau n'est pas connu, on demande des infos à l'utilisateur
        if (infos == null) {
            DialogFragment wifiInfo = new WifiInfoDialogFragment();
            Bundle b = new Bundle();
            b.putString(WIFI_SSID,ssid);
            wifiInfo.setArguments(b);
            FragmentManager fm = getFragmentManager();
            wifiInfo.show(fm, "wifi_info_dialogfragment");
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        switch (event.sensor.getType()) {
            case Sensor.TYPE_LIGHT:
                sensorValues[LIGHT_SENSOR] = event.values[0];
                break;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    public void addNoteToList(NoteInformation note){

        if(Math.abs(mViewPager.getCurrentItem()-note.getNotetype())<=1){
            String tag = NotePagerAdapter.VIEW_TAG + String.valueOf(note.getNotetype());
            View view = mViewPager.findViewWithTag(tag);
            ListView list = (ListView)view.findViewById(R.id.note_listview);
            NoteArrayAdapter adapter = (NoteArrayAdapter) list.getAdapter();
            //adapter.add(note);
            adapter.insert(note,0);
        }
        mViewPager.setCurrentItem(note.getNotetype());
    }

    public void validateNote(NoteInformation note) {

        DatabaseManager mDatabaseManager = new DatabaseManager(this);
        mDatabaseManager.open();

        if(note.getAssociatedName() == null){
            String associatedName = "Réseau inconnu";
            WifiInfo mWifiInfo = mWifiManager.getConnectionInfo();
            if (mWifiInfo != null)
                associatedName = mDatabaseManager.getWifiAssociatedName(mWifiInfo.getSSID());

            note.setAssociatedName(associatedName);
        }

        mDatabaseManager.addNoteInfo(note);
        mDatabaseManager.close();

        addNoteToList(note);
    }

    public void invalidateNote(String filepath) {

        if (filepath != null) {
            File f = new File(filepath);
            if (f.exists())
                f.delete();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        NoteInformation n = (NoteInformation) parent.getAdapter().getItem(position);

        int type = n.getNotetype();

        Bundle b = new Bundle();

        switch (type){
            case MainActivity.TEXT_NOTE:
                b.putSerializable("text_display_note",n);
                DisplayTextNoteDialogFragment ft = new DisplayTextNoteDialogFragment();
                ft.setArguments(b);
                ft.show(getFragmentManager(),"text_display_note");
                break;
            case MainActivity.AUDIO_NOTE:
                b.putSerializable("audio_display_note",n);
                DisplayAudioNoteDialogFragment fa = new DisplayAudioNoteDialogFragment();
                fa.setArguments(b);
                fa.show(getFragmentManager(),"audio_display_note");
                break;
            case MainActivity.PHOTO_NOTE:
                b.putSerializable("photo_display_note",n);
                DisplayPhotoNoteDialogFragment fp = new DisplayPhotoNoteDialogFragment();
                fp.setArguments(b);
                fp.show(getFragmentManager(),"photo_display_note");
                break;
            case MainActivity.VIDEO_NOTE:
                b.putSerializable("video_display_note",n);
                DisplayVideoNoteDialogFragment f = new DisplayVideoNoteDialogFragment();
                f.setArguments(b);
                f.show(getFragmentManager(),"video_display_note");
                break;
        }



    }

    class NotePagerAdapter extends PagerAdapter {

        public final static String VIEW_TAG = "notePagerAdapterViewTag";

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

            String tag = VIEW_TAG + String.valueOf(position);
            view.setTag(tag);

            mListView = (ListView) view.findViewById(R.id.note_listview);
            mNoteAdapter = new NoteArrayAdapter(mContext, new ArrayList<NoteInformation>());
            mListView.setAdapter(mNoteAdapter);

            mListView.setOnItemClickListener(MainActivity.this);

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

            NoteInformation[] mNotes = null;
            mDatabaseManager.open();

            switch(position){
                // Texte
                case TEXT_NOTE:
                    mNotes = mDatabaseManager.getNoteInformation(TEXT_NOTE);
                    break;
                // Audio
                case AUDIO_NOTE:
                    mNotes = mDatabaseManager.getNoteInformation(AUDIO_NOTE);
                    break;
                // Photo
                case PHOTO_NOTE:
                    mNotes = mDatabaseManager.getNoteInformation(PHOTO_NOTE);
                    break;
                // Vidéo
                case VIDEO_NOTE:
                    mNotes = mDatabaseManager.getNoteInformation(VIDEO_NOTE);
                    break;
            }

            mDatabaseManager.close();

            if(mNotes != null){
                for(NoteInformation n : mNotes)
                    mNoteAdapter.add(n);
            }
        }
    }
}
