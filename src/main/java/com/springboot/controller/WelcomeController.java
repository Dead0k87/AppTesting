package com.springboot.controller;

import com.springboot.configuration.BasicConfiguration;
import com.springboot.service.WelcomeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class WelcomeController {
    // Injected here
    @Autowired
    private WelcomeService welcomeService;

    @Autowired
    private BasicConfiguration basicConfiguration;

    @Value("${welcome.message}")
    private String welcomeMessage;

    @RequestMapping(value = "/welcome", method = RequestMethod.GET)
    public String welcomePage() {
        //return welcomeService.retrieveWelcomeMessage(); // вернет этот стринг на ввеб страницу
        return welcomeMessage;
    }

    @GetMapping(value = "/dynamic-configuration")
    public Map dynamicConfiguration() {
        Map map = new HashMap();
        map.put("message", basicConfiguration.getMessage());
        map.put("number", basicConfiguration.getNumber());
        map.put("value", basicConfiguration.isValue());
        return map;
    }
}

