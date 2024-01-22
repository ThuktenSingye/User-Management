package com.sdu.usermanagement.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sdu.usermanagement.dto.GenderDTO;
import com.sdu.usermanagement.service.GenderService;

@RestController
@RequestMapping("/genders")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
// @CrossOrigin(origins = "https://sdu-ums-internship.netlify.app/")
@Secured("ROLE_ADMIN")
public class GenderController {
// Define request and response endpoint
    @Autowired
    private GenderService genderService;

// Adding Gender
    @PostMapping
    private ResponseEntity<String> addGender(@RequestBody GenderDTO genderDTO){
        return genderService.saveGender(genderDTO);

    }

// Get All Gender
    @GetMapping
    private ResponseEntity<List<GenderDTO>> getAllGender() {
        return genderService.findAllGender();
    }

// Retrieve Single Gender By Id
    @GetMapping("/{gender_id}")
    private ResponseEntity<GenderDTO> getDepartmentById(@PathVariable Integer gender_id){
        return genderService.findGenderById(gender_id);

    }

// Updating the Gender
    @PutMapping
    private ResponseEntity<String> updateDepartment(@RequestBody GenderDTO genderDTO){
        return genderService.saveGender(genderDTO);
    }

// Deleting the Gender
    @DeleteMapping("/{gender_id}")
    private ResponseEntity<String> deleteDepartment(@PathVariable Integer gender_id){
        return genderService.deleteGender(gender_id);
    }

}
