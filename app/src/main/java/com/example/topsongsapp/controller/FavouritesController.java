package com.example.topsongsapp.controller;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import com.example.topsongsapp.R;
import com.example.topsongsapp.adapter.ImageSlideAdapter;
import com.example.topsongsapp.model.Favourites;
import com.example.topsongsapp.utils.CirclePageIndicator;
import com.example.topsongsapp.utils.PageIndicator;
import com.nostra13.universalimageloader.core.ImageLoader;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

@SuppressWarnings("ALL")
public class FavouritesController extends Fragment {
    public OnLoadAdapterListener loadAdapterListener;

    public FavouritesController(){

    }

    public static final String ARG_ITEM_ID = "favourites_controller";
    private static final String PREFS_NAME = "FavouritesController";
    private static final long ANIM_VIEWPAGER_DELAY = 5000;
    private static final long ANIM_VIEWPAGER_DELAY_USER_VIEW = 10000;
    private ViewPager mViewPager;
    TextView imgNameTxt;
    PageIndicator mIndicator;
    List<Favourites> mFavourites;
    private boolean stopSliding = false;
    private final ImageLoader imageLoader = ImageLoader.getInstance();
    private Runnable animateViewPager;
    private Handler handler;
    private int mPosition;
    private int mSongId;
    private int mPageSongId;
    private String mPageSongName;
    FragmentActivity activity;

    //
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_controller, container, false);
        findViewById(view);

        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey("PAGEPOSITION")) {
                mPosition = savedInstanceState.getInt("PAGEPOSITION");
            }
        }

        mIndicator.setOnPageChangeListener(new PageChangeListener());
        mViewPager.setOnPageChangeListener(new PageChangeListener());
        mViewPager.setOnTouchListener(new OnTouchListener() {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
        v.getParent().requestDisallowInterceptTouchEvent(true);
        switch (event.getAction()) {
          case MotionEvent.ACTION_CANCEL:
            break;
          case MotionEvent.ACTION_UP:
            // calls when touch release on ViewPager
            if(mFavourites != null && mFavourites.size() != 0) {
              stopSliding = false;
              runnable(mFavourites.size());
              handler.postDelayed(animateViewPager, ANIM_VIEWPAGER_DELAY_USER_VIEW);
              mPosition = mViewPager.getCurrentItem() + 1;
            }
            break;
          case MotionEvent.ACTION_MOVE:
            // calls when ViewPager touch
            if(handler != null && !stopSliding) {
              stopSliding = true;
              mPosition = mViewPager.getCurrentItem() + 1;
              handler.removeCallbacks(animateViewPager);
            }
            break;
        }
        return false;
      }
     });
     return view;
    }

    public interface OnLoadAdapterListener {
        public void onLoadAdapter(List<Favourites> mFavourites, boolean retain, int mSongId, String mSongName);
    }

    @Override
    public void onAttach(Activity activity) {
      super.onAttach(activity);
      try{
            loadAdapterListener = (OnLoadAdapterListener) activity;
         }catch(ClassCastException e) {
          throw new ClassCastException(activity.toString() + " must implement OnDatabaseLoadListener");
        }
    }

    private void findViewById(View view) {
        mViewPager = (ViewPager) view.findViewById(R.id.view_pager);
        mIndicator = (CirclePageIndicator) view.findViewById(R.id.indicator);
        imgNameTxt = (TextView) view.findViewById(R.id.img_name);
        Button fvdDelete = (Button) view.findViewById(R.id.delete_image);
        fvdDelete.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
           boolean retain = false;
          stopAnimation();
           loadAdapterListener.onLoadAdapter(mFavourites, retain, mPageSongId, mPageSongName);
            }
        });
    }


    public void runnable(final int size) {
      handler = new Handler();
      animateViewPager = new Runnable() {
        public void run() {
          if(!stopSliding) {
            if(mViewPager.getCurrentItem() == size - 1) {
              mViewPager.setCurrentItem(0);
              mPosition = mViewPager.getCurrentItem();
            }else{
              mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1, true);
                mPosition = mViewPager.getCurrentItem();
            }
            handler.postDelayed(animateViewPager, ANIM_VIEWPAGER_DELAY);
          }
        }
      };
    }

    @Override
    public void onResume() {
      if(mFavourites == null) {
        populateFavourites();
      }else{
        setPager(mFavourites);
      }
      super.onResume();
    }

    public void setPager(List<Favourites> favourites){
      mFavourites = favourites;
      if(!(mFavourites.isEmpty())) {
        try{
             mViewPager.setAdapter(new ImageSlideAdapter(activity, mFavourites));
             mIndicator.setViewPager(mViewPager);
             Collections.sort(mFavourites, ALPHA_COMPARATOR);
             mIndicator.notifyDataSetChanged();
             imgNameTxt.setText(""  + (mFavourites.get(mViewPager.getCurrentItem())).getSongName());
             mViewPager.setCurrentItem(mPosition, true);
             runnable(favourites.size());
             handler.postDelayed(animateViewPager, ANIM_VIEWPAGER_DELAY);
        }catch(NullPointerException npe) {
           npe.printStackTrace();
        }
      }
    }

    /**
     * Perform alphabetical comparison of application entry objects.
     */
    public static final Comparator<Favourites> ALPHA_COMPARATOR = new Comparator<Favourites>() {
        private final Collator sCollator = Collator.getInstance();
        @Override
        public int compare(Favourites object1, Favourites object2) {
            return sCollator.compare(object1.getSongName(), object2.getSongName());
        }
    };

    public void populateFavourites(){
      boolean retain = true;
      activity = getActivity();
      mFavourites = new ArrayList<Favourites>();
      loadAdapterListener.onLoadAdapter(mFavourites, retain, mSongId, mPageSongName);
    }


    public void stopAnimation(){
      if(handler != null) {
      //Remove callback
        handler.removeCallbacks(animateViewPager);
      }
    }

    @Override
    public void onPause() {
        stopAnimation();
      super.onPause();
    }

    @Override
    public void onStop(){

     super.onStop();
    }

    @Override
    public void onDetach() {
      super.onDetach();
    }

    private class PageChangeListener implements OnPageChangeListener {
      @Override
      public void onPageScrollStateChanged(int state) {
        if(state == ViewPager.SCROLL_STATE_IDLE) {
          if(mFavourites != null) {
            imgNameTxt.setText("" +(mFavourites.get(mViewPager.getCurrentItem())).getSongName());
            mPageSongName = (mFavourites.get(mViewPager.getCurrentItem())).getSongName();
            mPageSongId = (mFavourites.get(mViewPager.getCurrentItem())).getSongId();
          }
        }
      }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageSelected(int arg0) {
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
      outState.putInt("PAGEPOSITION", mPosition);
      super.onSaveInstanceState(outState);
    }
}