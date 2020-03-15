package com.procentplus;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class Permission {

    public static final int REQUEST_CODE_PERMISSION_READ_ALL = 1;
    private int permissionStatusCAMERA ;
    private Context mcontext;

    public Permission(Context context) {
        mcontext = context;
        permissionStatusCAMERA = ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA);
    }

    public void requestCamera(){
        if (permissionStatusCAMERA == PackageManager.PERMISSION_GRANTED){
            Log.d("TAP", " = permission granted");
        }
        else {
            Log.d("TAP", " = permission zapros");
            ActivityCompat.requestPermissions((Activity) mcontext, new String[]{
                            Manifest.permission.CAMERA},
                    REQUEST_CODE_PERMISSION_READ_ALL);
        }
    }
}
