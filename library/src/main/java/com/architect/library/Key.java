package com.architect.library;

import java.nio.charset.Charset;

public interface Key {

    String STRING_CHARSET_NAME = "UTF-8";
    Charset CHARSET = Charset.forName(STRING_CHARSET_NAME);
}
