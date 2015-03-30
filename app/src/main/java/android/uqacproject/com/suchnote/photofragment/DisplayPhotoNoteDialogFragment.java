package android.uqacproject.com.suchnote.photofragment;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.uqacproject.com.suchnote.BasicDialogFragment;
import android.uqacproject.com.suchnote.FileManager;
import android.uqacproject.com.suchnote.R;
import android.uqacproject.com.suchnote.database.NoteInformation;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;

/**
 * Created by corentin on 29/03/2015.
 */
public class DisplayPhotoNoteDialogFragment extends BasicDialogFragment
        implements DialogInterface.OnDismissListener {

    /* ---------------- */
    private View mView;

    private File currentImage;

    private ImageView myPhotoView;

    /* ---------------- */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.dialogfragment_display_photo, container, false);

        myPhotoView = (ImageView) mView.findViewById(R.id.imageViewPhoto);

        //Récupération du contenu du bundle
        NoteInformation n = (NoteInformation) getArguments().getSerializable("photo_display_note");
        ((TextView)mView.findViewById(R.id.title)).setText(n.getFilename());
        String filePath = FileManager.getPhotoFilePath(n.getFilename());
        currentImage = new File(filePath);

        if(currentImage.exists()){
            Bitmap myBitmap = BitmapFactory.decodeFile(currentImage.getAbsolutePath());
            myPhotoView.setImageBitmap(myBitmap);
        }

        return mView;
    }

}
