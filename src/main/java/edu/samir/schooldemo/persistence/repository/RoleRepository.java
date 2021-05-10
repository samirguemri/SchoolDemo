package edu.samir.schooldemo.persistence.repository;

import edu.samir.schooldemo.persistence.entity.UserRole;
import edu.samir.schooldemo.persistence.entity.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface RoleRepository extends JpaRepository<UserRole, Long> {
}
