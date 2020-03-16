package com.procentplus;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;


public class Permission {

    public static final int REQUEST_CODE_PERMISSION_READ_ALL = 1;
    private int permissionStatusCAMERA ;
    private Context mcontext;

    public Permission(Context context) {
        mcontext = context;
        permissionStatusCAMERA = ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA);
    }

    public Boolean requestCamera(Fragment fragment){
        if (permissionStatusCAMERA == PackageManager.PERMISSION_GRANTED){
            Log.d("TAP", " = permission granted");
            return true;
        }
        else {
            Log.d("TAP", " = permission zapros");
            fragment.requestPermissions(
                    new String[]{Manifest.permission.CAMERA},
                    REQUEST_CODE_PERMISSION_READ_ALL);
            return false;
        }
    }
}
