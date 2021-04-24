package edu.samir.schooldemo.persistence.entity.enums;

public enum RoleEnum {
    STUDENT("STUDENT"),
    MANAGER("MANAGER"),
    ADMIN("ADMIN"),
    TRAINEE("TRAINEE");

    private String roleName;

    RoleEnum(String roleName){
        this.roleName = roleName;
    }

    public String getRoleName() {
        return roleName;
    }
}
