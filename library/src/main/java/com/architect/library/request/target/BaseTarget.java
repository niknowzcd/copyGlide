package com.architect.library.request.target;

import android.graphics.drawable.Drawable;

import com.architect.library.request.Request;
import com.architect.library.request.Target;

import androidx.annotation.Nullable;

public abstract class BaseTarget<Z> implements Target<Z> {

    private Request request;

    @Override
    public void setRequest(Request request) {
        this.request = request;
    }

    @Nullable
    @Override
    public Request getRequest() {
        return request;
    }

}
