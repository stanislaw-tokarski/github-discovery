package com.github.stanislawtokarski.githubdiscovery.exception;

import lombok.Getter;

public class GithubApiException extends RuntimeException {
    @Getter
    private final int errorCode;

    public GithubApiException(int errorCode) {
        this.errorCode = errorCode;
    }
}
