package com.keylo.dto;

public class SignupRequest {
    private String email;
    private Long id;

//! rmbr constructor
    public SignupRequest(String email, Long id) {
        this.email = email;
        this.id = id;
    }


    public String getEmail() {
        return email;
    }

    public Long getId() {
        return id;
    }


    public void setEmail(String email) {
        this.email = email;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
