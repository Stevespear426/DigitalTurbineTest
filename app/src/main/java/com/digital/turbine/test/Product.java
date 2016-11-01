package com.digital.turbine.test;


import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by stevesp on 10/31/16.
 */

public class Product implements Parcelable {
    public String mAppId;
    public String mName;
    public String mDescription;
    public String mThumbnailUrl;
    public String mRating;
    public String mRatingPic;
    public String mNumberOfRatings;
    public String mCategory;
    public String mMinVersion;
    public String mProductId;
    public String mBidRate;

    public String mAction;
    public String mCampaignDisplayOrder;
    public String mCampaignId;
    public String mCampaignTypeId;
    public String mClickProxyUrl;
    public String mImpressionTrackingUrl;
    public String mCreativeId;
    public boolean mHomeScreen;
    public boolean mIsRandomPick;

    public Product() {
    }

    protected Product(Parcel in) {
        mAppId = in.readString();
        mName = in.readString();
        mDescription = in.readString();
        mThumbnailUrl = in.readString();
        mRating = in.readString();
        mRatingPic = in.readString();
        mNumberOfRatings = in.readString();
        mCategory = in.readString();
        mMinVersion = in.readString();
        mProductId = in.readString();
        mBidRate = in.readString();
        mAction = in.readString();
        mCampaignDisplayOrder = in.readString();
        mCampaignId = in.readString();
        mCampaignTypeId = in.readString();
        mClickProxyUrl = in.readString();
        mImpressionTrackingUrl = in.readString();
        mCreativeId = in.readString();
        mHomeScreen = in.readByte() != 0;
        mIsRandomPick = in.readByte() != 0;
    }

    public static final Creator<Product> CREATOR = new Creator<Product>() {
        @Override
        public Product createFromParcel(Parcel in) {
            return new Product(in);
        }

        @Override
        public Product[] newArray(int size) {
            return new Product[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mAppId);
        parcel.writeString(mName);
        parcel.writeString(mDescription);
        parcel.writeString(mThumbnailUrl);
        parcel.writeString(mRating);
        parcel.writeString(mRatingPic);
        parcel.writeString(mNumberOfRatings);
        parcel.writeString(mCategory);
        parcel.writeString(mMinVersion);
        parcel.writeString(mProductId);
        parcel.writeString(mBidRate);
        parcel.writeString(mAction);
        parcel.writeString(mCampaignDisplayOrder);
        parcel.writeString(mCampaignId);
        parcel.writeString(mCampaignTypeId);
        parcel.writeString(mClickProxyUrl);
        parcel.writeString(mImpressionTrackingUrl);
        parcel.writeString(mCreativeId);
        parcel.writeByte((byte) (mHomeScreen ? 1 : 0));
        parcel.writeByte((byte) (mIsRandomPick ? 1 : 0));
    }
}
