package com.romansl.url;

import java.util.Iterator;
import java.util.Map;

class ParamIterator implements Iterator<Map.Entry<String, String>> {
    private final Iterator<Map.Entry<String, URL>> mStorageIterator;
    private Iterator<Map.Entry<String, String>> mItemIterator;

    public ParamIterator(final Iterator<Map.Entry<String, URL>> iterator) {
        mStorageIterator = iterator;
    }

    @Override
    public boolean hasNext() {
        return (mItemIterator != null && mItemIterator.hasNext()) || mStorageIterator.hasNext();
    }

    @Override
    public Map.Entry<String, String> next() {
        if (mItemIterator == null || !mItemIterator.hasNext()) {
            final Map.Entry<String, URL> next = mStorageIterator.next();
            final URL value = next.getValue();
            if (value instanceof ArrayParam) {
                mItemIterator =  ((ArrayParam) value).iterator();
            } else {
                mItemIterator = null;
                return ((Param) value).toNameValuePair();
            }
        }

        return mItemIterator.next();
    }

    @Override
    public void remove() {

    }
}
