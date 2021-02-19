package com.architect.library;

import android.widget.ImageView;

import com.architect.library.request.BaseRequestOptions;
import com.architect.library.request.Request;
import com.architect.library.request.RequestListener;
import com.architect.library.request.SingleRequest;
import com.architect.library.request.Target;
import com.architect.library.request.target.ViewTarget;

public class RequestBuilder<Drawable> extends BaseRequestOptions<RequestBuilder<Drawable>> {

    private String urlString;
    private GlideContext glideContext;
    private final Class<Drawable> targetClass;

    public RequestBuilder(Glide glide, Class<Drawable> targetClass) {
        this.glideContext = glide.getGlideContext();
        this.targetClass = targetClass;
    }

    public RequestBuilder<Drawable> load(String string) {
        return loadGeneric(string);
    }

    private RequestBuilder<Drawable> loadGeneric(String string) {
        this.urlString = string;
        return this;
    }

    /**
     * 1.给target设置view
     * 2.开启图片加载
     */
    public void into(ImageView imageView) {
        ViewTarget<ImageView, Drawable> target = glideContext.buildImageViewTarget(imageView, targetClass);

        Request request = buildRequest(target, urlString, null, targetClass);

        request.begin();
    }

    private Request buildRequest(Target<Drawable> target, String urlString, RequestListener listener, Class<Drawable> transcodeClass) {
        return obtainRequest(target, urlString, listener, transcodeClass);
    }


    private Request obtainRequest(Target<Drawable> target, String urlString, RequestListener listener, Class<Drawable> transcodeClass) {
        return SingleRequest.obtain(target, listener, urlString, glideContext.getEngine(), transcodeClass);
    }

}















