package com.xfef0.runnerz.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@RestClientTest(UserRestClient.class)
class UserRestClientIntegrationTest {

    @Autowired
    MockRestServiceServer server;

    @Autowired
    UserRestClient client;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void shouldFindAllUsers() throws JsonProcessingException {
        List<User> users = List.of(getUser());

        this.server.expect(requestTo("https://jsonplaceholder.typicode.com/users"))
                .andRespond(withSuccess(objectMapper.writeValueAsString(users), MediaType.APPLICATION_JSON));

        List<User> allUsers = client.findAll();
        assertEquals(users, allUsers);
    }

    @Test
    void shouldFindUserById() throws JsonProcessingException {
        User user = getUser();

        this.server.expect(requestTo("https://jsonplaceholder.typicode.com/users/1"))
                .andRespond(withSuccess(objectMapper.writeValueAsString(user), MediaType.APPLICATION_JSON));

        User userFound = client.findById(1);

        assertEquals(user.id(), userFound.id());
        assertEquals(user.email(), userFound.email());
        assertEquals(user.username(), userFound.username());
        assertAll("Address",
                () -> assertEquals(user.address().city(), userFound.address().city()),
                () -> assertEquals(user.address().zipcode(), userFound.address().zipcode()));
        assertEquals(user.phone(), userFound.phone());
        assertAll("Company",
                () -> assertEquals(user.company().name(), userFound.company().name()),
                () -> assertEquals(user.company().bs(), userFound.company().bs()),
                () -> assertEquals(user.company().catchPhrase(), userFound.company().catchPhrase()));
    }

    private static User getUser() {
        return new User(
                1,
                "John Doe",
                "johny",
                "johndoe@mail.com",
                new Address(
                        "Kulas Light",
                        "Apt. 4",
                        "London",
                        "456-744",
                        new Geo("-35.2222", "25.7896")),
                "154758699",
                "johndoe.com",
                new Company(
                        "BigCom",
                        "We are the best",
                        "Sing with me"
                )
        );
    }


}