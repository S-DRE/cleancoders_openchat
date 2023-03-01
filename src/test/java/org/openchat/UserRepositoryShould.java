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
}