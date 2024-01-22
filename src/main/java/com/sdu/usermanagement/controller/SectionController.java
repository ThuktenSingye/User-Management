package com.sdu.usermanagement.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import com.sdu.usermanagement.dto.SectionDTO;
import com.sdu.usermanagement.service.SectionService;

@RestController
@RequestMapping("/sections")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
// @CrossOrigin(origins = "https://sdu-ums-internship.netlify.app/")
public class SectionController {

    @Autowired
    private SectionService sectionService;

    /* Add new Service */
    @PostMapping
    public ResponseEntity<String> addSection(@RequestBody SectionDTO sectionDTO){
        System.out.println(sectionDTO);
        return sectionService.saveSection(sectionDTO);
    }
    /* Get All Service  */
    @GetMapping
    private ResponseEntity<List<SectionDTO>> getAllSection() {
        return sectionService.findAllSection();
        
    }
    
   @GetMapping("/departments/{deptId}")
   public ResponseEntity<List<SectionDTO>> getSectionsByDepartment(@PathVariable Integer deptId) {
       return sectionService.findSectionByDepartmentId(deptId);
   }
    
    /* Get services by id */
    @GetMapping("/{sect_id}")
     private ResponseEntity<SectionDTO> getSectionById(@PathVariable Integer sect_id){
        return sectionService.findSectionById(sect_id);
    }

    @DeleteMapping("/{sect_id}")
    private ResponseEntity<String> deleteService(@PathVariable Integer sect_id){
        return sectionService.deleteSection(sect_id);
    }



}
