package com.keylo.dto;

public class JwtResponse {
    private String token;
    private String type="Bearer";
    private String email;
    private Long userId;

    public JwtResponse(String token, Long userId, String email){
        this.token = token;
        this.userId = userId;
        this.email = email;
    }

    //! getters

    public String getToken(){
        return token;
    }
    public Long getUserId(){
        return userId;
    }
    public String getType() {
        return type;
    }
    public String getEmail() {
        return email;
    }
    //! setters

    public void setToken(String token){
        this.token = token;
    }
    
    public void setEmail(String email){
        this.email = email;
    }
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    public void setType(String type) {
        this.type = type;
    }

}


