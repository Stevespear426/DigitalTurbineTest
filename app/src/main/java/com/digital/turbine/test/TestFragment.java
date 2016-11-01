package com.digital.turbine.test;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;

/**
 * Created by stevesp on 10/31/16.
 */

public abstract class TestFragment extends Fragment {

    abstract boolean hasUpAsBack();
    abstract String getTagName();
    abstract String getTitle();

    protected ActivityCallbacks mCallbacks;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ActivityCallbacks) mCallbacks = (ActivityCallbacks) context;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof ActivityCallbacks) mCallbacks = (ActivityCallbacks) activity;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mCallbacks != null) mCallbacks.setTitle(getTitle(), hasUpAsBack());
    }
}
