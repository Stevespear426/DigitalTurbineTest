package com.digital.turbine.test;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.AsyncTaskLoader;
import android.text.TextUtils;
import android.util.Log;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by stevesp on 10/31/16.
 */

public class ProductLoader extends AsyncTaskLoader<List<Product>> {

    private static final String PRODUCT_URL = "http://ads.appia.com/getAds?id=236&password=OVUJ1DJN&siteId=4288&deviceId=4230&sessionId=techtestsession&totalCampaignsRequested=10&lname=spear";

    public ProductLoader(@NonNull Context context) {
        super(context);
    }

    @Override
    public List<Product> loadInBackground() {
        List<Product> products = new ArrayList<>();
        try {
            URL url = new URL(PRODUCT_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();
            InputStream in = conn.getInputStream();
            products.addAll(parse(in));
        } catch (IOException | XmlPullParserException e) {
            Log.e("HttpGetLoader", "IOException in loadInBackground", e);
        }

        return products;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    public List<Product> parse(@NonNull InputStream in) throws XmlPullParserException, IOException {
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            parser.nextTag();
            return readFeed(parser);
        } finally {
            in.close();
        }
    }

    private List<Product> readFeed(@NonNull XmlPullParser parser) throws XmlPullParserException, IOException {
        List<Product> products = new ArrayList();

        parser.require(XmlPullParser.START_TAG, null, "ads");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            // Starts by looking for the entry tag
            if (name.equals("ad")) {
                products.add(readProduct(parser));
            } else {
                skip(parser);
            }
        }
        return products;
    }

    private Product readProduct(@NonNull XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, null, "ad");
        Product product = new Product();
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (TextUtils.equals(name, "productName")) {
                parser.require(XmlPullParser.START_TAG, null, "productName");
                product.mName = readText(parser);
                parser.require(XmlPullParser.END_TAG, null, "productName");

            } else if (TextUtils.equals(name, "appId")) {
                parser.require(XmlPullParser.START_TAG, null, "appId");
                product.mAppId = readText(parser);
                parser.require(XmlPullParser.END_TAG, null, "appId");

            } else if (TextUtils.equals(name, "productDescription")) {
                parser.require(XmlPullParser.START_TAG, null, "productDescription");
                product.mDescription = readText(parser);
                parser.require(XmlPullParser.END_TAG, null, "productDescription");

            } else if (TextUtils.equals(name, "productThumbnail")) {
                parser.require(XmlPullParser.START_TAG, null, "productThumbnail");
                product.mThumbnailUrl = readText(parser);
                parser.require(XmlPullParser.END_TAG, null, "productThumbnail");

            } else if (TextUtils.equals(name, "rating")) {
                parser.require(XmlPullParser.START_TAG, null, "rating");
                product.mRating = readText(parser);
                parser.require(XmlPullParser.END_TAG, null, "rating");

            } else if (TextUtils.equals(name, "averageRatingImageURL")) {
                parser.require(XmlPullParser.START_TAG, null, "averageRatingImageURL");
                product.mRatingPic = readText(parser);
                parser.require(XmlPullParser.END_TAG, null, "averageRatingImageURL");

            } else if (TextUtils.equals(name, "numberOfRatings")) {
                parser.require(XmlPullParser.START_TAG, null, "numberOfRatings");
                product.mNumberOfRatings = readText(parser);
                parser.require(XmlPullParser.END_TAG, null, "numberOfRatings");

            } else if (TextUtils.equals(name, "bidRate")) {
                parser.require(XmlPullParser.START_TAG, null, "bidRate");
                product.mBidRate = readText(parser);
                parser.require(XmlPullParser.END_TAG, null, "bidRate");

            } else if (TextUtils.equals(name, "callToAction")) {
                parser.require(XmlPullParser.START_TAG, null, "callToAction");
                product.mAction = readText(parser);
                parser.require(XmlPullParser.END_TAG, null, "callToAction");

            } else if (TextUtils.equals(name, "campaignDisplayOrder")) {
                parser.require(XmlPullParser.START_TAG, null, "campaignDisplayOrder");
                product.mCampaignDisplayOrder = readText(parser);
                parser.require(XmlPullParser.END_TAG, null, "campaignDisplayOrder");

            } else if (TextUtils.equals(name, "campaignId")) {
                parser.require(XmlPullParser.START_TAG, null, "campaignId");
                product.mCampaignId = readText(parser);
                parser.require(XmlPullParser.END_TAG, null, "campaignId");

            } else if (TextUtils.equals(name, "campaignTypeId")) {
                parser.require(XmlPullParser.START_TAG, null, "campaignTypeId");
                product.mCampaignTypeId = readText(parser);
                parser.require(XmlPullParser.END_TAG, null, "campaignTypeId");

            } else if (TextUtils.equals(name, "categoryName")) {
                parser.require(XmlPullParser.START_TAG, null, "categoryName");
                product.mCategory = readText(parser);
                parser.require(XmlPullParser.END_TAG, null, "categoryName");

            } else if (TextUtils.equals(name, "clickProxyURL")) {
                parser.require(XmlPullParser.START_TAG, null, "clickProxyURL");
                product.mClickProxyUrl = readText(parser);
                parser.require(XmlPullParser.END_TAG, null, "clickProxyURL");

            } else if (TextUtils.equals(name, "creativeId")) {
                parser.require(XmlPullParser.START_TAG, null, "creativeId");
                product.mCreativeId = readText(parser);
                parser.require(XmlPullParser.END_TAG, null, "creativeId");

            } else if (TextUtils.equals(name, "homeScreen")) {
                parser.require(XmlPullParser.START_TAG, null, "homeScreen");
                product.mHomeScreen = Boolean.parseBoolean(readText(parser));
                parser.require(XmlPullParser.END_TAG, null, "homeScreen");

            } else if (TextUtils.equals(name, "impressionTrackingURL")) {
                parser.require(XmlPullParser.START_TAG, null, "impressionTrackingURL");
                product.mImpressionTrackingUrl = readText(parser);
                parser.require(XmlPullParser.END_TAG, null, "impressionTrackingURL");

            } else if (TextUtils.equals(name, "isRandomPick")) {
                parser.require(XmlPullParser.START_TAG, null, "isRandomPick");
                product.mIsRandomPick = Boolean.parseBoolean(readText(parser));
                parser.require(XmlPullParser.END_TAG, null, "isRandomPick");

            } else if (TextUtils.equals(name, "minOSVersion")) {
                parser.require(XmlPullParser.START_TAG, null, "minOSVersion");
                product.mMinVersion = readText(parser);
                parser.require(XmlPullParser.END_TAG, null, "minOSVersion");

            } else if (TextUtils.equals(name, "productId")) {
                parser.require(XmlPullParser.START_TAG, null, "productId");
                product.mProductId = readText(parser);
                parser.require(XmlPullParser.END_TAG, null, "productId");
            } else {
                skip(parser);
            }
        }

        return product;
    }

    private String readText(@NonNull XmlPullParser parser) throws IOException, XmlPullParserException {
        String result = "";
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }
        return result;
    }

    private void skip(@NonNull XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }

}
