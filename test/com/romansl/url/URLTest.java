package com.romansl.url;

import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @Test
    public void testParamIterator() {
        final List<String> array = Arrays.asList("31", "32");
        final Iterable<Map.Entry<String, String>> params = google
                .withParam("a", 1)
                .withParam("b", 2)
                .withParam("c", array)
                .withParam("d", 4)
                .withParam("e", array)
                .getParams();
        assertEquals("d4e31e32b2c31c32a1", toString(params));
    }

    private static String toString(final Iterable<Map.Entry<String, String>> params) {
        final StringBuilder builder = new StringBuilder();
        for (final Map.Entry<String, String> entry : params) {
            builder.append(entry.getKey()).append(entry.getValue());
        }
        return builder.toString();
    }

    @Test
    public void testParse() throws IOException {
        final String expected = "http://google.com:1234/100%25/%D0%B0+%D0%B1?b=%D0%B0%D0%B1&c=&a=foo";
        assertEquals(expected, URL.parse(expected).toString());
    }
}
