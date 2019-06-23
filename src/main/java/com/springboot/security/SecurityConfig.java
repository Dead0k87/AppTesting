package com.springboot.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    //Users  - Roles
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication().withUser("user1").password("{noop}secret1").roles("USER").
                and().withUser("admin1").password("{noop}secret1").roles("USER", "ADMIN");

    }

    //Autorization : Roles - access
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // anything with survey in link needs to have a USER access
        http.httpBasic()
                .and().authorizeRequests()
                .antMatchers("/surveys/**").hasRole("USER")// users can access
                .antMatchers("/users/**").hasRole("USER")
                .antMatchers("/**").hasRole("ADMIN") // anything
                .anyRequest().authenticated().and().formLogin()
                .and().csrf().disable()
                .headers().frameOptions().disable();
    }
}
