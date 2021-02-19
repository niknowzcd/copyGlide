package com.architect.library.request;

import com.architect.library.load.engine.Engine;
import com.architect.library.load.engine.Resource;
import com.architect.library.load.model.ResourceCallback;

public final class SingleRequest<Drawable> implements Request, ResourceCallback {

    private final Target<Drawable> target;
    private final Class<Drawable> targetClass;
    private final RequestListener listener;
    private final Engine engine;
    private String urlString;

    public SingleRequest(Target<Drawable> target, RequestListener listener, String urlString, Engine engine, Class<Drawable> targetClass) {
        this.target = target;
        this.listener = listener;
        this.engine = engine;
        this.urlString = urlString;
        this.targetClass = targetClass;
    }

    public static <Drawable> SingleRequest<Drawable> obtain(Target<Drawable> target, RequestListener listener, String urlString, Engine engine, Class<Drawable> targetClass) {
        return new SingleRequest<>(target, listener, urlString, engine, targetClass);
    }

    @Override
    public void begin() {
        engine.load(this, urlString, targetClass);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onResourceReady(Resource<?> resource) {
        Object result = resource.get();
        onResourceReady(resource, (Drawable) result);
    }

    public void onResourceReady(Resource<?> resource, Drawable result) {
        target.onResourceReady(result, null);
    }


    @Override
    public void onLoadFailed() {

    }
}
