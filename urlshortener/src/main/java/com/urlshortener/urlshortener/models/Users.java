package com.urlshortener.urlshortener.models;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;

import java.sql.Date;

public class Users {
    @Id
    public ObjectId _id;
    public String email;
    public String password;
    @Transient
    private String passwordConfirm;

    public Users(){}

    public Users(ObjectId _id, String email, String password){
        this._id = _id;
        this.email = email;
        this.password = password;
    }
    public String get_id() { return _id.toHexString(); }
    public void set_id(ObjectId _id) { this._id = _id; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPasswordConfirm() {
        return passwordConfirm;
    }

    public void setPasswordConfirm(String passwordConfirm) {
        this.passwordConfirm = passwordConfirm;
    }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}

