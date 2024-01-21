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
import lombok.extern.log4j.Log4j2;

@Service
@Transactional
@Log4j2
public class GenderServiceImpl implements GenderService {

    @Autowired
    private GenderRepository genderRepository;

    @Override
    public ResponseEntity<List<GenderDTO>> findAllGender() {
        try {
            List<GenderDTO> genderDTOs = genderRepository.findAll().stream().map(this::genderEntityToDto)
                    .collect(Collectors.toList());
            return new ResponseEntity<>(genderDTOs, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error while finding all gender: ", e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<GenderDTO> findGenderById(Integer gender_id) {
        try {
            /* Bad Request */
            if (gender_id == null || gender_id < 0) {
                /* Bad Request - Invalid user_id format */
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            GenderDTO genderDTO = genderEntityToDto(genderRepository.findById(gender_id).orElseThrow());
            /* Succesful */
            return new ResponseEntity<>(genderDTO, HttpStatus.OK);
        } catch (Exception e) {
            /* Log the errrpr */
            log.error("Error while finding  gender by id: ", e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<String> saveGender(GenderDTO genderDTO) {
        try {
            if (genderRepository.saveAndFlush(genderDtoToEntity(genderDTO)) == null) {
                /* Log the error */
                log.info("The saved entity is null");
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (Exception e) {
            log.error("Error while adding/updating gender: ", e.getMessage());
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
            return new ResponseEntity<>(HttpStatus.OK);
        }

        catch (Exception e) {
            /* Log the error */
            log.error("Error while deleting gender: ", e.getMessage());
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
