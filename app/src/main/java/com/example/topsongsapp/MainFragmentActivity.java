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

import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.FragmentTransaction;
import android.view.Window;
import com.example.topsongsapp.handler.SongsFragmentTabHost;
import com.example.topsongsapp.topsongsinterface.TabConstants;
import com.example.topsongsapp.view.TopSongsList;


public class MainFragmentActivity extends TransactionActivity implements TabConstants{
    private SongsFragmentTabHost mTabHost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_main_fragment);

        if(android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        // Check to see if we have retained the worker fragment.
        TransactionFragment mTransactionFragment =
                (TransactionFragment)getSupportFragmentManager().findFragmentByTag("trans");

        // If not retained (or first time running), we need to create it.
        if(mTransactionFragment == null) {
            mTransactionFragment = new TransactionFragment();
            // Tell it who it is working with.
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(mTransactionFragment, "trans").commit();
        }

        mTabHost = (SongsFragmentTabHost) findViewById(android.R.id.tabhost);
        mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);
        mTabHost.addTab(mTabHost.newTabSpec(TAB_A).setIndicator(TAB_A,getResources().getDrawable(R.drawable.ic_action_locate)),
                TopSongsList.class);
        mTabHost.addTab(mTabHost.newTabSpec(TAB_B).setIndicator(TAB_B, getResources().getDrawable(R.drawable.ic_action_star)),
                FavouritesFragment.class);

        if (savedInstanceState != null) {
            mTabHost.setCurrentTabByTag(savedInstanceState.getString("tab"));
        }

    }// end create

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {

        super.onResume();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
      super.onSaveInstanceState(outState);
      outState.putString("tab", mTabHost.getCurrentTabTag());
    }

}