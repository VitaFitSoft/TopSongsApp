package com.example.topsongsapp.handler;

/* Copyright (C) 2010  VITAFIT SOFTWARE LIMITED. *
 * Licensed under the Apache License,
 * Version 2.0 (the "License");
 * you may not use or copy this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.topsongsapp.topsongsinterface.TopSongInterface;
import com.example.topsongsapp.model.Favourites;
import java.util.ArrayList;
import java.util.List;

public class DataBaseHandler implements TopSongInterface {

    private static final String DATABASE_NAME = "top_songs_db";
    // Favourites table name
    private static final String FAVOURITES_TABLE = "favourites_table";
    // Database version constant
    private static final int DATABASE_VERSION = 1;
  //  private final Context context;
    private final DatabaseHelper DBHelper;
    private SQLiteDatabase db;

    public DataBaseHandler(Context ctx) {
        DBHelper = new DatabaseHelper(ctx);
    }

    private static class DatabaseHelper extends SQLiteOpenHelper {
      DatabaseHelper(Context context) {
      super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

      // Creating Tables
      @Override
      public void onCreate(SQLiteDatabase db) {
        String CREATE_SONGS_LIST_TABLE = "CREATE TABLE " + FAVOURITES_TABLE + "("
          + KEY_ID + " INTEGER PRIMARY KEY NOT NULL,"
          + KEY_POSITION + " INTEGER NOT NULL,"
          + KEY_SONG_NAME + " TEXT,"
          + KEY_ARTIST + " TEXT,"
          + KEY_LARGE_IMAGE + " TEXT,"
          + KEY_THUMB_IMAGE + " TEXT,"
          + KEY_BLOB + " BLOB )";
          db.execSQL(CREATE_SONGS_LIST_TABLE);
      }

      // Upgrading database
      @Override
      public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + FAVOURITES_TABLE);
         // Create tables again
        onCreate(db);
      }
    } // end inner class DatabaseHelper

    // ---opens the database---
    public void openWrite() throws SQLException {
        db = DBHelper.getWritableDatabase();
    }

    public void openRead() throws SQLException {
        db = DBHelper.getReadableDatabase();
    }

    // ---closes the database---
    public void close() {
        DBHelper.close();
    }

    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */


    // Adding new contact
    public long addFavourites(int mSongId, String mLargeImageUrl, String mThumbUrl,
                        String songName, byte [] mByteBlob, String artist, int mPosition) {

        ContentValues values = new ContentValues();
        values.put(KEY_ID, mSongId); // song id
        values.put(KEY_POSITION, mPosition); // song id
        values.put(KEY_SONG_NAME, songName); // song name
        values.put(KEY_ARTIST, artist); //artist
        values.put(KEY_LARGE_IMAGE, mLargeImageUrl); // large image
        values.put(KEY_THUMB_IMAGE, mThumbUrl); // large image
        values.put(KEY_BLOB, mByteBlob); // large imag
        return db.insert(FAVOURITES_TABLE, null, values);
    }

    //This method retrieves the song id to check duplicates.
    public Cursor getFavouritesId(int mSongId)
    {
       return db.query(true,FAVOURITES_TABLE, new String[] {
                KEY_ID
                },
                KEY_ID + "=" + "'" + mSongId + "'",
                null,
                null,
                null,
                null,
                null);
    }

    public List<Favourites> getAllSongs() {
        List<Favourites> favList = new ArrayList<>();
        Cursor cursor = db.query(FAVOURITES_TABLE,
                null, null, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Favourites favourites = cursorToFavourites(cursor);
            favList.add(favourites);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return favList;
    }

    private Favourites cursorToFavourites(Cursor cursor) {
      int mNameColumn;
      int mSongIdColumn;
      int mBlobColumn;
      int mLargeUrlColumn;
      int mThumbUrlColumn;
      int mArtistColumn;
      int mPositionColumn;
      Favourites favourites = new Favourites();

      if(cursor != null) {
        mNameColumn = cursor.getColumnIndexOrThrow(KEY_SONG_NAME);
        mSongIdColumn = cursor.getColumnIndexOrThrow(KEY_ID);
        mPositionColumn = cursor.getColumnIndexOrThrow(KEY_POSITION);
        mLargeUrlColumn = cursor.getColumnIndexOrThrow(KEY_LARGE_IMAGE);
        mThumbUrlColumn = cursor.getColumnIndexOrThrow(KEY_THUMB_IMAGE);
        mBlobColumn = cursor.getColumnIndexOrThrow(KEY_BLOB);
        mArtistColumn = cursor.getColumnIndexOrThrow(KEY_ARTIST);
        try {
              favourites.setSongId(cursor.getInt(mSongIdColumn));
              favourites.setPosition(cursor.getInt(mPositionColumn));
              favourites.setSongName(cursor.getString(mNameColumn));
              favourites.setBlob(cursor.getBlob(mBlobColumn));
              favourites.setLargeImageUrl(cursor.getString(mLargeUrlColumn));
              favourites.setThumbImageUrl(cursor.getString(mThumbUrlColumn));
              favourites.setArtist(cursor.getString(mArtistColumn));
          } catch (NullPointerException npe) {
              npe.printStackTrace();
          }
          return favourites;
      }
      return null;
    }

    public Cursor getFavouritesCount()
    {
        Cursor mRowCnt =  db.query(FAVOURITES_TABLE, new String[] {
          "COUNT(*)" },
          null,
          null,
          null,
          null,
          null,
          null);
        return mRowCnt;
    }

    //---deletes a particular song id Detail---
    public boolean deleteSongId(int  songId)
    {
       return db.delete(FAVOURITES_TABLE, KEY_ID +
          " = " + songId, null) > 0;
    }

}// end class DbAdapter
