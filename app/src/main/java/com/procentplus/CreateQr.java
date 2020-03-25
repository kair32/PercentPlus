package com.procentplus;

import android.graphics.Bitmap;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.google.zxing.BarcodeFormat;
import com.journeyapps.barcodescanner.BarcodeEncoder;

public class CreateQr {
    public CreateQr(@Nullable String name, int id, ImageView iv){
        try {
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.encodeBitmap("PercentPlusApp" + "//" + name + "//" + id + "//15", BarcodeFormat.QR_CODE, 400, 400);
            iv.setImageBitmap(bitmap);
        } catch(Exception e) { }
    }
}
