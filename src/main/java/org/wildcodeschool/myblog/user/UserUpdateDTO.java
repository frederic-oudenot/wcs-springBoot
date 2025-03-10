package org.wildcodeschool.myblog.user;

import jakarta.validation.constraints.NotNull;

import java.util.Set;

public class UserUpdateDTO {
    @NotNull
    private String password;

    private Set<String> roles;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }
}
