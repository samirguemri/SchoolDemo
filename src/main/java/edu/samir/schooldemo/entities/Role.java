package edu.samir.schooldemo.entities;

public enum Role {
    STUDENT("STUDENT"),
    SUPERADMIN("SUPERADMIN"),
    ADMIN("ADMIN"),
    TRAINEE("TRAINEE");

    private String roleName;

    Role(String roleName){
        this.roleName = roleName;
    }

    public String getRoleName() {
        return roleName;
    }
}
