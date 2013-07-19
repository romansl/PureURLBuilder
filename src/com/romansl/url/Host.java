package com.romansl.url;

import java.io.IOException;

class Host extends URL {
    private final String mName;

    public Host(final URL next, final String name) {
        super(next);
        mName = name;
    }

    @Override
    public void store(final Storage storage) {
        if (storage.mHost == null) {
            storage.mHost = this;
        }
    }

    @Override
    protected void format(final Appendable out) throws IOException {
        if (mName != null) {
            out.append(mName);
        }
    }
}
