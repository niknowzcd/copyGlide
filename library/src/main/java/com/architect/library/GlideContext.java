package com.architect.library;

import android.content.Context;
import android.content.ContextWrapper;
import android.widget.ImageView;

import com.architect.library.load.engine.Engine;
import com.architect.library.request.target.ImageViewTargetFactory;
import com.architect.library.request.target.ViewTarget;

import androidx.annotation.NonNull;

public class GlideContext extends ContextWrapper {

    private Engine engine;
    private final ImageViewTargetFactory imageViewTargetFactory;

    public GlideContext(Context base, Engine engine) {
        super(base);
        this.engine = engine;
        imageViewTargetFactory = new ImageViewTargetFactory();
    }

//    public GlideContext(Context base) {
//        super(base);
//    }

    public Engine getEngine() {
        return engine;
    }

    @NonNull
    public <X> ViewTarget<ImageView, X> buildImageViewTarget(
            @NonNull ImageView imageView, @NonNull Class<X> transcodeClass) {
        return imageViewTargetFactory.buildTarget(imageView, transcodeClass);
    }
}
