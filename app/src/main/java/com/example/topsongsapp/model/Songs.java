package com.example.topsongsapp.model;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.List;

public class Songs implements Parcelable {

    public Songs(){
    }
	
    private String mSongName;
	private String mArtist;
    private String mThumbnailImageUrl;
    private String mLargeImageUrl;
    private String mMediumImageUrl;
	private int mSongId;
    private List<Songs> mImageArray;

    /**
     * Retrieving ChartEntry data from Parcel object
     * This constructor is invoked by the method createFromParcel(Parcel source) of
     * the object CREATOR
     **/
    public Songs(Parcel source){
        readFromParcel(source);
    }

    void readFromParcel(Parcel source){

        mSongName = source.readString();
        mThumbnailImageUrl = source.readString();
        mLargeImageUrl = source.readString();
        mMediumImageUrl = source.readString();
        //noinspection unchecked
        mImageArray = source.readArrayList(Songs.class.getClassLoader());
		mSongId = source.readInt();
		mArtist = source.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mSongName);
        dest.writeString(mThumbnailImageUrl);
        dest.writeString(mMediumImageUrl);
        dest.writeList(mImageArray);
        dest.writeString(mLargeImageUrl);
		dest.writeInt(mSongId);
		dest.writeString(mArtist);
    }

    public static final Creator<Songs> CREATOR = new Creator<Songs>() {
      public Songs createFromParcel(Parcel source) {
            return new Songs(source);
      }

      @Override
      public Songs[] newArray(int size) {
        return new Songs[size];
      }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    public void setSongName(String song_name){
      this.mSongName = song_name;
    }
	
	public void setSongId(int song_id){
        this.mSongId = song_id;
    }
	
	public void setArtist(String artist){
      this.mArtist = artist;
    }

    public void setThumbnailImageUrl(String thumbnail_image_url){
      this.mThumbnailImageUrl = thumbnail_image_url;
    }

    public void setMediumImageUrl(String medium_image_url){
        this.mMediumImageUrl = medium_image_url;
    }

    public void setLargeImageUrl(String large_image_url){
      this.mLargeImageUrl = large_image_url;
    }

    public void setImageArray(List<Songs> imageArray){
      this.mImageArray = imageArray;
    }

    public String getSongName(){
        return this.mSongName;
    }
	
	public int getSongId(){
      return this.mSongId;
    }

	public String getArtist(){
      return this.mArtist;
    }

     public String getThumbnailImageUrl(){
         return this.mThumbnailImageUrl;
    }


    public String getLargeImageUrl(){
      return this.mLargeImageUrl;
    }

    public String getMediumImageUrl(){
        return this.mMediumImageUrl;
    }

    public List<Songs> getmImageArray(){
      return this.mImageArray;
    }

}