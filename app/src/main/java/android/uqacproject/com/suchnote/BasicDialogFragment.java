package android.uqacproject.com.suchnote;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.uqacproject.com.suchnote.database.DatabaseManager;
import android.util.DisplayMetrics;
import android.view.Window;
import android.widget.EditText;

/**
 * Created by David Levayer on 23/03/15.
 */
public class BasicDialogFragment extends DialogFragment {

    protected static float dialogSize = 0.8f;

    public BasicDialogFragment() { }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);

        // request a window without the title
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    @Override
    public void onStart() {
        super.onStart();

        // safety check
        if (getDialog() == null) {
            return;
        }

        DisplayMetrics displaymetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int dialogHeight = (int)(displaymetrics.heightPixels * dialogSize);
        int dialogWidth = (int)(displaymetrics.widthPixels * dialogSize);

        getDialog().getWindow().setLayout(dialogWidth, dialogHeight);

    }

    protected void askAssociatedName(final Context context, final String ssid){

        final EditText input = new EditText(context);

        new AlertDialog.Builder(context)
                .setTitle("Associer ce réseau Wifi")
                .setMessage("Donner un alias à cette connexion")
                .setView(input)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                        String result = input.getText().toString();

                        if (result != null && result.length() > 0) {

                            DatabaseManager mDatabaseManager = new DatabaseManager(context);
                            mDatabaseManager.addWifiInfo(ssid,result);
                        }
                    }
                })
                .setNegativeButton("Réseau inconnu", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                        DatabaseManager mDatabaseManager = new DatabaseManager(context);
                        mDatabaseManager.addWifiInfo(ssid,"Réseau inconnu");

                    }
                }).show();
    }
}
