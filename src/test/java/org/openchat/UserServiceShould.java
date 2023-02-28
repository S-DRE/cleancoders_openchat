package org.openchat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.openchat.domain.users.*;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class UserServiceShould {

    private static final String USER_ID = UUID.randomUUID().toString();
    private static final String USERNAME = "Alice";
    private static final String PASSWORD = "23d";
    private static final String ABOUT = "About";
    private static final RegistrationData REGISTRATION_DATA =
            new RegistrationData(USERNAME, PASSWORD, ABOUT);


    // Creating the user directly because we have all the information needed
    private static final User USER = new User(USER_ID, USERNAME, PASSWORD, ABOUT);
    @Mock
    private IdGenerator idGenerator;
    private UserService userService;

    @Mock
    UserRepository userRepository;

    @BeforeEach
    public void initialise() {
        userService = new UserService(idGenerator, userRepository);
    }

    @Test
    public void createAUser() throws UsernameAlreadyInUseException {

        given(idGenerator.next()).willReturn(USER_ID);

        User result = userService.createUser(REGISTRATION_DATA);

        verify(userRepository).add(USER);
        assertEquals(USER, result);
    }
    
    @Test
    public void throwExceptionWhenAttemptingToCreateADuplicateUser() throws UsernameAlreadyInUseException {

        given(userRepository.isUsernameTaken(USERNAME)).willReturn(true);

        Exception exception = assertThrows(UsernameAlreadyInUseException.class, () -> {
            userService.createUser(REGISTRATION_DATA);
        });
    }

}
