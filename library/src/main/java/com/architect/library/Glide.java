package com.architect.library;

import android.app.Activity;
import android.content.Context;

import com.architect.library.load.engine.Engine;
import com.architect.library.manager.RequestManagerRetriever;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

public class Glide {

    private static volatile Glide glide;
    private final GlideContext glideContext;
    private static volatile boolean isInitializing;
    private Engine engine;
    private RequestManagerRetriever requestManagerRetriever;

    Glide(Context context, Engine engine, RequestManagerRetriever requestManagerRetriever) {
        this.engine = engine;
        this.requestManagerRetriever = requestManagerRetriever;

        glideContext = new GlideContext(context, engine);
    }

    public static Glide get(Context context) {
        if (glide == null) {
            synchronized (Glide.class) {
                if (glide == null) {
                    checkAndInitializeGlide(context);
                }
            }
        }
        return glide;
    }

    private static void checkAndInitializeGlide(Context context) {
//        if(isInitializing){
//            throw new IllegalStateException("Glide 初始化异常");
//        }

//        isInitializing = true;
        initializeGlide(context, new GlideBuilder());
//        isInitializing=false;
    }

    private static RequestManagerRetriever getRetriever(Context context) {
        if (context == null) throw new NullPointerException("context not null");

        return Glide.get(context).getRequestManagerRetriever();
    }

    @NonNull
    public static RequestManager with(@NonNull Context context) {
        return getRetriever(context).get(context);
    }

    @NonNull
    public static RequestManager with(@NonNull Activity activity) {
        return getRetriever(activity).get(activity);
    }

    @NonNull
    public static RequestManager with(@NonNull FragmentActivity activity) {
        return getRetriever(activity).get(activity);
    }

    @NonNull
    public static RequestManager with(@NonNull Fragment fragment) {
        return getRetriever(fragment.getContext()).get(fragment);
    }

    @NonNull
    public static RequestManager with(@NonNull android.app.Fragment fragment) {
        return getRetriever(fragment.getActivity()).get(fragment);
    }

    private static void initializeGlide(Context context, GlideBuilder builder) {
        Glide.glide = builder.build(context);
    }

    public RequestManagerRetriever getRequestManagerRetriever() {
        return requestManagerRetriever;
    }

    GlideContext getGlideContext() {
        return glideContext;
    }
}







