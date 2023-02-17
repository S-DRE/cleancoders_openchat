package org.openchat;

import com.eclipsesource.json.JsonObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.openchat.api.UsersAPI;
import org.openchat.domain.users.RegistrationData;
import org.openchat.domain.users.UserService;
import spark.Request;
import spark.Response;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class UsersAPIShould {

    private static final String USERNAME = "Alice";
    private static final String PASSWORD = "lñ$as#di34asd2#";
    private static final String ABOUT = "About Alice";
    private static final RegistrationData REGISTRATION_DATA = new RegistrationData(USERNAME, PASSWORD, ABOUT);
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

    private String jsonContaining(RegistrationData registrationData) {
        return new JsonObject()
                .add("username", registrationData.getUsername())
                .add("password", registrationData.getPassword())
                .add("about", registrationData.getAbout())
                .toString();
    }

}
