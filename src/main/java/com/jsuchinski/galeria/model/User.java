package com.jsuchinski.galeria.model;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

public class User {
    int id;
    String login;
    String password;
    String email;
    Date regiesterDate;
    Role permission;
    boolean active;

    private User() {}

    public User(int id, String login, String password, String email, Date regiesterDate, Role permission, boolean active) {
        this.id = id;
        this.login = login;
        this.password = password;
        this.email = email;
        this.regiesterDate = regiesterDate;
        this.permission = permission;
        this.active = active;
    }

    public static User fromResultSet(ResultSet rs) throws SQLException {
        User u = new User();
        u.id = rs.getInt("id");
        u.login = rs.getString("login");
        u.password = rs.getString("haslo");
        u.email = rs.getString("email");
        u.regiesterDate = rs.getDate("zarejestrowany");
        u.permission = roleFromString(rs.getString("uprawnienia"));
        u.active = rs.getBoolean("aktywny");
        return u;
    }

    private static Role roleFromString(String str) {
        switch (str) {
            case "administrator":
                return Role.admin;
            case "moderator":
                return Role.mod;
            case "uzytkownik":
            default:
                return Role.user;
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getRegiesterDate() {
        return regiesterDate;
    }

    public void setRegisterDate(Date regiesterDate) {
        this.regiesterDate = regiesterDate;
    }

    public Role getPermission() {
        return permission;
    }

    public void setPermission(Role permission) {
        this.permission = permission;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isAdmin() {
        return permission == Role.admin;
    }
    public boolean isMod() {
        return permission == Role.mod;
    }
}
