package com.romansl.url;

import java.util.Iterator;

class SimpleParamIterator implements Iterator<Param> {
    private final Iterator<BaseParam> mIterator;

    public SimpleParamIterator(final Iterator<BaseParam> iterator) {
        mIterator = iterator;
    }

    @Override
    public boolean hasNext() {
        return mIterator.hasNext();
    }

    @Override
    public Param next() {
        return ((Param) mIterator.next());
    }

    @Override
    public void remove() {
        mIterator.remove();
    }
}
