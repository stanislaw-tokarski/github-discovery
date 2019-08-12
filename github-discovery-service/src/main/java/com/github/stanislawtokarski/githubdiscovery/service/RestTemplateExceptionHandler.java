package com.github.stanislawtokarski.githubdiscovery.service;

import com.github.stanislawtokarski.githubdiscovery.exception.GithubApiException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResponseErrorHandler;

import java.io.IOException;

@Component
@Slf4j
public class RestTemplateExceptionHandler implements ResponseErrorHandler {
    @Override
    public boolean hasError(ClientHttpResponse clientHttpResponse) throws IOException {
        HttpStatus statusCode = clientHttpResponse.getStatusCode();
        log.debug("Response from GitHub API has been received with status code {}", statusCode);
        return statusCode.isError();
    }

    @Override
    public void handleError(ClientHttpResponse clientHttpResponse) throws IOException {
        int errorCode = clientHttpResponse.getStatusCode().value();
        log.error("Request to GitHub API has failed with error code {}", errorCode);
        throw new GithubApiException(errorCode);
    }
}

