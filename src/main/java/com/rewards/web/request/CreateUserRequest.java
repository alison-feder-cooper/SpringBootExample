package com.rewards.web.request;

import java.io.Serializable;

public class CreateUserRequest implements Serializable {

    private static final long serialVersionUID = 3096257963076226830L;

    private String email, firstName, lastName;

    //TODO clean up default constructor; in for json serialization
    public CreateUserRequest() {

    }

    public CreateUserRequest(String email, String firstName, String lastName) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }
}
