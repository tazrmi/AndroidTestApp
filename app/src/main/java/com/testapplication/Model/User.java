package com.testapplication.Model;

/**
 * Created by tazrmi on 2/12/2017.
 */

public class User {

    private String email;
    private String password;
    private String token;

    private static User instance;

    private User() {
        email = "candidate@creitive.com";
        password = "1234567";
    }

    public static User getInstance (){
        if (null == instance){
            instance = new User();
        }
        return instance;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() { return password; }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
