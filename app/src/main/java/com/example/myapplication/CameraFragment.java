/*package com.example.myapplication;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.transform.Source;

import static android.app.Activity.RESULT_OK;
import static android.provider.MediaStore.Images.Media.getBitmap;

//public class CameraFragment extends Fragment {
public class CameraFragment extends AppCompatActivity {

    public static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
    public static final int CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE = 200;

    private Button bt_image;
    private Button bt_video;
    private FrameLayout fl_camera;
    private Uri fileUri;

    private Resources r;

    //ação de captura de imagem
    private class ImagemOnClickAction implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            //TODO Caso queira gravar em disco
            fileUri = MediaHelper.getOutputMediaImageFileUri(getBaseContext());
            i.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
            //
            startActivityForResult(i, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);

        }
    }

    //Ação para capturar video
    private class VideoOnClickAction implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            Intent i;

            i = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
            fileUri = MediaHelper.getOutputMediaVideoFileUri(getBaseContext());

            i.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

            startActivityForResult(i, CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE);

        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_camera);


        r = getResources();
        bt_image = findViewById(R.id.lab04_bt_imagem_capturar);
        bt_video = findViewById(R.id.lab04_bt_video_capturar);
        fl_camera = findViewById(R.id.lab04_fl_camera);

        //Acessa a camera para filmar;
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        bt_image.setOnClickListener(new ImagemOnClickAction());
        bt_video.setOnClickListener(new VideoOnClickAction());

    }
    @SuppressWarnings("deprecation")
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String message = null;

        fl_camera.removeAllViews();

        switch (requestCode) {
            case CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE:


                //tratamento da imagem
                switch (resultCode) {

                    case RESULT_OK:

                        ImageView ivDATA = new ImageView(this);
                        Bitmap bitmap;
                        try {
                            if (Build.VERSION.SDK_INT < 28){
                                bitmap = getBitmap(this.getContentResolver(), fileUri);
                            } else {
                                Source source = createSource(this.getContentResolver(), fileUri);
                                bitmap = decodeBitmap(source);
                            }
                            ivDATA.setImageBitmap(bitmap);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        bt_image.setBackgroundColor(Color.GREEN);

                        bt_video.setBackgroundColor(Color.LTGRAY);

                        fl_camera.addView(ivDATA);
                        break;
                    case RESULT_CANCELED:
                        message = r.getString(R.string.lab04_toast_imagem_cancelada);
                        break;
                }

                break;
            case CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE:
                //tratamento de resultado para vídeo
                switch (resultCode) {
                    case RESULT_OK:

                        VideoView vvDATA = new VideoView(this);
                        MediaController mc = new MediaController(this);
                        FrameLayout.LayoutParams lpPARAMS = new FrameLayout.LayoutParams(
                                FrameLayout.LayoutParams.MATCH_PARENT,
                                FrameLayout.LayoutParams.WRAP_CONTENT);

                        lpPARAMS.gravity = Gravity.CENTER;

                        vvDATA.setLayoutParams(lpPARAMS);
                        vvDATA.setMediaController(mc);
                        vvDATA.setVideoURI(fileUri);

                        fl_camera.addView(vvDATA);
                        bt_image.setBackgroundColor(Color.LTGRAY);
                        bt_video.setBackgroundColor(Color.GREEN);
                        break;
                    case RESULT_CANCELED:
                        message = r.getString(R.string.lab04_toast_video_cancelada);
                        break;
                }
                break;
        }

        if (message != null) {
            Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        }
    }
}
*/