package com.romansl.url;

import java.io.IOException;
import java.net.URLEncoder;

class ArrayPath extends URL {
    private final String[] mParts;

    public ArrayPath(final URL url, final String[] parts) {
        super(url);
        mParts = parts;
    }

    @Override
   protected void store(final Storage storage) {
       if (storage.mPath == null) {
           storage.mPath = this;
       }
   }

    @Override
    protected void format(final Appendable out) throws IOException {
        if (mParts == null || mParts.length == 0)
            return;

        for (final String part : mParts) {
            out.append('/');
            out.append(URLEncoder.encode(part, "UTF-8"));
        }
    }
}
