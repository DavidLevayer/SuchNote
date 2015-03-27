package android.uqacproject.com.suchnote.videofragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.uqacproject.com.suchnote.BasicDialogFragment;
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
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.VideoView;

import java.io.File;
import java.util.Date;

/**
 * Created by David Levayer on 23/03/15.
 */
public class VideoDialogFragment  extends NoteDialogFragment
        implements DialogInterface.OnDismissListener {

    /* ---------------- */
    private View mView;

    private String fileName;

    private File currentVideo;

    private Button buttonSave;

    private Button buttonPlay;

    private Button buttonStop;

    /*----------------------*/
    private VideoView myVideoView;
    private int position = 0;
    private MediaController mediaControls;
    private  boolean videoready = false;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.dialogfragment_video, container, false);

        Button button = (Button) mView.findViewById(R.id.button_capture_video);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                fileName = ((EditText)mView.findViewById(R.id.title_video)).getText().toString();

                if(fileName != null && !fileName.isEmpty()){
                    Intent videoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                    String filePath = FileManager.getVideoFilePath(fileName);
                    currentVideo = new File(filePath);
                    setNoteFilePath(filePath);
                    Uri uriSavedVideo = Uri.fromFile(currentVideo);
                    videoIntent.putExtra(MediaStore.EXTRA_OUTPUT, uriSavedVideo);
                    startActivityForResult(videoIntent,0);
                }
            }
        });

        buttonSave = (Button) mView.findViewById(R.id.button_save_video);

        buttonSave.setVisibility(View.GONE);

        buttonSave.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setNote(new NoteInformation(
                        fileName,
                        MainActivity.VIDEO_NOTE,
                        null,
                        new Date()));
                dismiss();
            }
        });

        myVideoView = (VideoView) mView.findViewById(R.id.video_view);
        myVideoView.setVisibility(View.GONE);

        buttonPlay = (Button) mView.findViewById(R.id.button_start_preview_video);
        buttonPlay.setVisibility(View.GONE);

        buttonPlay.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(videoready){
                    myVideoView.start();
                }
            }
        });

        buttonStop = (Button) mView.findViewById(R.id.button_stop_preview_video);
        buttonStop.setVisibility(View.GONE);
        buttonStop.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(videoready){
                    myVideoView.pause();
                }
            }
        });


    return mView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == getActivity().RESULT_OK) {
            // Image captured and saved to fileUri specified in the Intent
            buttonSave.setVisibility(View.VISIBLE);
            buttonPlay.setVisibility(View.VISIBLE);
            buttonStop.setVisibility(View.VISIBLE);
            /* ------*/
            if (mediaControls == null) {
                mediaControls = new MediaController(getActivity());
            }
            //initialize the VideoView
            myVideoView = (VideoView) mView.findViewById(R.id.video_view);

            myVideoView.setVisibility(View.VISIBLE);

            try {
                //set the media controller in the VideoView
                myVideoView.setMediaController(mediaControls);
                //set the uri of the video to be played
                myVideoView.setVideoURI(Uri.fromFile(currentVideo));
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


        } else if (resultCode == getActivity().RESULT_CANCELED) {
            // User cancelled the image capture
        } else {
            // Image capture failed, advise user
        }
    }

}
