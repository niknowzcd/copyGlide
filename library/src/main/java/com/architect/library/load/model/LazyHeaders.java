package com.architect.library.load.model;

import android.text.TextUtils;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import androidx.annotation.NonNull;

/**
 * Collections.unmodifiableMap() 返回一个只读的map，类似final一样不可修改
 */
public final class LazyHeaders implements Headers {

    private final Map<String, List<LazyHeaderFactory>> headers;
    private volatile Map<String, String> combinedHeaders;

    public LazyHeaders(Map<String, List<LazyHeaderFactory>> headers) {
        this.headers = Collections.unmodifiableMap(headers);
    }

    @Override
    public Map<String, String> getHeaders() {
        if (combinedHeaders == null) {
            synchronized (this) {
                if (combinedHeaders == null) {
                    this.combinedHeaders = Collections.unmodifiableMap(generateHeaders());
                }
            }
        }
        return combinedHeaders;
    }

    private Map<String, String> generateHeaders() {
        Map<String, String> combinedHeaders = new HashMap<>();

        for (Map.Entry<String, List<LazyHeaderFactory>> entry : headers.entrySet()) {
            String values = buildHeaderValue(entry.getValue());
            if (!TextUtils.isEmpty(values)) {
                combinedHeaders.put(entry.getKey(), values);
            }
        }
        return combinedHeaders;
    }

    private String buildHeaderValue(@NonNull List<LazyHeaderFactory> factories) {
        StringBuilder builder = new StringBuilder();
        int size = factories.size();
        for (int i = 0; i < size; i++) {
            LazyHeaderFactory factory = factories.get(i);
            String header = factory.buildHeaders();
            if (!TextUtils.isEmpty(header)) {
                builder.append(header);
                if (i != factories.size() - 1) {
                    builder.append(",");
                }
            }
        }
        return builder.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof LazyHeaders) {
            LazyHeaders other = (LazyHeaders) o;
            return headers.equals(other.headers);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getHeaders(), combinedHeaders);
    }

    public static final class Builder {
        private static final String USER_AGENT_HEADER = "User-Agent";
        private static final String DEFAULT_USER_AGENT = getSanitizedUserAgent();
        private static final Map<String, List<LazyHeaderFactory>> DEFAULT_HEADERS;

        private Map<String, List<LazyHeaderFactory>> headers = DEFAULT_HEADERS;

        static {
            Map<String, List<LazyHeaderFactory>> temp = new HashMap<>(2);
            if (!TextUtils.isEmpty(DEFAULT_USER_AGENT)) {
                temp.put(
                        USER_AGENT_HEADER,
                        Collections.<LazyHeaderFactory>singletonList(
                                new StringHeaderFactory(DEFAULT_USER_AGENT)));
            }
            DEFAULT_HEADERS = Collections.unmodifiableMap(temp);
        }

        private static String getSanitizedUserAgent() {
            //或者设备的一些http需要的基本信息如  Dalvik/2.1.0 (Linux; U; Android 9; MIX 3 MIUI/V11.0.4.0.PEECNXM)
            String defaultUserAgent = System.getProperty("http.agent");
            if (TextUtils.isEmpty(defaultUserAgent)) {
                return defaultUserAgent;
            }

            int length = defaultUserAgent.length();
            StringBuilder builder = new StringBuilder(length);
            for (int i = 0; i < length; i++) {
                char c = defaultUserAgent.charAt(i);
                //todo
                if ((c > '\u001f' || c == '\t') && c < '\u007f') {
                    builder.append(c);
                } else {
                    builder.append('?');
                }
            }
            return builder.toString();
        }

        public LazyHeaders build() {
            return new LazyHeaders(headers);
        }
    }


    static final class StringHeaderFactory implements LazyHeaderFactory {

        private final String value;

        public StringHeaderFactory(String value) {
            this.value = value;
        }

        @Override
        public String buildHeaders() {
            return value;
        }
    }


}
