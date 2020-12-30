package com.architect.library;

import android.widget.ImageView;

import com.architect.library.request.BaseRequestOptions;
import com.architect.library.request.Request;
import com.architect.library.request.RequestListener;
import com.architect.library.request.SingleRequest;
import com.architect.library.request.target.DrawableImageViewTarget;

import androidx.annotation.Nullable;

public class RequestBuilder<T> extends BaseRequestOptions<RequestBuilder<T>> {

    private String urlString;
    private GlideContext glideContext;

    public RequestBuilder(Glide glide) {
        this.glideContext = glide.getGlideContext();
    }

    public RequestBuilder<T> load(String string) {
        return loadGeneric(string);
    }

    private RequestBuilder<T> loadGeneric(String string) {
        this.urlString = string;
        return this;
    }

    /**
     * 1.给target设置view
     * 2.开启图片加载
     */
    public void into(ImageView imageView) {
        DrawableImageViewTarget target = new DrawableImageViewTarget(imageView);
        Request request = buildRequest(target, urlString, null);

        request.begin();
    }

    private Request buildRequest(DrawableImageViewTarget target, String urlString, RequestListener listener) {
        return obtainRequest(target, urlString, listener);
    }


    private Request obtainRequest(DrawableImageViewTarget target, String urlString, RequestListener listener) {
        return SingleRequest.obtain(target, listener, urlString, glideContext.getEngine());
    }


}















