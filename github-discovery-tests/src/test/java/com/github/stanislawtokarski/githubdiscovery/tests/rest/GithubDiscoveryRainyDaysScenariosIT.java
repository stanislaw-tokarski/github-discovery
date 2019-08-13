package com.github.stanislawtokarski.githubdiscovery.tests.rest;

import com.github.stanislawtokarski.githubdiscovery.tests.config.GithubDiscoveryContext;
import com.github.stanislawtokarski.githubdiscovery.tests.config.GithubDiscoveryContextAsParameterExtension;
import org.apache.http.HttpHeaders;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static io.restassured.RestAssured.given;
import static org.apache.http.HttpHeaders.*;
import static org.apache.http.HttpStatus.SC_NOT_FOUND;
import static org.hamcrest.Matchers.is;

@ExtendWith(GithubDiscoveryContextAsParameterExtension.class)
class GithubDiscoveryRainyDaysScenariosIT {

    private static final String APPLICATION_VND_GITHUB_V3_JSON = "application/vnd.github.v3+json";

    @Test
    void shouldNotFetchRepositoryDetailsWithoutAuthentication(GithubDiscoveryContext context){

        final String query = "/repositories/stanislaw-tokarski/evacuate.us";

        given()
                .header(ACCEPT, APPLICATION_VND_GITHUB_V3_JSON)
        .when()
                .get(context.getGithubDiscoveryServiceUrl() + query)
        .then()
                .statusCode(SC_NOT_FOUND)
                .and().body("errorCode", is(404))
                .and().body("errorMessage", is("Request to GitHub API has failed with error code 404"));
    }
}
