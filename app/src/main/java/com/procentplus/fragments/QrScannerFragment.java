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
import com.procentplus.activities.AuthActivity;
import com.procentplus.activities.MainActivity;
import com.procentplus.databinding.FragmentScannerBinding;
import com.procentplus.retrofit.RetrofitClient;
import com.procentplus.retrofit.interfaces.ILogout;
import com.procentplus.retrofit.models.AuthResponse;
import com.procentplus.retrofit.models.CategoriesResponse;
import com.procentplus.retrofit.models.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class QrScannerFragment extends Fragment implements BarcodeCallback {
    public ObservableBoolean isCameraPermission = new ObservableBoolean(false);

    private String tag = "QrScannerFragment";
    private FragmentScannerBinding binding;
    private DecoratedBarcodeView cameraPreview;
    private User userDetail;
    private Retrofit retrofit;

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
        // init api
        retrofit = RetrofitClient.getInstance();
        cameraPreview = binding.decoratedBarcodeView;
        cameraPreview.resume();
        cameraPreview.decodeSingle(this);
        cameraPreview.setStatusText("");

        binding.logout.setOnClickListener(view1 -> logout());
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

    private void logout() {
        ILogout iLogout = retrofit.create(ILogout.class);

        Call<CategoriesResponse> call = iLogout.logOut(MainActivity.prefConfig.readToken());

        call.enqueue(new Callback<CategoriesResponse>() {
            @Override
            public void onResponse(Call<CategoriesResponse> call, Response<CategoriesResponse> response) {
                int statusCode = response.code();
                Log.d("LOGGER Logout", "statusCode: " + statusCode);
                if (statusCode == 200 || statusCode == 204) {
                    MainActivity.prefConfig.writeLoginStatus(false);
                    Intent intent = new Intent(getContext(), AuthActivity.class);
                    startActivity(intent);
                    getActivity().finish();
                }
            }

            @Override
            public void onFailure(Call<CategoriesResponse> call, Throwable t) {
                MainActivity.prefConfig.displayToast("Произошла ошибка при попытке выхода из профиля");
            }
        });
    }
}
