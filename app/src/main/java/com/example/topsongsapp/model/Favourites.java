package com.example.topsongsapp.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Favourites implements Parcelable {

    private int mPosition;
	private int mSongId;
	private String mSongName;
	private String mLargeImageUrl;
    private String mThumbUrl;
	private String mArtist;
    private byte[] mByteBlob;

	public Favourites() {
		super();
	}

	private Favourites(Parcel in) {
      super();
      this.mPosition = in.readInt();
      this.mSongId = in.readInt();
      this.mLargeImageUrl = in.readString();
      this.mThumbUrl = in.readString();
      this.mByteBlob = new byte[in.readInt()];
      this.mSongName = in.readString();
	  this.mArtist = in.readString();
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeInt(getPosition());
		parcel.writeInt(getSongId());
		parcel.writeString(getSongName());
		parcel.writeString(getLargeImageUrl());
        parcel.writeString(getThumbImageUrl());
        parcel.writeString(getArtist());
        parcel.writeByteArray(getBlob());
	}

	public static final Creator<Favourites> CREATOR = new Creator<Favourites>() {
		public Favourites createFromParcel(Parcel in) {
			return new Favourites(in);
		}

		public Favourites[] newArray(int size) {
			return new Favourites[size];
		}
	};

    int getPosition() {
        return mPosition;
    }

	public int getSongId() {
		return mSongId;
	}

	public String getSongName() {
		return mSongName;
	}

	public String getLargeImageUrl() {
		return mLargeImageUrl;
	}

    String getThumbImageUrl() {
        return mThumbUrl;
    }
	
	public String getArtist() {
        return mArtist;
    }

    byte[] getBlob() {
        return mByteBlob;
    }

    public void setPosition(int position) {
        this.mPosition = position;
    }

    public void setSongId(int songId) {
        this.mSongId = songId;
    }

    public void setSongName(String name) {
        this.mSongName = name;
    }

    public void setLargeImageUrl(String largeImageUrl) {
        this.mLargeImageUrl = largeImageUrl;
    }

    public void setThumbImageUrl(String thumbImageUrl) {
        this.mThumbUrl = thumbImageUrl;
    }

    public void setBlob(byte[] byteBlob){
        this.mByteBlob = byteBlob;
    }

    public void setArtist(String artist) {
        this.mArtist = artist;
    }



// --Commented out by Inspection START (23/12/14 15:19):
//	@SuppressWarnings("SameReturnValue")
//    public static Creator<Favourites> getCreator() {
//		return CREATOR;
//	}
// --Commented out by Inspection STOP (23/12/14 15:19)

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + mSongId;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (((Object) this).getClass() != obj.getClass())
			return false;
		Favourites other = (Favourites) obj;
        return mSongId == other.mSongId;
    }

	@Override
	public String toString() {
		return "Favourites [position=" + mPosition + ", id=" + mSongId + ", name=" + mSongName + ", LargeimageUrl="
				+ mLargeImageUrl  + ", ThumbimageUrl="   + mThumbUrl + ", artist=" + mArtist + "]";
	}
}
