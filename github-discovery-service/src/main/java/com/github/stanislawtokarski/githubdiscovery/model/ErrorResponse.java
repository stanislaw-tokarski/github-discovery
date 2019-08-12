package com.github.stanislawtokarski.githubdiscovery.model;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ErrorResponse {
    private final int errorCode;
    private final String errorMessage;
}
