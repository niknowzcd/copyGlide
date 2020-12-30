package com.architect.library.manager;

import android.graphics.drawable.Drawable;

import com.architect.library.Glide;
import com.architect.library.RequestBuilder;

public class RequestManager implements LifecycleListener {

    private ActivityFragmentLifecycle lifecycle;
    private ApplicationLifecycle applicationLifecycle;
    private Glide glide;

    public RequestManager(ApplicationLifecycle lifecycle, Glide glide) {
        this.applicationLifecycle = lifecycle;
        this.glide = glide;
    }

    public RequestManager(ActivityFragmentLifecycle lifecycle, Glide glide) {
        this.lifecycle = lifecycle;
        this.glide = glide;
    }

    public RequestBuilder<Drawable> load(String string) {
        return asDrawable().load(string);
    }

    private RequestBuilder<Drawable> asDrawable() {
        return as(Drawable.class);
    }

    private <ResourceType> RequestBuilder<ResourceType> as(Class<ResourceType> drawableClass) {
        return new RequestBuilder<>(glide);
    }


    @Override
    public void onStart() {

    }

    @Override
    public void onStop() {

    }

    @Override
    public void onDestroy() {

    }

}
