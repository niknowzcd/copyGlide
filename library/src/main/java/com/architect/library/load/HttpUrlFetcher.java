package com.architect.library.load;

import android.text.TextUtils;

import com.architect.library.util.ContentLengthInputStream;
import com.architect.library.load.model.GlideUrl;
import com.architect.library.HttpException;
import com.architect.library.util.LogTime;
import com.architect.library.Priority;
import com.architect.library.util.MyLogger;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

public class HttpUrlFetcher implements DataFetcher<InputStream> {

    private static final String TAG = MyLogger.TAG + HttpUrlFetcher.class.getSimpleName();

    private static final int MAXIMUM_REDIRECTS = 5;
    static final int INVALID_STATUS_CODE = -1;
    static final String REDIRECT_HEADER_FIELD = "Location";

    private final GlideUrl glideUrl;
    private final int timeout;

    private HttpURLConnection urlConnection;
    private InputStream stream;
    private volatile boolean isCanceled;


    public HttpUrlFetcher(GlideUrl glideUrl, int timeout) {
        this.glideUrl = glideUrl;
        this.timeout = timeout;
    }


    @Override
    public void loadData(Priority priority, DataCallback<? super InputStream> callback) {
        long startTime = LogTime.getLogTime();
        try {
            InputStream inputStream = loadDataWithRedirect(glideUrl.toURL(), 0, null, glideUrl.getHeaders());
            callback.onDataReady(inputStream);
        } catch (Exception e) {
            callback.onLoadFailed(e);
            MyLogger.d(TAG, "loadData error >> " + e.getMessage());
        }
    }

    private InputStream loadDataWithRedirect(URL url, int redirects, URL lastUrl, Map<String, String> headers) throws HttpException {
        //重定向的次数最多不能超过5次
//        if (redirects >= MAXIMUM_REDIRECTS) {
//            throw new HttpException("Too many (> " + MAXIMUM_REDIRECTS + ") redirects!", INVALID_STATUS_CODE);
//        } else {
//            // URL对比会耗费额外的I/O，而且容易中断
//            try {
//                if (lastUrl != null && url.toURI().equals(lastUrl.toURI())) {
//                    throw new HttpException("In re-direct loop", INVALID_STATUS_CODE);
//                }
//            } catch (URISyntaxException e) {
//                e.printStackTrace();
//            }
//        }

        urlConnection = buildAndConfigureConnection(url, headers);

        try {
            urlConnection.connect();
            stream = urlConnection.getInputStream();
        } catch (IOException e) {
            throw new HttpException("Failed to connect or obtain data", getHttpStatusCodeOrInvalid(urlConnection), e);
        }

        if (isCanceled) return null;
        final int statusCode = getHttpStatusCodeOrInvalid(urlConnection);
        if (isHttpOk(statusCode)) {
            return getStreamForSuccessfulRequest(urlConnection);
        } else if (isHttpRedirect(statusCode)) {
            //获取重定向的url
            String redirectUrlString = urlConnection.getHeaderField(REDIRECT_HEADER_FIELD);
            if (TextUtils.isEmpty(redirectUrlString)) {
                throw new HttpException("Received empty or null redirect url", statusCode);
            }

            URL redirectUrl;

            try {
                redirectUrl = new URL(url, redirectUrlString);
            } catch (MalformedURLException e) {
                throw new HttpException("Bad redirect url: " + redirectUrlString, statusCode, e);
            }
            return loadDataWithRedirect(redirectUrl, redirects + 1, url, headers);
        } else {
            throw new HttpException("HttpUrlFetcher other error ", statusCode);
        }
    }

    private HttpURLConnection buildAndConfigureConnection(URL url, Map<String, String> headers) throws HttpException {
        HttpURLConnection connection;

        try {
            connection = (HttpURLConnection) url.openConnection();
        } catch (IOException e) {
            throw new HttpException("URL open error ", 0, e);
        }
        for (Map.Entry<String, String> headerMap : headers.entrySet()) {
            connection.addRequestProperty(headerMap.getKey(), headerMap.getValue());
        }
        connection.setConnectTimeout(timeout);
        connection.setReadTimeout(timeout);
        connection.setUseCaches(false);
        connection.setDoInput(true);

        //本次请求不设置重定向,为了之后递归调用 loadData_0
        connection.setInstanceFollowRedirects(false);
        return connection;
    }

    //通过urlConnection 获取code
    private static int getHttpStatusCodeOrInvalid(HttpURLConnection urlConnection) {
        try {
            return urlConnection.getResponseCode();
        } catch (IOException e) {
            MyLogger.d(TAG, "getHttpStatusCodeOrInvalid error >> " + e.getMessage());
        }
        return INVALID_STATUS_CODE;
    }

    /**
     * statusCode / 100 == 2 而不是 statusCode == 200
     * Referencing constants is less clear than a simple static method.
     * 不过我确实是没理解 todo
     * <p>
     * 至于为什么不直接卸载函数，估计是为了少写注释，能用方法名就能表达表达式的意义吧
     */
    private static boolean isHttpOk(int statusCode) {
//        return statusCode / 100 == 2;
        return statusCode == 200;
    }

    private InputStream getStreamForSuccessfulRequest(HttpURLConnection urlConnection) throws HttpException {
        try {
            if (TextUtils.isEmpty(urlConnection.getContentEncoding())) {
                int contentLength = urlConnection.getContentLength();
                stream = ContentLengthInputStream.obtain(urlConnection.getInputStream(), contentLength);
            } else {
                stream = urlConnection.getInputStream();
            }
        } catch (IOException e) {
            throw new HttpException(
                    "Failed to obtain InputStream", getHttpStatusCodeOrInvalid(urlConnection), e);
        }
        return stream;
    }


    private static boolean isHttpRedirect(int statusCode) {
        return statusCode == 300;
    }


    @Override
    public void cancel() {

    }

    @Override
    public void cleanup() {
        if (stream != null) {
            try {
                stream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (urlConnection != null) {
            urlConnection.disconnect();
        }
        urlConnection = null;
    }
}




















