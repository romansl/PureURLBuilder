package com.romansl.url;

public class Main {
    public static void main(final String[] params) {
        final URL url1 = URL.empty().withHost("google.com");
        final URL url2 = url1.withHost("microsoft.com");
        final URL url3 = url2.withPath("search");
        final URL url4 = url3.withParam("q", "foo").withParam("format", "json").withParam("q", "bar");
        final URL url5 = url3.withPath("post").withParam("message", "hello");
        final URL url6 = url1.withPort(8080).withParam("array", new String[]{"foo", "bar"}).withParam("array2", new String[]{});

        System.out.println(url1.toString());
        System.out.println(url2.toString());
        System.out.println(url3.toString());
        System.out.println(url4.toString());
        System.out.println(url5.toString());
        System.out.println(url6.toString());
        System.out.println(url1.withPath("hello world/100%/привет").toString());
        System.out.println(url1.withPath(new String[]{"a", "b", "100%", "привет"}).toString());
        System.out.println(URL.empty()
                .withScheme("https")
                .withHost("ru.wikipedia.org")
                .withPath("wiki/URL")
                .withFragment("Структура URL")
                .toString());
    }
}
