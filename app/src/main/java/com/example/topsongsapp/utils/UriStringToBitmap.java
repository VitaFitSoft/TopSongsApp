package com.example.topsongsapp.utils;

import java.io.InputStream;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

class UriStringToBitmap {

 //   private static final String PREFS_NAME = "UriStringToBitmap";

    public UriStringToBitmap() {

    }

    public Bitmap decodeImageString(String url) {
        Bitmap bitmap = null;
         try {
              InputStream in = new java.net.URL(url).openStream();
              bitmap = BitmapFactory.decodeStream(new FlushedInputStream(in));
            }   catch (Exception e) {
            e.printStackTrace();
            }
        return bitmap;
    }
}