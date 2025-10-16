package com.example.achstudapp.models;

public class RegisterRequest {
    private String firstname;
    private String lastname;
    private String surname;
    private String email;
    private String password;
    private String role;
    private String college;

    public RegisterRequest(String firstname, String lastname, String surname,
                           String email, String password, String role, String college) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.surname = surname;
        this.email = email;
        this.password = password;
        this.role = role;
        this.college = college;
    }

    // Геттеры и сеттеры (если нужно)
}
