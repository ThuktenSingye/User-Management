package com.sdu.usermanagement.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;


import com.sdu.usermanagement.dto.RoleDTO;
import com.sdu.usermanagement.model.Role;
import com.sdu.usermanagement.repository.RoleRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class RoleService {
    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private LogService logService;

    public ResponseEntity<RoleDTO> findRoleById(@NonNull Integer roleId) {
        try {
            /* Bad Request */
            if (roleId == null || roleId < 0) {
                /* Bad Request - Invalid user_id format */
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            RoleDTO roleDTO = roleEntityToDto(roleRepository.findById(roleId).orElseThrow());
            logService.logApplicationStatus("Role retrieved");
            return new ResponseEntity<>(roleDTO, HttpStatus.OK);
        } catch (Exception e) {
            /* Log the errrpr */
            logService.logApplicationStatus("Error: Cannot Retrieved Role "+ e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }



    public ResponseEntity<String> saveRole(RoleDTO roleDTO) {
        try {

            roleRepository.saveAndFlush(roleDtoToEntity(roleDTO));
            logService.logApplicationStatus("Saved Role");
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (Exception e) {
            logService.logApplicationStatus("Error: Saving Role "+ e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        }

    }
    
    public ResponseEntity<String> deleteRole(Integer roleId) {
        try {
            /* Bad Request */
            if (roleId == null || roleId < 0) {
                /* Bad Request - Invalid user_id format */
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            roleRepository.deleteById(roleId);
            logService.logApplicationStatus("Role deleted");
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            logService.logApplicationStatus("Error: Role not deleted" + e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
    

private RoleDTO roleEntityToDto(Role role) {
    RoleDTO roleDTO = new RoleDTO();
    roleDTO.setRoleId(role.getRoleId());
    roleDTO.setRoleName(role.getRoleName());
    return roleDTO;
}

private Role roleDtoToEntity(RoleDTO roleDTO) {
    Role role = new Role();
    role.setRoleId(roleDTO.getRoleId());
    role.setRoleName(roleDTO.getRoleName());
    return role;
}
}

