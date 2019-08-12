package com.github.stanislawtokarski.githubdiscovery.tests.config;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;

public class GithubDiscoveryContextAsParameterExtension implements ParameterResolver {
    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return GithubDiscoveryContext.class.equals(parameterContext.getParameter().getType());
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        String githubDiscoveryServiceUrl = System.getProperty("githubDiscoveryServiceUrl");
        String oAuthToken = System.getProperty("oAuthToken");
        return new GithubDiscoveryContext(githubDiscoveryServiceUrl, oAuthToken);
    }
}
