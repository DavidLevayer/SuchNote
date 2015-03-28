package android.uqacproject.com.suchnote.textfragment;

import android.content.Context;
import android.os.Bundle;
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
import android.widget.Toast;

import java.util.Date;

/**
 * Created by David Levayer on 23/03/15.
 */
public class TextDialogFragment extends NoteDialogFragment implements View.OnClickListener {

    private View mView;
    private Context mContext;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.dialogfragment_text, container, false);
        mContext = getActivity();

        Bundle b = getArguments();

        Button sendButton = (Button) mView.findViewById(R.id.textButton);
        sendButton.setOnClickListener(this);

        Button autoTitle = (Button) mView.findViewById(R.id.autoTitle);
        autoTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNoteTitle(v.getRootView());
            }
        });

        return mView;
    }

    public void onClick(View v) {

        String filename = ((EditText)mView.findViewById(R.id.title)).getText().toString();

        if(filename != null && !filename.isEmpty()){

            String content = ((EditText)mView.findViewById(R.id.textArea)).getText().toString();

            FileManager.saveText(mContext,filename,content);

            setNote(new NoteInformation(
                    filename,
                    MainActivity.TEXT_NOTE,
                    null,
                    new Date()));

            dismiss();

        } else {
            Toast.makeText(getActivity(), "Titre invalide", Toast.LENGTH_SHORT).show();
        }

    }
}
