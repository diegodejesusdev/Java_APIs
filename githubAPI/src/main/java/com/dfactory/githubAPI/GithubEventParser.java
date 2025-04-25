package com.dfactory.githubAPI;

import java.util.List;
import java.util.Map;

import static org.springframework.util.StringUtils.capitalize;

public class GithubEventParser {

    @SuppressWarnings("unchecked")
    public static String parseEvent(Map<?, ?> event){
        String type = (String) event.get("type");

        Map<String, Object> repo = (Map<String, Object>) event.get("repo");
        String repoName = repo != null ? (String) repo.get("name") : "Unknown repository";

        Map<String, Object> payload = (Map<String, Object>) event.get("payload");

        switch (type) {
            case "PushEvent":
                List<Object> commits = (List<Object>) payload.get("commits");
                int commitCount = commits != null ? commits.size() : 0;
                return "Pushed " +commitCount+ " commit(s) to " + repoName;

            case "IssuesEvent":
                String action = (String) payload.get("action");
                return capitalize(action) + " an issue in " + repoName;

            case "WatchEvent":
                return "Starred " + repoName;

            case "CreateEvent":
                String refType = (String) payload.get("ref_type");
                return "Created a new " + refType + " in " + repoName;

            default:
                return type + " event in " + repoName;
        }
    }
}
