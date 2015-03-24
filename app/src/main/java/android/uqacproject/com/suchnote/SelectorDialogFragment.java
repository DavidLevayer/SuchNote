package android.uqacproject.com.suchnote;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.Map;

/**
 * Created by David Levayer on 23/03/15.
 */
public class SelectorDialogFragment extends BasicDialogFragment {

    private View mView;
    private ListView mListView;
    private Map<Integer,Object> sensorsValues;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.dialogfragment_selector, container, false);
        mListView = (ListView) mView.findViewById(R.id.choicelist);
        return mView;
    }

    @Override
    public void onResume() {
        super.onResume();
        selectChoices();
    }

    private void selectChoices(){

    }


}
