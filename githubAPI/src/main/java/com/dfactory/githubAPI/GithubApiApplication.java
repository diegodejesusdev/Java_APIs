package com.dfactory.githubAPI;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class GithubApiApplication implements CommandLineRunner {

	private final GithubService githubService;

    public GithubApiApplication(GithubService githubService) {
        this.githubService = githubService;
    }

    public static void main(String[] args) {
		SpringApplication.run(GithubApiApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		if(args.length != 1){
			System.out.println("Usage: githubAPI <username>");
			return;
		}

		String username = args[0];
		githubService.printUserActivity(username);
	}
}
