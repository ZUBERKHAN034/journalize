package com.journalize.journalize.repositories;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.journalize.journalize.entities.User;

public interface UserRepository extends MongoRepository<User, String>, UserQueriesRepository {

    boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);
}
