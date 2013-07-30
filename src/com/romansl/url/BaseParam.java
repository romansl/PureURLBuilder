package com.romansl.url;

class BaseParam extends URL {
    protected final String mKey;

    protected BaseParam(final URL next, final String key) {
        super(next);
        mKey = key;
    }

    public String getKey() {
        return mKey;
    }

    @Override
    public boolean equals(final Object o) {
        return o instanceof BaseParam && mKey.equals(((BaseParam) o).mKey);
    }

    @Override
    public int hashCode() {
        return mKey.hashCode();
    }
}
