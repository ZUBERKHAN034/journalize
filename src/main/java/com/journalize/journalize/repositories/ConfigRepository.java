package com.journalize.journalize.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.journalize.journalize.entities.Config;

public interface ConfigRepository extends MongoRepository<Config, String> {
}