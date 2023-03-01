package org.openchat.api;

import org.openchat.domain.users.UserRepository;
import spark.Request;
import spark.Response;

public class LoginAPI {

    private final UserRepository userRepository;

    public LoginAPI(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public String login(Request request, Response response) {
        throw new UnsupportedOperationException();
    }
}