package com.architect.library.load.engine;

import com.architect.library.Key;

public class EngineKey implements Key {
    private final String urlString;


    public EngineKey(String urlString) {
        this.urlString = urlString;
    }
}
