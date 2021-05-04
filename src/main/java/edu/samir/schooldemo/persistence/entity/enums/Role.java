package edu.samir.schooldemo.persistence.entity.enums;

public enum Role {
    STUDENT("STUDENT"),
    MANAGER("MANAGER"),
    ADMIN("ADMIN"),
    TEACHER("TEACHER");

    private String roleName;

    Role(String roleName){
        this.roleName = roleName;
    }

    public String getRoleName() {
        return roleName;
    }
}
