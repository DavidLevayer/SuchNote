package android.uqacproject.com.suchnote;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by David Levayer on 23/03/15.
 */
public class FileManager {

    private final static String FILE_PATH = Environment.getExternalStorageDirectory().getPath();
    private final static String TEXT_DIRECTORY = "/SuchText/text/";

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
}
