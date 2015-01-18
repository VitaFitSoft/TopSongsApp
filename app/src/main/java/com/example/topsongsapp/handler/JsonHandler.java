package com.example.topsongsapp.handler;


import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONException;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.JsonReader;
import android.util.JsonToken;

import com.example.topsongsapp.model.Songs;

public class JsonHandler implements Parcelable{

    private List<Songs> songObject;
    private final String mJsonResponse;

    /**
     * Reads the sample JSON data and loads it into a table.
     *  @param response returned internet response data
     */
    public JsonHandler(String response) {
        this.mJsonResponse = response;
    }

    //  public JsonHandler(Parcel in) {
    //     readFromParcel(in);
    //   }

    public static final Creator<Songs> CREATOR = new Creator<Songs>() {
        public Songs createFromParcel(Parcel source) {
            return new Songs(source);
        }

        @Override
        public Songs[] newArray(int size) {
            return new Songs[size];
        }
    };


    public List<Songs> loadJsonData()
            throws JSONException, NullPointerException{
        try{
            InputStream stream = new ByteArrayInputStream(mJsonResponse.getBytes("UTF-8"));
            songObject =  new ArrayList<>();
            InputStreamReader inputStreamReader;
            BufferedReader bufferedReader;
            JsonReader reader;
            inputStreamReader  = new InputStreamReader(stream, "UTF-8");
            bufferedReader = new BufferedReader(inputStreamReader);
            reader = new JsonReader(bufferedReader);
            songObject = populateFeed(reader);
            reader.close();
        } catch (IOException e) {
            // Do nothing
        }
        return  songObject;
    }


    /**
     * Populates Songs data with the JSON data.
     *
     * @param reader
     *             the {@link android.util.JsonReader} used to read in the data
     * @return listResults the SongsResults
     * @throws java.io.IOException
     */
    private List<Songs> populateFeed(JsonReader reader) throws JSONException, IOException {
        List<Songs> entryList = new ArrayList<>();
        reader.beginObject();
        while( reader.hasNext() ) {
          final String name = reader.nextName();
          final boolean isNull = reader.peek() == JsonToken.NULL;
          if(name.equals("feed") && !isNull) {
            reader.beginObject();
            while (reader.hasNext()) {
              final String eName = reader.nextName();
              if(eName.equals("entry")) {
                entryList = parseJsonArray(reader);
              } else {
                 reader.skipValue();
              }
            }
            reader.endObject();
          }
        }
        reader.endObject();
        return entryList;
    }


    /**
     * Parses an array in the JSON data stream.
     *
     * @param reader
     *            the {@link android.util.JsonReader} used to read in the data
     * @return
     *           the SongsSalesPopulated
     * @throws java.io.IOException
     */

    List<Songs> parseJsonArray(JsonReader reader) throws JSONException, IOException {
        Songs entryData;
        List<Songs> entryListPopulated = new ArrayList<>();
        reader.beginArray();
        reader.peek();
        while(reader.hasNext()) {
          entryData = parseArrayData(reader);
          if((entryData.getSongName() != null  && entryData.getSongName().length() > 0)) {
            entryListPopulated.add(entryData);
          }
          reader.peek();
        }
        reader.endArray();
        return entryListPopulated;
    }

    /**
     * Loads the next observation into the Songs class.
     *
     * @param reader
     *            the {@link android.util.JsonReader} containing the observation
     * @throws java.io.IOException
     */
    private Songs parseArrayData(JsonReader reader) throws JSONException, IOException {
      Songs songs = new Songs();
      List<Songs> imageArray;
      reader.beginObject();
      while(reader.hasNext()){
        final String name = reader.nextName();
        if(name.equals("im:name") && reader.peek() != JsonToken.NULL){
          reader.beginObject();
          while(reader.hasNext()){
            final String labName = reader.nextName();
            if(labName.equals("label") && reader.peek() != JsonToken.NULL) {
              songs.setSongName(reader.nextString());
            }else{
              reader.skipValue();
            }// end if label
          }// end while
          reader.endObject();
        }else if(name.equals("im:image") && reader.peek() != JsonToken.NULL){
           imageArray = parseImageArray(reader);
           songs.setImageArray(imageArray);
        }else if(name.equals("id") && reader.peek() != JsonToken.NULL){
           reader.beginObject();
           while(reader.hasNext()){
             final String attName = reader.nextName();
             if(attName.equals("attributes") && reader.peek() != JsonToken.NULL){
               reader.beginObject();
               while(reader.hasNext()){
                 final String idName = reader.nextName();
                 if(idName.equals("im:id") && reader.peek() != JsonToken.NULL){
                   songs.setSongId(Integer.parseInt(reader.nextString()));
             }else{
                 reader.skipValue();
             }// end if hasnext
           }// end while
               reader.endObject();
             }else{
                reader.skipValue();
             }// end if attributes
           }// end while
           reader.endObject();
        }else if(name.equals("im:artist") && reader.peek() != JsonToken.NULL){
           reader.beginObject();
           while(reader.hasNext()){
             final String artistName = reader.nextName();
             if(artistName.equals("label") && reader.peek() != JsonToken.NULL){
               songs.setArtist(reader.nextString());
             }else{
                reader.skipValue();
             }// end if hasnext
           }// end while
           reader.endObject();
        }else{
           reader.skipValue();
        }// end if hasnext
      }// end while
      reader.endObject();
       return songs;
    }

    List<Songs> parseImageArray(JsonReader reader) throws JSONException, IOException {
      List<Songs> imageArrayPopulated = new ArrayList<>();
      reader.beginArray();
      reader.peek();
      while(reader.hasNext()) {
        imageArrayPopulated.add(parseImageData(reader));
      }// end while
      reader.endArray();
      return imageArrayPopulated;
    }

    private Songs parseImageData(JsonReader reader) throws JSONException, IOException {
      Songs image  = new Songs();
      String urlString;

      reader.beginObject();
      while(reader.hasNext()){
        String name = reader.nextName();
        if(name.equals("label") && reader.peek() != JsonToken.NULL) {
          urlString = reader.nextString();
          String nextName = reader.nextName();
          if(nextName.equals("attributes") && reader.peek() != JsonToken.NULL) {
            reader.beginObject();
            while(reader.hasNext()) {
              String heightName = reader.nextName();
              if(heightName.equals("height") && reader.peek() != JsonToken.NULL) {
                int imageHeight = Integer.parseInt(reader.nextString());
                switch (imageHeight) {
                  case 55:
                    image.setThumbnailImageUrl(urlString);
                    break;
                  case 60:
                    image.setMediumImageUrl(urlString);
                    break;
                  case 170:
                    image.setLargeImageUrl(urlString);
                    break;
                  default:
                    break;
                }// end switch
              }else{
                reader.skipValue();
              }// end if height
            }// end height while
            reader.endObject();
          }else{
            reader.skipValue();
          }// end if attributes
      //}// end while label
        }else {
          reader.skipValue();
        }// end if label
      }// end while
      reader.endObject();
      return image;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(songObject);

    }
}