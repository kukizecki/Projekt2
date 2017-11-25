package com.example.student.projektfm2;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;


public class MainActivity extends AppCompatActivity {

    private Button przycisk1;
    private android.hardware.Camera camera;
    private boolean isFlashOn;
    private boolean hasFlash;
    private android.hardware.Camera.Parameters params;


     @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        przycisk1 = (Button) findViewById(R.id.button4);

         hasFlash = getApplicationContext().getPackageManager()
                 .hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
         if (!hasFlash) {
             // device doesn't support flash
             // Show alert message and close the application
             AlertDialog alert = new AlertDialog.Builder(MainActivity.this)
                     .create();
             alert.setTitle("Error");
             alert.setMessage("Sorry, your device doesn't support flashlight!");
             alert.setButton("OK", new DialogInterface.OnClickListener() {
                 public void onClick(DialogInterface dialog, int which) {
                     // closing the application
                     finish();
                 }
             });
             alert.show();
             return;
         }

        przycisk1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (isFlashOn) {
                    // turn off flash
                    turnOffFlash();
                } else {
                    // turn on flash

                    System.out.print("Włącz... latarkę");

                    turnOnFlash();
                }

//                AlertDialog alert = new AlertDialog.Builder(MainActivity.this)
//                        .create();
//                alert.setTitle("Test");
//                alert.setMessage("Jajeczko");
//                alert.setButton("OK", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        // closing the application
//                        finish();
//                    }
//                });
//                alert.show();

            }

        });

    }
    @SuppressLint("LongLogTag")
    private void getCamera() {
        if (camera == null) {
            try {
                camera = camera.open();
                params = camera.getParameters();
            } catch (RuntimeException e) {
                Log.e("Camera Error. Failed to Open. Error: ", e.getMessage());
            }
        }
    }

    private void turnOnFlash() {
        if (!isFlashOn) {
            if (camera == null || params == null) {
                return;
            }

            params = camera.getParameters();
            params.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
            camera.setParameters(params);
            camera.startPreview();
            isFlashOn = true;

        }
    }

    private void turnOffFlash() {
        if (isFlashOn) {
            if (camera == null || params == null) {
                return;
            }

            params = camera.getParameters();
            params.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
            camera.setParameters(params);
            camera.stopPreview();
            isFlashOn = false;

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();

        // on pause turn off the flash
        turnOffFlash();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onResume() {
        super.onResume();

        // on resume turn on the flash
        if(hasFlash)
            turnOnFlash();
    }

    @Override
    protected void onStart() {
        super.onStart();

        // on starting the app get the camera params
        getCamera();
    }

    @Override
    protected void onStop() {
        super.onStop();

        // on stop release the camera
        if (camera != null) {
            camera.release();
            camera = null;
        }
    }
}
