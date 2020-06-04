package com.procentplus;

import android.graphics.Bitmap;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.google.zxing.BarcodeFormat;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.procentplus.activities.MainActivity;

public class CreateQr {
    public static String NAME = "PercentPlusApp";
    public CreateQr(@Nullable String name, ImageView iv){
        try {
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Log.d("tag",""+ MainActivity.prefConfig.readId());
            Bitmap bitmap = barcodeEncoder.encodeBitmap(NAME + "//" + name + "//" + MainActivity.prefConfig.readId() + "//15", BarcodeFormat.QR_CODE, 400, 400);
            iv.setImageBitmap(bitmap);
        } catch(Exception e) { }
    }
}
