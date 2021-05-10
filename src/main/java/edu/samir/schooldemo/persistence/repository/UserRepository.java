package edu.samir.schooldemo.persistence.repository;

import edu.samir.schooldemo.persistence.entity.UserEntity;
import edu.samir.schooldemo.persistence.entity.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity,Long> {

    @Query("select distinct u from UserEntity u inner join u.userRoles r where r.role = :role")
    List<UserEntity> findByRole(Role role);

    Optional<UserEntity> findByEmail(String email);

    Optional<UserEntity> findByUsername(String username);
}
