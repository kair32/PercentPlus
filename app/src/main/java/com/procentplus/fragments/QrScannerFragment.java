package com.procentplus.fragments;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.ObservableBoolean;
import androidx.fragment.app.Fragment;

import com.google.zxing.ResultPoint;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;
import com.procentplus.BuildConfig;
import com.procentplus.Permission;
import com.procentplus.databinding.FragmentScannerBinding;
import com.procentplus.retrofit.RetrofitClient;

import java.util.List;

import retrofit2.Retrofit;

public class QrScannerFragment extends Fragment implements BarcodeCallback {
    public ObservableBoolean isCameraPermission = new ObservableBoolean(false);

    private String tag = "QrScannerFragment";
    private FragmentScannerBinding binding;
    private DecoratedBarcodeView cameraPreview;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == Permission.REQUEST_CODE_PERMISSION_READ_ALL)
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Log.d("TAP", " 0 = permission granted");
                isCameraPermission.set(true);
                cameraPreview.resume();
            } else {
                Log.d("TAP", " 0 = permission denied");
                isCameraPermission.set(false);
            }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // init api
        isCameraPermission.set(new Permission(this.getContext()).requestCamera(this));
        binding = FragmentScannerBinding.inflate(inflater, container, false);
        binding.setFragment(this);
        cameraPreview = binding.decoratedBarcodeView;
        cameraPreview.resume();
        cameraPreview.decodeSingle(this);
        cameraPreview.setStatusText("");

        return binding.getRoot();
    }
    @Override
    public void barcodeResult(BarcodeResult result) {
        Log.d(tag, "result = " + result.getText());
        String str = result.getText();
        String[] s = str.split(BuildConfig.APPLICATION_ID);
    }

    public void onPermission(){
        new Permission(this.getContext()).requestCamera(this);
    }

    private void resumeScanner() {
        if (!cameraPreview.isActivated())
            cameraPreview.resume();
    }

    private void pauseScanner() {
        cameraPreview.pause();
    }

    @Override
    public void onResume() {
        super.onResume();
        resumeScanner();
    }

    @Override
    public void onPause() {
        super.onPause();
        pauseScanner();
    }

    @Override
    public void possibleResultPoints(List<ResultPoint> resultPoints) { }
}
