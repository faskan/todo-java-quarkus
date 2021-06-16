package com.faskan.todo;

import org.bson.types.ObjectId;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.Optional;

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

    @GET
    @Path("/{id}")
    public Todo getTodo(@PathParam("id") String id) {
        return Optional.ofNullable(todoRepository.findById(new ObjectId(id))).orElseThrow(NotFoundException::new);
    }

    @POST
    public Todo saveTodo(Todo todo) {
        todoRepository.persist(todo);
        return todo;
    }

    @PUT
    @Path("/{id}")
    public Todo updateTodo(@PathParam("id") String id, Todo todo) {
        Todo newTodo = new Todo(new ObjectId(id), todo.name, todo.description);
        todoRepository.update(newTodo);
        return newTodo;
    }
    @DELETE
    @Path("/{id}")
    public void deleteTodo(@PathParam("id") String id) {
        todoRepository.deleteById(new ObjectId(id));
    }
}

