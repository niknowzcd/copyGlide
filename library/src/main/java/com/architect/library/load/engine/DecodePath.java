package com.architect.library.load.engine;

import com.architect.library.load.ResourceDecoder;
import com.architect.library.load.data.DataRewinder;
import com.architect.library.load.resource.ResourceTranscoder;
import com.architect.library.util.MyLogger;

import java.util.List;

import androidx.annotation.NonNull;

/**
 * @Author: dly
 * @CreateDate: 2021/2/5 上午11:56
 */

public class DecodePath<DataType, ResourceType, Transcode> {

    private final Class<DataType> dataClass;
    private final ResourceTranscoder<ResourceType, Transcode> transcoder;
    private final List<? extends ResourceDecoder<DataType, ResourceType>> decoders;


    public DecodePath(Class<DataType> dataClass, ResourceTranscoder<ResourceType, Transcode> transcoder, List<? extends ResourceDecoder<DataType, ResourceType>> decoders) {
        this.dataClass = dataClass;
        this.transcoder = transcoder;
        this.decoders = decoders;
    }

    public Resource<Transcode> decode(DataRewinder<DataType> rewinder, DecodeCallback<ResourceType> callback) throws Exception {
        Resource<ResourceType> decoded = decodeResource(rewinder);
        Resource<ResourceType> transformed = callback.onResourceDecoded(decoded);
        return transcoder.transcode(transformed);
    }

    @NonNull
    private Resource<ResourceType> decodeResource(DataRewinder<DataType> rewinder) throws Exception {
        return decodeResourceWithList(rewinder);
    }

    @NonNull
    private Resource<ResourceType> decodeResourceWithList(DataRewinder<DataType> rewinder) throws Exception {
        Resource<ResourceType> result = null;
        for (int i = 0, size = decoders.size(); i < size; i++) {
            ResourceDecoder<DataType, ResourceType> decoder = decoders.get(i);
            try {
                DataType data = rewinder.getData();
                if (decoder.handles(data)) {
                    data = rewinder.getData();
                    result = decoder.decode(data);
                }
            } catch (Exception e) {
                MyLogger.d("DecodePath error >> " + e.getMessage());
            }

            if (result != null) {
                break;
            }
        }
//
//        if (result == null) {
//            throw new GlideException(failureMessage, new ArrayList<>(exceptions));
//        }
        return result;
    }

    interface DecodeCallback<ResourceType> {
        Resource<ResourceType> onResourceDecoded(@NonNull Resource<ResourceType> resource);
    }
}
