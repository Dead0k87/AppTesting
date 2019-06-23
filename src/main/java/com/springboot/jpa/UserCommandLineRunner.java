package com.springboot.jpa;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class UserCommandLineRunner implements CommandLineRunner {

    @Autowired
    private UserRepository repository;

    private static final Logger log = LoggerFactory.getLogger(UserCommandLineRunner.class);

    @Override
    public void run(String... args) throws Exception {
        repository.save(new User("", "")); // to safe USer to a database
        repository.save(new User("Ranga", "Admin"));
        repository.save(new User("Ravi", "User"));
        repository.save(new User("Satish", "Admin"));
        repository.save(new User("Raghu", "User"));

        for (User user : repository.findAll()) {
            log.info(user.toString());

        }
        log.info("Admin users are...........");
        log.info("");
        for (User user : repository.findByRoleIgnoreCase("ADMIN")
        ) {
            log.info(user.toString());
        }
    }
// this class will be invoked wit a Spring during the run


}
