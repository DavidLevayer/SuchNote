package android.uqacproject.com.suchnote;

import android.app.DialogFragment;
import android.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.uqacproject.com.suchnote.audiofragment.AudioDialogFragment;
import android.uqacproject.com.suchnote.photofragment.PhotoDialogFragment;
import android.uqacproject.com.suchnote.textfragment.TextDialogFragment;
import android.uqacproject.com.suchnote.videofragment.VideoDialogFragment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;


public class MainActivity extends ActionBarActivity {

    public final static int AUDIO_FRAGMENT_ID = 0;
    public final static int VIDEO_FRAGMENT_ID = 1;
    public final static int PHOTO_FRAGMENT_ID = 2;
    public final static int TEXT_FRAGMENT_ID = 3;


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
}
