package com.journalize.journalize.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.journalize.journalize.entities.Config;

@Repository
public interface ConfigRepository extends MongoRepository<Config, String> {
}