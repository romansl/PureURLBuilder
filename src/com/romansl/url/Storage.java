package com.romansl.url;

import java.util.HashMap;

class Storage extends HashMap<String, URL> {
    URL mScheme;
    URL mHost;
    URL mPort;
    URL mPath;
    URL mFragment;
}
