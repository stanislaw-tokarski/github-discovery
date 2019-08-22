# GitHub Discovery
REST service for fetching details of repositories using GitHub API

## API
* **GET: http://localhost:8080/repositories/{owner}/{repository-name}**

Consumes GET requests with owner of a repository and repository name in a path. It is possible to authenticate with oAuth2 token present in "Authorization:" header and therefore be able to work with private repositories. 
Produces JSON with repository details, for example:

```json
{
	"fullName": "stanislaw-tokarski/evacuate.us",
	"description": "Crowd modeling with artificial intelligence algorithms and agent programming",
	"cloneUrl": "https://github.com/stanislaw-tokarski/evacuate.us.git",
	"stars": "0",
	"createdAt": "2018-10-17T11:23:48Z"
}
```

## Getting application up and running
Following commands need to be executed:
````
cd github-discovery-service
mvn spring-boot:run
````
Running e2e tests is possible as described in README located in github-discovery-tests module.
