package com.github.stanislawtokarski.githubdiscovery.exception;

import lombok.Getter;

//Can be easily used in the future when new functional requirements are added
public class InternalServerErrorException extends Exception {
    @Getter
    private final String message;

    public InternalServerErrorException(String message) {
        this.message = message;
    }
}
