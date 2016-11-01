package com.digital.turbine.test;


import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by stevesp on 10/31/16.
 */

public class ProductListFragment extends TestFragment implements LoaderManager.LoaderCallbacks<List<Product>>, View.OnClickListener {
    private static final String TAG = "ProductListFragment";
    private static final String EXTRA_PRODUCTS = "products";
    private static final int LOADER_ID = 1234;
    private TextView mEmpty;
    private ProgressBar mSpinner;
    private SwipeRefreshLayout mSwipeRefresh;
    private final ProductAdapter mAdapter = new ProductAdapter();
    ArrayList<Product> mData = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View main = inflater.inflate(R.layout.product_list_fragment, container, false);
        mEmpty = (TextView) main.findViewById(R.id.empty);
        mSpinner = (ProgressBar) main.findViewById(R.id.progress);
        mSwipeRefresh = (SwipeRefreshLayout) main.findViewById(R.id.swipe_layout);
        mSwipeRefresh.setColorSchemeColors(Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW);
        mSwipeRefresh.setOnRefreshListener(() -> {
            getLoaderManager().restartLoader(LOADER_ID, null, this);
        });
        RecyclerView recyclerView = (RecyclerView) main.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(mAdapter);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                mSwipeRefresh.setEnabled(((LinearLayoutManager)recyclerView.getLayoutManager()).findFirstCompletelyVisibleItemPosition() == 0);
            }
        });
        if (savedInstanceState != null && savedInstanceState.containsKey(EXTRA_PRODUCTS)) {
            setData(savedInstanceState.getParcelableArrayList(EXTRA_PRODUCTS));
        }
        if (mData.isEmpty()) {
            getLoaderManager().restartLoader(LOADER_ID, null, this);
        } else {
            mSpinner.setVisibility(View.GONE);
            mEmpty.setVisibility(View.GONE);
            mSwipeRefresh.setVisibility(View.VISIBLE);
        }
        return main;
    }

    @Override
    public boolean hasUpAsBack() {
        return false;
    }

    @Override
    String getTagName() {
        return TAG;
    }

    @Override
    String getTitle() {
        return getString(R.string.products);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(EXTRA_PRODUCTS, mData);
        super.onSaveInstanceState(outState);
    }

    private void setData(List<Product> data) {
        mData.clear();
        mData.addAll(data);
        mAdapter.notifyDataSetChanged();
        mSpinner.setVisibility(View.GONE);
        if (data.isEmpty()) {
            mEmpty.setVisibility(View.VISIBLE);
            mSwipeRefresh.setVisibility(View.GONE);
        } else {
            mEmpty.setVisibility(View.GONE);
            mSwipeRefresh.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public Loader<List<Product>> onCreateLoader(int id, Bundle args) {
        return new ProductLoader(getActivity());
    }

    @Override
    public void onLoadFinished(Loader<List<Product>> loader, List<Product> data) {
        if (getActivity() == null) return;
        mSwipeRefresh.setRefreshing(false);
        getLoaderManager().destroyLoader(loader.getId());
        setData(data);
    }

    @Override
    public void onLoaderReset(Loader<List<Product>> loader) {
    }

    @Override
    public void onClick(View view) {
        Product product = (Product) view.getTag();
        if (mCallbacks != null) mCallbacks.setFragment(ProductDetailFragment.getInstance(product), true);
    }

    private class ProductAdapter extends RecyclerView.Adapter<ProductViewHolder> {

        @Override
        public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.product_list_item, parent, false);
            return new ProductViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ProductViewHolder holder, int position) {
            Product product = mData.get(position);
            holder.itemView.setTag(product);
            holder.mName.setText(product.mName);
            holder.mRating.setText(getString(R.string.stars, product.mRating));
            if (IconLoadTask.cancelPotentialWork(product.mThumbnailUrl, holder.mThumbnail)) {
                final IconLoadTask task = new IconLoadTask(holder.mThumbnail,
                        product.mThumbnailUrl);
                final IconLoadTask.AsyncDrawable asyncDrawable =
                        new IconLoadTask.AsyncDrawable(task);
                holder.mThumbnail.setImageDrawable(asyncDrawable);
                task.execute();
            }
        }

        @Override
        public int getItemCount() {
            return mData.size();
        }
    }

    private class ProductViewHolder extends RecyclerView.ViewHolder {
        ImageView mThumbnail;
        TextView mName;
        TextView mRating;

        public ProductViewHolder(View itemView) {
            super(itemView);
            mThumbnail = (ImageView) itemView.findViewById(R.id.thumbnail);
            mName = (TextView) itemView.findViewById(R.id.name);
            mRating = (TextView) itemView.findViewById(R.id.rating);
            itemView.setOnClickListener(ProductListFragment.this);
        }
    }
}
