package com.sdu.usermanagement.service;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.sdu.usermanagement.dto.DepartmentDTO;
import com.sdu.usermanagement.model.Department;
import com.sdu.usermanagement.model.DepartmentImage;
import com.sdu.usermanagement.repository.DepartmentRepository;
import com.sdu.usermanagement.utility.FileNameGenerator;

import jakarta.transaction.Transactional;

import lombok.extern.log4j.Log4j2;

@Service
@Transactional
@Log4j2
// In real life, minimized the try and catch
public class DepartmentServiceImpl implements DepartmentService {

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private FileNameGenerator fileNameGenerator;

    @Value("${department-image.upload-dir}")
    private String FOLDER_PATH;

    private String filePath;

    @Override
    public ResponseEntity<String> saveDepartment(DepartmentDTO departmentDTO, MultipartFile departmentImageFile) {

        try {

            Department department = dtoToEntity(departmentDTO);
            DepartmentImage departmentImage = null;

            if (departmentImageFile != null) {

                filePath = Paths
                        .get(FOLDER_PATH,
                                fileNameGenerator.generateUniqueFileName(departmentImageFile.getOriginalFilename()))
                        .toString();
                log.info("File path:" + filePath);
                // Create the directory if it doesn't exist
                File directory = new File(FOLDER_PATH);
                if (!directory.exists()) {
                    if (directory.mkdirs()) {
                        log.info("Directory created successfully: {}", FOLDER_PATH);
                    } else {
                        log.error("Failed to create directory: {}", FOLDER_PATH);
                        return new ResponseEntity<>("Failed to create directory", HttpStatus.INTERNAL_SERVER_ERROR);
                    }
                }

                departmentImage = DepartmentImage.builder()
                        .deptImageName(departmentImageFile.getOriginalFilename())
                        .deptImageType(departmentImageFile.getContentType())
                        .deptImagePath(filePath)
                        .build();
                departmentImageFile.transferTo(new File(filePath));
                department.setDepartmentImage(departmentImage);

            }

            if (departmentRepository.saveAndFlush(department) == null) {
                log.error("returned value is null");
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);

            }
            return new ResponseEntity<>(HttpStatus.CREATED);

        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @Override
    public ResponseEntity<List<DepartmentDTO>> findAllDepartment() {
        try {
            List<DepartmentDTO> departmentDTOs = departmentRepository.findAll().stream().map(this::entityToDto)
                    .collect(Collectors.toList());
            return new ResponseEntity<>(departmentDTOs, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error while finding the list of department: " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<DepartmentDTO> findDepartmentById(Integer dept_id) {
        try {
            /* Bad Request */
            if (dept_id == null || dept_id < 0) {
                /* Bad Request - Invalid user_id format */
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            DepartmentDTO departmentDTO = entityToDto(departmentRepository.findById(dept_id).orElseThrow());
            /* Succesful */
            return new ResponseEntity<>(departmentDTO, HttpStatus.OK);
        } catch (Exception e) {
            /* Log the errrpr */
            log.error("Error while finding department by id:" + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @Override
    public ResponseEntity<String> deleteDepartment(Integer dept_id) {
        try {
            if (dept_id == null || dept_id < 0) {
                /* log the error */
                return new ResponseEntity<>("Invalid dept_id format", HttpStatus.BAD_REQUEST);
            }
            if (!departmentRepository.existsById(dept_id)) {
                return new ResponseEntity<>("Department not found", HttpStatus.NOT_FOUND);
            }
            departmentRepository.deleteById(dept_id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            /* Log the error */
            log.error("Error while deleting department" + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    // Method to convert Department Entity to Department DTO
    private DepartmentDTO entityToDto(Department department) {
        DepartmentDTO departmentDTO = new DepartmentDTO();
        departmentDTO.setDeptId(department.getDeptId());
        departmentDTO.setDeptName(department.getDeptName());
        departmentDTO.setDeptDescription(department.getDeptDescription());
        departmentDTO.setDepartmentImage(department.getDepartmentImage());
        return departmentDTO;
    }

    /**
     * Converts a DepartmentDTO to a Department Entity and vice versa.
     */
    private Department dtoToEntity(DepartmentDTO departmentDTO) {

        Department department = new Department();
        department.setDeptId(departmentDTO.getDeptId());
        department.setDeptName(departmentDTO.getDeptName());
        department.setDeptDescription((departmentDTO.getDeptDescription()));
        department.setDepartmentImage(departmentDTO.getDepartmentImage());
        return department;
    }

    /**
     * Returns the total count of departments in the database.
     *
     * @return the total count of departments
     */
    @Override
    public ResponseEntity<Long> findTotalDepartmentCount() {
        try {
            long totalDepartmentCount = departmentRepository.count(); // Using count() to get the total department count
            return new ResponseEntity<>(totalDepartmentCount, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error while retrieving total department count: " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Returns the image of the department with the specified department ID.
     *
     * @param dept_id the ID of the department
     * @return the image of the department, or an error response if the department
     *         does not exist or if there was an error retrieving the image
     */
    @Override
    public ResponseEntity<byte[]> findDepartmentImage(Integer dept_id) {
        try {
            Department department = departmentRepository.findById(dept_id).orElseThrow();

            if (department.getDepartmentImage() == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            String filePath = department.getDepartmentImage().getDeptImagePath();
            log.info("Department File path:" + filePath);

            byte[] images = Files.readAllBytes(Paths.get(filePath));

            if (images == null) {
                // Cannot read image byte
                log.info("Read image file is null");
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
            HttpHeaders headers = new HttpHeaders();
            String contentType = Files.probeContentType(Paths.get(filePath));
            // log.info("Content type: " + contentType);
            if (contentType == null) {
                headers.setContentType(MediaType.MULTIPART_FORM_DATA);
            }
            headers.setContentType(MediaType.parseMediaType(contentType));

            return new ResponseEntity<>(images, headers, HttpStatus.OK);

        } catch (Exception e) {
            // Handle the IOException
            log.error("Error while fetching department image: " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
