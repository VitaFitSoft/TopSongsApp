package com.example.topsongsapp.io;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import com.example.topsongsapp.R;
import com.example.topsongsapp.controller.AppController;
import com.example.topsongsapp.handler.JsonHandler;
import com.example.topsongsapp.model.Songs;
import com.example.topsongsapp.topsongsinterface.ShowMessage;
import com.example.topsongsapp.topsongsinterface.TopSongInterface;
import com.example.topsongsapp.utils.Utils;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.List;

@SuppressWarnings("ALL")
public class LoadListItems extends ListFragment implements TopSongInterface, ShowMessage{

    private static List<Songs> songList;
    public static final String PREFS_NAME = "LoadListItems";
    public LoadListItems() {

    }

    public void connectAndDownLoad() {
        boolean isConnected = false;
        isConnected = isConnectionAvailable(getActivity());
        if (isConnected) {
            downLoadList();
        } else {
            showToast(getActivity().getResources().getString(R.string.no_data), -1);
        }
    }

    public void downLoadList() {
        final String uri_key = getActivity().getResources().getString(R.string.key);
        getEntryList(uri_key);
    }

    public boolean isConnectionAvailable(Context context) {
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivityManager != null) {
                NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();
                if (netInfo != null && netInfo.isConnected() && netInfo.isConnectedOrConnecting() && netInfo.isAvailable()) {
                    return true;
                }
            }
        } catch (final IllegalArgumentException iae) {
            Log.e(PREFS_NAME, iae.getMessage());
        }
        return false;
    }

    public void getEntryList(final String uri_key) {
      final  Utils utils = new Utils();

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(uri_key, null, new Response.Listener<JSONObject>() {
        @Override
        public void onResponse(JSONObject response) {
          String sResponse = response.toString();
          try {
                    if (utils.isEmpty(sResponse)) {
                      showToast(getResources().getString(R.string.no_data), -1);
                    } else {
                      final JsonHandler jsonHandler = new JsonHandler(sResponse);
                      songList = jsonHandler.loadJsonData();
                        if (songList.isEmpty()) {
                        showToast(getActivity().getResources().getString(R.string.no_data), -1);
                      }else{
                        setSongList(songList);
                      }
                    }
                  }catch(NullPointerException e) {
                     e.printStackTrace();
                  }catch(JSONException e) {
                     e.printStackTrace();
                  }
            }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                          VolleyLog.d(PREFS_NAME, "Error: " + error.getMessage());
                      }
                  }); 

                  // Adding request to request queue
                  AppController.getInstance().addToRequestQueue(jsonObjReq);

    }// end method getTopChartList

    public void setSongList(List<Songs> mSongList){
        songList = mSongList;
    }

    public List<Songs> getSongList(){
        return songList;
    }

    // customised toast.
    public void showToast(CharSequence message, int rtnId) {
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService
                (Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.toast1_layout,
                (ViewGroup) getActivity().findViewById(R.id.toast_layout_root));

        ImageView image = (ImageView) layout.findViewById(R.id.toast_image);
        if(rtnId < 0) {
            image.setImageResource(R.drawable.warning_image);
        }else{
            image.setImageResource(R.drawable.tick_image);
        }
        TextView text = (TextView) layout.findViewById(R.id.toast_text);
        text.setTextSize(20.0f);
        text.setText(message);
        Toast toast = new Toast(getActivity().getApplicationContext());
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();
    }// end showToast method
}