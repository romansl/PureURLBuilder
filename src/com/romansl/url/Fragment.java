package com.romansl.url;

import java.io.IOException;

class Fragment extends URL {
    private final String mFragment;

    public Fragment(final URL url, final String fragment) {
        super(url);
        mFragment = fragment;
    }

    @Override
    protected void store(final Storage storage) {
        if (storage.mFragment == null) {
            storage.mFragment = this;
        }
    }

    @Override
    protected void format(final Appendable out) throws IOException {
        out.append('#');
        out.append(mFragment);
    }
}
