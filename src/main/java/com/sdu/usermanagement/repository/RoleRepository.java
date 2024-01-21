package com.sdu.usermanagement.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sdu.usermanagement.model.Role;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    
    Optional<Role> findByRoleName(String roleName);

}
