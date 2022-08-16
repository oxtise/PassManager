package com.oxtise;

public class Account {
    public String title; //titolo del sito/app dell'account
    public String username; //username
    public String email; //email
    public String pwd; //password

    Account(String title,String username,String email,String pwd) {
        this.title = title;
        this.username = username;
        this.email = email;
        this.pwd = pwd;
    }


}
