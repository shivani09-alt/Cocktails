package com.example.cocktail;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;

import android.view.View;
import android.widget.Toast;

import me.dm7.barcodescanner.zbar.ZBarScannerView;

public class ScanActivity extends AppCompatActivity implements ZBarScannerView.ResultHandler  {

        boolean CameraPermission = false;
        private ZBarScannerView mScannerView;
        final int CAMERA_PERM = 1;
        //camera permission is needed.
        @Override
        public void onCreate(Bundle state) {
            super.onCreate(state);
            mScannerView = new ZBarScannerView(this);    // Programmatically initialize the scanner view
            setContentView(mScannerView);
            mScannerView.setBorderColor(this.getResources().getColor(R.color.white));
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//  set status text dark
            getWindow().setStatusBarColor(ContextCompat.getColor(this,R.color.white));// set status background white
            askPermission();// Set the scanner view as the content view
            if (CameraPermission) {
                mScannerView.setOnClickListener(v -> mScannerView.startCamera());
            }

        }
        @Override
        public void onResume() {
            super.onResume();
            if (CameraPermission) {
                mScannerView.setResultHandler(this); // Register ourselves as a handler for scan results.
                mScannerView.startCamera();
            }// Start camera on resume
        }
        @Override
        public void onPause() {

            if (CameraPermission) {
                mScannerView.stopCamera();
            }// Stop camera on pause
            super.onPause();
        }
        @Override
        public void handleResult(me.dm7.barcodescanner.zbar.Result result) {
            // Do something with the result here
//            Toast.makeText(getApplicationContext(),  result.getContents(), Toast.LENGTH_LONG).show();

//            Log.v("Result 1", result.getContents()); // Prints scan results
//            Log.v("Result 2", result.getBarcodeFormat().getName()); // Prints the scan format (qrcode, pdf417 etc.)
            MainActivity.noData.setVisibility(View.GONE);
            MainActivity.progressBar.setVisibility(View.VISIBLE);
            MainActivity.scannningData=true;
            MainActivity.searchData.setText(result.getContents());
            MainActivity.getScanResult results=new MainActivity.getScanResult();

            results.execute();
            this.finish();

//            MainActivity.tvresult.setText(result.getContents());
//            onBackPressed();
            // If you would like to resume scanning, call this method below:
            //mScannerView.resumeCameraPreview(this);
        }
    private void askPermission(){

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP){


            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ){

                ActivityCompat.requestPermissions(ScanActivity.this,new String[]{Manifest.permission.CAMERA},CAMERA_PERM);


            }else {

                mScannerView.startCamera();
                CameraPermission = true;
            }

        }


    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == CAMERA_PERM){


            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){

                mScannerView.startCamera();
                CameraPermission = true;
            }else {

                if (ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.CAMERA)){

                    new AlertDialog.Builder(this)
                            .setTitle("Permission")
                            .setMessage("Please provide the camera permission for using all the features of the app")
                            .setPositiveButton("Proceed", (dialog, which) -> ActivityCompat.requestPermissions(ScanActivity.this,new String[]{Manifest.permission.CAMERA},CAMERA_PERM)).setNegativeButton("Cancel", (dialog, which) ->{ dialog.dismiss();
                            onBackPressed();}).create().show();

                }else {

                    new AlertDialog.Builder(this)
                            .setTitle("Permission")
                            .setMessage("You have denied some permission. Allow all permission at [Settings] > [Permissions]")
                            .setPositiveButton("Settings", (dialog, which) -> {

                                dialog.dismiss();
                                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                        Uri.fromParts("package",getPackageName(),null));
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();


                            }).setNegativeButton("Cancel", (dialog, which) -> {

                                dialog.dismiss();
                                onBackPressed();

                            }).create().show();



                }

            }

        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


}
