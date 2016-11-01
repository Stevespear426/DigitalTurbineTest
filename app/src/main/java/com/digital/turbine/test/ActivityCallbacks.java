package com.digital.turbine.test;

/**
 * Created by stevesp on 10/31/16.
 */

public interface ActivityCallbacks {
    void setFragment(TestFragment fragment, boolean addToBackStack);
    void setTitle(String title, boolean upAsBack);
}
