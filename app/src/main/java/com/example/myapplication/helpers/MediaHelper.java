/*
 *
 *  * Created by Murillo Comino on 09/02/19 15:39
 *  * Github: github.com/MurilloComino
 *  * StackOverFlow: pt.stackoverflow.com/users/128573
 *  * Email: murillo_comino@hotmail.com
 *  *
 *  * Copyright (c) 2019 . All rights reserved.
 *  * Last modified 09/02/19 15:39
 *
 */

package com.example.myapplication.helpers;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.util.Log;


import com.example.myapplication.BuildConfig;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public final class MediaHelper {
    private static final int TYPE_IMAGE = 1;
    private static final int TYPE_VIDEO = 2;

    private MediaHelper() {
        super();
    }

    //Determina o local e nome do arquivo gerado
    @SuppressWarnings("deprecation")
    private static File getOutputMediaFile(final int type, Context context) {
        File path;
        File dir;
        String timeStamp;
        File mediaFile;

        if (Build.VERSION.SDK_INT >= 29){
            path = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        } else {
            path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        }
        dir = new File(path, "image1");

        if (!dir.exists()) {
            if (!dir.mkdirs()) {
                Log.d("LAB04", "Pasta não criada");
                return null;
            }
        }

        timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());

        switch (type) {
            case TYPE_IMAGE:
                mediaFile = new File(String.format("%s%sIMG_%s.jpg",
                        dir.getPath(), File.separator, timeStamp));
                break;

            case TYPE_VIDEO:
                mediaFile = new File(String.format("%s%sVID_%s.mp4",
                        dir.getPath(), File.separator, timeStamp));
                break;
            default:
                mediaFile = null;
                break;
        }
        return mediaFile;
    }

    public static Uri getOutputMediaImageFileUri(Context context) {
        return getUri(context, TYPE_IMAGE);
    }

    private static Uri getUri(Context context, int type) {
        Uri uri;
        uri = getUriFrom(context, getOutputMediaFile(type, context));
        return uri;
    }

    private static Uri getUriFrom(Context context, File file) {
        return FileProvider.getUriForFile(context, getAuthority(), file);
    }

    @NonNull
    private static String getAuthority() {
        return BuildConfig.APPLICATION_ID + ".provider";
    }

    public static String assetFilePath(Context context, String assetName) {
        File file = new File(context.getFilesDir(), assetName);

        try (InputStream is = context.getAssets().open(assetName)) {
            try (OutputStream os = new FileOutputStream(file)) {
                byte[] buffer = new byte[4 * 1024];
                int read;
                while ((read = is.read(buffer)) != -1) {
                    os.write(buffer, 0, read);
                }
                os.flush();
            }
            return file.getAbsolutePath();
        } catch (IOException e) {
            Log.e("pytorchandroid", "Error process asset " + assetName + " to file path");
        }
        return null;
    }
}
