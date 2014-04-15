package com.ridecharge.android.screenflow;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Environment;
import android.view.View;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.HashMap;

/**
 * Created by ahuttner on 4/11/14.
 */
public class ScreenFlow {
    private static ScreenFlow mInstance = null;

    private static String mPath = null;

    private MajorStep mCurStep;
    private String mCurActivity;

    private static String mMajorStepFile;

    protected ScreenFlow() {
        // do not instantiate
    }

    public static ScreenFlow getInstance() {
        if (mInstance == null) {
            mPath = Environment.getExternalStorageDirectory().toString() + "/screenflow";
            mInstance = new ScreenFlow();
            mMajorStepFile = HtmlWriter.writeIndex(mPath);

        }
        return mInstance;
    }


    public void recordStep(Activity activity, String message) {

        if (mCurActivity == null || mCurActivity.isEmpty() || !mCurActivity.equals(activity.getClass().getSimpleName())) {
            mCurActivity = activity.getClass().getSimpleName();
            String fileName = saveScreenCap(activity);

            mCurStep = new MajorStep(mCurActivity, fileName);
            writeMajorInBackground();
        }

        mCurStep.addMinorStep(message);
        writeMinorInBackground();
    }

    private void writeMajorInBackground() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String minorStepFile = HtmlWriter.writeMajorStep(mPath, mMajorStepFile, mCurStep);
                mCurStep.addMinorStep(minorStepFile);
            }
        }).start();
    }

    private void writeMinorInBackground() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HtmlWriter.writeMinorStep(mPath, mCurStep.minorStepFile, mCurStep);
            }
        }).start();
    }

//    private void saveFileInBackground(final Activity activity){
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//               saveScreenCap(activity);
//            }
//        }).start();
//    }

    private String saveScreenCap(Activity activity) {
        Bitmap bm;
        View v = activity.getWindow().getDecorView().getRootView();
        v.setDrawingCacheEnabled(true);
        bm = Bitmap.createBitmap(v.getDrawingCache());
        v.setDrawingCacheEnabled(true);

        String fileName = null;
        OutputStream out;
        try {
            fileName = activity.getClass().getSimpleName() + "_" + System.currentTimeMillis();
            File screenCap = new File(mPath + "/" + fileName);
            out = new FileOutputStream(screenCap);
            bm.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.flush();
            out.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return fileName;
    }


}
