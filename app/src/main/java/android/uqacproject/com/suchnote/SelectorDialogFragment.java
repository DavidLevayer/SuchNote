package android.uqacproject.com.suchnote;

import android.app.DialogFragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.uqacproject.com.suchnote.audiofragment.AudioDialogFragment;
import android.uqacproject.com.suchnote.photofragment.PhotoDialogFragment;
import android.uqacproject.com.suchnote.textfragment.TextDialogFragment;
import android.uqacproject.com.suchnote.videofragment.VideoDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by David Levayer on 23/03/15.
 */
public class SelectorDialogFragment extends BasicDialogFragment {

    private View mView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.dialogfragment_selector, container, false);

        Bundle b = getArguments();
        float[] sensorValues = b.getFloatArray(MainActivity.SENSOR_VALUES);

        String[] modeNames = getResources().getStringArray(R.array.activity_tabs);
        int[] modeScores = evaluateModes(sensorValues);
        int maxIndice = getMaxIndice(modeScores);

        int j = 0;
        for(int i=0; i< MainActivity.MODE_NUMBER; i++){

            int resourceId = mView.getResources().getIdentifier(
                    "button"+i,
                    "id",
                    getActivity().getPackageName());

            Button button = (Button)mView.findViewById(resourceId);

            if(i == 0){
                setButton(button, modeNames[maxIndice], maxIndice);
            } else {

                if(j==maxIndice)
                    j++;

                setButton(button, modeNames[j], j);
                j++;
            }
        }

        return mView;
    }

    private int[] evaluateModes(float[] sensorValues){

        int[] scores = new int[MainActivity.MODE_NUMBER];

        // On défavorise un peu le texte, afin qu'il ne soit pas pris par défaut
        // car c'est un mode de saisie fastidieux sur Smartphone
        scores[MainActivity.TEXT_NOTE] -= 5;

        float lightValue = sensorValues[MainActivity.LIGHT_SENSOR];
        float audioLevel = sensorValues[MainActivity.SOUND_SENSOR];
        float speed = sensorValues[MainActivity.SPEED_SENSOR];

        if(lightValue < 5){
            scores[MainActivity.PHOTO_NOTE] -= 50;
            scores[MainActivity.VIDEO_NOTE] -= 50;
        }

        if(lightValue >= 5 && lightValue < 20){
            scores[MainActivity.PHOTO_NOTE] -= 35;
            scores[MainActivity.VIDEO_NOTE] -= 35;
        }

        if(lightValue >= 20 && lightValue < 50){
            scores[MainActivity.PHOTO_NOTE] -= 20;
            scores[MainActivity.VIDEO_NOTE] -= 20;
        }

        if(audioLevel == 0){
            scores[MainActivity.AUDIO_NOTE] -= 20;
            scores[MainActivity.VIDEO_NOTE] -= 20;
        }

        if(speed >= 20){
            scores[MainActivity.PHOTO_NOTE] -= 100;
            scores[MainActivity.VIDEO_NOTE] -= 100;
        }

        return scores;
    }

    private int getMaxIndice(int[] values){

        if(values == null || values.length == 0){
            return -1;
        }

        int indice = 0;
        for(int i=0; i<values.length; i++){
            if(values[indice]<values[i])
                indice = i;
        }

        return indice;
    }

    private void setButton(Button b, String text, final int fragmentId){

        b.setText(text);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DialogFragment f = getFragmentById(fragmentId);
                FragmentManager fm = getActivity().getFragmentManager();
                dismiss();
                f.show(fm,"note_fragment");
            }
        });
    }

    private DialogFragment getFragmentById(int fragmentId) {

        DialogFragment f = null;

        switch (fragmentId) {
            case MainActivity.AUDIO_NOTE:
                f = new AudioDialogFragment();
                break;
            case MainActivity.VIDEO_NOTE:
                f = new VideoDialogFragment();
                break;
            case MainActivity.PHOTO_NOTE:
                f = new PhotoDialogFragment();
                break;
            default:
                f = new TextDialogFragment();
                break;
        }

        return f;
    }
}
