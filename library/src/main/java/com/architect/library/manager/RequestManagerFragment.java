package com.architect.library.manager;

import android.annotation.SuppressLint;
import android.app.Fragment;


public class RequestManagerFragment extends Fragment {

    private final ActivityFragmentLifecycle lifecycle;


    public RequestManagerFragment() {
        this(new ActivityFragmentLifecycle());
    }

    @SuppressLint("ValidFragment")
    public RequestManagerFragment(ActivityFragmentLifecycle lifecycle) {
        this.lifecycle = lifecycle;
    }


    public ActivityFragmentLifecycle getGlideLifecycle() {
        return lifecycle;
    }

}
