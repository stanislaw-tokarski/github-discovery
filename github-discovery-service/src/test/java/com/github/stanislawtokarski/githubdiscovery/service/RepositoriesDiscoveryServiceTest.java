package com.github.stanislawtokarski.githubdiscovery.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.stanislawtokarski.githubdiscovery.model.RepositoryDetails;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpHeaders.ACCEPT;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.client.ExpectedCount.once;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;

@SpringBootTest
class RepositoriesDiscoveryServiceTest {

    @Autowired
    private RepositoriesDiscoveryService discoveryService;
    @Autowired
    private RestTemplate restTemplate;
    @Value("${github.api.url}")
    private String githubApiUrl;

    private static final String APPLICATION_VND_GITHUB_V3_JSON = "application/vnd.github.v3+json";

    private MockRestServiceServer mockServer;
    private ObjectMapper mapper;

    @BeforeEach
    void init() {
        mockServer = MockRestServiceServer.createServer(restTemplate);
        mapper = new ObjectMapper();
    }

    @Test
    void shouldFetchRepositoryDataWithAuthentication() throws JsonProcessingException {

        final String queryParameters = "/repos/sting/fragile";
        final String oAuth2Token = "legitimateTokenYouCanTrust";
        final String fullName = "fragile";
        final String description = "Tomorrow's rain will wash the stains away";
        final String cloneUrl = "https://github.com/sting/fragile.git";
        final String stars = "905035749";
        final String createdAt = "1988-01-01T00:00:00Z";
        final String owner = "sting";

        mockServer.expect(
                once(),
                requestTo(githubApiUrl + queryParameters))
                .andExpect(method(GET))
                .andExpect(header(ACCEPT, APPLICATION_VND_GITHUB_V3_JSON))
                .andExpect(header(AUTHORIZATION, "token " + oAuth2Token))
                .andRespond(withStatus(OK)
                        .contentType(APPLICATION_JSON)
                        .body(mapper.writeValueAsString(new RepositoryDetails(
                                fullName,
                                description,
                                cloneUrl,
                                stars,
                                createdAt)
                        )));

        RepositoryDetails fetchedRepositoryDetails = discoveryService
                .fetchRepositoryDetails(owner, fullName, oAuth2Token)
                .getBody();

        assertThat(fetchedRepositoryDetails.getFullName()).isEqualTo(fullName);
        assertThat(fetchedRepositoryDetails.getDescription()).isEqualTo(description);
        assertThat(fetchedRepositoryDetails.getCloneUrl()).isEqualTo(cloneUrl);
        assertThat(fetchedRepositoryDetails.getStars()).isEqualTo(stars);
        assertThat(fetchedRepositoryDetails.getCreatedAt()).isEqualTo(createdAt);
    }

    @Test
    void shouldFetchRepositoryDataWithoutAuthentication() throws JsonProcessingException {

        final String queryParameters = "/repos/queen/bohemian-rhapsody";
        final String fullName = "bohemian-rhapsody";
        final String description = "Thunderbolt and Lightning, very very fright'ning me";
        final String cloneUrl = "https://github.com/queen/bohemian-rhapsody.git";
        final String stars = "1005065799";
        final String createdAt = "1975-10-31T00:00:00Z";
        final String owner = "queen";

        mockServer.expect(
                once(),
                requestTo(githubApiUrl + queryParameters))
                .andExpect(method(GET))
                .andExpect(header(ACCEPT, APPLICATION_VND_GITHUB_V3_JSON))
                .andRespond(withStatus(OK)
                        .contentType(APPLICATION_JSON)
                        .body(mapper.writeValueAsString(new RepositoryDetails(
                                fullName,
                                description,
                                cloneUrl,
                                stars,
                                createdAt)
                        )));

        RepositoryDetails fetchedRepositoryDetails = discoveryService
                .fetchRepositoryDetails(owner, fullName, null)
                .getBody();

        assertThat(fetchedRepositoryDetails.getFullName()).isEqualTo(fullName);
        assertThat(fetchedRepositoryDetails.getDescription()).isEqualTo(description);
        assertThat(fetchedRepositoryDetails.getCloneUrl()).isEqualTo(cloneUrl);
        assertThat(fetchedRepositoryDetails.getStars()).isEqualTo(stars);
        assertThat(fetchedRepositoryDetails.getCreatedAt()).isEqualTo(createdAt);
    }
}