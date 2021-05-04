package edu.samir.schooldemo.persistence.entity.enums;

public enum Permission {

    STUDENT_READ("student:read"),
    STUDENT_WRITE("student:write"),
    ADMIN_READ("admin:read"),
    ADMIN_WRITE("admin:write"),
    COURSE_READ("course:read"),
    COURSE_WRITE("course:write");

    private String  permission;

    Permission(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return this.permission;
    }
}
