package com.romansl.url;

import java.io.IOException;

class Port extends URL {
    private final int mPort;

    Port(final URL url, final int port) {
        super(url);
        mPort = port;
    }

    @Override
    protected void store(final FinalURL storage) {
        if (storage.mPort == null) {
            storage.mPort = this;
        }
    }

    @Override
    protected void format(final Appendable out) throws IOException {
        if (mPort > 0) {
            out.append(':');
            out.append(Integer.toString(mPort));
        }
    }

    @Override
    int getIntContent() {
        return mPort > 0 ? mPort : 80;
    }
}
