package com.example.mayur.socketclient;

import android.os.Environment;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by mayur on 12/3/2016.
 */

public class FileUtil {
    public static String PATH = Environment.getExternalStorageDirectory()
            .getAbsolutePath() + "/";

    public static void writeTextFile(File file, String str) throws IOException {
        DataOutputStream out = null;
        try {
            out = new DataOutputStream(new FileOutputStream(file));
            out.write(str.getBytes());
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }
    // just test
}
