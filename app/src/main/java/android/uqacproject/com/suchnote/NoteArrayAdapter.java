package android.uqacproject.com.suchnote;

import android.content.Context;
import android.uqacproject.com.suchnote.database.NoteInformation;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by David Levayer on 12/03/15.
 */
public class NoteArrayAdapter extends ArrayAdapter<NoteInformation> {

    public NoteArrayAdapter(Context context, ArrayList<NoteInformation> objects) {
        super(context, 0, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // On récupère la note à traiter
        NoteInformation n = getItem(position);

        // Si la vue ne peut pas être réutilisée, on la recrée
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.activity_main_list_item,
                    parent,
                    false);
        }

        ((TextView) convertView.findViewById(R.id.title)).setText(n.getFilename());
        ((TextView) convertView.findViewById(R.id.place)).setText(n.getAssociatedName());

        return convertView;
    }


}
