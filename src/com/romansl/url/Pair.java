package com.romansl.url;

import java.util.Map;

class Pair implements Map.Entry<String, String> {
    private final String mName;
    private final String mValue;

    Pair(final String name, final String value) {
        mName = name;
        mValue = value;
    }

    @Override
    public String getKey() {
        return mName;
    }

    @Override
    public String getValue() {
        return mValue;
    }

    @Override
    public String setValue(final String object) {
        return null;
    }
}
