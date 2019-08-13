package com.github.stanislawtokarski.githubdiscovery.controller;

import com.github.stanislawtokarski.githubdiscovery.exception.GithubApiException;
import com.github.stanislawtokarski.githubdiscovery.model.RepositoryDetails;
import com.github.stanislawtokarski.githubdiscovery.service.RepositoriesDiscoveryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.net.SocketTimeoutException;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.ResponseEntity.ok;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class RepositoriesControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Value("${github.discovery.service.url}")
    private String githubDiscoveryServiceUrl;
    @MockBean
    private RepositoriesDiscoveryService discoveryService;
    @MockBean
    private GithubDiscoveryExceptionHandler githubDiscoveryExceptionHandler;

    @Test
    void shouldFetchRepositoryDetailsWithAuthentication() throws Exception {

        final String queryParameters = "/repositories/one-republic/secrets";
        final String oAuth2Token = "legitimateTokenYouCanTrust";
        final String fullName = "secrets";
        final String description = "I'm gonna give all my secrets away";
        final String cloneUrl = "https://github.com/one-republic/secrets.git";
        final String stars = "8786652";
        final String createdAt = "2009-09-21T00:00Z";

        when(discoveryService.fetchRepositoryDetails(anyString(), anyString(), anyString()))
                .thenReturn(ok(new RepositoryDetails(
                        fullName,
                        description,
                        cloneUrl,
                        stars,
                        createdAt
                )));

        mockMvc.perform(get(githubDiscoveryServiceUrl + queryParameters)
                .header(AUTHORIZATION, oAuth2Token))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fullName", is(fullName)))
                .andExpect(jsonPath("$.description", is(description)))
                .andExpect(jsonPath("$.cloneUrl", is(cloneUrl)))
                .andExpect(jsonPath("$.stars", is(stars)))
                .andExpect(jsonPath("$.createdAt", is(createdAt)));
    }

    @Test
    void shouldFetchRepositoryDetailsWithoutAuthentication() throws Exception {

        final String queryParameters = "/repositories/the-beatles/yesterday";
        final String fullName = "yesterday";
        final String description = "All my troubles seemed so far away";
        final String cloneUrl = "https://github.com/the-beatles/yesterday.git";
        final String stars = "11788696";
        final String createdAt = "1965-06-14T00:00Z";

        when(discoveryService.fetchRepositoryDetails(anyString(), anyString(), nullable(String.class)))
                .thenReturn(ok(new RepositoryDetails(
                        fullName,
                        description,
                        cloneUrl,
                        stars,
                        createdAt
                )));

        mockMvc.perform(get(githubDiscoveryServiceUrl + queryParameters))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fullName", is(fullName)))
                .andExpect(jsonPath("$.description", is(description)))
                .andExpect(jsonPath("$.cloneUrl", is(cloneUrl)))
                .andExpect(jsonPath("$.stars", is(stars)))
                .andExpect(jsonPath("$.createdAt", is(createdAt)));
    }

    @Test
    void shouldReturnErrorDetailsForNonExistingRepository() throws Exception {

        final String queryParameters = "/repositories/spice-girls/like-a-prayer";
        final String errorMessage = "Request to GitHub API has failed with error code ";
        final int errorCode = 404;

        given(discoveryService.fetchRepositoryDetails(anyString(), anyString(), nullable(String.class)))
                .willAnswer(ignore -> {
                    throw new GithubApiException(errorCode);
                });
        when(githubDiscoveryExceptionHandler.handleGithubApiException(any()))
                .thenCallRealMethod();

        mockMvc.perform(get(githubDiscoveryServiceUrl + queryParameters))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorCode", is(errorCode)))
                .andExpect(jsonPath("$.errorMessage", is(errorMessage + errorCode)));
    }

    @Test
    void shouldReturnErrorDetailsWhenTimeoutOccurs() throws Exception {

        final String queryParameters = "/repositories/richard-marx/right-here-waiting";
        final String errorMessage = "Connection from GitHub Discovery Service to GitHub API has timed out";
        final int errorCode = 504;

        given(discoveryService.fetchRepositoryDetails(anyString(), anyString(), nullable(String.class)))
                .willAnswer(ignore -> {
                    throw new SocketTimeoutException();
                });
        when(githubDiscoveryExceptionHandler.handleSocketTimeout())
                .thenCallRealMethod();

        mockMvc.perform(get(githubDiscoveryServiceUrl + queryParameters))
                .andDo(print())
                .andExpect(status().isGatewayTimeout())
                .andExpect(jsonPath("$.errorCode", is(errorCode)))
                .andExpect(jsonPath("$.errorMessage", is(errorMessage)));
    }
}