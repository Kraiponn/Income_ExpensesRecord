package com.ksn.kraiponn.income_expensesrecord.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class ScanningCodeActivity extends AppCompatActivity
        implements ZXingScannerView.ResultHandler {

    private ZXingScannerView mScannerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mScannerView = new ZXingScannerView(this);
        setContentView(mScannerView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this);
        mScannerView.startCamera();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mScannerView.stopCamera();
    }

    @Override
    public void handleResult(Result result) {
        // Do something with the result here
        // Prints scan results
        //Log.v("Print Scan", result.getText());
        // Prints the scan format (qrcode, pdf417 etc.)
        //Log.v("Print Scan2", result.getBarcodeFormat().toString());

        // If you would like to resume scanning, call this method below:
        //mScannerView.resumeCameraPreview(this);

        mScannerView.stopCamera();
        Intent itn = new Intent();
        itn.putExtra("barcode", result.getText());
        setResult(RESULT_OK, itn);
        finish();
    }
}
