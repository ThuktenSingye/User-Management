package com.sdu.usermanagement.controller;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/admin")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class AdminController {

    @GetMapping
    public ResponseEntity<String> getAdmin() {
        return ResponseEntity.ok("Hello Admin");
    }   

}
