package org.openchat;

import com.eclipsesource.json.JsonObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.openchat.api.UsersAPI;
import org.openchat.domain.users.RegistrationData;
import org.openchat.domain.users.User;
import org.openchat.domain.users.UserService;
import spark.Request;
import spark.Response;


import java.util.UUID;

import static org.assertj.core.api.FactoryBasedNavigableListAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class UsersAPIShould {

    private static final String USERNAME = "Alice";
    private static final String PASSWORD = "lñ$as#di34asd2#";
    private static final String ABOUT = "About Alice";
    private static final String USER_ID = UUID.randomUUID().toString();
    private static final RegistrationData REGISTRATION_DATA = new RegistrationData(USERNAME, PASSWORD, ABOUT);
    private static final User USER = new User(USER_ID, USERNAME, PASSWORD, ABOUT);
    @Mock
    Request request;
    @Mock
    Response response;

    @Mock
    UserService userService;

    // The class under test
    private UsersAPI usersAPI;

    @BeforeEach
    public void initialise() {
        usersAPI = new UsersAPI(userService);
    }

    @Test
    public void createANewUser() {
        given(request.body()).willReturn(jsonContaining(REGISTRATION_DATA));
        usersAPI.createUser(request, response);
        verify(userService).createUser(REGISTRATION_DATA);
    }

    @Test
    public void returnJSONRepresentingANewlyCreatedUser() {
        given(request.body()).willReturn(jsonContaining(REGISTRATION_DATA));
        given(userService.createUser(REGISTRATION_DATA)).willReturn(USER);

        String result = usersAPI.createUser(request, response);

        verify(response).status(201);
        verify(response).type("application/json");
        assertEquals(result, jsonContaining(USER));
    }

    private String jsonContaining(User user) {
        return new JsonObject()
                .add("id", user.getId())
                .add("username", user.getUsername())
                .add("about", user.getAbout())
                .toString();
    }

    private String jsonContaining(RegistrationData registrationData) {
        return new JsonObject()
                .add("username", registrationData.getUsername())
                .add("password", registrationData.getPassword())
                .add("about", registrationData.getAbout())
                .toString();
    }

}
