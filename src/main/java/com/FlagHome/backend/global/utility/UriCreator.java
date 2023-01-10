package com.FlagHome.backend.global.utility;

import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

public class UriCreator {
    private UriCreator() { }

    public static URI createUri(String defaultUrl, long resourceId) {
        return UriComponentsBuilder
                .newInstance()
                .path(defaultUrl + "/{resource-id}")
                .buildAndExpand(resourceId)
                .toUri();
    }

    public static URI createMemberUri(String defaultUrl, String loginId) {
        return UriComponentsBuilder
                .newInstance()
                .path(defaultUrl + "/{login-id}")
                .buildAndExpand(loginId)
                .toUri();
    }
}