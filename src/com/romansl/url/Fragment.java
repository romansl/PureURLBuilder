package com.romansl.url;

import java.io.IOException;

class Fragment extends URL {
    private final String mFragment;

    Fragment(final URL url, final String fragment) {
        super(url);
        mFragment = fragment;
    }

    @Override
    protected void store(final FinalURL storage) {
        if (storage.mFragment == null) {
            storage.mFragment = this;
        }
    }

    @Override
    protected void format(final Appendable out) throws IOException {
        if (mFragment != null && mFragment.length() != 0) {
            out.append('#');
            out.append(mFragment);
        }
    }

    @Override
    String getStringContent() {
        return mFragment == null ? "" : mFragment;
    }
}
