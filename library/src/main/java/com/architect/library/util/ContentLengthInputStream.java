package com.architect.library.util;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

import androidx.annotation.NonNull;

public final class ContentLengthInputStream extends FilterInputStream {

    private static final int UNKNOWN = -1;

    private final long contentLength;
    private int readSoFar;

    @NonNull
    public static InputStream obtain(@NonNull InputStream other, long contentLength) {
        return new ContentLengthInputStream(other, contentLength);
    }

    protected ContentLengthInputStream(InputStream in, long contentLength) {
        super(in);
        this.contentLength = contentLength;
    }

    @Override
    public synchronized int available() throws IOException {
        return (int) Math.max(contentLength - readSoFar, in.available());
    }

    @Override
    public synchronized int read() throws IOException {
        int value = super.read();
        checkReadSoFarOrThrow(value >= 0 ? 1 : -1);
        return value;
    }

    @Override
    public int read(byte[] buffer) throws IOException {
        return read(buffer, 0 /*byteOffset*/, buffer.length /*byteCount*/);
    }

    @Override
    public synchronized int read(byte[] buffer, int byteOffset, int byteCount) throws IOException {
        return checkReadSoFarOrThrow(super.read(buffer, byteOffset, byteCount));
    }

    private int checkReadSoFarOrThrow(int read) throws IOException {
        if (read >= 0) {
            readSoFar += read;
        } else if (contentLength - readSoFar > 0) {
            throw new IOException(
                    "Failed to read all expected data"
                            + ", expected: "
                            + contentLength
                            + ", but read: "
                            + readSoFar);
        }
        return read;
    }
}
