package com.romansl.url;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;

class ArrayParam extends URL implements Iterable<Map.Entry<String, String>> {
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
            storage.hasArrayParam = true;
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

    @Override
    public Iterator<Map.Entry<String, String>> iterator() {
        return new Iterator<Map.Entry<String, String>>() {
            int current;

            @Override
            public boolean hasNext() {
                return current < mValues.length;
            }

            @Override
            public Map.Entry<String, String> next() {
                return new Pair(mName, mValues[current++]);
            }

            @Override
            public void remove() {

            }
        };
    }
}
