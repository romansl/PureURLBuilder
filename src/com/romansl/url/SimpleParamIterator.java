package com.romansl.url;

import java.util.Iterator;
import java.util.Map;

class SimpleParamIterator implements Iterator<Map.Entry<String, String>> {
    private final Iterator<Map.Entry<String, URL>> mIterator;

    public SimpleParamIterator(final Iterator<Map.Entry<String, URL>> iterator) {mIterator = iterator;}

    @Override
    public boolean hasNext() {
        return mIterator.hasNext();
    }

    @Override
    public Map.Entry<String, String> next() {
        final Map.Entry<String, URL> next = mIterator.next();
        return ((Param) next.getValue()).toNameValuePair();
    }

    @Override
    public void remove() {
        mIterator.remove();
    }
}
