package android.uqacproject.com.suchnote;

import android.content.DialogInterface;
import android.os.Bundle;
import android.uqacproject.com.suchnote.database.DatabaseManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.larswerkman.holocolorpicker.ColorPicker;

/**
 * Created by David Levayer on 29/03/15.
 */
public class WifiInfoDialogFragment extends BasicDialogFragment implements View.OnClickListener {

    private View mView;
    private String ssid;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.dialogfragment_wifiinfo, container, false);

        Bundle b = getArguments();
        ssid = b.getString(MainActivity.WIFI_SSID);

        ColorPicker picker = (ColorPicker) mView.findViewById(R.id.picker);
        picker.setShowOldCenterColor(false);

        Button validate = (Button) mView.findViewById(R.id.validate);
        validate.setOnClickListener(this);

        Button notNow = (Button) mView.findViewById(R.id.notnow);
        notNow.setOnClickListener(this);

        return mView;
    }

    @Override
    public void onClick(View v) {

        String value = "Réseau inconnu";

        if(v!=null && v.getId()==R.id.validate){
            String name = ((EditText)mView.findViewById(R.id.title)).getText().toString();
            if(name != null && !name.isEmpty())
                value = name;
        }

        DatabaseManager mDatabaseManager = new DatabaseManager(getActivity());
        mDatabaseManager.open();
        mDatabaseManager.addWifiInfo(ssid,value);
        mDatabaseManager.close();

        dismiss();
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        onClick(null);
    }
}
