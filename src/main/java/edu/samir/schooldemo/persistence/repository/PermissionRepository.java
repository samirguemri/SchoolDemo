package edu.samir.schooldemo.persistence.repository;

import edu.samir.schooldemo.persistence.entity.UserPermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PermissionRepository extends JpaRepository<UserPermission, Long> {
}
