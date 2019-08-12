package com.github.stanislawtokarski.githubdiscovery.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class RepositoryDetails {
    private String fullName;
    private String description;
    private String cloneUrl;
    private String stars;
    private String createdAt;

    public RepositoryDetails(@JsonProperty("full_name") String fullName,
                             @JsonProperty("description") String description,
                             @JsonProperty("clone_url") String cloneUrl,
                             @JsonProperty("stargazers_count") String stars,
                             @JsonProperty("created_at") String createdAt) {
        this.fullName = fullName;
        this.description = description;
        this.cloneUrl = cloneUrl;
        this.stars = stars;
        this.createdAt = createdAt;
    }
}
