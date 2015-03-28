package android.uqacproject.com.suchnote;

import android.app.Activity;
import android.content.DialogInterface;
import android.uqacproject.com.suchnote.database.NoteInformation;
import android.view.View;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by David Levayer on 27/03/15.
 */
public class NoteDialogFragment extends BasicDialogFragment {

    private NoteInformation note = null;
    private String noteFilePath = null;

    private boolean cancelled = false;

    public NoteInformation getNoteInformation(){
        return note;
    }

    public String getNoteFilePath(){
        return noteFilePath;
    }

    protected void setNote(NoteInformation note){
        this.note = note;
    }

    protected void setNoteFilePath(String filepath){
        this.noteFilePath = filepath;
    }

    public boolean isValidated(){
        return note == null;
    }

    public void addNoteTitle (View v) {
        EditText title = (EditText)v.findViewById(R.id.title);
        String text = new SimpleDateFormat("'Note de 'HH'h'mm").format(new Date());
        title.setText(text);
        title.clearFocus();
    }

    @Override
    public void onDismiss(final DialogInterface dialog) {
        super.onDismiss(dialog);
        if(!cancelled) {
            final Activity activity = getActivity();
            if (activity != null && activity instanceof MainActivity) {
                ((MainActivity) activity).validateNote(note);
            }
        }
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        cancelled = true;
        final Activity activity = getActivity();
        if (activity != null && activity instanceof MainActivity && noteFilePath != null) {
            ((MainActivity) activity).invalidateNote(noteFilePath);
        }
    }
}
