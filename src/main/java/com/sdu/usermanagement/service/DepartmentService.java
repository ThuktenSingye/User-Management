package com.sdu.usermanagement.service;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import com.sdu.usermanagement.dto.DepartmentDTO;



public interface DepartmentService {
// List all Department
    ResponseEntity<List<DepartmentDTO>> findAllDepartment();

// // Find Department By Id
    ResponseEntity<DepartmentDTO> findDepartmentById(Integer dept_id);

// Save Department
    ResponseEntity<String> saveDepartment(DepartmentDTO departmentDTO, MultipartFile departmentImageFile);

//     void delete(int dept_id);
    ResponseEntity<String> deleteDepartment(Integer dept_id);
    
    ResponseEntity<Long> findTotalDepartmentCount();

    
    ResponseEntity<byte[]> findDepartmentImage(Integer dept_id);

    
}
