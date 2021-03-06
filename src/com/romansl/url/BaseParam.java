package com.romansl.url;

import java.util.ArrayList;

abstract class BaseParam extends URL implements Comparable<BaseParam> {
    protected final String mKey;

    protected BaseParam(final URL next, final String key) {
        super(next);

        if (key == null)
            throw new NullPointerException("Key can not be null");

        mKey = key;
    }

    public String getKey() {
        return mKey;
    }

    @Override
    public boolean equals(final Object o) {
        return o == this || o instanceof BaseParam && mKey.equals(((BaseParam) o).mKey);
    }

    @Override
    public int hashCode() {
        return mKey.hashCode();
    }

    abstract boolean equalValues(final BaseParam param2);
    abstract int getValueHashCode();
    abstract void store(final ArrayList<Param> out);

    @Override
    public int compareTo(final BaseParam another) {
        return mKey.compareTo(another.mKey);
    }
}
