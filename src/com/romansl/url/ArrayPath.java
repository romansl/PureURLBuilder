package com.romansl.url;

import java.io.IOException;
import java.net.URLEncoder;

class ArrayPath extends URL {
    private final String[] mParts;

    ArrayPath(final URL url, final String[] parts) {
        super(url);
        mParts = parts;
    }

    @Override
   protected void store(final FinalURL storage) {
       if (storage.mPath == null) {
           storage.mPath = this;
       }
   }

    @Override
    protected void format(final Appendable out) throws IOException {
        if (mParts == null)
            return;

        if (mParts.length == 0) {
            out.append('/');
            return;
        }

        for (final String part : mParts) {
            out.append('/');
            out.append(URLEncoder.encode(part, "UTF-8"));
        }
    }

    @Override
    String getStringContent() {
        if (mParts == null || mParts.length == 0)
            return "";

        final StringBuilder out = new StringBuilder();
        for (final String part : mParts) {
            out.append('/');
            out.append(part);
        }

        return out.toString();
    }
}
