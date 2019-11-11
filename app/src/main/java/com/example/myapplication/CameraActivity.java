package com.example.myapplication;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Mat;

import java.io.IOException;
import java.util.ArrayList;

import static android.graphics.ImageDecoder.*;
import static android.provider.MediaStore.Images.Media.*;

public class CameraActivity extends AppCompatActivity {
    static{ System.loadLibrary("opencv_java3"); }
    public static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;


    EditText editText;
    EditText editText2;
    private Button bt_image;
    private FrameLayout fl_camera;
    private Uri fileUri;
    private Bitmap bitmap;
    private BaseLoaderCallback mOpenCVCallBack = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS:
                    //DO YOUR WORK/STUFF HERE
                    break;
                default:
                    super.onManagerConnected(status);
                    break;
            }
        }
    };



    @Override
    protected void onResume() {
        super.onResume();
        OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_2_4_8, this,
                mOpenCVCallBack);
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_camera);

        takePicture();
        bt_image = findViewById(R.id.lab04_bt_imagem_capturar);
        fl_camera = findViewById(R.id.lab04_fl_camera);
        editText = findViewById(R.id.editText);
        editText2 = findViewById(R.id.editText2);

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePicture();
            }
        };
        View.OnClickListener button_listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageView ivDATA = new ImageView(getApplicationContext());
                ImageProcessing imageProcessing = new ImageProcessing(bitmap);
                bitmap = imageProcessing.imagePreProcessing(bitmap, Integer.parseInt(editText.getText().toString()), Integer.parseInt(editText2.getText().toString()));
                ArrayList<Mat> symbols = imageProcessing.getSymbols();
                ivDATA.setImageBitmap(bitmap);
                fl_camera.addView(ivDATA);
            }
        };

        fl_camera.setOnClickListener(listener);
        bt_image.setOnClickListener(button_listener);

    }

    private void takePicture(){
        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        fileUri = MediaHelper.getOutputMediaImageFileUri(getBaseContext());
        i.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        startActivityForResult(i, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);


    }

    @SuppressWarnings("deprecation")
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        fl_camera.removeAllViews();


        switch (resultCode) {

            case RESULT_OK:

                ImageView ivDATA = new ImageView(this);
                try {
                    if (Build.VERSION.SDK_INT < 28) {
                        bitmap = getBitmap(this.getContentResolver(), fileUri);
                    } else {
                        Source source = createSource(this.getContentResolver(), fileUri);
                        bitmap = decodeBitmap(source);
                    }
                    //ImageProcessing imageProcessing = new ImageProcessing(bitmap);
                    //bitmap = imageProcessing.imagePreProcessing(bitmap);
                    ivDATA.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }


                fl_camera.addView(ivDATA);
                break;


        }
    }
}

