package com.example.topsongsapp.view;

/*
 * <!-- Copyright (C) 2010 The Android Open Source Project

     Licensed under the Apache License, Version 2.0 (the "License");
     you may not use this file except in compliance with the License.
     You may obtain a copy of the License at

          http://www.apache.org/licenses/LICENSE-2.0

     Unless required by applicable law or agreed to in writing, software
     distributed under the License is distributed on an "AS IS" BASIS,
     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
     See the License for the specific language governing permissions and
     limitations under the License.
-->
 */

//import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.topsongsapp.R;
import com.example.topsongsapp.topsongsinterface.TopSongInterface;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.example.topsongsapp.controller.AppController;
import com.example.topsongsapp.utils.Utils;

public class ImageDialogFragment extends DialogFragment implements TopSongInterface {

    private static NetworkImageView imageView;
    private boolean isDismissible = false;
    private ImageLoader imageLoader;

    public static ImageDialogFragment newInstance(String imageUrl){

        ImageDialogFragment imageFragment = new ImageDialogFragment();

        Bundle bundle = new Bundle();
        bundle.putString(ARG_IMAGEURL, imageUrl);
        imageFragment.setArguments(bundle);
        return imageFragment;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View imageDialogView = inflater.inflate(R.layout.full_image_layout, container, false);

        imageView = (NetworkImageView)  imageDialogView.findViewById(R.id.image_view);
        final Button closeDialogButton = (Button) imageDialogView.findViewById
                (R.id.close_full_image_dialog_button);
        closeDialogButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
              dismiss();
            }
        });
        return imageDialogView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final String noImage = getString(R.string.no_image);
        final String imageMessage = getString(R.string.no_image_found);
        final String sImageUrl = getArguments().getString(ARG_IMAGEURL);

        if(Utils.isNotMissing(sImageUrl)) {
          if (imageLoader == null) {
             imageLoader = AppController.getInstance().getImageLoader();
             if (sImageUrl != null) {
               imageView.setImageUrl(sImageUrl, imageLoader);
             }
            }
        }else{
          noImageDialog(noImage, imageMessage);
          dismiss();
        }

    }// end onActivityCreated

    @Override
    public void dismiss() {

        try {
            isDismissible = true;
            super.dismiss();

       //     Log.d(getClass().getSimpleName(), "Dialog dismissed!");

        } catch (IllegalStateException ilse) {
            ilse.printStackTrace();
        }
    }

    @Override
    public void onDismiss(DialogInterface dialog) {

        // So that dialog should not dismiss on orientation change
        if (isDismissible) {
            // So that dialog can be dismissed
            super.onDismiss(dialog);
        }
    }

    void noImageDialog(String noImage, String imageMessage) {
        DialogFragment imageAlertDialog = ImageAlertDialog.newInstance(noImage, imageMessage);
        imageAlertDialog.show(getFragmentManager(), "dialog");
    }

}// end ImageDialogFragment class