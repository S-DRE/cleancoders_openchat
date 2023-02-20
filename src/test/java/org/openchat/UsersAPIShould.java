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
import org.openchat.domain.users.UsernameAlreadyInUseException;
import spark.Request;
import spark.Response;


import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.openchat.infrastructure.builders.UserBuilder.aUser;

@ExtendWith(MockitoExtension.class)
public class UsersAPIShould {

    private static final String USERNAME = "Alice";
    private static final String PASSWORD = "lñ$as#di34asd2#";
    private static final String ABOUT = "About Alice";
    private static final String USER_ID = UUID.randomUUID().toString();
    private static final RegistrationData REGISTRATION_DATA = new RegistrationData(USERNAME, PASSWORD, ABOUT);
    private static final User USER = aUser()
                                            .withId(USER_ID)
                                            .withUsername(USERNAME)
                                            .withPassword(PASSWORD)
                                            .withAbout(ABOUT)
                                            .build();
    @Mock
    Request request;
    @Mock
    Response response;

    @Mock
    UserService userService;

    // The class under test
    private UsersAPI usersAPI;

    @BeforeEach
    public void initialise() throws UsernameAlreadyInUseException {
        usersAPI = new UsersAPI(userService);

        given(request.body()).willReturn(jsonContaining(REGISTRATION_DATA));
        given(userService.createUser(REGISTRATION_DATA)).willReturn(USER);
    }

    @Test
    public void createANewUser() throws UsernameAlreadyInUseException {
        usersAPI.createUser(request, response);

        verify(userService).createUser(REGISTRATION_DATA);
    }

    @Test
    public void returnJSONRepresentingANewlyCreatedUser() {
        String result = usersAPI.createUser(request, response);

        verify(response).status(201);
        verify(response).type("application/json");
        assertEquals(result, jsonContaining(USER));
    }
    
    @Test
    public void returnAnErrorWhenCreatingAUserWithAnExistingUsername() throws UsernameAlreadyInUseException {

        given(userService.createUser(REGISTRATION_DATA)).willThrow(UsernameAlreadyInUseException.class);

        String result = usersAPI.createUser(request, response);

        verify(response).status(400);
        assertEquals("Username already in use", result);
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
