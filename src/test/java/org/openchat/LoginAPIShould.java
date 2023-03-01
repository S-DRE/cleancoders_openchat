package org.openchat;

import com.eclipsesource.json.JsonObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.openchat.api.LoginAPI;
import org.openchat.domain.users.User;
import org.openchat.domain.users.UserCredentials;
import org.openchat.domain.users.UserRepository;
import spark.Request;
import spark.Response;

import java.util.Optional;

import static java.util.Optional.empty;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.openchat.infrastructure.builders.UserBuilder.aUser;

@ExtendWith(MockitoExtension.class)
public class LoginAPIShould {

    private static final User USER = aUser().build();

    // Both username and password mean something together,
    // and that's why use them inside a constant
    // that represent what they mean.

    private static final String USERNAME = "Alice";
    private static final String PASSWORD = "as234sdf";
    private static final UserCredentials USER_CREDENTIALS = new UserCredentials(USERNAME, PASSWORD);
    @Mock
    private Request request;

    @Mock
    private Response response;

    @Mock
    private UserRepository userRepository;
    private LoginAPI loginAPI;

    @BeforeEach
    public void initialise() {
        loginAPI = new LoginAPI(userRepository);
    }


    // Sunny Day / common scenario
    @Test
    public void returnAJSONRepresentationOfAValidUser() {

        given(request.body()).willReturn(jsonContaining(USER_CREDENTIALS));

        // the userFor method might or might not return a User, that's why
        // that Optional class is used here, from java.util
        given(userRepository.userFor(USER_CREDENTIALS)).willReturn(Optional.of(USER));

        String result = loginAPI.login(request, response);

        verify(response).status(200);
        verify(response).type("application/json");
        assertEquals(jsonContaining(USER), result);
    }

    @Test
    public void returnAnErrorWhenCredentialsAreInvalid() {
        given(request.body()).willReturn(jsonContaining(USER_CREDENTIALS));
        given(userRepository.userFor(USER_CREDENTIALS)).willReturn(empty());

        String result = loginAPI.login(request, response);

        verify(response).status(404);

        assertEquals("Invalid Credentials.", result);
    }
    
    private String jsonContaining(User user) {
        return new JsonObject()
                .add("id", user.getId())
                .add("username", user.getUsername())
                .add("about", user.getAbout())
                .toString();
    }

    private String jsonContaining(UserCredentials userCredentials) {
        return new JsonObject()
                .add("username", userCredentials.getUsername())
                .add("password", userCredentials.getPassword())
                .toString();

    }

}