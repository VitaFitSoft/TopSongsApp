package com.example.topsongsapp.view;


import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import com.example.topsongsapp.R;
import com.example.topsongsapp.adapter.SongsAdapter;
import com.example.topsongsapp.io.LoadListItems;
import com.example.topsongsapp.model.Songs;
import com.example.topsongsapp.utils.SongImage;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class TopSongsList extends LoadListItems {
    private int mCurCheckPosition;
    private static List<Songs> mSongList;
    private static SongsAdapter songsAdapter;
    private ProgressDialog pDialog;
    private String mSongName;
    private String mArtist;
    private int mSongId;
	private SharedPreferences settings = null;
	private static final String SAVED_NAME = "TopSongsList";
    private static Handler handler;
    private Thread downloadThread;

    private FragmentActivity activity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        inflater.inflate(R.layout.activity_topsongs_list, container, false);

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
      super.onActivityCreated(savedInstanceState);
      this.activity = getActivity();

	  if(settings != null){
		getPreferences();
      }else{
        mSongList = new ArrayList<>();
        showProgressDialog();
        downloadThread = new DownLoadThread();
        downloadThread.start();
      }

      handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
          mSongList = getSongList();
          if(mSongList != null && !mSongList.isEmpty()) {
            songsAdapter = new SongsAdapter(activity, mSongList);
            setListAdapter(songsAdapter);
            // Sort the list.
            Collections.sort(mSongList, ALPHA_COMPARATOR);
            songsAdapter.notifyDataSetChanged();
            hideProgressDialog();
          }
        }
      };
    }// end onActivityCreated

    /**
     * Perform alphabetical comparison of application entry objects.
     */
    private static final Comparator<Songs> ALPHA_COMPARATOR = new Comparator<Songs>() {
        private final Collator sCollator = Collator.getInstance();
        @Override
        public int compare(Songs object1, Songs object2) {
            return sCollator.compare(object1.getSongName(), object2.getSongName());
        }
    };

    public class DownLoadThread extends Thread {
      @Override
      public void run() {
        connectAndDownLoad();
        try {
              new Thread().sleep(5000);
            }catch (InterruptedException e) {
               e.printStackTrace();
            }
            // Updates the user interface
        handler.sendEmptyMessage(0);
      }
    }

    /**
     * Called by PropertyList when a list item is selected
     *    /**
     * Reads the sample JSON data and loads it into a table.
     *  @param l ListView rows
     *  @param v View
     *  @param position of element in the list
     */
    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {

        Songs songs = songsAdapter.getItem(position);
        final int medium = 1;
        final int large = 2;
        String mLargeImageUrl = null;
        String mThumbImageUrl = null;
        mSongId = 0;
        byte[] mBlob;

        if(songsAdapter.getCount() > 0){
          mLargeImageUrl = getImage(large, songs);
          mThumbImageUrl = getImage(medium, songs);
          mSongName = songs.getSongName();
          mArtist = songs.getArtist();
          mSongId = songs.getSongId();
          mCurCheckPosition = position;
        }

        SongImage songdata = new SongImage();
        Bitmap mImage = songdata.getImage(large, mLargeImageUrl);
        mBlob = songdata.getBlob(mImage);


        FragmentManager fm = getFragmentManager();

        DialogFragment showSaveDialog = ShowAndSaveDialog.newInstance
           (mLargeImageUrl, mThumbImageUrl, mBlob, mSongName,mSongId, mArtist, position);
        showSaveDialog.setTargetFragment(this, 0);
        showSaveDialog.show(fm, "show_save_dialog");

    }// end onListItemClick method

    private String getImage(int size, Songs songs){
        List<Songs> mImageArray;
        String image = null;
        mImageArray = songs.getmImageArray();
        Songs imageUrl  = mImageArray.get(size);
        switch(size) {
          case 0:
            image = imageUrl.getThumbnailImageUrl();
            break;
          case 1:
            image = imageUrl.getMediumImageUrl();
            break;
          case 2:
            image = imageUrl.getLargeImageUrl();
            break;
          default:
            break;
        }
         return image;
    }


  @Override
  public void onPause(){
  	safePersistantState();
	super.onPause();

  }

  void safePersistantState(){
	settings = getActivity().getSharedPreferences(SAVED_NAME, 0);
    SharedPreferences.Editor editor = settings.edit();
    editor.putInt(ARG_POSITION, mCurCheckPosition);
    editor.putInt(KEY_ID, mSongId);
    editor.putString(KEY_SONG_NAME, mSongName);
    editor.putString(KEY_ARTIST, mArtist);
	// Commit the edits!
    editor.apply();
  }
  
  void getPreferences(){
    mCurCheckPosition = settings.getInt(ARG_POSITION, mCurCheckPosition);
    mSongId = settings.getInt(KEY_ID, mSongId);
    mSongName = settings.getString(KEY_SONG_NAME, mSongName);
    mArtist = settings.getString(KEY_ARTIST, mArtist);
   }// end method getPreferences

    @Override
    public void onSaveInstanceState(Bundle outState) {
      super.onSaveInstanceState(outState);
      outState.putInt(ARG_POSITION, mCurCheckPosition);
      outState.putString(KEY_SONG_NAME, mSongName);
      outState.putString(KEY_ARTIST, mArtist);
      outState.putInt(KEY_ID, mSongId);
      super.onSaveInstanceState(outState);
    }// end onSaveInstanceState

    void showProgressDialog() {
      pDialog = new ProgressDialog(getActivity());
      final String PLEASE_WAIT = getActivity().getResources().getString(R.string.wait_retrieving);
      pDialog.setMessage(PLEASE_WAIT);
      pDialog.setCancelable(true);
      if(!pDialog.isShowing())
        pDialog.show();
    }

    void hideProgressDialog() {
      if(pDialog != null) {
        try{
             pDialog.dismiss();
             pDialog = null;
           }catch(IllegalArgumentException e) {// nothing
              e.printStackTrace();
           }
        }
    }


}// end class TopChartList