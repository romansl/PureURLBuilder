package com.romansl.url;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

public final class FinalURL extends HashSet<BaseParam> {
    URL mScheme;
    URL mHost;
    URL mPort;
    URL mPath;
    URL mFragment;
    boolean hasArrayParam;

    FinalURL() {}

    public void build(final Appendable out) throws IOException {
        if (mScheme != null) {
            mScheme.format(out);
        }

        if (mHost != null) {
            mHost.format(out);
        }

        if (mPort != null) {
            mPort.format(out);
        }

        if (mPath != null) {
            mPath.format(out);
        }

        if (!isEmpty()) {
            out.append('?');
            final Iterator<BaseParam> iterator = iterator();
            iterator.next().format(out);
            while (iterator.hasNext()) {
                out.append('&');
                iterator.next().format(out);
            }
        }

        if (mFragment != null) {
            mFragment.format(out);
        }
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

    public String getScheme() {
        return mScheme == null ? "" : mScheme.getStringContent();
    }

    public String getHost() {
        return mHost == null ? "" : mHost.getStringContent();
    }

    public int getPort() {
        return mPort == null ? 80 : mPort.getIntContent();
    }

    public String getPath() {
        return mPath == null ? "" : mPath.getStringContent();
    }

    public String getFragment() {
        return mFragment == null ? "" : mFragment.getStringContent();
    }

    public Iterable<Param> getParamsIterable() {
        return new Iterable<Param>() {
            @Override
            public Iterator<Param> iterator() {
                return hasArrayParam
                        ? new ParamIterator(FinalURL.this.iterator())
                        : FinalURL.this.<Param>iteratorType();
            }
        };
    }

    public ArrayList<Param> getParamsList() {
        final ArrayList<Param> out = new ArrayList<Param>(size());
        for (final BaseParam param : this) {
            param.store(out);
        }
        return out;
    }

    @Override
    public boolean equals(final Object object) {
        if (object == this)
            return true;

        if (object instanceof FinalURL) {
            final FinalURL rhs = (FinalURL) object;
            return getPort() == rhs.getPort()
                    && getScheme().equals(rhs.getScheme())
                    && getHost().equals(rhs.getHost())
                    && getPath().equals(rhs.getPath())
                    && getFragment().equals(rhs.getFragment())
                    && paramEquals(this, rhs);
        }

        return false;
    }

    private static boolean paramEquals(final HashSet<BaseParam> params1, final HashSet<BaseParam> params2) {
        if (params1.size() != params2.size())
            return false;

        for (final BaseParam param1 : params1) {
            final BaseParam param2 = params2.get(param1);
            if (param2 == null || !param1.equalValues(param2))
                return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = getPort();
        result += getScheme().hashCode();
        result += getHost().hashCode();
        result += getPath().hashCode();
        result += getFragment().hashCode();

        for (final BaseParam param : this) {
            result += param.getKey().hashCode();
            result += param.getValueHashCode();
        }

        return result;
    }
}
