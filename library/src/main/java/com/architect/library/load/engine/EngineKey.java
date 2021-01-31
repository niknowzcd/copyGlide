package com.architect.library.load.engine;

import com.architect.library.Key;

import java.util.Objects;

public class EngineKey implements Key {

    private final String urlString;

    public EngineKey(String urlString) {
        this.urlString = urlString;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EngineKey)) return false;
        EngineKey engineKey = (EngineKey) o;
        return Objects.equals(urlString, engineKey.urlString);
    }

    @Override
    public int hashCode() {
        return Objects.hash(urlString);
    }
}
