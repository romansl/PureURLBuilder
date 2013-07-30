package com.romansl.url;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.Iterator;

class ArrayParam extends BaseParam implements Iterable<Param> {
    private final String[] mValues;

    public ArrayParam(final URL url, final String key, final String[] values) {
        super(url, key);
        mValues = values;
    }

    @Override
    public void store(final Storage storage) {
        if (storage.add(this)) {
            storage.hasArrayParam = true;
        }
    }

    @Override
    protected void format(final Appendable out) throws IOException {
        out.append(URLEncoder.encode(mKey, "UTF-8"));
        out.append('=');

        if (mValues.length > 0) {
            String value = mValues[0];
            out.append(value != null ? URLEncoder.encode(value, "UTF-8") : "");

            for (int i = 1; i < mValues.length; ++i) {
                value = mValues[i];
                out.append('&');
                out.append(URLEncoder.encode(mKey, "UTF-8"));
                out.append('=');
                out.append(value != null ? URLEncoder.encode(value, "UTF-8") : "");
            }
        }
    }

    @Override
    public Iterator<Param> iterator() {
        return new Iterator<Param>() {
            int current;

            @Override
            public boolean hasNext() {
                return current < mValues.length;
            }

            @Override
            public Param next() {
                return new Param(null, mKey, mValues[current++]);
            }

            @Override
            public void remove() {

            }
        };
    }
}
