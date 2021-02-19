package com.architect.library;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.widget.ImageView;

import com.architect.library.load.ResourceDecoder;
import com.architect.library.load.data.DataRewinder;
import com.architect.library.load.data.SingleDataRewinder;
import com.architect.library.load.engine.BitmapDrawableDecoder;
import com.architect.library.load.engine.DataRewinderRegistry;
import com.architect.library.load.engine.DecodePath;
import com.architect.library.load.engine.LoadPath;
import com.architect.library.load.resource.BitmapDrawableTranscoder;
import com.architect.library.load.resource.ResourceTranscoder;
import com.architect.library.temp.InputStreamBitmapImageDecoderResourceDecoder;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

/**
 * @Author: dly
 * @CreateDate: 2021/2/4 下午6:10
 * <p>
 * 这个类很关键，相当于一个全局的注册表，很多需要使用的类都会在一开始的时候注册在这里
 */
public class Registry {

    public static final String BUCKET_BITMAP_DRAWABLE = "BitmapDrawable";
    private static final ResourceDecoderRegistry decoderRegistry;
    private static final TranscoderRegistry transcoderRegistry;

    public static ResourceTranscoder<Bitmap, BitmapDrawable> transcoderCache;
    private static final DataRewinderRegistry dataRewinderRegistry = new DataRewinderRegistry();

    static {
        decoderRegistry = new ResourceDecoderRegistry();
        transcoderRegistry = new TranscoderRegistry();
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    public static void register(Context context, String name, ResourceTranscoder<Bitmap, BitmapDrawable> transcoder) {
        transcoderCache = transcoder;

        transcoderRegistry.register(Bitmap.class, BitmapDrawable.class, new BitmapDrawableTranscoder(context.getResources()));

        ResourceDecoder<InputStream, Bitmap> streamBitmapDecoder = new InputStreamBitmapImageDecoderResourceDecoder();
        decoderRegistry.append(Registry.BUCKET_BITMAP_DRAWABLE, new BitmapDrawableDecoder<>(context, streamBitmapDecoder), InputStream.class, BitmapDrawable.class);
    }


    public static ResourceTranscoder<Bitmap, BitmapDrawable> getRegister(String name) {
        return transcoderCache;
    }

    @Nullable
    public static <Data, TResource, Drawable> LoadPath<Data, TResource, Drawable> getLoadPath(
            @NonNull Class<Data> dataClass,
            @NonNull Class<TResource> resourceClass,
            @NonNull Class<Drawable> drawableClass) {
        LoadPath<Data, TResource, Drawable> result;
        List<DecodePath<Data, TResource, Drawable>> decodePaths = getDecodePaths(dataClass, resourceClass, drawableClass);
        result = new LoadPath<>(dataClass, decodePaths);
        return result;
    }

    @NonNull
    private static <Data, TResource, Drawable> List<DecodePath<Data, TResource, Drawable>> getDecodePaths(
            @NonNull Class<Data> dataClass,
            @NonNull Class<TResource> resourceClass,
            @NonNull Class<Drawable> transcodeClass) {


        List<DecodePath<Data, TResource, Drawable>> decodePaths = new ArrayList<>();
        List<Class<TResource>> registeredResourceClasses = decoderRegistry.getResourceClasses(dataClass, resourceClass);

        for (Class<TResource> registeredResourceClass : registeredResourceClasses) {
            List<Class<Drawable>> registeredTranscodeClasses =
                    transcoderRegistry.getTranscodeClasses(registeredResourceClass, transcodeClass);

            for (Class<Drawable> registeredTranscodeClass : registeredTranscodeClasses) {

                List<ResourceDecoder<Data, TResource>> decoders =
                        decoderRegistry.getDecoders(dataClass, registeredResourceClass);
                ResourceTranscoder<TResource, Drawable> transcoder =
                        transcoderRegistry.get(registeredResourceClass, registeredTranscodeClass);
                @SuppressWarnings("PMD.AvoidInstantiatingObjectsInLoops")
                DecodePath<Data, TResource, Drawable> path =
                        new DecodePath<>(dataClass, transcoder, decoders);
                decodePaths.add(path);
            }
        }
        return decodePaths;
    }

    @NonNull
    public static DataRewinder<Object> getRewinder(Object data) {
        return new SingleDataRewinder<>(data);
    }
}
