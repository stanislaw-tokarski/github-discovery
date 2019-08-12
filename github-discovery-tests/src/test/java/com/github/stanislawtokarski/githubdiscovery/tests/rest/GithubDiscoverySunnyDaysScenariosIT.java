package com.github.stanislawtokarski.githubdiscovery.tests.rest;

import com.github.stanislawtokarski.githubdiscovery.tests.config.GithubDiscoveryContext;
import com.github.stanislawtokarski.githubdiscovery.tests.config.GithubDiscoveryContextAsParameterExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static io.restassured.RestAssured.given;
import static org.apache.http.HttpHeaders.AUTHORIZATION;
import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.Matchers.is;

@ExtendWith(GithubDiscoveryContextAsParameterExtension.class)
class GithubDiscoverySunnyDaysScenariosIT {

    private static final String HEADER_ACCEPT_VALUE = "application/vnd.github.v3+json";

    @Test
    void shouldFetchRepositoryDetailsWithoutAuthentication(GithubDiscoveryContext context){

        final String query = "/repositories/stanislaw-tokarski/globee";

        given()
        .when()
                .get(context.getGithubDiscoveryServiceUrl() + query)
        .then()
                .statusCode(SC_OK)
                .and().body("fullName", is("stanislaw-tokarski/globee"))
                .and().body("description", is("null"))
                .and().body("cloneUrl", is("https://github.com/stanislaw-tokarski/globee.git"))
                .and().body("stars", is("0"))
                .and().body("createdAt", is("018-09-30T18:57:13Z"));
    }

    @Test
    void shouldFetchRepositoryDetailsWithAuthentication(GithubDiscoveryContext context){

        final String query = "/repositories/stanislaw-tokarski/evacuate.us";

        given()
                .header(AUTHORIZATION, context.getOAuthToken())
        .when()
                .get(context.getGithubDiscoveryServiceUrl() + query)
        .then()
                .statusCode(SC_OK)
                .and().body("fullName", is("stanislaw-tokarski/evacuate.us"))
                .and().body("description", is("Crowd modeling with artificial intelligence algorithms and agent programming"))
                .and().body("cloneUrl", is("https://github.com/stanislaw-tokarski/evacuate.us.git"))
                .and().body("stars", is("0"))
                .and().body("createdAt", is("2018-10-17T11:23:48Z"));
    }
}
