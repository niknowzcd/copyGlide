package com.architect.library;

import android.content.Context;
import android.content.ContextWrapper;

import com.architect.library.load.engine.Engine;

public class GlideContext extends ContextWrapper {

    private Engine engine;

    public GlideContext(Context base, Engine engine) {
        super(base);
        this.engine = engine;
    }

    public GlideContext(Context base) {
        super(base);
    }

    public Engine getEngine() {
        return engine;
    }
}
