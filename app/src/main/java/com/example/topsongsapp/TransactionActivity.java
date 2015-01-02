/*
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.topsongsapp;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import com.example.topsongsapp.controller.FavouritesController;
import com.example.topsongsapp.model.Favourites;
import com.example.topsongsapp.view.ShowAndSaveDialog;
import java.util.List;

public class TransactionActivity extends FragmentActivity implements  ShowAndSaveDialog.OnSaveFavouritesListener,
        FavouritesController.OnLoadAdapterListener,  FavouritesFragment.OnStopAnimationListener {

    private TransactionFragment getTranFragment(){
     //   TransactionFragment trans = (TransactionFragment) getSupportFragmentManager().findFragmentByTag("trans");
        return (TransactionFragment) getSupportFragmentManager().findFragmentByTag("trans");
      //  return trans;
    }

    private Fragment getLastFragment(){
  //    Fragment contentFragment = (Fragment) getSupportFragmentManager().findFragmentById(R.id.content_frame);
      return getSupportFragmentManager().findFragmentById(R.id.content_frame);

    //    return contentFragment;
    }

    private FavouritesController getFavConFragment(){
    //    FavouritesController favCon = (FavouritesController) getSupportFragmentManager().findFragmentById(R.id.content_frame);
        return (FavouritesController) getSupportFragmentManager().findFragmentById(R.id.content_frame);
    }

    private void DeleteDetailPage(int songId, String songName){
        TransactionFragment trans = getTranFragment();
      boolean deleted = trans.deleteSongFromList(songId);
      if(deleted) {
          trans.showToast(songName + " " + getResources().getString(R.string.delete_successful), 0);
      }else{
          trans.showToast(getResources().getString(R.string.failure), -1);
      }
    }


    @Override
    public void onStopAnimation(){
      Fragment transFrag = getLastFragment();
      if(transFrag instanceof FavouritesController){
        FavouritesController favCon = getFavConFragment();
        favCon.stopAnimation();
      }
    }

    @Override
    public void onLoadAdapter(List<Favourites> favourites, boolean retain, int songId, String songName){
        if(!(retain)){
            DeleteDetailPage(songId, songName);
        }
        TransactionFragment trans = getTranFragment();
        favourites = trans.manageFavorites();
        if(!favourites.isEmpty() && favourites.size()  > 0) {
            FavouritesController favCon = getFavConFragment();
            favCon.setPager(favourites);
        }else{
            trans.showToast(getResources().getString(R.string.no_favourites), -1);
        }
    }

    @Override
    public void onSaveFavourites(int mSongId, String mLargeImageUrl, String mThumbUrl,
                                 String mSongName, byte[] mByteData, String mArtistName, int mPosition){
        TransactionFragment trans = getTranFragment();
        if(trans.countFavorites() > 0) {
            final int dbId = trans.getFavouritesId(mSongId);
            if(dbId == mSongId) {
                trans.showToast(getResources().getString(R.string.duplicate_entry), -1);
            }else {
              long rTnId = trans.saveFavouritesToDb(mSongId, mLargeImageUrl, mThumbUrl, mSongName,
                                                               mByteData, mArtistName, mPosition);
              informUser(trans, rTnId, mSongName);
            }
        }else{
            long rTnId = trans.saveFavouritesToDb(mSongId, mLargeImageUrl,  mThumbUrl, mSongName,
                                                              mByteData, mArtistName, mPosition);
            informUser(trans, rTnId, mSongName);
        }
    }

    void informUser(TransactionFragment trans, long result, String songName){
      if(result == -1) {
        trans.showToast(getResources().getString(R.string.failure), -1);
      } else {
         trans.showToast(songName + " " + getResources().getString(R.string.successful), 0);
      }
    }

}