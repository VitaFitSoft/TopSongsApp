package com.example.topsongsapp.utils;


import java.io.ByteArrayOutputStream;
import android.graphics.Bitmap;


public class BlobGenerator {
    private static final int largeWidth = 250;
    private static final int largeHeight = 250;


	BlobGenerator(){
	}

	public Bitmap getImage(final int size, final String songUri){
      if(songUri != null) {
        try {
              UriStringToBitmap uStoBm = new UriStringToBitmap();
              final Bitmap bpap =  uStoBm.decodeImageString(songUri);
              if(size == 2){
                return scaleImage(bpap);
              }else {
                return bpap;
              }
            }catch(NullPointerException e) {// nothing
               e.printStackTrace();
            }
	   }
	   return null;
	}

    Bitmap scaleImage(Bitmap bitmap){
      Bitmap largeBitmap = Bitmap.createScaledBitmap(bitmap, largeWidth, largeHeight, true);
      bitmap.recycle();
      return largeBitmap;
    }

    public byte[] getBlob(Bitmap bitmap) {
        try {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            return stream.toByteArray();
        } catch (NullPointerException e) {// nothing
            e.printStackTrace();
        }
        return null;
    }
  }