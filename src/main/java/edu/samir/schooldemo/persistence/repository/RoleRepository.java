package edu.samir.schooldemo.persistence.repository;

import edu.samir.schooldemo.persistence.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<UserRole, Long> {

}
