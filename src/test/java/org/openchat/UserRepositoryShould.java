package org.openchat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openchat.domain.users.User;
import org.openchat.domain.users.UserRepository;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.openchat.infrastructure.builders.UserBuilder.aUser;

public class UserRepositoryShould {

    private static final User ALICE = aUser().withUsername("Alice").build();
    private static final User CHARLIE = aUser().withUsername("Charlie").build();
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
        // We could use assertJ since jUnit does not have an assertThat function
        // assertThat(userRepository.userFor(ALICE_CREDENTIALS)).contains(ALICE);

        // But another way of doing this without adding that extra import is:
        assertTrue(userRepository.userFor(ALICE_CREDENTIALS).contains(ALICE));
    }
}