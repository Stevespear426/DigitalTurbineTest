package com.digital.turbine.test;

import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertTrue;

/**
 */
@RunWith(AndroidJUnit4.class)
public class LoaderTest {

    @Test
    public void loadData() throws Exception {
        List<Product> data = new ArrayList<>();
        CountDownLatch latch;
        assertTrue(data.isEmpty());
        latch = new CountDownLatch(1);
        ProductLoader loader = new ProductLoader(InstrumentationRegistry.getTargetContext());
        loader.registerListener(1235, (l, d) -> {
            data.addAll(d);
            latch.countDown();
        });
        loader.startLoading();
        latch.await(10, TimeUnit.SECONDS);
        assertTrue(!data.isEmpty());
    }
}
