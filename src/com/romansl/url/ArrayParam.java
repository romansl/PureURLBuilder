package com.romansl.url;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.Iterator;

class ArrayParam extends BaseParam implements Iterable<Param> {
    final String[] mValues;

    ArrayParam(final URL url, final String key, final String[] values) {
        super(url, key);
        mValues = values == null ? new String[0] : values;
    }

    @Override
    public void store(final FinalURL storage) {
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

    @Override
    boolean equalValues(final BaseParam param2) {
        final String[] values1 = mValues;
        if (param2 instanceof Param) {
            return values1.length == 1 && ((Param) param2).mValue.equals(values1[0]);
        } else {
            final String[] values2 = ((ArrayParam) param2).mValues;
            if (values1.length != values2.length)
                return false;

            for (final String item : values1) {
                if (!contains(values2, item))
                    return false;
            }

            return true;
        }
    }

    private static boolean contains(final String[] array, final String value) {
        if (value == null) {
            for (final String item : array) {
                if (item == null)
                    return true;
            }

            return false;
        }

        for (final String item : array) {
            if (value.equals(item))
                return true;
        }

        return false;
    }

    @Override
    int getValueHashCode() {
        int result = 0;

        for (final String value : mValues) {
            result += 31;
            result += (value == null ? "".hashCode() : value.hashCode());
        }

        return result;
    }
}
