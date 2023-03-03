package org.openchat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openchat.domain.users.User;
import org.openchat.domain.users.UserCredentials;
import org.openchat.domain.users.UserRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.openchat.infrastructure.builders.UserBuilder.aUser;

public class UserRepositoryShould {

    private static final User ALICE = aUser().withUsername("Alice").build();
    private static final User CHARLIE = aUser().withUsername("Charlie").build();
    private static final UserCredentials ALICE_CREDENTIALS = new UserCredentials(ALICE.getUsername(), ALICE.getPassword());
    private static final UserCredentials CHARLIE_CREDENTIALS = new UserCredentials(CHARLIE.getUsername(), CHARLIE.getPassword());
    private static final UserCredentials UNKNOWN_CREDENTIALS = new UserCredentials("Unknown", "Unknown");
    private UserRepository userRepository;

    @BeforeEach
    public void initialise() {
        userRepository = new UserRepository();
    }

    @Test
    public void informWhenAUsernameIsAlreadyTaken() {
        userRepository.add(ALICE);

        assertTrue(userRepository.isUsernameTaken(ALICE.getUsername()));
        assertFalse(userRepository.isUsernameTaken(CHARLIE.getUsername()));
    }
    
    @Test
    public void returnUserMatchingValidCredentials() {
        userRepository.add(ALICE);
        userRepository.add(CHARLIE);
        Optional<User> userForAliceCredentials = userRepository.userFor(ALICE_CREDENTIALS);
        Optional<User> userForCharlieCredentials = userRepository.userFor(CHARLIE_CREDENTIALS);
        Optional<User> userForUnknownCredentials = userRepository.userFor(UNKNOWN_CREDENTIALS);

        /*
            We could use assertJ since jUnit does not have an assertThat function
            assertThat(userRepository.userFor(ALICE_CREDENTIALS)).contains(ALICE)

            But another way of doing this without adding that extra import is:
         */

        assertTrue(userForAliceCredentials.equals(Optional.of(ALICE)));
        assertTrue(userForCharlieCredentials.equals(Optional.of(CHARLIE)));
        assertTrue(userForUnknownCredentials.isEmpty());

        /*
            For checking equality for an Optional, you can also use the next example,
            which may be easier to understand at first sight, both methods I've taken from
            https://stackoverflow.com/a/37005067/17089665:
        */

        // assertTrue(userForAliceCredentials.isPresent() && userForAliceCredentials.get().equals(ALICE));
    }
}