package com.faskan.todo;

import io.quarkus.test.junit.QuarkusTest;
import org.json.JSONException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.testcontainers.containers.GenericContainer;

import javax.inject.Inject;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

@QuarkusTest
public class TodoResourceIT {

    @Inject
    TodoRepository todoRepository;

    static GenericContainer mongodb = new GenericContainer<>("mongo:4.2").withExposedPorts(27017);
    static {
        mongodb.start();
        System.setProperty("quarkus.mongodb.connection-string",
                "mongodb://" + mongodb.getContainerIpAddress() + ":" + mongodb.getFirstMappedPort());
    }

    @AfterEach
    void deleteAll() {
        todoRepository.deleteAll();
    }

    @Test
    void shouldReturnAllTodos() throws JSONException {
        todoRepository.persist(new Todo("Find", "Find the letter F"));
        String response = given()
                .when().get("/api/todos")
                .then()
                .statusCode(200)
                .extract().response().asString();
        JSONAssert.assertEquals("[{\n" +
                "                    \"name\" : \"Find\",\n" +
                "                    \"description\" : \"Find the letter F\"\n" +
                "                }]", response, JSONCompareMode.LENIENT);
    }

    @Test
    void shouldReturnTodoById() throws JSONException {
        Todo todo = new Todo("Find", "Find the letter F");
        todoRepository.persist(todo);
        String response = given()
                .when().get("/api/todos/{id}", Map.of("id", todo.id.toString()))
                .then()
                .statusCode(200)
                .extract().response().asString();
        JSONAssert.assertEquals("{\n" +
                "                    \"name\" : \"Find\",\n" +
                "                    \"description\" : \"Find the letter F\"\n" +
                "                }", response, JSONCompareMode.LENIENT);
    }

    @Test
    void shouldSaveTodo() throws JSONException {
        Todo todo = given()
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .body(new Todo("Find", "Find the letter F"))
                .when().post("/api/todos")
                .then()
                .statusCode(200)
                .extract()
                .as(Todo.class);
        assertThat(todo.id).isNotNull();
        assertThat(todo.name).isEqualTo("Find");
        assertThat(todo.description).isEqualTo("Find the letter F");
    }

    @Test
    void shouldUpdateTodo() throws JSONException {
        Todo todo = new Todo("Find", "Find the letter F");
        todoRepository.persist(todo);
        //update
        given()
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .body("{\n" +
                        "                            \"name\" : \"Replace\",\n" +
                        "                            \"description\" : \"Replace by K\"\n" +
                        "                        }")
                .when().put("/api/todos/{id}", Map.of("id", todo.id.toString()))
                .then()
                .statusCode(200);
        //get
        String response = given()
                .when().get("/api/todos/{id}", Map.of("id", todo.id.toString()))
                .then()
                .statusCode(200)
                .extract().response().asString();
        JSONAssert.assertEquals("{\n" +
                "                    \"name\" : \"Replace\",\n" +
                "                    \"description\" : \"Replace by K\"\n" +
                "                }", response, JSONCompareMode.LENIENT);
    }

    @Test
    void shouldDeleteTodo() throws JSONException {
        Todo todo = new Todo("Find", "Find the letter F");
        todoRepository.persist(todo);
        //delete
        given()
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .when().delete("/api/todos/{id}", Map.of("id", todo.id.toString()))
                .then()
                .statusCode(204);
        //get
        given()
                .when().get("/api/todos/{id}", Map.of("id", todo.id.toString()))
                .then()
                .statusCode(404);
    }
}
