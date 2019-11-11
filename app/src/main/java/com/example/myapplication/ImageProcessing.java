package com.example.myapplication;

import android.graphics.Bitmap;

import org.opencv.android.Utils;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;

import static org.opencv.imgproc.Imgproc.ADAPTIVE_THRESH_GAUSSIAN_C;
import static org.opencv.imgproc.Imgproc.ADAPTIVE_THRESH_MEAN_C;
import static org.opencv.imgproc.Imgproc.THRESH_BINARY;
import static org.opencv.imgproc.Imgproc.THRESH_BINARY_INV;
import static org.opencv.imgproc.Imgproc.preCornerDetect;

public class ImageProcessing {
    static {
        System.loadLibrary("opencv_java3");
    }
    ArrayList<Mat> symbols;
    private Bitmap bitmap;

    public ImageProcessing(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public Bitmap imagePreProcessing(Bitmap bitmap, int bS, int C) {
        Mat mat = new Mat(bitmap.getHeight(), bitmap.getWidth(), CvType.CV_8UC4);
        Bitmap bmp32 = bitmap.copy(Bitmap.Config.RGB_565, true);
        Utils.bitmapToMat(bmp32, mat);
        Mat grayMat = new Mat();
        Imgproc.cvtColor(mat, grayMat, Imgproc.COLOR_BGR2GRAY);

        Mat gBMat = new Mat();
        Mat aTMat = new Mat();
        Imgproc.GaussianBlur(grayMat, gBMat, new Size(19, 19), 2, 2);
        Imgproc.adaptiveThreshold(gBMat, aTMat, 255, ADAPTIVE_THRESH_GAUSSIAN_C, THRESH_BINARY, bS, C);

        List<org.opencv.core.MatOfPoint> contours = new ArrayList<org.opencv.core.MatOfPoint>();
        Mat hierarchy = new Mat();
        Imgproc.findContours(aTMat, contours, hierarchy, Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_NONE);

        symbols = new ArrayList<Mat>();
        for (int i = 0; i < contours.size(); i++) {
            Rect rect = Imgproc.boundingRect(contours.get(i));
            // find the best candidates
            if (rect.height < aTMat.height() / 15 )
                continue;
            //Imgproc.drawContours(aTMat, rec, i, new Scalar(0, 255, 0), 1);
            symbols.add(aTMat.submat(rect));
            Imgproc.rectangle(aTMat, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height), new Scalar(0, 255, 0, 255), 5);
        }

        //Rect rect_min = new Rect();
        //rect_min = contoursImgproc.boundingRect(contours.get(i));

        //Mat result = original.submat(rect_min);

        Bitmap frame = Bitmap.createBitmap(aTMat.width(), aTMat.height(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(aTMat, frame);
        return frame;
    }

    public ArrayList<Mat> getSymbols() {
        return symbols;
    }
}
