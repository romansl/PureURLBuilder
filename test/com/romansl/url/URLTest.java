package com.romansl.url;

import org.junit.Test;

import java.util.HashMap;

import static org.junit.Assert.assertEquals;

public class URLTest {
    final URL google = URL.http().withHost("google.com");

    @Test
    public void testHost() {
        assertEquals("http://google.com", google.toString());
        assertEquals("http://microsoft.com", google.withHost("microsoft.com").toString());
        assertEquals("http://", google.withHost(null).toString());
    }

    @Test
    public void testPath() {
        assertEquals("http://google.com/test/path", google.withPath("test/path").toString());
        assertEquals("http://google.com/test/path", google.withPath("/test/path").toString());
        assertEquals("http://google.com/", google.withPath("test/path").withPath("").toString());
        assertEquals("http://google.com", google.withPath("test/path").withPath((String) null).toString());
        assertEquals("http://google.com/100%25/%D0%B0+%D0%B1", google.withPath("100%/а б").toString());
    }

    @Test
    public void testArrayPath() {
        assertEquals("http://google.com/test/path", google.withPath(new String[]{"test", "path"}).toString());
        assertEquals("http://google.com/", google.withPath("test/path").withPath(new String[]{}).toString());
        assertEquals("http://google.com", google.withPath("test/path").withPath((String[]) null).toString());
        assertEquals("http://google.com/100%25/%D0%B0+%D0%B1", google.withPath(new String[]{"100%", "а б"}).toString());
    }

    @Test
    public void testMapParam() {
        final HashMap<String, String> map = new HashMap<String, String>();
        map.put("a", "foo");
        map.put("b", "аб");
        assertEquals("http://google.com?b=%D0%B0%D0%B1&a=foo", google.withParam(map).toString());
    }
}
