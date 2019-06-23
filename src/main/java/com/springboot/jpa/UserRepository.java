package com.springboot.jpa;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


public interface UserRepository extends CrudRepository<User, Long> {
// <class which has to be an Entiry, and second parameter is ID field type>
// class to safe, retrieve user details from database

    // search who has a role of Admin.
    // findBy{Column_name} -- Spring data JPA feature
    List<User> findByRoleIgnoreCase(String role); // spring data will provide implementation which will get details by role
    //будет искать по полю(названию колонки) в базе данных
}
