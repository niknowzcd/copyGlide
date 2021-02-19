package com.architect.library.load.engine;

import com.architect.library.load.data.DataRewinder;

import java.util.List;

/**
 * @Author: dly
 * @CreateDate: 2021/2/5 上午11:54
 */

public class LoadPath<Data, ResourceType, Drawable> {

    private final Class<Data> dataClass;
    private final List<? extends DecodePath<Data, ResourceType, Drawable>> decodePaths;


    public LoadPath(Class<Data> dataClass, List<? extends DecodePath<Data, ResourceType, Drawable>> decodePaths) {
        this.dataClass = dataClass;
        this.decodePaths = decodePaths;
    }

    public Resource<Drawable> load(DataRewinder<Data> rewinder, DecodePath.DecodeCallback<ResourceType> decodeCallback) {
        return loadWithExceptionList(rewinder, decodeCallback);
    }

    private Resource<Drawable> loadWithExceptionList(
            DataRewinder<Data> rewinder,
            DecodePath.DecodeCallback<ResourceType> decodeCallback) {
        Resource<Drawable> result = null;
        for (int i = 0, size = decodePaths.size(); i < size; i++) {
            DecodePath<Data, ResourceType, Drawable> path = decodePaths.get(i);
            try {
                result = path.decode(rewinder, decodeCallback);
            } catch (Exception e) {

            }
            if (result != null) {
                break;
            }
        }

        return result;
    }
}
