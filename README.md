## Functionalities

### 1. Fetching User Repositories

- **Endpoint**: `GET /api/github/users/{username}/repos`
- **Description**: Returns a list of user repositories that are not forks.
- **Data returned for each repository**:
    - **Repository name**: The name of the repository.
    - **Owner**: The login of the repository owner.
    - **Branches**: A list of branches with information about the last commit (SHA).

#### Example response:
```json
[
    {
        "name": "repo1",
        "owner": {
            "login": "DucMajek"
        },
        "branches": [
            {
                "name": "main",
                "commit": {
                    "sha": "abc123"
                }
            },
            {
                "name": "feature-branch",
                "commit": {
                    "sha": "def111"
                }
            }
        ]
    }
]
```

---

### 2. Error Handling

- **404 Not Found Error**: If the user does not exist, a `404 Not Found` code is returned with the message:
  ```json
  {
    "status": 404,
    "message": "User not found."
  }
  ```

---

### 3. Usage Examples

#### a) Fetching repositories of an existing user:
```bash
GET /api/github/users/DucMajek/repos
```

#### b) Fetching repositories of a non-existing user:
```bash
GET /api/github/users/nonExistingUser/repos
```

---

## File Structure

The project is organized as follows:

```
src/main/java/com/example/atipera
├── controller
│   └── GitHubController.java
├── service
│   └── GitHubService.java
├── model
│   ├── BranchInfo.java
│   ├── RepositoryInfo.java
│   └── ErrorResponse.java
├── exception
│   └── UserNotFoundException.java
└── config
    └── RestTemplateConfig.java
```

### Class Descriptions

#### 1. **GitHubController**
- **Location**: `controller/GitHubController.java`
- **Description**: REST controller that handles endpoints.
- **Functions**:
    - Maps HTTP requests to service methods.
    - Returns responses in JSON format.

#### 2. **GitHubService**
- **Location**: `service/GitHubService.java`
- **Description**: Service responsible for communicating with the GitHub API and processing data.
- **Functions**:
    - Fetches the list of user repositories.
    - Filters out forks.
    - Fetches branches for each repository.

#### 3. **RepositoryInfo**
- **Location**: `model/RepositoryInfo.java`
- **Description**: Model representing a repository.
- **Fields**:
    - `name`: The name of the repository.
    - `owner`: The owner of the repository (an object of type `Owner`).
    - `branches`: A list of branches (objects of type `BranchInfo`).

#### 4. **BranchInfo**
- **Location**: `model/BranchInfo.java`
- **Description**: Model representing a repository branch.
- **Fields**:
    - `name`: The name of the branch.
    - `commit`: Information about the last commit (an object of type `Commit`).

#### 5. **ErrorResponse**
- **Location**: `model/ErrorResponse.java`
- **Description**: Model for error responses.
- **Fields**:
    - `status`: The error code (e.g., 404).
    - `message`: The error message (e.g., "User not found").

#### 6. **UserNotFoundException**
- **Location**: `exception/UserNotFoundException.java`
- **Description**: Exception thrown when the user does not exist.
- **Functions**:
    - Stores the error message.
    - Returns an `ErrorResponse` object with a `404` code.

#### 7. **RestTemplateConfig**
- **Location**: `config/RestTemplateConfig.java`
- **Description**: Spring bean configuration.
- **Functions**:
    - Defines the `RestTemplate` bean, which is used for communication with the GitHub API.