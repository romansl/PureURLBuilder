package com.romansl.url;

import java.io.IOException;
import java.net.URLEncoder;

class ArrayParam extends URL {
    private final String mName;
    private final String[] mValues;

    public ArrayParam(final URL url, final String name, final String[] values) {
        super(url);
        mName = name;
        mValues = values;
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

        if (mValues.length > 0) {
            String value = mValues[0];
            out.append(value != null ? URLEncoder.encode(value, "UTF-8") : "");

            for (int i = 1; i < mValues.length; ++i) {
                value = mValues[i];
                out.append('&');
                out.append(URLEncoder.encode(mName, "UTF-8"));
                out.append('=');
                out.append(value != null ? URLEncoder.encode(value, "UTF-8") : "");
            }
        }
    }
}
