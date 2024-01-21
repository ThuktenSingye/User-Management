package com.sdu.usermanagement.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.sdu.usermanagement.dto.UserDTO;
import com.sdu.usermanagement.service.UserService;

@RestController
@RequestMapping("/users")
@CrossOrigin(origins = "http://localhost:3000")
public class UserController {

    @Autowired
    private UserService userService;

    // Adding user (accessible by ROLE_ADMIN)
    @PostMapping
    public ResponseEntity<String> addUser(
            @RequestPart(name = "user") UserDTO userDTO,
            @RequestPart(name = "profileImageFile", required = false) MultipartFile profileImageFile) {
        return userService.saveUser(userDTO, profileImageFile);
    }

    @GetMapping
    private ResponseEntity<List<UserDTO>> getAllUser() {
        return userService.findAllUser();
    }

    // Get Total User Count (accessible by ROLE_ADMIN and ROLE_USER)
    @GetMapping("/total")
    private ResponseEntity<Long> getAllUserCount() {
        return userService.getTotalUserCount();
    }

    // Get User Count By Gender (accessible by ROLE_ADMIN and ROLE_USER)

    @GetMapping("/total/{genderId}")
    private ResponseEntity<Long> getUserByGender(@PathVariable Integer genderId) {
        return userService.getUserCountByGender(genderId);
    }

    // Update User (accessible by ROLE_ADMIN)
    @PutMapping
    public ResponseEntity<String> updateUser(
            @RequestPart(name = "user", required = true) UserDTO userDTO,
            @RequestPart(value = "profileImageFile", required = false) MultipartFile profileImageFile) {
        return userService.saveUser(userDTO, profileImageFile);
    }

    // Retrieve single user (accessible by ROLE_ADMIN and ROLE_USER)
    @GetMapping("/{user_id}")
    private ResponseEntity<UserDTO> getUserById(@PathVariable Integer user_id) {
        return userService.findUserById(user_id);
    }

    // Delete User (accessible by ROLE_ADMIN)
    @DeleteMapping("/{user_id}")
    private ResponseEntity<String> deleteUser(@PathVariable Integer user_id) {
        return userService.deleteUser(user_id);
    }

    // Update User Email (accessible by ROLE_ADMIN)

    @PutMapping("/email")
    private ResponseEntity<String> updateUserEmail(@RequestBody UserDTO userDTO) {
        return userService.updateEmail(userDTO.getEmail(), userDTO.getUserId());
    }

    // Get Users By Section ID (accessible by ROLE_ADMIN and ROLE_USER)
    @GetMapping("/sections/{sect_id}")
    private ResponseEntity<List<UserDTO>> getSectionUsers(@PathVariable Integer sect_id) {
        return userService.getAllUserBySectionId(sect_id);
    }

    // Get Users By Department ID (accessible by ROLE_ADMIN and ROLE_USER)
    @GetMapping("/departments/{dept_Id}")
    private ResponseEntity<List<UserDTO>> getAllDepartmentUser(@PathVariable Integer dept_Id) {
        return userService.getAllUserByDepartmentId(dept_Id);
    }
}
