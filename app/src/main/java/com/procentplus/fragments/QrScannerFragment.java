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
import com.procentplus.CreateQr;
import com.procentplus.Permission;
import com.procentplus.R;
import com.procentplus.activities.ActivityCalculate;
import com.procentplus.databinding.FragmentScannerBinding;
import com.procentplus.retrofit.models.AuthResponse;
import com.procentplus.retrofit.models.User;

import java.util.List;


public class QrScannerFragment extends Fragment implements BarcodeCallback {
    public ObservableBoolean isCameraPermission = new ObservableBoolean(false);

    private String tag = "QrScannerFragment";
    private FragmentScannerBinding binding;
    private DecoratedBarcodeView cameraPreview;
    private User userDetail;

    public QrScannerFragment(User userDetail){
        this.userDetail = userDetail;
    }

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
        String regex = "//";
        String[] s = str.split(regex);
        if (s.length!=4)return;
        if (!s[0].equals(CreateQr.NAME)) return;

        Intent intent = new Intent(getContext(), ActivityCalculate.class);
        intent.putExtra("userName", s[1]);
        intent.putExtra("userId", Integer.parseInt(s[2]));
        intent.putExtra("userBonus", s[3]);
        intent.putExtra("partnerId", userDetail.getPartner());
        intent.putExtra("operatorId", userDetail.getId());
        startActivity(intent);
    }

    public void onPermission(){
        new Permission(this.getContext()).requestCamera(this);
    }

    private void resumeScanner() {
        if (!cameraPreview.isActivated())
            cameraPreview.resume();
        cameraPreview.decodeSingle(this);
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
