package com.romansl.url;

import java.io.IOException;

class Scheme extends URL {
    private final String mScheme;

    Scheme(final URL url, final String scheme) {
        super(url);
        mScheme = scheme;
    }

    @Override
    protected void store(final FinalURL storage) {
        if (storage.mScheme == null) {
            storage.mScheme = this;
        }
    }

    @Override
    protected void format(final Appendable out) throws IOException {
        if (mScheme != null && mScheme.length() != 0) {
            out.append(mScheme);
            out.append("://");
        }
    }

    @Override
    String getStringContent() {
        return mScheme == null ? "" : mScheme;
    }
}
