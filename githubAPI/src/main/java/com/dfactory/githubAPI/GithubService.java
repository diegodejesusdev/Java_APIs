package com.dfactory.githubAPI;

import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class GithubService {
    private final RestTemplate restTemplate = new RestTemplate();

    public void printUserActivity(String username) {
        String url = "https://api.github.com/users/" + username + "/events";

        try{
            var response = restTemplate.getForObject(url, Object[].class);

            if(response == null || response.length == 0){
                System.out.println("No recent activity found for user: " + username);
                return;
            }

            System.out.println("Recent activity for user: " + username);
            for (int i = 0; i < Math.min(10, response.length); i++) {
                var event = (Map<?, ?>) response[i];
                System.out.println("- " + GithubEventParser.parseEvent(event));
            }
        }catch (HttpClientErrorException.NotFound e) {
            System.out.println("User not found:" + username);
        }catch (Exception e){
            System.out.println("An error occurred while fetching user activity: " + e.getMessage());
        }
    }
}
