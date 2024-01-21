package com.sdu.usermanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sdu.usermanagement.model.ProfileImage;

public interface ProfileImageRepository extends JpaRepository<ProfileImage, Integer>{
    
}
