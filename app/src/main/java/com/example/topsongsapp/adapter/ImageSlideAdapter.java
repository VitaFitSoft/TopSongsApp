package com.example.topsongsapp.adapter;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.example.topsongsapp.R;
import com.example.topsongsapp.model.Favourites;
import com.example.topsongsapp.view.FavouriteDetailFragment;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

public class ImageSlideAdapter extends PagerAdapter {

	private final DisplayImageOptions options;
	private final ImageLoadingListener imageListener;
    private static final String ARG_SINGLE_FAVOURITES ="singleFavourites";
	private static FragmentActivity mActivity;
	private final List<Favourites> favourites;
    private static ImageLoader imageLoader;

	public ImageSlideAdapter(FragmentActivity activity, List<Favourites> favourites) {
        {
           	mActivity = activity;
            this.favourites = favourites;

            options = new DisplayImageOptions.Builder()
               .showImageOnFail(R.drawable.ic_launcher)
               .showImageForEmptyUri(R.drawable.ic_launcher)
               .showImageForEmptyUri(R.drawable.ic_launcher)
               .cacheInMemory(true)
               .cacheOnDisk(true)
               .build();

            imageLoader = ImageLoader.getInstance();
            imageListener = new ImageDisplayListener();
        }
        try {
            imageLoader.init(ImageLoaderConfiguration.createDefault(activity));
        }catch(NullPointerException npe) {
            npe.printStackTrace();
        }
    }

    @Override
	public int getCount() {
		return favourites.size();
	}

	@Override
	public View instantiateItem(ViewGroup container, final int position) {
      LayoutInflater inflater = (LayoutInflater) mActivity.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
	  View view = inflater.inflate(R.layout.vp_image, container, false);
	  ImageView mImageView = (ImageView) view.findViewById(R.id.image_display);

	  mImageView.setOnClickListener(new OnClickListener() {

        @Override
        public void onClick(View v) {
            Bundle arguments = new Bundle();
            Fragment detailFragment;
            Favourites favourite =  favourites.get(position);
            arguments.putParcelable(ARG_SINGLE_FAVOURITES, favourite);

            // Start a new fragment
            detailFragment = new FavouriteDetailFragment();
            detailFragment.setArguments(arguments);

            FragmentTransaction transaction = mActivity.getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.content_frame, detailFragment, FavouriteDetailFragment.ARG_ITEM_ID);
            transaction.addToBackStack(FavouriteDetailFragment.ARG_ITEM_ID);
            transaction.commit();
        }
	  });

      ImageSlideAdapter.imageLoader.displayImage((favourites.get(position)).getLargeImageUrl(), mImageView, options, imageListener);
      container.addView(view);
      return view;
	}

    @Override
    public int getItemPosition(Object object){
        return PagerAdapter.POSITION_NONE;
    }

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView((View) object);
	}

     @Override
	public boolean isViewFromObject(View view, Object object) {
		return view == object;
	}

	private static class ImageDisplayListener extends SimpleImageLoadingListener {

      static final List<String> displayedImages = Collections.synchronizedList(new LinkedList<String>());

      @Override
	  public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
        super.onLoadingComplete(imageUri, view, loadedImage);
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
}