/*
 *
 *  * Created by Murillo Comino on 09/02/19 15:39
 *  * Github: github.com/MurilloComino
 *  * StackOverFlow: pt.stackoverflow.com/users/128573
 *  * Email: murillo_comino@hotmail.com
 *  *
 *  * Copyright (c) 2019 . All rights reserved.
 *  * Last modified 09/02/19 14:25
 *
 */

package com.example.myapplication;


import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


@SuppressLint("Registered")
public class Utils extends AppCompatActivity {


    //reduntante! versão mais nova não precisa declarar o Tipo;
   // protected <T extends View> T getViewById (int id) {
   //     return (T) findViewById(id);
   // }

    protected void setOnClickAction (final View view, View.OnClickListener action) {
        if (view != null) {
            view.setOnClickListener(action);
        }
    }

    protected void setToastOnClickAction (final int id) {
        final Button b = findViewById(id);
        final Resources r = getResources();
        final CharSequence text;

        text = "+"; //r.getString(R.string.lab01_toast_clicado, b.getText());

        setOnClickAction(b, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
            }
        });


    }

    protected void setOnClickAction (final int id, View.OnClickListener action) {
        View v = findViewById(id);
        setOnClickAction(v, action);
    }

    protected void setOnClickActivityAction (final int id, final Class<?> _class) {
        setOnClickAction(id, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), _class);

                startActivity(i);
            }
        });
    }


    public interface Keys {
        String CLIPBOARD = "image1.clip";
    }


    protected void copyToClipBoard(CharSequence key, CharSequence value, int idMessage)
    {
        ClipboardManager clipboard = (ClipboardManager)getSystemService(CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText(key, value);
        Toast t = Toast.makeText(getApplicationContext(), idMessage, Toast.LENGTH_LONG);

        if (clipboard != null) {
            clipboard.setPrimaryClip(clip);
        }
        t.show();
    }

    protected boolean copyToClipBoard(CharSequence key, CharSequence value) {
        //copyToClipBoard(key, value, R.string.clipboard);
        return false;
    }

    protected class copyToClipboardAction implements View.OnLongClickListener {
        String clipboard;
        int idMessage;

        public copyToClipboardAction(String clipboard, int idMessage) {
            this.clipboard = clipboard;
            this.idMessage = idMessage;


        }

        @Override
        public boolean onLongClick(View v) {
            ClipboardManager myClipboard;

            TextView tv = (TextView) v;

            myClipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText(clipboard, tv.getText());
            if (myClipboard != null) {
                myClipboard.setPrimaryClip(clip);
            }

            //Toast.makeText(getApplicationContext(), R.string.lab03_copy, Toast.LENGTH_SHORT).show();

            return true;
        }
    }


    //Gorjeta
    protected void setText (int id, CharSequence value) {
        TextView et = findViewById(id);
        et.setText(value);
    }

    protected void setOnLongClickAction(View view, View.OnLongClickListener action) {
        if (view != null) {
            view.setOnLongClickListener(action);
        }
    }

    protected void setOnLongClickAction (int id, View.OnLongClickListener action) {
        View v = findViewById(id);

        setOnLongClickAction(v, action);

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


