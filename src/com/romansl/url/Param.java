package com.romansl.url;

import java.io.IOException;
import java.net.URLEncoder;

public class Param extends BaseParam {
    private final String mValue;

    public Param(final URL url, final String key, final String value) {
        super(url, key);
        mValue = value;
    }

    @Override
    public void store(final Storage storage) {
        storage.add(this);
    }

    @Override
    protected void format(final Appendable out) throws IOException {
        out.append(URLEncoder.encode(mKey, "UTF-8"));
        out.append('=');
        out.append(mValue != null ? URLEncoder.encode(mValue, "UTF-8") : "");
    }

    public String getValue() {
        return mValue;
    }
}
