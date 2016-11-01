package com.digital.turbine.test;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.customtabs.CustomTabsIntent;
import android.support.v4.content.res.ResourcesCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by stevesp on 10/31/16.
 */

public class ProductDetailFragment extends TestFragment {

    private static final String TAG = "ProductDetailFragment";
    private static final String EXTRA_PRODUCT = "product";
    private static final String EXTRA_SHOWING_MORE = "showing_more";
    private Product mProduct;
    private Button mShowMore;
    private boolean mShowingMore = false;

    private static final int[] hiddenIds = new int[] {
            R.id.app_id,
            R.id.app_id_label,
            R.id.bidrate,
            R.id.bidrate_label,
            R.id.campaign_id,
            R.id.campaign_id_label,
            R.id.campaign_type_id,
            R.id.campaign_type_id_label,
            R.id.campaign_display_order,
            R.id.campaign_display_order_label,
            R.id.creative_id,
            R.id.creative_id_label,
            R.id.homescreen,
            R.id.homescreen_label,
            R.id.random_pick,
            R.id.random_pick_label,
            R.id.impressions_url
    };

    public static ProductDetailFragment getInstance(@NonNull Product product) {
        ProductDetailFragment fragment = new ProductDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(EXTRA_PRODUCT, product);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() == null || !getArguments().containsKey(EXTRA_PRODUCT))
            throw new IllegalArgumentException("Arguments must contain a Product!");

        mProduct = getArguments().getParcelable(EXTRA_PRODUCT);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View main = inflater.inflate(R.layout.product_detail_fragment, container, false);
        setText(main, R.id.name, mProduct.mName);
        setText(main, R.id.description, mProduct.mDescription);
        setText(main, R.id.rating, getString(R.string.stars, mProduct.mRating));
        setText(main, R.id.num_ratings, mProduct.mNumberOfRatings);
        setText(main, R.id.min_version, mProduct.mMinVersion);
        setText(main, R.id.category, mProduct.mCategory);
        setText(main, R.id.action, mProduct.mAction);
        setText(main, R.id.bidrate, mProduct.mBidRate);
        setText(main, R.id.app_id, mProduct.mAppId);
        setText(main, R.id.creative_id, mProduct.mCreativeId);
        setText(main, R.id.campaign_id, mProduct.mCampaignId);
        setText(main, R.id.campaign_type_id, mProduct.mCampaignTypeId);
        setText(main, R.id.campaign_display_order, mProduct.mCampaignDisplayOrder);
        setText(main, R.id.homescreen, String.valueOf(mProduct.mHomeScreen));
        setText(main, R.id.random_pick, String.valueOf(mProduct.mIsRandomPick));

        mShowMore = (Button) main.findViewById(R.id.show_more);
        mShowMore.setOnClickListener(v -> {
            mShowingMore = !mShowingMore;
            showMoreInfo(getView());
        });

        if (savedInstanceState != null) mShowingMore = savedInstanceState.getBoolean(EXTRA_SHOWING_MORE, false);

        showMoreInfo(main);

        main.findViewById(R.id.impressions_url).setOnClickListener(v -> {
            impressionsUrl();
        });

        main.findViewById(R.id.action).setOnClickListener(v -> {
            proxyUrl();
        });

        ImageView thumbnail = (ImageView) main.findViewById(R.id.thumbnail);
        if (IconLoadTask.cancelPotentialWork(mProduct.mThumbnailUrl, thumbnail)) {
            final IconLoadTask task = new IconLoadTask(thumbnail,
                    mProduct.mThumbnailUrl);
            final IconLoadTask.AsyncDrawable asyncDrawable =
                    new IconLoadTask.AsyncDrawable(task);
            thumbnail.setImageDrawable(asyncDrawable);
            task.execute();
        }

        ImageView ratingImage = (ImageView) main.findViewById(R.id.rating_image);
        if (IconLoadTask.cancelPotentialWork(mProduct.mRatingPic, ratingImage)) {
            final IconLoadTask task = new IconLoadTask(ratingImage,
                    mProduct.mRatingPic);
            final IconLoadTask.AsyncDrawable asyncDrawable =
                    new IconLoadTask.AsyncDrawable(task);
            ratingImage.setImageDrawable(asyncDrawable);
            task.execute();
        }
        return main;
    }

    private void setText(View main, int id, String text) {
        ((TextView) main.findViewById(id)).setText(text);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(EXTRA_PRODUCT, mProduct);
        outState.putBoolean(EXTRA_SHOWING_MORE, mShowingMore);
        super.onSaveInstanceState(outState);
    }

    @Override
    boolean hasUpAsBack() {
        return true;
    }

    @Override
    String getTagName() {
        return TAG;
    }

    @Override
    String getTitle() {
        return mProduct.mName;
    }

    public void showMoreInfo(@NonNull View v) {
        mShowMore.setText(mShowingMore ? R.string.show_less_info : R.string.show_more_info);
        for (int id : hiddenIds) {
            v.findViewById(id).setVisibility(mShowingMore ? View.VISIBLE : View.GONE);
        }
    }

    public void impressionsUrl() {
        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        builder.setToolbarColor(ResourcesCompat.getColor(getResources(), R.color.colorPrimary, getActivity().getTheme()));
        CustomTabsIntent customTabsIntent = builder.build();
        customTabsIntent.launchUrl(getActivity(), Uri.parse(mProduct.mImpressionTrackingUrl));
    }

    public void proxyUrl() {
        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        builder.setToolbarColor(ResourcesCompat.getColor(getResources(), R.color.colorPrimary, getActivity().getTheme()));
        CustomTabsIntent customTabsIntent = builder.build();
        customTabsIntent.launchUrl(getActivity(), Uri.parse(mProduct.mClickProxyUrl));
    }

}
