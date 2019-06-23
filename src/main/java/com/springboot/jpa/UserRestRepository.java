package com.springboot.jpa;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource(path = "users", collectionResourceRel = "users")
// @RepositoryRestResource Repostiry + customized export mapping and relatives and Map the URL

public interface UserRestRepository extends PagingAndSortingRepository<User, Long> {
    List<User> findByRole(@Param("role") String role); // spring data will provide implementation which will get details by role
    // http://localhost:8080/users/search/findByRole?role=Admin
}
