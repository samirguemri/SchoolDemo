package edu.samir.schooldemo.entities;

public enum Permission {

    STUDENT_READ("student:read"),
    STUDENT_WRITE("student:write"),
    ADMIN_READ("admin:read"),
    ADMIN_WRITE("admin:write");

    Permission(String permission) { }
}
