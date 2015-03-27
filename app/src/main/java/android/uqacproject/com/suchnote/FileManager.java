package android.uqacproject.com.suchnote;

import android.content.Context;
import android.os.Environment;
import android.uqacproject.com.suchnote.database.NoteInformation;

import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by David Levayer on 23/03/15.
 */
public class FileManager {

    private final static String FILE_PATH = Environment.getExternalStorageDirectory().getPath();
    private final static String TEXT_DIRECTORY = "/SuchNote/text/";
    private final static String AUDIO_DIRECTORY = "/SuchNote/audio/";
    private final static String PHOTO_DIRECTORY = "/SuchNote/photo/";
    private final static String VIDEO_DIRECTORY = "/SuchNote/video/";

    public static void saveText(Context context, String filename, String text){
        FileOutputStream outputStream;

        try {
            File folder = new File(FILE_PATH+TEXT_DIRECTORY);

            if(!folder.exists())
                folder.mkdirs();

            outputStream = new FileOutputStream(FILE_PATH+TEXT_DIRECTORY+filename);
            outputStream.write(text.getBytes());
            outputStream.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getAudioFilePath(String filename){

        File folder = new File(FILE_PATH+AUDIO_DIRECTORY);
        if(!folder.exists())
            folder.mkdirs();

        return FILE_PATH+AUDIO_DIRECTORY+filename;
    }

    @Deprecated
    public static NoteInformation[] getNotes(int noteType){
        switch(noteType){
            case MainActivity.TEXT_NOTE:
                return getNotes(FILE_PATH+TEXT_DIRECTORY);
            case MainActivity.AUDIO_NOTE:
                return getNotes(FILE_PATH+AUDIO_DIRECTORY);
            case MainActivity.PHOTO_NOTE:
                return getNotes(FILE_PATH+PHOTO_DIRECTORY);
            case MainActivity.VIDEO_NOTE:
                return getNotes(FILE_PATH+VIDEO_DIRECTORY);
            default:
                return null;
        }
    }

    @Deprecated
    private static NoteInformation[] getNotes(String dir){

        File folder = new File(dir);
        String[] fileList = folder.list();

        if(fileList == null)
            return null;

        NoteInformation[] notes = new NoteInformation[fileList.length];

        for(int i=0; i<fileList.length; i++) {
            NoteInformation n = new NoteInformation();
            n.setFilename(fileList[i]);
            notes[i] = n;
        }

        return notes;
    }


}
