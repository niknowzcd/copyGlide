package com.architect.library.load.model;

import java.util.Map;

public interface Headers {

    Headers DEFAULT = new LazyHeaders.Builder().build();

    Map<String, String> getHeaders();
}
