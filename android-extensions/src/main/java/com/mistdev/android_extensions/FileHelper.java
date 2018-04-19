package com.mistdev.android_extensions;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by mcastro on 08/04/17.
 */

public class FileHelper {

    public static boolean copyFile(String inputFilePath, OutputStream output) {

        boolean success = false;

        try {

            File file = new File(inputFilePath);

            //If doesn't exist and couldn't create, return
            if(!file.exists() && !file.createNewFile()) {
                return false;
            }

            InputStream input = new FileInputStream(file);

            byte[] buffer = new byte[1024];
            int length;
            while ((length = input.read(buffer)) != -1) {
                output.write(buffer, 0, length);
            }

            //Close the streams
            output.flush();
            output.close();
            input.close();

            success = true;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return success;
    }

    public static boolean copyFile(InputStream input, String outputFilePath) {

        boolean success = false;

        try {

            File file = new File(outputFilePath);

            //If doesn't exist and couldn't create, return
            if(!file.exists() && !file.createNewFile()) {
                return false;
            }

            OutputStream output = new FileOutputStream(file);

            byte[] buffer = new byte[1024];
            int length;
            while ((length = input.read(buffer)) > 0){
                output.write(buffer, 0, length);
            }

            //Close the streams
            output.flush();
            output.close();
            input.close();

            success = true;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return success;
    }

}
