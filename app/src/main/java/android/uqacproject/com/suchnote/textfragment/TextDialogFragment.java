package android.uqacproject.com.suchnote.textfragment;

import android.content.Context;
import android.os.Bundle;
import android.uqacproject.com.suchnote.BasicDialogFragment;
import android.uqacproject.com.suchnote.FileManager;
import android.uqacproject.com.suchnote.MainActivity;
import android.uqacproject.com.suchnote.R;
import android.uqacproject.com.suchnote.database.DatabaseManager;
import android.uqacproject.com.suchnote.database.NoteInformation;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by David Levayer on 23/03/15.
 */
public class TextDialogFragment extends BasicDialogFragment implements View.OnClickListener {

    private View mView;
    private Context mContext;
    private String mSSID;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.dialogfragment_text, container, false);
        mContext = getActivity();

        Bundle b = getArguments();
        mSSID = b.getString(MainActivity.WIFI_SSID);

        Button sendButton = (Button) mView.findViewById(R.id.textButton);
        sendButton.setOnClickListener(this);

        return mView;
    }

    public void onClick(View v) {

        String filename = ((EditText)mView.findViewById(R.id.title)).getText().toString();
        String content = ((EditText)mView.findViewById(R.id.textArea)).getText().toString();

        FileManager.saveText(mContext,filename,content);

        String associatedName = "Réseau inconnu";

        if(mSSID != null) {
            DatabaseManager mDatabaseManager = new DatabaseManager(mContext);
            mDatabaseManager.open();
            associatedName = mDatabaseManager.getWifiAssociatedName(mSSID);
        }

        NoteInformation note = new NoteInformation(filename, MainActivity.TEXT_NOTE, associatedName);

        DatabaseManager mDatabaseManager = new DatabaseManager(mContext);
        mDatabaseManager.open();
        mDatabaseManager.addNoteInfo(note);
        mDatabaseManager.close();

        dismiss();
    }
}
