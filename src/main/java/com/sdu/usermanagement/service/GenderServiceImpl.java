package com.sdu.usermanagement.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.sdu.usermanagement.dto.GenderDTO;
import com.sdu.usermanagement.model.Gender;
import com.sdu.usermanagement.repository.GenderRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class GenderServiceImpl implements GenderService {

    @Autowired
    private GenderRepository genderRepository;

    @Autowired
    private LogService logService;


    @Override
    public ResponseEntity<List<GenderDTO>> findAllGender() {
        try {
            List<GenderDTO> genderDTOs = genderRepository.findAll().stream().map(this::genderEntityToDto)
                    .collect(Collectors.toList());
            logService.logApplicationStatus("Gnder retrieved");
            return new ResponseEntity<>(genderDTOs, HttpStatus.OK);
        } catch (Exception e) {
            logService.logApplicationStatus("Error: Retriving gender "+ e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<GenderDTO> findGenderById(Integer gender_id) {
        try {
            GenderDTO genderDTO = genderEntityToDto(genderRepository.findById(gender_id).orElseThrow());
            logService.logApplicationStatus("Gender retrieved");
            return new ResponseEntity<>(genderDTO, HttpStatus.OK);
        } catch (Exception e) {
            /* Log the errrpr */
            logService.logApplicationStatus("Error: Cannot Retrieved Gender "+ e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<String> saveGender(GenderDTO genderDTO) {
        try {
            genderRepository.saveAndFlush(genderDtoToEntity(genderDTO));
            logService.logApplicationStatus("Saved Gender");
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (Exception e) {
            logService.logApplicationStatus("Error: Saving Gender "+ e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @Override
    public ResponseEntity<String> deleteGender(Integer gender_id) {
        try {
            if (gender_id == null || gender_id < 0) {
                /* log the error */
                return new ResponseEntity<>("Invalid dept_id format", HttpStatus.BAD_REQUEST);
            }
            if (!genderRepository.existsById(gender_id)) {
                return new ResponseEntity<>("Gender not found", HttpStatus.NOT_FOUND);
            }
            genderRepository.deleteById(gender_id);
            logService.logApplicationStatus("Gender Deleted");
            return new ResponseEntity<>(HttpStatus.OK);
        }

        catch (Exception e) {
            /* Log the error */
            logService.logApplicationStatus("Error: Gender Deleted "+ e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    private GenderDTO genderEntityToDto(Gender gender) {
        GenderDTO genderDTO = new GenderDTO();
        genderDTO.setGenderId(gender.getGenderId());
        genderDTO.setGenderType(gender.getGenderType());
        return genderDTO;

    }

    private Gender genderDtoToEntity(GenderDTO genderDTO) {
        Gender gender = new Gender();
        gender.setGenderId(genderDTO.getGenderId());
        gender.setGenderType(genderDTO.getGenderType());

        return gender;
    }
}
