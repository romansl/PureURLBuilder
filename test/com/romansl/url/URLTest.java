package com.romansl.url;

import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

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
    public void testParam() {
        assertEquals("http://google.com?foo=bar", google.withParam("foo", "bar").toString());
        assertEquals("http://google.com?foo=ggg", google.withParam("foo", "bar").withParam("foo", "ggg").toString());
        assertEquals("http://google.com?b=%D0%B0%D0%B1&a=12", google.withParam("a", "12").withParam("b", "аб").toString());
    }

    @Test
    public void testArrayParam() {
        final List<String> array = Arrays.asList("31", "32");
        final URL params = google
                .withParam("a", 1)
                .withParam("b", 2)
                .withParam("c", array)
                .withParam("d", 4)
                .withParam("e", array);
        assertEquals("http://google.com?e=31&e=32&c=31&c=32&b=2&a=1&d=4", params.toString());
        assertEquals("http://google.com?e=31&e=32&c=31&c=32&a=31&a=32&b=2&d=4", params.withParam("a", array).toString());
    }

    @Test
    public void testMapParam() {
        final HashMap<String, String> map = new HashMap<String, String>();
        map.put("a", "foo");
        map.put("b", "аб");
        assertEquals("http://google.com?a=foo&b=%D0%B0%D0%B1", google.withParam(map).toString());
    }

    @Test
    public void testParamIterator() {
        final List<String> array = Arrays.asList("31", "32");
        final Iterable<Param> params = google
                .withParam("a", 1)
                .withParam("b", 2)
                .withParam("c", array)
                .withParam("d", 4)
                .withParam("e", array)
                .toFinalUrl().getParams();
        assertEquals("e31e32c31c32b2a1d4", toString(params));
    }

    private static String toString(final Iterable<Param> params) {
        final StringBuilder builder = new StringBuilder();
        for (final Param entry : params) {
            builder.append(entry.getKey()).append(entry.getValue());
        }
        return builder.toString();
    }

    @Test
    public void testParse() throws IOException {
        assertEquals("http://google.com:1234/100%25/%D0%B0+%D0%B1?c=&a=foo&b=%D0%B0%D0%B1",
                URL.parse("http://google.com:1234/100%25/%D0%B0+%D0%B1?b=%D0%B0%D0%B1&c=&a=foo").toString());
    }

    @Test
    public void testEquals() {
        final URL u1 = google.withPath("abc").withParam("a", 1).withParam("b", 2);
        final URL u2 = google.withPath("abc").withParam("b", 2).withParam("a", 1);
        final FinalURL fu1 = u1.toFinalUrl();

        assertTrue(fu1.equals(u2.toFinalUrl()));
        assertFalse(fu1.equals(u2.withParam("c", 3).toFinalUrl()));
        assertFalse(fu1.equals(u2.withParam("a", 11).toFinalUrl()));
        assertFalse(fu1.equals(u2.withHost("microsoft.com").toFinalUrl()));
        assertFalse(fu1.equals(u2.withScheme("https").toFinalUrl()));
    }

    @Test
    public void testHashCode() {
        final URL u1 = google.withPath("abc").withParam("a", 1).withParam("b", 2);
        final URL u2 = google.withPath("abc").withParam("b", 2).withParam("a", 1);

        assertFalse(u1.toString().hashCode() == u2.toString().hashCode());
        assertEquals(u1.toFinalUrl().hashCode(), u2.toFinalUrl().hashCode());
    }
}
