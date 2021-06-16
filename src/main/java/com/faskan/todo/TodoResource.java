package com.faskan.todo;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/api/todos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TodoResource {

    private final TodoRepository todoRepository;

    public TodoResource(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    @GET
    public List<Todo> getAllTodos() {
        return todoRepository.listAll();
    }

    @POST
    public Todo saveTodo(Todo todo) {
        todoRepository.persist(todo);
        return todo;
    }
}

