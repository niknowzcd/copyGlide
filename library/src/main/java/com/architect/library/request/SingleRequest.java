package com.architect.library.request;

import android.graphics.drawable.BitmapDrawable;

import com.architect.library.load.engine.Engine;
import com.architect.library.load.engine.EngineResource;
import com.architect.library.load.engine.Resource;
import com.architect.library.load.model.ResourceCallback;
import com.architect.library.request.target.DrawableImageViewTarget;

public final class SingleRequest implements Request, ResourceCallback {

    private final DrawableImageViewTarget target;
    private final RequestListener listener;
    private final Engine engine;
    private String urlString;

    public SingleRequest(DrawableImageViewTarget target, RequestListener listener, String urlString, Engine engine) {
        this.target = target;
        this.listener = listener;
        this.engine = engine;
        this.urlString = urlString;
    }

    public static SingleRequest obtain(DrawableImageViewTarget target, RequestListener listener, String urlString, Engine engine) {
        return new SingleRequest(target, listener, urlString, engine);
    }


    @Override
    public void begin() {
        engine.load(this, urlString);
    }

    @Override
    public void onResourceReady(Resource<?> resource) {
        if (resource instanceof EngineResource) {
            BitmapDrawable object = ((EngineResource<Object>) resource).getBitmapDrawable();
            target.onResourceReady(object, null);
        }
    }

    @Override
    public void onLoadFailed() {

    }
}
