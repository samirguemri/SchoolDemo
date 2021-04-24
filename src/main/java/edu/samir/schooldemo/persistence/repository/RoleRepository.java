package edu.samir.schooldemo.persistence.repository;

import edu.samir.schooldemo.persistence.entity.Role;
import edu.samir.schooldemo.persistence.entity.enums.RoleEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

}
