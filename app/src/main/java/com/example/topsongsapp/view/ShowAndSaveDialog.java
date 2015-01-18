package com.example.topsongsapp.view;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import com.example.topsongsapp.R;
import com.example.topsongsapp.topsongsinterface.TopSongInterface;

public class ShowAndSaveDialog extends DialogFragment implements TopSongInterface{
    private OnSaveFavouritesListener saveFavouritesListener;

    private static int mSongId;
    private static int mPosition;
    private static String mLargeImageUrl;
    private static String mThumbUrl;
    private static String mSongName;
    private static String mArtistName;
    private static byte[] mByteData;


    public static ShowAndSaveDialog newInstance(String mLargeImageUrl, String mThumbUrl, byte[] mBlob, String songName,
                                                int mSongId, String mArtist, int mPosition){
        ShowAndSaveDialog showAndSaveDialog = new ShowAndSaveDialog();
        Bundle bundle = new Bundle();
        bundle.putString(KEY_LARGE_IMAGE, mLargeImageUrl);
        bundle.putString(KEY_THUMB_IMAGE, mThumbUrl);
        bundle.putByteArray(KEY_BLOB, mBlob);
        bundle.putString(KEY_SONG_NAME, songName);
        bundle.putInt(KEY_ID, mSongId);
        bundle.putString(KEY_ARTIST, mArtist);
        bundle.putInt(KEY_POSITION, mPosition);
        showAndSaveDialog.setArguments(bundle);
        return showAndSaveDialog;

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        mSongId = getArguments().getInt(KEY_ID);
        mPosition = getArguments().getInt(KEY_POSITION);
        mLargeImageUrl = getArguments().getString(KEY_LARGE_IMAGE);
        mThumbUrl = getArguments().getString(KEY_THUMB_IMAGE);
        mSongName = getArguments().getString(KEY_SONG_NAME);
        mArtistName = getArguments().getString(KEY_ARTIST);
        mByteData = getArguments().getByteArray(KEY_BLOB);

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        final CharSequence[] choiceList = {
                getActivity().getResources().getString(R.string.download_image),
                getActivity().getResources().getString(R.string.save_favourites)};

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layoutR.layout.save_and_display, R.array.options
        builder.setTitle(R.string.title)
                .setSingleChoiceItems(choiceList, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                DialogFragment imageFragment = ImageDialogFragment.newInstance(mLargeImageUrl);
                                imageFragment.show(getFragmentManager(), "dialog");
                                break;
                            case 1:
                                saveFavouritesListener.onSaveFavourites(mSongId, mLargeImageUrl, mThumbUrl, mSongName, mByteData, mArtistName, mPosition);
                                break;
                            default:
                                break;
                        }
                    } // onClick
                }); // end runnable

        // Add action buttons
        builder.setPositiveButton(R.string.alert_dialog_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                switch (id){
                    case 1:

                        break;
                    case 2:

                        break;
                    default:
                        break;
                }
            }
        })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        ShowAndSaveDialog.this.getDialog().cancel();
                    }
                });
        return builder.create();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            saveFavouritesListener = (OnSaveFavouritesListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnSaveFavouritesListener");
        }
    }

    public interface OnSaveFavouritesListener {
        public void onSaveFavourites(int mSongId, String mLargeImageUrl, String mThumbUrl, String mSongName,
                                     byte[] mByteData, String mArtistName, int mPosition);
    }
}
