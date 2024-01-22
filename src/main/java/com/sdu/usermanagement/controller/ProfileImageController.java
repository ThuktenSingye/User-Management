package com.sdu.usermanagement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.sdu.usermanagement.service.ProfileImageServie;

@RestController
@RequestMapping("/profile_images")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
// @CrossOrigin(origins = "https://sdu-ums-internship.netlify.app/")
public class ProfileImageController {
    
    @Autowired
    private ProfileImageServie profileImageServie;

    @PostMapping("/{user_id}")
    private ResponseEntity<String> uploadProfile(
        @RequestPart(name = "profileImageFile", required = true) MultipartFile profileImageFile,@PathVariable Integer user_id){
        return profileImageServie.uploadImage(Integer.valueOf(user_id), profileImageFile);
    }

    @GetMapping("/{user_id}")
    private ResponseEntity<byte[]> getProfileImage(@PathVariable Integer user_id){
        return profileImageServie.getProfileImage(user_id);
    }
}
