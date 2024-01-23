package com.sdu.usermanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.sdu.usermanagement.model.UserRole;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, Integer> {

    void deleteByUserId(@Param("userId") Integer userId);
    
}
