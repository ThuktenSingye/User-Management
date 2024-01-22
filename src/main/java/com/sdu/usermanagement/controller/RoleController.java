package com.sdu.usermanagement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sdu.usermanagement.dto.RoleDTO;
import com.sdu.usermanagement.service.RoleService;

@RestController
@RequestMapping("/roles")
@Secured("ROLE_ADMIN")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @PostMapping
    public ResponseEntity<String> addRole(@RequestBody RoleDTO roleDTO) {
        return roleService.saveRole(roleDTO);
    }

    @GetMapping("/{role_id}")
    public ResponseEntity<RoleDTO> getRoleById(Integer role_id) {
        return roleService.findRoleById(role_id);
    }

    @DeleteMapping("/{role_id}")
    public ResponseEntity<String> deleteRole(Integer role_id) {
        return roleService.deleteRole(role_id);
    }

}
