package com.digital.turbine.test;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;

import java.util.concurrent.TimeUnit;

import static android.support.test.espresso.Espresso.pressBack;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class MainActivityTest {


    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(
            MainActivity.class);


    @Test
    public void testBackButton() {
        // Type text and then press the button.
        Fragment frag = mActivityRule.getActivity().getSupportFragmentManager().findFragmentById(R.id.main);
        Assert.assertTrue(frag instanceof ProductListFragment);

        final RecyclerView recyclerView = (RecyclerView) frag.getView().findViewById(R.id.recycler_view);
        Assert.assertNotNull(recyclerView);

        mActivityRule.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                recyclerView.getChildAt(0).performClick();
            }
        });

        long finishTime = System.currentTimeMillis() + TimeUnit.MILLISECONDS.convert(10, TimeUnit.SECONDS);

        do {
            frag = mActivityRule.getActivity().getSupportFragmentManager().findFragmentById(R.id.main);
        } while (!(frag instanceof ProductDetailFragment) && finishTime > System.currentTimeMillis());

        Assert.assertTrue(frag instanceof ProductDetailFragment);

        pressBack();

        finishTime = System.currentTimeMillis() + TimeUnit.MILLISECONDS.convert(10, TimeUnit.SECONDS);

        do {
            frag = mActivityRule.getActivity().getSupportFragmentManager().findFragmentById(R.id.main);
        } while (!(frag instanceof ProductListFragment) && finishTime > System.currentTimeMillis());

        Assert.assertTrue(frag instanceof ProductListFragment);
    }
}
