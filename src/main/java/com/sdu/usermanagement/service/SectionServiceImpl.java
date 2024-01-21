package com.sdu.usermanagement.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.sdu.usermanagement.dto.SectionDTO;
import com.sdu.usermanagement.model.Department;
import com.sdu.usermanagement.model.Section;
import com.sdu.usermanagement.repository.DepartmentRepository;
import com.sdu.usermanagement.repository.SectionRepository;

import jakarta.transaction.Transactional;
import lombok.extern.log4j.Log4j2;

@Service
@Transactional
@Log4j2
public class SectionServiceImpl implements SectionService{



    @Autowired
    private SectionRepository sectionRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Override
    public ResponseEntity<List<SectionDTO>> findAllSection() {
        try{
            List<SectionDTO> sectionDTOs = sectionRepository.findAll().stream().map(this::sectionEntityToDto).collect(Collectors.toList());
            return new ResponseEntity<>(sectionDTOs, HttpStatus.OK);
        }
        catch(Exception e){
            log.error("Error while retrieving all section: ", e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    
    }

    @Override
    public ResponseEntity<SectionDTO> findSectionById(Integer sect_id) {
        try{
            /* Bad Request */
            if (sect_id ==  null || sect_id < 0) {
                /* Bad Request - Invalid user_id format */
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            SectionDTO sectionDTO = sectionEntityToDto(sectionRepository.findById(sect_id).orElseThrow());
            
            return new ResponseEntity<>(sectionDTO ,HttpStatus.OK);
        }
        catch(Exception e){
            /* Log the errrpr */
            log.error("Error while retrieving section by id: ", e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<String> saveSection(SectionDTO sectionDTO) {
        try{
            Department department = departmentRepository.findById(sectionDTO.getDepartment().getDeptId()).orElseThrow();
            // Set the Department in the Section entity
            Section section = sectionDtoToEntity(sectionDTO);

            section.setDepartment(department);
            log.info("Section: " + section);
            Section savedSection = sectionRepository.saveAndFlush(section);

            if(savedSection == null){
                log.info("Saved Section is null! Error while saving");
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
            return new ResponseEntity<>(HttpStatus.CREATED);
        }
        catch(Exception e){
            log.error("Error while saving/updating  section: " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public ResponseEntity<String> deleteSection(Integer sect_id) {
        try{
            if(sect_id == null || sect_id < 0){
                /* log the error */
                return new ResponseEntity<>("Invalid sec_id format", HttpStatus.BAD_REQUEST);
            }
            if (!sectionRepository.existsById(sect_id)) {
                return new ResponseEntity<>("Section not found", HttpStatus.NOT_FOUND);
            }
            sectionRepository.deleteById(sect_id);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        
        catch(Exception e){
            /* Log the error */
            log.error("Error while deleting section: ", e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        
    }
    
   @Override
   public ResponseEntity<List<SectionDTO>> findSectionByDepartmentId(Integer deptId){
        try{
            if (deptId ==  null || deptId < 0) {
                /* Bad Request - Invalid user_id format */
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

            List<SectionDTO> sectionDTOs = sectionRepository.findByDepartmentDeptId(deptId).stream().map(this::sectionEntityToDto).collect(Collectors.toList());
            return new ResponseEntity<>(sectionDTOs, HttpStatus.OK);
        }
        catch(Exception e){
            log.error("Error while retrieving all section: ", e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }	
   }

    /* Define method to convert Section Entity to Section DTO */
    private SectionDTO sectionEntityToDto(Section section){
        SectionDTO sectionDTO = new SectionDTO();
        sectionDTO.setSectId(section.getSectId());
        sectionDTO.setSectName(section.getSectName());
        sectionDTO.setDepartment(section.getDepartment());

        return sectionDTO;
    }
    /* Method to convert Section DTO to Section Entity */
    private Section sectionDtoToEntity(SectionDTO sectionDTO){
        Section section = new Section();
        section.setSectId(sectionDTO.getSectId());
        section.setSectName(sectionDTO.getSectName());
        section.setDepartment(sectionDTO.getDepartment());

        return section;
    }
    
}
