package android.uqacproject.com.suchnote.videofragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.File;
import java.util.Date;

/**
 * Created by corentin on 29/03/2015.
 */
public class DisplayVideoNoteDialogFragment extends NoteDialogFragment
        implements DialogInterface.OnDismissListener {


    /* ---------------- */
    private View mView;

    private Button buttonPlay;

    private Button buttonStop;

    /*----------------------*/
    private VideoView myVideoView;
    private MediaController mediaControls;
    private  boolean videoready = false;

    private File currentFile;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.dialogfragment_display_video, container, false);

        //Récupération du contenu du bundle
        NoteInformation n = (NoteInformation) getArguments().getSerializable("video_display_note");

        String filePath = FileManager.getVideoFilePath(n.getFilename());
        currentFile = new File(filePath);



        myVideoView = (VideoView) mView.findViewById(R.id.video_view);
        if (mediaControls == null) {
            mediaControls = new MediaController(getActivity());
        }

        try {
            //set the media controller in the VideoView
            myVideoView.setMediaController(mediaControls);
            //set the uri of the video to be played
            if(currentFile.exists())
                myVideoView.setVideoURI(Uri.fromFile(currentFile));
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
        myVideoView.requestFocus();
        //we also set an setOnPreparedListener in order to know when the video file is ready for playback
        myVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            public void onPrepared(MediaPlayer mediaPlayer) {
                //if we have a position on savedInstanceState, the video playback should start from here
                videoready = true;
            }
        });

        buttonPlay = (Button) mView.findViewById(R.id.button_start_preview_video);
        buttonPlay.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(videoready){
                    myVideoView.start();
                }
            }
        });

        buttonStop = (Button) mView.findViewById(R.id.button_stop_preview_video);
        buttonStop.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(videoready){
                    myVideoView.pause();
                }
            }
        });



        return mView;
    }


}
