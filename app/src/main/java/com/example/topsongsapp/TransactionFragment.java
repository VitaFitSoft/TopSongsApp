package com.example.topsongsapp;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.topsongsapp.topsongsinterface.ShowMessage;
import com.example.topsongsapp.topsongsinterface.TopSongInterface;
import com.example.topsongsapp.handler.DataBaseHandler;
import com.example.topsongsapp.model.Favourites;
import java.util.ArrayList;
import java.util.List;

public class  TransactionFragment extends Fragment implements TopSongInterface, ShowMessage {

    private DataBaseHandler dbh;
    public TransactionFragment(){

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        dbh = new DataBaseHandler(getActivity().getApplicationContext());
    }

    public List<Favourites> manageFavorites() {
        List<Favourites> fravArray = new ArrayList<>();
        //  DataBaseHandler db = new DataBaseHandler(getActivity());

        try{
            dbh.openRead();
            fravArray = dbh.getAllSongs();
        }catch(IllegalStateException e) {
            e.printStackTrace();
        }finally {
            dbh.close();
        }
        return fravArray;
    }

    public boolean deleteSongFromList(int songId) {
        boolean rtnId = false;
        try {
            dbh.openWrite();
            rtnId = dbh.deleteSongId(songId);
        }catch(NullPointerException npe) {
            npe.printStackTrace();
        }finally {
            dbh.close();
        }
        return rtnId;
    }

    public int countFavorites(){

        Cursor cRowCur;
        int rowCount = 0;
        try{
            dbh.openRead();
            cRowCur = dbh.getFavouritesCount();
            if(cRowCur != null) {
                if (cRowCur.moveToFirst()) {
                    rowCount = cRowCur.getInt(0);
                }
            }
        }catch(NullPointerException npe) {
            npe.printStackTrace();
        }finally {
            dbh.close();
        }
        return rowCount;
    }//end countFavorites method

    public long saveFavouritesToDb(int mSongId, String mLargeImageUrl, String mThumbUrl,
                                   String songName, byte[] mByteData, String artistName, int mPosition) {
        long rtnId = 0L;
        try {
            dbh.openWrite();
            rtnId = dbh.addFavourites(mSongId, mLargeImageUrl, mThumbUrl,
                    songName, mByteData, artistName, mPosition);
        }catch(NullPointerException npe) {
            npe.printStackTrace();
        }finally {
            dbh.close();
        }
        return rtnId;
    }

    public int getFavouritesId(int songId){
        int dbId = 0;
        int mIdColumn;
        Cursor cRowCur;
        try{
            dbh.openRead();
            cRowCur = dbh.getFavouritesId(songId);
            if(cRowCur != null) {
                mIdColumn = cRowCur.getColumnIndex(KEY_ID);
                if(cRowCur.moveToFirst()) {
                    dbId = cRowCur.getInt(mIdColumn);
                }
            }
        }catch(NullPointerException npe) {
            npe.printStackTrace();
        }finally {
            dbh.close();
        }
        return dbId;
    }

    public void showToast(CharSequence message, int rtnId){
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View layout = inflater.inflate(R.layout.toast1_layout,
                (ViewGroup) getActivity().findViewById(R.id.toast_layout_root));

        ImageView image = (ImageView) layout.findViewById(R.id.toast_image);
        if(rtnId < 0) {
          image.setImageResource(R.drawable.warning_image);
        }else{
          image.setImageResource(R.drawable.tick_image);
        }
        TextView text = (TextView) layout.findViewById(R.id.toast_text);
        text.setText(message);

        Toast toast = new Toast(getActivity().getApplicationContext());
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();
    }// end showToast method

}// end getFavorites class