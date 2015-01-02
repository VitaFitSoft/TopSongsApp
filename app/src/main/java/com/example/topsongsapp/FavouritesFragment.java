package com.example.topsongsapp;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import com.example.topsongsapp.controller.FavouritesController;
import com.example.topsongsapp.topsongsinterface.TagName;
import com.example.topsongsapp.view.FavouriteDetailFragment;

public class FavouritesFragment extends Fragment implements TagName {

    public FavouritesFragment() {

    }

    private FrameLayout frmLayout;
    private Fragment contentFragment;
    private OnStopAnimationListener stopAnimationListener;

    @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            inflater.inflate(R.layout.favourites_fragment_home, container);
            findViewById();
            return super.onCreateView(inflater, container, savedInstanceState);
        }

        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
            FavouritesController favouritesController;

            FragmentManager fragmentManager = getFragmentManager();

            if (savedInstanceState != null) {
                if (savedInstanceState.containsKey(ARG_ITEM_ID)) {
                    String content = savedInstanceState.getString(ARG_ITEM_ID);
                    if (content.equals(FavouriteDetailFragment.ARG_ITEM_ID)) {
                        if (fragmentManager
                                .findFragmentByTag(FavouriteDetailFragment.ARG_ITEM_ID) != null) {
                            contentFragment = fragmentManager
                                    .findFragmentByTag(FavouriteDetailFragment.ARG_ITEM_ID);
                        }
                    }
                }
                if (fragmentManager.findFragmentByTag(FavouritesController.ARG_ITEM_ID) != null) {
                    favouritesController = (FavouritesController) fragmentManager
                            .findFragmentByTag(FavouritesController.ARG_ITEM_ID);
                    contentFragment = favouritesController;
                }
            } else {
                favouritesController = new FavouritesController();
                switchContent(favouritesController);
            }
        }

    private void findViewById() {
        frmLayout = (FrameLayout) getActivity().getWindow().findViewById(R.id.content_frame);
    }

    public interface OnStopAnimationListener {
        public void onStopAnimation();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try{
            stopAnimationListener = (OnStopAnimationListener) activity;
        }catch(ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnDatabaseLoadListener");
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (contentFragment instanceof FavouritesController) {
            outState.putString(ARG_ITEM_ID, FavouritesController.ARG_ITEM_ID);
        } else {
            outState.putString(ARG_ITEM_ID, FavouriteDetailFragment.ARG_ITEM_ID);
        }
        super.onSaveInstanceState(outState);
    }

    void switchContent(Fragment fragment) {
        FragmentManager fragmentManager = getFragmentManager();

        if (fragment != null) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.content_frame, fragment, FavouritesController.ARG_ITEM_ID);
            // Only FavouritesDetailFragment is added to the back stack.
            if (!(fragment instanceof FavouritesController)) {
                transaction.addToBackStack(FavouritesController.ARG_ITEM_ID);
            }
            transaction.commit();
            contentFragment = fragment;
        }
    }
	
    @Override
    public void onResume() {
      frmLayout.setVisibility(View.VISIBLE);
      super.onResume();
    }

    @Override
    public void onPause() {
      try{
          stopAnimationListener.onStopAnimation();
          frmLayout.setVisibility(View.GONE);
      }catch(ClassCastException e) {
        e.printStackTrace();
      }
      super.onPause();
    }

    @Override
    public void onDetach() {
      FragmentManager fm = getFragmentManager();
      if(fm.getBackStackEntryCount() > 0) {
        super.onDetach();
      }else if (contentFragment instanceof FavouritesController || fm.getBackStackEntryCount() == 0) {
        getActivity().finish();
      }
        super.onDetach();
    }

}