package com.github.stanislawtokarski.githubdiscovery.tests.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import static lombok.AccessLevel.PACKAGE;

@RequiredArgsConstructor(access = PACKAGE)
@Getter
public class GithubDiscoveryContext {
    private final String githubDiscoveryServiceUrl;
    private final String oAuthToken;
}
