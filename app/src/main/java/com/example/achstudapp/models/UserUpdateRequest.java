package com.example.achstudapp.models;

public class UserUpdateRequest {
    private String firstname;
    private String lastname;
    private String surname;
    private String email;
    private String college;

    public UserUpdateRequest(String firstname, String lastname, String surname,
                           String email, String college) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.surname = surname;
        this.email = email;
        this.college = college;
    }
}
