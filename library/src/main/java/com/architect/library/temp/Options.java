package com.architect.library.temp;

import com.architect.library.Key;

import java.security.MessageDigest;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.collection.ArrayMap;
import androidx.collection.SimpleArrayMap;

/** A set of {@link Option Options} to apply to in memory and disk cache keys. */
public final class Options implements Key {
  private final ArrayMap<Option<?>, Object> values = new CachedHashCodeArrayMap<>();

  public void putAll(@NonNull Options other) {
    values.putAll((SimpleArrayMap<Option<?>, Object>) other.values);
  }

  @NonNull
  public <T> Options set(@NonNull Option<T> option, @NonNull T value) {
    values.put(option, value);
    return this;
  }

  @Nullable
  @SuppressWarnings("unchecked")
  public <T> T get(@NonNull Option<T> option) {
    return values.containsKey(option) ? (T) values.get(option) : option.getDefaultValue();
  }

  @Override
  public boolean equals(Object o) {
    if (o instanceof Options) {
      Options other = (Options) o;
      return values.equals(other.values);
    }
    return false;
  }

  @Override
  public int hashCode() {
    return values.hashCode();
  }


  @Override
  public String toString() {
    return "Options{" + "values=" + values + '}';
  }

  @SuppressWarnings("unchecked")
  private static <T> void updateDiskCacheKey(
          @NonNull Option<T> option, @NonNull Object value, @NonNull MessageDigest md) {
    option.update((T) value, md);
  }
}
