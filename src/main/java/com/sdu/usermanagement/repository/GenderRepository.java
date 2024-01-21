package com.sdu.usermanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sdu.usermanagement.model.Gender;

public interface GenderRepository extends JpaRepository<Gender, Integer> {
    
}
