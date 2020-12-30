package com.architect.library.manager;

import android.annotation.SuppressLint;

import androidx.fragment.app.Fragment;

public class SupportRequestManagerFragment extends Fragment {

    private final ActivityFragmentLifecycle lifecycle;

    public SupportRequestManagerFragment() {
        this(new ActivityFragmentLifecycle());
    }

    @SuppressLint("ValidFragment")
    public SupportRequestManagerFragment(ActivityFragmentLifecycle lifecycle) {
        this.lifecycle = lifecycle;
    }

    public ActivityFragmentLifecycle getGlideLifecycle() {
        return lifecycle;
    }

    @Override
    public void onStart() {
        super.onStart();
        System.out.println("SupportRequestManagerFragment onStart");
    }

    @Override
    public void onStop() {
        super.onStop();
        System.out.println("SupportRequestManagerFragment onStop");
    }
}
