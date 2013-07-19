package com.romansl.url;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.StringTokenizer;

public abstract class URL {
    protected abstract URL next();
    protected abstract void store(Storage storage);
    protected abstract void format(final Appendable out) throws IOException;

    public static URL empty() {
        return new URL() {
            @Override
            protected void store(final Storage storage) {

            }

            @Override
            protected void format(final Appendable out) {

            }

            @Override
            protected URL next() {
                return null;
            }
        };
    }

    public URL withHost(final String name) {
        return new URL() {
            @Override
            public void store(final Storage storage) {
                if (storage.mHost == null) {
                    storage.mHost = this;
                }
            }

            @Override
            protected void format(final Appendable out) throws IOException {
                out.append(name);
            }

            @Override
            protected URL next() {
                return URL.this;
            }
        };
    }

    public URL withPath(final String name) {
        return new URL() {
            @Override
            public void store(final Storage storage) {
                if (storage.mPath == null) {
                    storage.mPath = this;
                }
            }

            @Override
            protected void format(final Appendable out) throws IOException {
                if (name == null)
                    return;

                if (name.charAt(0) != '/') {
                    out.append('/');
                }

                final StringTokenizer st = new StringTokenizer(name, "/", true);
                while (st.hasMoreElements()) {
                    final String element = st.nextToken();
                    if ("/".equals(element)) {
                        out.append(element);
                    } else if (!element.isEmpty()) {
                        out.append(URLEncoder.encode(element, "UTF-8"));
                    }
                }
            }

            @Override
            protected URL next() {
                return URL.this;
            }
        };
    }

    public URL withPath(final String[] parts) {
        return new URL() {
            @Override
            protected URL next() {
                return URL.this;
            }

            @Override
            protected void store(final Storage storage) {
                if (storage.mPath == null) {
                    storage.mPath = this;
                }
            }

            @Override
            protected void format(final Appendable out) throws IOException {
                if (parts == null || parts.length == 0)
                    return;

                for (final String part : parts) {
                    out.append('/');
                    out.append(URLEncoder.encode(part, "UTF-8"));
                }
            }
        };
    }

    public URL withPort(final int port) {
        return new URL() {
            @Override
            protected void store(final Storage storage) {
                if (storage.mPort == null) {
                    storage.mPort = this;
                }
            }

            @Override
            protected void format(final Appendable out) throws IOException {
                out.append(':');
                out.append(Integer.toString(port));
            }

            @Override
            protected URL next() {
                return URL.this;
            }
        };
    }

    public URL withScheme(final String scheme) {
        return new URL() {
            @Override
            protected void store(final Storage storage) {
                if (storage.mScheme == null) {
                    storage.mScheme = this;
                }
            }

            @Override
            protected void format(final Appendable out) throws IOException {
                out.append(scheme);
                out.append("://");
            }

            @Override
            protected URL next() {
                return URL.this;
            }
        };
    }

    public URL withFragment(final String fragment) {
        return new URL() {
            @Override
            protected void store(final Storage storage) {
                if (storage.mFragment == null) {
                    storage.mFragment = this;
                }
            }

            @Override
            protected void format(final Appendable out) throws IOException {
                out.append('#');
                out.append(fragment);
            }

            @Override
            protected URL next() {
                return URL.this;
            }
        };
    }

    public URL withParam(final String name, final String value) {
        return new URL() {
            @Override
            public void store(final Storage storage) {
                if (storage.get(name) == null) {
                    storage.put(name, this);
                }
            }

            @Override
            protected void format(final Appendable out) throws IOException {
                out.append(URLEncoder.encode(name, "UTF-8"));
                out.append('=');
                out.append(value != null ? URLEncoder.encode(value, "UTF-8") : "");
            }

            @Override
            protected URL next() {
                return URL.this;
            }
        };
    }

    public URL withParam(final String name, final Collection<String> values) {
        return withParam(name, values.toArray(new String[values.size()]));
    }

    public URL withParam(final String name, final String[] values) {
        return new URL() {
            @Override
            public void store(final Storage storage) {
                if (storage.get(name) == null) {
                    storage.put(name, this);
                }
            }

            @Override
            protected void format(final Appendable out) throws IOException {
                out.append(URLEncoder.encode(name, "UTF-8"));
                out.append('=');

                if (values.length > 0) {
                    String value = values[0];
                    out.append(value != null ? URLEncoder.encode(value, "UTF-8") : "");

                    for (int i = 1; i < values.length; ++i) {
                        value = values[i];
                        out.append('&');
                        out.append(URLEncoder.encode(name, "UTF-8"));
                        out.append('=');
                        out.append(value != null ? URLEncoder.encode(value, "UTF-8") : "");
                    }
                }
            }

            @Override
            protected URL next() {
                return URL.this;
            }
        };
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
            item = item.next();
        }

        if (storage.mScheme != null) {
            storage.mScheme.format(out);
        } else {
            out.append("http://");
        }

        if (storage.mHost != null) {
            storage.mHost.format(out);
        }

        if (storage.mPort != null) {
            storage.mPort.format(out);
        }

        final URL path = storage.mPath;
        if (path != null) {
            path.format(out);
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

    private static class Storage extends HashMap<String, URL> {
        URL mScheme;
        URL mHost;
        URL mPort;
        URL mPath;
        URL mFragment;
    }
}
