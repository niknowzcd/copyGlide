package com.architect.library.load.engine;

import com.architect.library.DataSource;
import com.architect.library.Registry;
import com.architect.library.SourceGenerator;
import com.architect.library.load.data.DataRewinder;
import com.architect.library.util.Synthetic;

import androidx.annotation.NonNull;

public class DecodeJob<Drawable> implements Runnable, DataFetcherGenerator.FetcherReadyCallback {

    private final DecodeHelper<Drawable> decodeHelper;
    private DataFetcherGenerator currentGenerator;
    private String urlString;
    private Callback<Drawable> callback;
    private Class<Drawable> drawableClass;

    public DecodeJob(String urlString, Callback<Drawable> callback, Class<Drawable> drawableClass) {
        this.urlString = urlString;
        this.callback = callback;
        this.drawableClass = drawableClass;

        decodeHelper = new DecodeHelper<>(Object.class, drawableClass);
    }

    @Override
    public void run() {
        currentGenerator = getNextGenerator();
        currentGenerator.startNext();
    }

    private DataFetcherGenerator getNextGenerator() {

        return new SourceGenerator(urlString, this);
    }

    @Override
    public void onDataFetcherReady(Object data) {
        //这里有一系列的读取字符流过程,暂时省去
//        InputStream inputStream = (InputStream) data;
//        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
//        System.out.println("111111 bitmap >> " + bitmap);

        Resource<Drawable> resource = decodeFromRetrievedData(data);

        callback.onResourceReady(resource);
    }

    private Resource<Drawable> decodeFromRetrievedData(Object data) {
        return decodeFromFetcher(data);
    }

    private Resource<Drawable> decodeFromFetcher(Object data) {
        LoadPath<Object, ?, Drawable> path = decodeHelper.getLoadPath((Class<Object>) data.getClass());
        return runLoadPath(data, path);
    }

    private <ResourceType> Resource<Drawable> runLoadPath(
            Object data, LoadPath<Object, ResourceType, Drawable> path) {

        DataRewinder<Object> rewinder = Registry.getRewinder(data);

        try {
            return path.load(rewinder, new DecodeCallback<ResourceType>(DataSource.REMOTE));
        } finally {
            rewinder.cleanup();
        }
    }

    <Z> Resource<Z> onResourceDecoded(DataSource dataSource, @NonNull Resource<Z> decoded) {
        return decoded;
    }

    private final class DecodeCallback<Z> implements DecodePath.DecodeCallback<Z> {

        private final DataSource dataSource;

        @Synthetic
        DecodeCallback(DataSource dataSource) {
            this.dataSource = dataSource;
        }

        @NonNull
        @Override
        public Resource<Z> onResourceDecoded(@NonNull Resource<Z> decoded) {
            return DecodeJob.this.onResourceDecoded(dataSource, decoded);
        }
    }


    interface Callback<R> {
        void onResourceReady(Resource<R> resource);

        void onLoadFailed();
    }
}
