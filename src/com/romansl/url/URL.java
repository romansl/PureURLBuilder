package com.romansl.url;

import org.apache.http.NameValuePair;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

public class URL {
    private static final URL EMPTY = new URL(null);
    private static final URL HTTP = new Scheme(null, "http");
    private static final URL HTTPS = new Scheme(null, "https");

    private final URL mNext;

    protected URL(final URL next) {
        mNext = next;
    }

    protected void store(final Storage storage) {

    }

    protected void format(final Appendable out) throws IOException {

    }

    public static URL empty() {
        return EMPTY;
    }

    public static URL http() {
        return HTTP;
    }

    public static URL https() {
        return HTTPS;
    }

    public URL withHost(final String name) {
        return new Host(this, name);
    }

    public URL withPath(final String name) {
        return new Path(this, name);
    }

    public URL withPath(final String[] parts) {
        return new ArrayPath(this, parts);
    }

    public URL withPort(final int port) {
        return new Port(this, port);
    }

    public URL withScheme(final String scheme) {
        return new Scheme(this, scheme);
    }

    public URL withFragment(final String fragment) {
        return new Fragment(this, fragment);
    }

    public URL withParam(final String name, final int value) {
        return new Param(this, name, Integer.toString(value));
    }

    public URL withParam(final String name, final long value) {
        return new Param(this, name, Long.toString(value));
    }

    public URL withParam(final String name, final boolean value) {
        return new Param(this, name, Boolean.toString(value));
    }

    public URL withParam(final String name, final String value) {
        return new Param(this, name, value);
    }

    public URL withParam(final String name, final Collection<String> values) {
        return withParam(name, values.toArray(new String[values.size()]));
    }

    public URL withParam(final String name, final String[] values) {
        return new ArrayParam(this, name, values);
    }

    public URL withParam(final Collection<NameValuePair> pairList) {
        URL next = this;
        for (final NameValuePair item : pairList) {
            next = new Param(next, item.getName(), item.getValue());
        }
        return next;
    }

    public URL withParam(final Map<String, String> map) {
        URL next = this;
        for (final Map.Entry<String, String> item : map.entrySet()) {
            next = new Param(next, item.getKey(), item.getValue());
        }
        return next;
    }


    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        try {
            build(sb);
        } catch (final IOException ignored) {

        }
        return sb.toString();
    }

    public void build(final Appendable out) throws IOException {
        final Storage storage = new Storage();

        URL item = this;
        while (item != null) {
            item.store(storage);
            item = item.mNext;
        }

        if (storage.mScheme != null) {
            storage.mScheme.format(out);
        }

        if (storage.mHost != null) {
            storage.mHost.format(out);
        }

        if (storage.mPort != null) {
            storage.mPort.format(out);
        }

        if (storage.mPath != null) {
            storage.mPath.format(out);
        }

        final Collection<URL> query = storage.values();
        if (!query.isEmpty()) {
            out.append('?');
            final Iterator<URL> iterator = query.iterator();
            iterator.next().format(out);
            while (iterator.hasNext()) {
                out.append('&');
                iterator.next().format(out);
            }
        }

        if (storage.mFragment != null) {
            storage.mFragment.format(out);
        }
    }
}
