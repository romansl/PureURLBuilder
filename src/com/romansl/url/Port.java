package com.romansl.url;

import java.io.IOException;

class Port extends URL {
    private final int mPort;

    public Port(final URL url, final int port) {
        super(url);
        mPort = port;
    }

    @Override
    protected void store(final Storage storage) {
        if (storage.mPort == null) {
            storage.mPort = this;
        }
    }

    @Override
    protected void format(final Appendable out) throws IOException {
        out.append(':');
        out.append(Integer.toString(mPort));
    }
}
