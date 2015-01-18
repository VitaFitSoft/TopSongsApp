package com.example.topsongsapp.view;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.topsongsapp.R;
import com.example.topsongsapp.model.Favourites;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

public class FavouriteDetailFragment extends Fragment {

    private TextView fvdtIdTxt;
    private TextView fvdtNameTxt;
    private TextView fvdtArtistTxt;
    private ImageView fvdtImg;
    private final ImageLoader imageLoader = ImageLoader.getInstance();
    private DisplayImageOptions options;
    private ImageLoadingListener imageListener;
    public static final String ARG_ITEM_ID = "fvdt_detail_fragment";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        options = new DisplayImageOptions.Builder()
                .showImageOnFail(R.drawable.ic_launcher)
                .showImageForEmptyUri(R.drawable.ic_launcher).cacheOnDisk(true)
                .build();

        imageListener = new ImageDisplayListener();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pdt_detail, container, false);
        findViewById(view);
        Favourites favourites;

        Bundle bundle = this.getArguments();
        if(bundle != null) {
            favourites = bundle.getParcelable("singleFavourites");
            setFavouritesItem(favourites);
        }
        return view;
    }

    private void findViewById(View view) {
        fvdtNameTxt = (TextView) view.findViewById(R.id.pdt_name);
        fvdtIdTxt = (TextView) view.findViewById(R.id.favourites_id_text);
        fvdtArtistTxt = (TextView) view.findViewById(R.id.pdt_artist);
        fvdtImg = (ImageView) view.findViewById(R.id.favourites_detail_img);
    }

    private static class ImageDisplayListener extends SimpleImageLoadingListener {

        static final List<String> displayedImages = Collections.synchronizedList(new LinkedList<String>());

        @Override
        public void onLoadingComplete(String imageUri, View view,
                                      Bitmap loadedImage) {
            if(loadedImage != null) {
                ImageView imageView = (ImageView) view;
                boolean firstDisplay = !displayedImages.contains(imageUri);
                if(firstDisplay) {
                    FadeInBitmapDisplayer.animate(imageView, 500);
                    displayedImages.add(imageUri);
                }
            }
        }
    }

    private void setFavouritesItem(Favourites resultFavourites) {
        fvdtNameTxt.setText("" + resultFavourites.getSongName());
        fvdtArtistTxt.setText("" + resultFavourites.getArtist());
        fvdtIdTxt.setText("Favourites Id: " + resultFavourites.getSongId());
        imageLoader.displayImage(resultFavourites.getLargeImageUrl(), fvdtImg, options, imageListener);
    }

    @Override
    public void onDetach() {
      if(imageLoader != null) {
        imageLoader.destroy();
      }
      super.onDetach();
    }

}