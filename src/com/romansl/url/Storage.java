package com.romansl.url;

class Storage extends HashSet<BaseParam> {
    URL mScheme;
    URL mHost;
    URL mPort;
    URL mPath;
    URL mFragment;
    boolean hasArrayParam;
}
