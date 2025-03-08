package com.example.atipera.Service;

import com.example.atipera.Exception.UserNotFoundException;
import com.example.atipera.Models.BranchInfo;
import com.example.atipera.Models.RepositoryInfo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GitHubService {

    private static final String GITHUB_API_URL = "https://api.github.com";
    private final RestTemplate restTemplate;

    @Value("${github.token}")
    private String githubToken;

    public GitHubService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<RepositoryInfo> getUserRepos(String username) {
        String url = GITHUB_API_URL + "/users/" + username + "/repos";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + githubToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<RepositoryInfo[]> response = restTemplate.exchange(url, HttpMethod.GET,
                    entity, RepositoryInfo[].class);
            RepositoryInfo[] repos = response.getBody();

            if (repos == null) {
                throw new UserNotFoundException("User not found or has no repositories.");
            }

            return Arrays.stream(repos)
                    .filter(repo -> !repo.isFork())
                    .peek(repo -> {
                        String branchesUrl = GITHUB_API_URL + "/repos/" + repo.getOwner().getLogin() + "/"
                                + repo.getName() + "/branches";
                        ResponseEntity<BranchInfo[]> branchesResponse = restTemplate.exchange(branchesUrl,
                                HttpMethod.GET, entity, BranchInfo[].class);
                        repo.setBranches(Arrays.asList(branchesResponse.getBody()));
                    })
                    .collect(Collectors.toList());
        } catch (HttpClientErrorException.NotFound e) {
            throw new UserNotFoundException("User not found.");
        } catch (HttpClientErrorException.Forbidden e) {
            throw new UserNotFoundException("API rate limit exceeded. Please try again later or use a GitHub token.");
        } catch (HttpClientErrorException e) {
            throw new UserNotFoundException("GitHub API error: " + e.getStatusCode() + " - " + e.getStatusText());
        }
    }
}