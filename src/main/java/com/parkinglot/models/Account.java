package com.parkinglot.models;

import com.parkinglot.enums.AccountRole;
import com.parkinglot.enums.AccountStatus;

public class Account {
    private int id;
    private String username;
    private String password;
    private String email;
    private AccountRole role;
    private AccountStatus status;

    public Account() {}

    public Account(int id, String username, String password, String email,
                   AccountRole role, AccountStatus status) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
        this.status = status;
    }

    public int getId()                  { return id; }
    public void setId(int id)           { this.id = id; }
    public String getUsername()         { return username; }
    public void setUsername(String u)   { this.username = u; }
    public String getPassword()         { return password; }
    public void setPassword(String p)   { this.password = p; }
    public String getEmail()            { return email; }
    public void setEmail(String e)      { this.email = e; }
    public AccountRole getRole()        { return role; }
    public void setRole(AccountRole r)  { this.role = r; }
    public AccountStatus getStatus()    { return status; }
    public void setStatus(AccountStatus s) { this.status = s; }

    public boolean resetPassword(String newPassword) {
        if (newPassword == null || newPassword.isEmpty()) return false;
        this.password = newPassword;
        return true;
    }

    @Override
    public String toString() {
        return username + " (" + role + ")";
    }
}
