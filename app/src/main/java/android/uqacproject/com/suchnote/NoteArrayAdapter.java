package android.uqacproject.com.suchnote;

import android.content.Context;
import android.uqacproject.com.suchnote.database.DatabaseManager;
import android.uqacproject.com.suchnote.database.NoteInformation;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

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
        final NoteInformation n = getItem(position);

        // Si la vue ne peut pas être réutilisée, on la recrée
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.activity_main_list_item,
                    parent,
                    false);
        }

        ((TextView) convertView.findViewById(R.id.title)).setText(n.getFilename());
        ((TextView) convertView.findViewById(R.id.place)).setText(n.getAssociatedName());

        Date d = n.getDate();

        String day = new SimpleDateFormat("dd").format(d);
        String month = new SimpleDateFormat("MMMM").format(d);
        ((TextView) convertView.findViewById(R.id.day)).setText(day);
        ((TextView) convertView.findViewById(R.id.month)).setText(month);

        FloatingActionButton deleteButton = (FloatingActionButton) convertView.findViewById(R.id.remove_button);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Suppression de la base de données
                DatabaseManager mDatabaseManager = new DatabaseManager(getContext());
                mDatabaseManager.open();
                mDatabaseManager.removeNoteInformation(n);
                mDatabaseManager.close();

                // Suppression du fichier
                FileManager.deleteNote(n);

                // Suppression de la liste
                remove(n);
                notifyDataSetChanged();
            }
        });

        return convertView;
    }


}
