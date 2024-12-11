package com.codegym.casestudymd4.model;

import jakarta.persistence.MappedSuperclass;

@MappedSuperclass
public class User {
    private String name;
    private String gender;
    private String birth;
    private String address;
    private String email;
    private String phone;
    private Long userId;

    public User(String name, String gender, String birth, String address, String email, String phone, Long userId) {
        this.name = name;
        this.gender = gender;
        this.birth = birth;
        this.address = address;
        this.email = email;
        this.phone = phone;
        this.userId = userId;
    }

    public User() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBirth() {
        return birth;
    }

    public void setBirth(String birth) {
        this.birth = birth;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
