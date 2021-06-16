package com.faskan.todo;

import io.quarkus.mongodb.panache.PanacheMongoRepository;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class TodoRepository implements PanacheMongoRepository<Todo> {
}
