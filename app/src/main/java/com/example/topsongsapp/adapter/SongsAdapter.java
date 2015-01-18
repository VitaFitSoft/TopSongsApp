package com.example.topsongsapp.adapter;

import com.example.topsongsapp.R;
import com.example.topsongsapp.controller.AppController;
import com.example.topsongsapp.model.Songs;
import java.util.List;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;


public class SongsAdapter extends BaseAdapter {

    private final List<Songs> mSongItems;
    private final int iResourceId;
    private final Context context;

    private ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    public SongsAdapter(Context context, List<Songs> songItems) {
        this.context = context;
        this.mSongItems = songItems;
        this.iResourceId = R.layout.songlist_data_rows;
    }

    @Override
    public int getCount() {
        return mSongItems.size();
    }

    @Override
    public Songs getItem(int location) {
      return mSongItems.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
      View view = convertView;
      SongsViewHolder songsHolder;

        if(view == null){
            LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = vi.inflate(iResourceId, parent, false);
            songsHolder = new SongsViewHolder();

            if (imageLoader == null) {
                imageLoader = AppController.getInstance().getImageLoader();
            }

            songsHolder.thumbNailImageView = (NetworkImageView) view.findViewById(R.id.topsongs_thumb_icon);
            songsHolder.songNameTextView = (TextView) view.findViewById(R.id.song_name_text);
            view.setTag(songsHolder);
        } else {
            songsHolder = (SongsViewHolder) view.getTag();
        }

        // getting song data for the rows
        Songs songs = mSongItems.get(position);
        if(songs != null) {
            String songName = songs.getSongName();
            songsHolder.songNameTextView.setText(songName);
            String image = getImage(songs);
            songsHolder.thumbNailImageView.setImageUrl(image, imageLoader);
        }
        return view;
    }

    private String getImage(Songs songs){
        List<Songs> mImageArray;
        mImageArray = songs.getmImageArray();
        Songs imageUrl  = mImageArray.get(1);
        return imageUrl.getMediumImageUrl();
    }

    static class SongsViewHolder {
        TextView songNameTextView;
        NetworkImageView thumbNailImageView;
    }// end anonomous class SongsViewHolder

}