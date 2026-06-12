package com.journalize.journalize.repositories;

import java.util.List;

import com.journalize.journalize.entities.User;

interface UserQueriesRepository {

    List<User> getUsersForSA();
}