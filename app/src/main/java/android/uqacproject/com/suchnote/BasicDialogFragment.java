package android.uqacproject.com.suchnote;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Window;

/**
 * Created by David Levayer on 23/03/15.
 */
public class BasicDialogFragment extends DialogFragment {

    protected static float dialogSize = 0.7f;

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
        //int dialogHeight = (int)(displaymetrics.heightPixels * dialogSize);
        int dialogWidth = (int)(displaymetrics.widthPixels * dialogSize);

        //getDialog().getWindow().setLayout(dialogWidth, dialogHeight);
        Window dialogWindow = getDialog().getWindow();
        dialogWindow.setLayout(dialogWidth, dialogWindow.getAttributes().height);
    }
}
