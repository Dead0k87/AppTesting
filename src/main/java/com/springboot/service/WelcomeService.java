package com.springboot.service;


import org.springframework.stereotype.Service;

//spring to manage this bean and create an instance of it
@Service
public class WelcomeService {
    public String retrieveWelcomeMessage() {
        //business service method
        return "Good morning!1";
    }
}