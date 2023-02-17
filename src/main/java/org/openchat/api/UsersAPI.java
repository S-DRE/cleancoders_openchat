package org.openchat.api;

import org.openchat.domain.users.UserService;
import spark.Request;
import spark.Response;

public class UsersAPI {
    private final UserService userService;

    public UsersAPI(UserService userService) {
        this.userService = userService;
    }

    public String createUser(Request request, Response response) {
        throw new UnsupportedOperationException();
    }
}
