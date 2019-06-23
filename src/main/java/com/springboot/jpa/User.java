package com.springboot.jpa;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
//now this can be mapped to a DATABASE table
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    // this will autogenerate and an ID and will keep it growing automatically
    private Long id;

    private String name;// Not perfect!! Should be a proper object!
    private String role;// Not perfect!! An enum should be a better choice!

    private User() {
        //JPA needs and empty private Constructor
    }

    public User(String name, String role) {
        this.name = name;
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getRole() {
        return role;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}

