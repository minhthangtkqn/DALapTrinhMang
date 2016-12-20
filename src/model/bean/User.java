/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.bean;

/**
 *
 * @author TLDs
 */
public class User {

    private String username, password;
    private boolean keepSignIn;

    public User(String username, String password, boolean keepSignIn) {
        this.username = username;
        this.password = password;
        this.keepSignIn = keepSignIn;
    }

    public boolean isKeepSignIn() {
        return keepSignIn;
    }

    public void setKeepSignIn(boolean keepSignIn) {
        this.keepSignIn = keepSignIn;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}