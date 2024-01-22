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

@Service
@Transactional
public class SectionServiceImpl implements SectionService{



    @Autowired
    private SectionRepository sectionRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private LogService logService;

    @Override
    public ResponseEntity<List<SectionDTO>> findAllSection() {
        try{
            List<SectionDTO> sectionDTOs = sectionRepository.findAll().stream().map(this::sectionEntityToDto)
                    .collect(Collectors.toList());
            logService.logApplicationStatus("Section retrieved");
            return new ResponseEntity<>(sectionDTOs, HttpStatus.OK);
        }
        catch(Exception e){
            logService.logApplicationStatus("Error: Retriving section "+ e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
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
            logService.logApplicationStatus("Section retrieved");
            return new ResponseEntity<>(sectionDTO ,HttpStatus.OK);
        }
        catch(Exception e){
            /* Log the errrpr */
            logService.logApplicationStatus("Error: Cannot Retrieved Section "+ e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public ResponseEntity<String> saveSection(SectionDTO sectionDTO) {
        try{
            Department department = departmentRepository.findById(sectionDTO.getDepartment().getDeptId()).orElseThrow();
            // Set the Department in the Section entity
            Section section = sectionDtoToEntity(sectionDTO);
            section.setDepartment(department);
            sectionRepository.saveAndFlush(section);
            logService.logApplicationStatus("Section saved");
            return new ResponseEntity<>(HttpStatus.CREATED);
        }
        catch(Exception e){
            logService.logApplicationStatus("Error: Saving Section "+ e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
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
            logService.logApplicationStatus("Section Deleted");
            return new ResponseEntity<>(HttpStatus.OK);
        }
        
        catch(Exception e){
            logService.logApplicationStatus("Error: Section Deleted "+ e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        
    }
    
   @Override
   public ResponseEntity<List<SectionDTO>> findSectionByDepartmentId(Integer deptId){
        try{
            if (deptId ==  null || deptId < 0) {
                /* Bad Request - Invalid user_id format */
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

            List<SectionDTO> sectionDTOs = sectionRepository.findByDepartmentDeptId(deptId).stream()
                    .map(this::sectionEntityToDto).collect(Collectors.toList());
            logService.logApplicationStatus("Deparmtent Section retrieved");
            return new ResponseEntity<>(sectionDTOs, HttpStatus.OK);
        }
        catch(Exception e){
            logService.logApplicationStatus("Error: Cannot Retrieved Deparmtent Section "+ e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
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
