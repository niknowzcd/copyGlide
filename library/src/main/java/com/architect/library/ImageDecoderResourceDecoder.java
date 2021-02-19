package com.architect.library;

import android.annotation.SuppressLint;
import android.graphics.ColorSpace;
import android.graphics.ImageDecoder;
import android.graphics.ImageDecoder.DecodeException;
import android.graphics.ImageDecoder.ImageInfo;
import android.graphics.ImageDecoder.OnHeaderDecodedListener;
import android.graphics.ImageDecoder.OnPartialImageListener;
import android.graphics.ImageDecoder.Source;
import android.os.Build;
import android.util.Size;

import com.architect.library.load.ResourceDecoder;
import com.architect.library.load.engine.Resource;
import com.architect.library.request.Target;
import com.architect.library.temp.HardwareConfigState;
import com.architect.library.temp.PreferredColorSpace;
import com.architect.library.util.Synthetic;

import java.io.IOException;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

/**
 * Downsamples, decodes, and rotates images according to their exif orientation using {@link
 * ImageDecoder}.
 *
 * @param <T> The type of resource to be decoded (Bitmap, Drawable etc).
 */
@RequiresApi(api = 28)
public abstract class ImageDecoderResourceDecoder<T> implements ResourceDecoder<Source, T> {
    private static final String TAG = "ImageDecoder";

    @SuppressWarnings("WeakerAccess")
    @Synthetic
    final HardwareConfigState hardwareConfigState = HardwareConfigState.getInstance();

    @Override
    public final boolean handles(@NonNull Source source) {
        return true;
    }

    @Nullable
    @Override
    public final Resource<T> decode(
            @NonNull Source source)
            throws IOException {
        return decode(
                source,
                new OnHeaderDecodedListener() {
                    @SuppressLint("Override")
                    @Override
                    public void onHeaderDecoded(ImageDecoder decoder, ImageInfo info, Source source) {
                        if (hardwareConfigState.isHardwareConfigAllowed(
                                /*isExifOrientationRequired=*/ false)) {
                            decoder.setAllocator(ImageDecoder.ALLOCATOR_HARDWARE);
                        } else {
                            decoder.setAllocator(ImageDecoder.ALLOCATOR_SOFTWARE);
                        }


                        decoder.setMemorySizePolicy(ImageDecoder.MEMORY_POLICY_LOW_RAM);

                        decoder.setOnPartialImageListener(
                                new OnPartialImageListener() {
                                    @Override
                                    public boolean onPartialImage(@NonNull DecodeException e) {
                                        // Never return partial images.
                                        return false;
                                    }
                                });

                        Size size = info.getSize();
                        int targetWidth = 200;
                        if (200 == Target.SIZE_ORIGINAL) {
                            targetWidth = size.getWidth();
                        }
                        int targetHeight = 200;
                        if (200 == Target.SIZE_ORIGINAL) {
                            targetHeight = size.getHeight();
                        }

                        int resizeWidth = Math.round(1.0f * size.getWidth());
                        int resizeHeight = Math.round(1.0f * size.getHeight());

                        decoder.setTargetSize(resizeWidth, resizeHeight);

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                            boolean isP3Eligible=true;
//                            boolean isP3Eligible =
//                                    preferredColorSpace == PreferredColorSpace.DISPLAY_P3 && info.getColorSpace() != null && info.getColorSpace().isWideGamut();
                            decoder.setTargetColorSpace(
                                    ColorSpace.get(
                                            isP3Eligible ? ColorSpace.Named.DISPLAY_P3 : ColorSpace.Named.SRGB));
                        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            decoder.setTargetColorSpace(ColorSpace.get(ColorSpace.Named.SRGB));
                        }
                    }
                });
    }

    protected abstract Resource<T> decode(
            Source source, OnHeaderDecodedListener listener)
            throws IOException;
}
