package com.faskan.todo;

import io.quarkus.test.junit.QuarkusTest;
import org.json.JSONException;
import org.junit.jupiter.api.Test;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

@QuarkusTest
public class TodoResourceIT {

    @Test
    void shouldReturnAllTodos() {
        given()
                .when().get("/api/todos")
                .then()
                .statusCode(200);
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
}
