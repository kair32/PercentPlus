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
import androidx.fragment.app.Fragment;

import com.google.zxing.ResultPoint;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.CameraPreview;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;
import com.procentplus.Permission;
import com.procentplus.databinding.FragmentScannerBinding;
import com.procentplus.retrofit.RetrofitClient;

import java.util.List;

import retrofit2.Retrofit;

public class QrScannerFragment extends Fragment {
    String tag = "QrScannerFragment";
    FragmentScannerBinding binding;
    private Retrofit retrofit;
    private DecoratedBarcodeView cameraPreview;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == Permission.REQUEST_CODE_PERMISSION_READ_ALL)
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    Log.d("TAP", " 0 = permission granted");
                    cameraPreview.resume();
        } else {
            Log.d("TAP", " 0 = permission denied");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // init api
        retrofit = RetrofitClient.getInstance();

        new Permission(this.getContext()).requestCamera();
        binding = FragmentScannerBinding.inflate(inflater, container, false);
        cameraPreview = binding.decoratedBarcodeView;

        cameraPreview.decodeSingle(new BarcodeCallback() {
            @Override
            public void barcodeResult(BarcodeResult result) {
                Log.d(tag, "result = " + result.getText());
            }

            @Override public void possibleResultPoints(List<ResultPoint> resultPoints) { }
        });
        binding.btStart.setOnClickListener(v -> {
            Intent intent = new Intent("com.google.zxing.client.android.SCAN");
            intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
            startActivityForResult(intent, 0);
        });
        return binding.getRoot();
    }

}
