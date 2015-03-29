package android.uqacproject.com.suchnote.textfragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.uqacproject.com.suchnote.BasicDialogFragment;
import android.uqacproject.com.suchnote.FileManager;
import android.uqacproject.com.suchnote.NoteDialogFragment;
import android.uqacproject.com.suchnote.R;
import android.uqacproject.com.suchnote.database.NoteInformation;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by corentin on 29/03/2015.
 */
public class DisplayTextNoteDialogFragment extends BasicDialogFragment
        implements DialogInterface.OnDismissListener {

    private View mView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.dialogfragment_display_text, container, false);

        NoteInformation n = (NoteInformation) getArguments().getSerializable("text_display_note");

        TextView tViewTitle = (TextView) mView.findViewById(R.id.textViewTitle);
        tViewTitle.setText(n.getFilename());

        String filePath = FileManager.getTextFilePath(n.getFilename());
        File myFile = new File( filePath );

        if(myFile.exists()){

            InputStream inputStream = null;
            try {
                inputStream = new FileInputStream(myFile);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            BufferedReader r = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder total = new StringBuilder();
            String line;

            try {
                while ((line = r.readLine()) != null) {
                    total.append(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            TextView tView = (TextView) mView.findViewById(R.id.textViewContent);
            tView.setText(total.toString());

        }



        return mView;
    }

}
