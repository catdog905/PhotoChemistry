package com.example.myapplication.camera;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.helpers.MediaHelper;
import com.example.myapplication.R;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;

import java.io.IOException;
import java.util.ArrayList;

import static android.graphics.ImageDecoder.*;
import static android.provider.MediaStore.Images.Media.*;

public class CameraActivity extends AppCompatActivity {
    static{ System.loadLibrary("opencv_java3"); }
    public static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
    public final static String TAG_EQUATION = "com.example.myapplication.equations";
    public static final int PIC_CROP = 2;
    String str = "";
    Classifier classifier;
    ArrayList<String> outputs = new ArrayList<String>();;
    TextView textView;
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
        Log.d("Opencan", "Yes");
        setContentView(R.layout.activity_camera);
        //classifier = new Classifier(MediaHelper.assetFilePath(getApplicationContext(),"model.pt"));
        takePicture();
        bt_image = findViewById(R.id.lab04_bt_imagem_capturar);
        fl_camera = findViewById(R.id.lab04_fl_camera);
        textView = findViewById(R.id.textView);

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePicture();
            }
        };
        View.OnClickListener button_listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent answerIntent = new Intent();
                answerIntent.putExtra(TAG_EQUATION, str);
                setResult(RESULT_OK, answerIntent);
                finish();
            }
        };

        fl_camera.setOnClickListener(listener);
        bt_image.setOnClickListener(button_listener);

    }

    private void takePicture(){
        if (fl_camera != null)
            fl_camera.removeAllViews();
        str = "";
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

                if(requestCode ==CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
                    ImageView ivDATA = new ImageView(this);
                    try {
                        if (Build.VERSION.SDK_INT < 28) {
                            bitmap = getBitmap(this.getContentResolver(), fileUri);
                        } else {
                            Source source = createSource(this.getContentResolver(), fileUri);
                            bitmap = decodeBitmap(source);
                        }
                        ivDATA.setImageBitmap(bitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    //if(fileUri != null)
                    //cropImage();
                    ImageProcessing imageProcessing = new ImageProcessing(bitmap);
                    bitmap = imageProcessing.imagePreProcessing(bitmap);
                    outputs = imageProcessing.imageClassification(getApplicationContext());
                    for (String symbol : outputs) {
                        str += symbol;
                    }
                    textView.setText(str);
                    fl_camera.addView(ivDATA);
                }else if(requestCode == PIC_CROP){//get the returned data
                    Bundle extras = data.getExtras();
                    bitmap = extras.getParcelable("data");
                }

        }
    }


    private void cropImage(){
        try {
            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            //indicate image type and Uri
            cropIntent.setDataAndType(fileUri, "image/*");
            //set crop properties
            cropIntent.putExtra("crop", "true");
            //indicate aspect of desired crop
            cropIntent.putExtra("aspectX", 1);
            cropIntent.putExtra("aspectY", 1);
            //indicate output X and Y
            cropIntent.putExtra("outputX", 256);
            cropIntent.putExtra("outputY", 256);
            //retrieve data on return
            cropIntent.putExtra("return-data", true);
            //start the activity - we handle returning in onActivityResult
            startActivityForResult(cropIntent, PIC_CROP);
        }
        catch(ActivityNotFoundException anfe){
            //display an error message
            String errorMessage = "Your device doesn't support the crop action!";
            Toast toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelable("fileUri", fileUri);
    }

    // Recover the saved state when the activity is recreated.
    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        fileUri= savedInstanceState.getParcelable("fileUri");

    }
}

