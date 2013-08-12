package com.romansl.url;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;

public class Param extends BaseParam {
    final String mValue;

    Param(final URL url, final String key, final String value) {
        super(url, key);
        mValue = value == null ? "" : value;
    }

    @Override
    public void store(final FinalURL storage) {
        storage.add(this);
    }

    @Override
    protected void format(final Appendable out) throws IOException {
        out.append(URLEncoder.encode(mKey, "UTF-8"));
        out.append('=');
        out.append(URLEncoder.encode(mValue, "UTF-8"));
    }

    public String getValue() {
        return mValue;
    }

    @Override
    boolean equalValues(final BaseParam param2) {
        final String value1 = mValue;
        if (param2 instanceof Param) {
            return value1.equals(((Param) param2).mValue);
        } else {
            final String[] values = ((ArrayParam) param2).mValues;
            return values.length == 1 && value1.equals(values[0]);
        }
    }

    @Override
    int getValueHashCode() {
        return 31 + mValue.hashCode();
    }

    @Override
    void store(final ArrayList<Param> out) {
        out.add(this);
    }
}
