package org.xzteam.cpphelper.constants;

import com.google.common.collect.ImmutableSet;

import java.net.URL;

public enum Platform {
    CODEFORCES(ImmutableSet.<String>builder().add("codeforces.ru").add("codeforces.com").build());

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
