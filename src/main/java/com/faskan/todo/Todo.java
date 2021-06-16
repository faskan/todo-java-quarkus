package com.faskan.todo;

import io.quarkus.mongodb.panache.PanacheMongoEntity;
import org.bson.types.ObjectId;

public class Todo extends PanacheMongoEntity {
    public String name;
    public String description;

    public Todo(){}

    public Todo(ObjectId id, String name, String description){
        this(name, description);
        this.id = id;
    }

    public Todo(String name, String description){
        this.name = name;
        this.description = description;
    }
}
//record Todo(String id, String name, String description) {
//}
