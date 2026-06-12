package com.journalize.journalize.repositories;

import java.util.List;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.journalize.journalize.entities.User;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
class UserQueriesRepositoryImpl implements UserQueriesRepository {

    private final MongoTemplate mongoTemplate;

    @Override
    public List<User> getUsersForSA() {
        Query query = new Query();
        query.addCriteria(Criteria.where("sentimentAnalysis").is(true));
        query.addCriteria(Criteria.where("email").regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,6}$"));
        return mongoTemplate.find(query, User.class);
    }
}