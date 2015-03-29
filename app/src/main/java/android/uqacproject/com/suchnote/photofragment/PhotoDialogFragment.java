package android.uqacproject.com.suchnote.photofragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.uqacproject.com.suchnote.FileManager;
import android.uqacproject.com.suchnote.MainActivity;
import android.uqacproject.com.suchnote.NoteDialogFragment;
import android.uqacproject.com.suchnote.R;
import android.uqacproject.com.suchnote.database.NoteInformation;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.util.Date;

/**
 * Created by David Levayer on 23/03/15.
 */
public class PhotoDialogFragment extends NoteDialogFragment
        implements DialogInterface.OnDismissListener {

    /* ---------------- */
    private View mView;

    private String fileName;

    private File currentImage;

    private  Button buttonSave;

    private ImageView myPhotoView;

    /* ---------------- */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.dialogfragment_photo, container, false);

        Button button = (Button) mView.findViewById(R.id.button_capture_photo);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                fileName = ((EditText)mView.findViewById(R.id.title)).getText().toString();

                if(fileName != null && !fileName.isEmpty()){
                    Intent imageIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    String filePath = FileManager.getPhotoFilePath(fileName);
                    currentImage = new File(filePath);
                    setNoteFilePath(filePath);
                    Uri uriSavedImage = Uri.fromFile(currentImage);
                    imageIntent.putExtra(MediaStore.EXTRA_OUTPUT, uriSavedImage);
                    startActivityForResult(imageIntent,0);
                } else {
                    Toast.makeText(getActivity(), "Titre invalide", Toast.LENGTH_SHORT).show();
                }
            }
        });

        buttonSave = (Button) mView.findViewById(R.id.button_save_photo);

        buttonSave.setVisibility(View.GONE);

        buttonSave.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                setNote(new NoteInformation(
                        fileName,
                        MainActivity.PHOTO_NOTE,
                        null,
                        new Date()));

                dismiss();
            }
        });

        myPhotoView = (ImageView) mView.findViewById(R.id.imageViewPhoto);
        myPhotoView.setVisibility(View.GONE);

        Button autoTitle = (Button) mView.findViewById(R.id.autoTitle);
        autoTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNoteTitle(v.getRootView());
            }
        });

        return mView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
            if (resultCode == getActivity().RESULT_OK) {
                // Image captured and saved to fileUri specified in the Intent

                Bitmap myBitmap = BitmapFactory.decodeFile(currentImage.getAbsolutePath());
                myPhotoView = (ImageView) mView.findViewById(R.id.imageViewPhoto);
                myPhotoView.setImageBitmap(myBitmap);

                myPhotoView.setVisibility(View.VISIBLE);

                buttonSave.setVisibility(View.VISIBLE);

            } else if (resultCode == getActivity().RESULT_CANCELED) {
                // User cancelled the image capture
            } else {
                // Image capture failed, advise user
            }

    }

}
