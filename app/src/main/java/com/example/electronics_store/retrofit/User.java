package com.example.electronics_store.retrofit;

public class User {

    private String yourName;
    private String emailAddress;
    private String phone;
    private String password;
    private String role;

    public User(String yourName, String emailAddress, String phone, String password, String role) {
        this.yourName = yourName;
        this.emailAddress = emailAddress;
        this.phone = phone;
        this.password = password;
        this.role = role;
    }

    public String getYourName() {
        return yourName;
    }

    public void setYourName(String yourName) {
        this.yourName = yourName;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
