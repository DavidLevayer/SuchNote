package android.uqacproject.com.suchnote;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
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
import android.uqacproject.com.suchnote.database.DatabaseManager;
import android.uqacproject.com.suchnote.database.NoteInformation;
import android.uqacproject.com.suchnote.photofragment.PhotoDialogFragment;
import android.uqacproject.com.suchnote.textfragment.TextDialogFragment;
import android.uqacproject.com.suchnote.videofragment.VideoDialogFragment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
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

    private void checkWifiInfo(final String ssid){

        mDatabaseManager = new DatabaseManager(this);

        mDatabaseManager.open();
        String associatedName = mDatabaseManager.getWifiAssociatedName(ssid);
        mDatabaseManager.close();

        // Si le réseau n'est pas connu, on demande des infos à l'utilisateur
        if (associatedName == null) {

            final EditText input = new EditText(this);

            new AlertDialog.Builder(this)
                    .setTitle("Associer ce réseau Wifi")
                    .setMessage("Donner un alias à cette connexion")
                    .setView(input)
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {

                            String result = input.getText().toString();

                            if (result != null && result.length() > 0){
                                mDatabaseManager.open();
                                mDatabaseManager.addWifiInfo(ssid,result);
                                mDatabaseManager.close();
                            }
                        }
                    })
                    .setNegativeButton("Réseau inconnu", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            mDatabaseManager.open();
                            mDatabaseManager.addWifiInfo(ssid,"Réseau inconnu");
                            mDatabaseManager.close();
                        }
                    }).show();
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        switch (event.sensor.getType()) {
            case Sensor.TYPE_LIGHT:
                // TODO save la luminosité
                break;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    public void addNoteToList(NoteInformation note){

        mViewPager.setCurrentItem(note.getNotetype());
        String tag = NotePagerAdapter.VIEW_TAG + String.valueOf(note.getNotetype());
        View view = mViewPager.findViewWithTag(tag);
        ListView list = (ListView)view.findViewById(R.id.note_listview);
        NoteArrayAdapter adapter = (NoteArrayAdapter) list.getAdapter();
        adapter.add(note);
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
        // TODO CORENTIN
        NoteInformation n = (NoteInformation) parent.getAdapter().getItem(position);

        /*Bundle b = new Bundle();
        b.putSerializable();

        Fragment f;
        f.setArguments(b);*/
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
