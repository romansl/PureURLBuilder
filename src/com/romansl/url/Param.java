package com.romansl.url;

import java.io.IOException;
import java.net.URLEncoder;

class Param extends URL {
    private final String mName;
    private final String mValue;

    public Param(final URL url, final String name, final String value) {
        super(url);
        mName = name;
        mValue = value;
    }

    @Override
    public void store(final Storage storage) {
        if (storage.get(mName) == null) {
            storage.put(mName, this);
        }
    }

    @Override
    protected void format(final Appendable out) throws IOException {
        out.append(URLEncoder.encode(mName, "UTF-8"));
        out.append('=');
        out.append(mValue != null ? URLEncoder.encode(mValue, "UTF-8") : "");
    }
}
