package com.github.stanislawtokarski.githubdiscovery.controller;

import com.github.stanislawtokarski.githubdiscovery.model.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.bitbucket.stanislawtokarski.githubdiscovery.exception.GithubApiException;
import org.bitbucket.stanislawtokarski.githubdiscovery.exception.InternalServerErrorException;
import org.bitbucket.stanislawtokarski.githubdiscovery.model.ErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.net.SocketTimeoutException;

import static org.springframework.http.HttpStatus.GATEWAY_TIMEOUT;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@ControllerAdvice
@Slf4j
public class GithubDiscoveryExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(GithubApiException.class)
    public ResponseEntity<ErrorResponse> handleGithubApiException(GithubApiException e) {
        int errorCode = e.getErrorCode();
        final String errorMessage = "Request to GitHub API has failed with error code " + errorCode;
        log.error(errorMessage);
        return ResponseEntity.status(errorCode)
                .body(ErrorResponse.builder()
                        .errorCode(errorCode)
                        .errorMessage(errorMessage)
                        .build());
    }

    @ExceptionHandler({SocketTimeoutException.class})
    public ResponseEntity<ErrorResponse> handleSocketTimeout() {
        final String errorMessage = "Connection from GitHub Discovery Service to GitHub API has timed out";
        log.error(errorMessage);
        final int timeoutErrorCode = GATEWAY_TIMEOUT.value();
        return ResponseEntity.status(timeoutErrorCode)
                .body(ErrorResponse.builder()
                        .errorCode(timeoutErrorCode)
                        .errorMessage(errorMessage)
                        .build());
    }

    @ExceptionHandler({InternalServerErrorException.class})
    public ResponseEntity<ErrorResponse> handleInternalServerError(InternalServerErrorException e) {
        final int internalServerErrorCode = INTERNAL_SERVER_ERROR.value();
        final String errorMessage = e.getMessage();
        log.error(errorMessage);
        return ResponseEntity.status(internalServerErrorCode)
                .body(ErrorResponse.builder()
                        .errorCode(internalServerErrorCode)
                        .errorMessage(errorMessage)
                        .build());
    }
}
