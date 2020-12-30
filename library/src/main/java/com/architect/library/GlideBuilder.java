package com.architect.library;

import android.content.Context;

import com.architect.library.manager.Engine;
import com.architect.library.manager.RequestManagerRetriever;

public final class GlideBuilder {

    Glide build(Context context) {
        Engine engine = new Engine();
        RequestManagerRetriever retriever = new RequestManagerRetriever();

        return new Glide(context, engine, retriever);
    }
}
