package com.romansl.url;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.StringTokenizer;

class Path extends URL {
    private final String mName;

    Path(final URL url, final String name) {
        super(url);
        mName = name;
    }

    @Override
    public void store(final FinalURL storage) {
        if (storage.mPath == null) {
            storage.mPath = this;
        }
    }

    @Override
    protected void format(final Appendable out) throws IOException {
        if (mName == null)
            return;

        if (mName.length() == 0 || mName.charAt(0) != '/') {
            out.append('/');
        }

        final StringTokenizer st = new StringTokenizer(mName, "/", true);
        while (st.hasMoreElements()) {
            final String element = st.nextToken();
            if ("/".equals(element)) {
                out.append(element);
            } else if (element.length() != 0) {
                out.append(URLEncoder.encode(element, "UTF-8"));
            }
        }
    }

    @Override
    String getStringContent() {
        return mName == null ? "" : mName;
    }
}
