package com.example.atipera.Controller;
import com.example.atipera.Exception.UserNotFoundException;
import com.example.atipera.Models.RepositoryInfo;
import com.example.atipera.Service.GitHubService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/github")
public class GitHubController {
    private final GitHubService gitHubService;
    public GitHubController(GitHubService gitHubService) {
        this.gitHubService = gitHubService;
    }

    @GetMapping("/users/{username}/repos")
    public ResponseEntity<?> getUserRepos(@PathVariable String username) {
        try {
            List<RepositoryInfo> repos = gitHubService.getUserRepos(username);
            return ResponseEntity.ok(repos);
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getErrorResponse());
        }
    }
}
