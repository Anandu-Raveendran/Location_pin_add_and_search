package com.example.anandu_sem2_final_project.Database;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.room.TypeConverter;

import java.io.ByteArrayOutputStream;


public class ImageTypeConverter {

    @TypeConverter
    public static byte[] fromBitmap(Bitmap image) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.PNG, 0, byteArrayOutputStream);
        byte[] bytes = byteArrayOutputStream.toByteArray();
        return bytes;
    }

    @TypeConverter
    public static Bitmap toBitmap(byte[] array) {
        return BitmapFactory.decodeByteArray(array, 0, array.length);
    }

}

