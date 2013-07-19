package com.romansl.url;

import java.io.IOException;

class Scheme extends URL {
    private final String mScheme;

    public Scheme(final URL url, final String scheme) {
        super(url);
        mScheme = scheme;
    }

    @Override
    protected void store(final Storage storage) {
        if (storage.mScheme == null) {
            storage.mScheme = this;
        }
    }

    @Override
    protected void format(final Appendable out) throws IOException {
        out.append(mScheme);
        out.append("://");
    }
}
