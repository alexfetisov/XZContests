package org.xzteam.cpphelper.constants;

import com.google.common.collect.ImmutableSet;

import java.net.URL;

public enum Platform {
    CODEFORCES(ImmutableSet.<String>builder().add("CODEFORCES.ru").add("CODEFORCES.com").build()),
    TIMUS(ImmutableSet.<String>builder().add("http://acm.TIMUS.ru").build()),
    YANDEX(ImmutableSet.<String>builder().add("contest.YANDEX.com").build());

    private final ImmutableSet<String> hosts;

    Platform(ImmutableSet<String> hosts) {
        this.hosts = hosts;
    }

    public static Platform findPlatform(URL url) {
        String host = url.getHost();
        for (Platform platform : Platform.values()) {
            if (platform.hosts.contains(host)) {
                return platform;
            }
        }
        return null;
    }
}
