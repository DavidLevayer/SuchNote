package android.uqacproject.com.suchnote.videofragment;

import android.os.Bundle;
import android.uqacproject.com.suchnote.BasicDialogFragment;
import android.uqacproject.com.suchnote.R;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by David Levayer on 23/03/15.
 */
public class VideoDialogFragment extends BasicDialogFragment {

    private View mView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.dialogfragment_video, container, false);
        return mView;
    }
}
