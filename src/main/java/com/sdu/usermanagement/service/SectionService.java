package com.sdu.usermanagement.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.sdu.usermanagement.dto.SectionDTO;

public interface SectionService {
    /* List all Section */
    ResponseEntity<List<SectionDTO>> findAllSection();
    /* Find Section by ID */
    ResponseEntity<SectionDTO> findSectionById(Integer sect_id);
    /* Save or Update Sectino */
    ResponseEntity<String> saveSection(SectionDTO sectionDTO);
    /* Delete Section */
    ResponseEntity<String> deleteSection(Integer sect_id);
    
   ResponseEntity<List<SectionDTO>> findSectionByDepartmentId(Integer deptId);
}
