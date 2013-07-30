package com.romansl.url;

import org.apache.http.NameValuePair;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.regex.Pattern;

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

    public URL withParam(final String name, final CharSequence value) {
        return new Param(this, name, value.toString());
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

    public Iterable<Map.Entry<String, String>> getParams() {
        return new Iterable<Map.Entry<String, String>>() {
            @Override
            public Iterator<Map.Entry<String, String>> iterator() {
                final Storage storage = new Storage();

                URL item = URL.this;
                while (item != null) {
                    item.store(storage);
                    item = item.mNext;
                }

                final Iterator<Map.Entry<String, URL>> iterator = storage.entrySet().iterator();
                return storage.hasArrayParam ? new ParamIterator(iterator) : new SimpleParamIterator(iterator);
            }
        };
    }

    public static URL parse(final String src) throws IOException {
        final java.net.URL url = new java.net.URL(src);
        URL result = URL.empty().withScheme(url.getProtocol());

        final String host = url.getHost();
        if (host != null && host.length() != 0) {
            result = result.withHost(host);
        }

        final int port = url.getPort();
        if (port >= 0) {
            result = result.withPort(port);
        }

        final String path = url.getPath();
        if (path != null && path.length() != 0) {
            result = result.withPath(parsePath(path));
        }

        final String query = url.getQuery();
        if (query != null && query.length() != 0) {
            result = parseQuery(result, query);
        }

        return result;
    }

    private static URL parseQuery(URL result, final String query) throws UnsupportedEncodingException {
        final Pattern pattern = Pattern.compile("=");
        for (final String part : query.split("&")) {
            final String[] kvp = pattern.split(part, 2);
            final String key = URLDecoder.decode(kvp[0], "UTF-8");
            final String value = kvp.length == 2 ? URLDecoder.decode(kvp[1], "UTF-8") : null;
            result = result.withParam(key, value);
        }

        return result;
    }

    private static String parsePath(final String path) throws UnsupportedEncodingException {
        final StringBuilder out = new StringBuilder(path.length());

        final StringTokenizer st = new StringTokenizer(path, "/", true);
        while (st.hasMoreElements()) {
            final String element = st.nextToken();
            if ("/".equals(element)) {
                out.append('/');
            } else if (element.length() != 0) {
                out.append(URLDecoder.decode(element, "UTF-8"));
            }
        }

        return out.toString();
    }
}
