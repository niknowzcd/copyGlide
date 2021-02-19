package com.architect.library;

import com.architect.library.load.engine.Resource;
import com.architect.library.load.resource.ResourceTranscoder;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * A simple {@link ResourceTranscoder} that simply returns the given resource.
 *
 * @param <Z> The type of the resource that will be transcoded from and to.
 */
public class UnitTranscoder<Z> implements ResourceTranscoder<Z, Z> {
  private static final UnitTranscoder<?> UNIT_TRANSCODER = new UnitTranscoder<>();

  @SuppressWarnings("unchecked")
  public static <Z> ResourceTranscoder<Z, Z> get() {
    return (ResourceTranscoder<Z, Z>) UNIT_TRANSCODER;
  }

  @Nullable
  @Override
  public Resource<Z> transcode(@NonNull Resource<Z> toTranscode) {
    return toTranscode;
  }
}
