package com.example.atipera;

import com.example.atipera.Models.BranchInfo;
import com.example.atipera.Models.RepositoryInfo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AtiperaApplicationTests {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void testGetUserRepos_HappyPath() {

        String username = "DucMajek";
        String url = "http://localhost:" + port + "/api/github/users/" + username + "/repos";

        ResponseEntity<RepositoryInfo[]> response = restTemplate.getForEntity(url, RepositoryInfo[].class);

        assertThat(response.getStatusCode(), is(HttpStatus.OK));

        RepositoryInfo[] repos = response.getBody();
        assertThat(repos, is(notNullValue()));
        assertThat(repos.length, is(greaterThan(0)));

        for (RepositoryInfo repo : repos) {
            assertThat(repo.getName(), is(notNullValue()));
            assertThat(repo.getOwner(), is(notNullValue()));
            assertThat(repo.getOwner().getLogin(), is(notNullValue()));
            assertThat(repo.getBranches(), is(notNullValue()));


            for (BranchInfo branch : repo.getBranches()) {
                assertThat(branch.getName(), is(notNullValue()));
                assertThat(branch.getCommit(), is(notNullValue()));
                assertThat(branch.getCommit().getSha(), is(notNullValue()));
            }
        }
    }
}