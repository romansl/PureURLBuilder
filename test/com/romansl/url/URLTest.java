package com.romansl.url;

import org.junit.Assert;
import org.junit.Test;

public class URLTest {
    @Test
    public void testHost() {
        final URL google = URL.empty().withHost("google.com");
        Assert.assertEquals("http://google.com", google.toString());
        Assert.assertEquals("http://microsoft.com", google.withHost("microsoft.com").toString());
        Assert.assertEquals("http://", google.withHost(null).toString());
    }

    @Test
    public void testPath() {
        final URL google = URL.empty().withHost("google.com");
        Assert.assertEquals("http://google.com/test/path", google.withPath("test/path").toString());
        Assert.assertEquals("http://google.com/test/path", google.withPath("/test/path").toString());
        Assert.assertEquals("http://google.com/", google.withPath("test/path").withPath("").toString());
        Assert.assertEquals("http://google.com", google.withPath("test/path").withPath((String) null).toString());
        Assert.assertEquals("http://google.com/100%25/%D0%B0+%D0%B1", google.withPath("100%/а б").toString());
    }

    @Test
    public void testArrayPath() {
        final URL google = URL.empty().withHost("google.com");
        Assert.assertEquals("http://google.com/test/path", google.withPath(new String[] {"test", "path"}).toString());
        Assert.assertEquals("http://google.com/", google.withPath("test/path").withPath(new String[] {}).toString());
        Assert.assertEquals("http://google.com", google.withPath("test/path").withPath((String[]) null).toString());
        Assert.assertEquals("http://google.com/100%25/%D0%B0+%D0%B1", google.withPath(new String[] {"100%", "а б"}).toString());
    }
}
