package android.uqacproject.com.suchnote.audiofragment;

import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.uqacproject.com.suchnote.FileManager;
import android.uqacproject.com.suchnote.MainActivity;
import android.uqacproject.com.suchnote.NoteDialogFragment;
import android.uqacproject.com.suchnote.R;
import android.uqacproject.com.suchnote.database.NoteInformation;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.util.Date;

/**
 * Created by David Levayer on 23/03/15.
 */
public class AudioDialogFragment extends NoteDialogFragment
        implements DialogInterface.OnDismissListener {

    private View mView;
    private Button start, stop, play, validate;

    private MediaPlayer mPlayer = null;
    private MediaRecorder mRecorder = null;
    private String mFilename;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.dialogfragment_audio, container, false);

        start = (Button) mView.findViewById(R.id.start);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startRecording();
            }
        });

        stop = (Button) mView.findViewById(R.id.stop);
        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopRecording();
            }
        });

        play = (Button) mView.findViewById(R.id.play);
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playRecording();
            }
        });

        validate = (Button) mView.findViewById(R.id.validate);
        validate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String filename = ((EditText)mView.findViewById(R.id.title)).getText().toString();
                setNote(new NoteInformation(
                        filename,
                        MainActivity.AUDIO_NOTE,
                        null,
                        new Date()));
                dismiss();
            }
        });

        stop.setEnabled(false);
        play.setEnabled(false);
        validate.setEnabled(false);

        Button autoTitle = (Button) mView.findViewById(R.id.autoTitle);
        autoTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNoteTitle(v.getRootView());
            }
        });

        return mView;
    }

    private void startRecording(){
        start.setEnabled(false);
        stop.setEnabled(true);

        String filename = ((EditText)mView.findViewById(R.id.title)).getText().toString();

        if(filename != null && !filename.isEmpty()){

            mRecorder = new MediaRecorder();
            mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            mFilename = FileManager.getAudioFilePath(filename);
            mRecorder.setOutputFile(mFilename);
            mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

            setNoteFilePath(mFilename);

            try {
                mRecorder.prepare();
            } catch (IOException e) {
                Log.e("Audio", "prepare() failed");
            }

            mRecorder.start();

        } else {
            Toast.makeText(getActivity(), "Titre invalide", Toast.LENGTH_SHORT).show();
        }
    }

    private void stopRecording(){
        mRecorder.stop();
        mRecorder.release();
        mRecorder = null;
        start.setEnabled(true);
        stop.setEnabled(false);
        play.setEnabled(true);
        validate.setEnabled(true);
    }

    private void playRecording(){
        mPlayer = new MediaPlayer();
        try {
            mPlayer.setDataSource(mFilename);
            mPlayer.prepare();
            mPlayer.start();
        } catch (IOException e) {
            Log.e("Audio", "prepare() failed");
        }
    }


}
