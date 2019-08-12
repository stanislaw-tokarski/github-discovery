package com.github.stanislawtokarski.githubdiscovery.controller;

import com.github.stanislawtokarski.githubdiscovery.model.RepositoryDetails;
import com.github.stanislawtokarski.githubdiscovery.service.RepositoriesDiscoveryService;
import com.google.common.base.Stopwatch;
import lombok.extern.slf4j.Slf4j;
import org.bitbucket.stanislawtokarski.githubdiscovery.model.RepositoryDetails;
import org.bitbucket.stanislawtokarski.githubdiscovery.service.RepositoriesDiscoveryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@RestController
@RequestMapping(value = "repositories", produces = "application/json")
@Slf4j
public class RepositoriesController {

    private final RepositoriesDiscoveryService discoveryService;

    public RepositoriesController(RepositoriesDiscoveryService discoveryService) {
        this.discoveryService = discoveryService;
    }

    @GetMapping("/{owner}/{repository-name}")
    public ResponseEntity<RepositoryDetails> getRepository(
            @PathVariable("owner") String owner,
            @PathVariable("repository-name") String repositoryName,
            @RequestHeader(value = AUTHORIZATION, required = false) String oAuthToken) {
        Stopwatch sw = Stopwatch.createStarted();
        final ResponseEntity<RepositoryDetails> repositoryDetailsResponseEntity =
                discoveryService.fetchRepositoryDetails(owner, repositoryName, oAuthToken);
        log.info("Fetching repository details for query {}/{} completed in {}", owner, repositoryName, sw.stop());
        return repositoryDetailsResponseEntity;
    }
}
