package android.uqacproject.com.suchnote.audiofragment;

import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.uqacproject.com.suchnote.BasicDialogFragment;
import android.uqacproject.com.suchnote.FileManager;
import android.uqacproject.com.suchnote.R;
import android.uqacproject.com.suchnote.database.NoteInformation;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;

/**
 * Created by corentin on 29/03/2015.
 */
public class DisplayAudioNoteDialogFragment extends BasicDialogFragment
        implements DialogInterface.OnDismissListener {

    private View mView;
    private Button play;

    private MediaPlayer mPlayer = null;
    private String mFilename;

    private boolean isPlaying = false;

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if(mPlayer != null)
               mPlayer.stop();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.dialogfragment_display_audio, container, false);

        play = (Button) mView.findViewById(R.id.play);
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playRecording();
            }
        });

        NoteInformation n = (NoteInformation) getArguments().getSerializable("audio_display_note");
        ((TextView)mView.findViewById(R.id.title)).setText(n.getFilename());

        mFilename = FileManager.getAudioFilePath(n.getFilename());

        return mView;
    }

    private void playRecording(){
        if (!isPlaying){
            mPlayer = new MediaPlayer();

            mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                public void onCompletion(MediaPlayer mp) {
                    isPlaying = false;
                }
            });

            try {
                mPlayer.setDataSource(mFilename);
                mPlayer.prepare();
                mPlayer.start();
                isPlaying = true;
            } catch (IOException e) {
                Log.e("Audio", "prepare() failed");
            }
        }
    }
}
