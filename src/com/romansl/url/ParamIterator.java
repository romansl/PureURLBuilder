package com.romansl.url;

import java.util.Iterator;

class ParamIterator implements Iterator<Param> {
    private final Iterator<BaseParam> mStorageIterator;
    private Iterator<Param> mItemIterator;

    public ParamIterator(final Iterator<BaseParam> iterator) {
        mStorageIterator = iterator;
    }

    @Override
    public boolean hasNext() {
        return (mItemIterator != null && mItemIterator.hasNext()) || mStorageIterator.hasNext();
    }

    @Override
    public Param next() {
        if (mItemIterator == null || !mItemIterator.hasNext()) {
            final BaseParam value = mStorageIterator.next();
            if (value instanceof ArrayParam) {
                mItemIterator =  ((ArrayParam) value).iterator();
            } else {
                mItemIterator = null;
                return ((Param) value);
            }
        }

        return mItemIterator.next();
    }

    @Override
    public void remove() {

    }
}
