package com.github.stanislawtokarski.githubdiscovery.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bitbucket.stanislawtokarski.githubdiscovery.model.RepositoryDetails;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import static org.springframework.http.HttpHeaders.ACCEPT;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpMethod.GET;

@Service
@Slf4j
@RequiredArgsConstructor
public class RepositoriesDiscoveryService {

    private static final String REPOS = "repos";
    private static final String SLASH = "/";
    private static final String TOKEN = "token ";
    private static final String HEADER_ACCEPT_VALUE = "application/vnd.github.v3+json";

    private final RestTemplate githubRestTemplate;

    public ResponseEntity<RepositoryDetails> fetchRepositoryDetails(String owner, String repositoryName, @Nullable String oAuthToken) {

        final String queryUrl = SLASH + REPOS + SLASH + owner + SLASH + repositoryName;

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set(ACCEPT, HEADER_ACCEPT_VALUE);
        if (oAuthToken != null) {
            httpHeaders.set(AUTHORIZATION, TOKEN + oAuthToken);
        } else {
            log.debug("Authorization header has not been provided in a request");
        }
        HttpEntity httpEntity = new HttpEntity(httpHeaders);

        return githubRestTemplate.exchange(queryUrl, GET, httpEntity, RepositoryDetails.class);
    }
}
