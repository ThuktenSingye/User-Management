package com.sdu.usermanagement.service;
import com.sdu.usermanagement.dto.UserDTO;

import java.util.ArrayList;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.nio.file.Paths;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import com.sdu.usermanagement.model.Gender;
import com.sdu.usermanagement.model.ProfileImage;
import com.sdu.usermanagement.model.Role;
import com.sdu.usermanagement.model.Section;
import com.sdu.usermanagement.model.User;
import com.sdu.usermanagement.model.User.UserStatus;
import com.sdu.usermanagement.repository.GenderRepository;
import com.sdu.usermanagement.repository.RoleRepository;
import com.sdu.usermanagement.repository.SectionRepository;
import com.sdu.usermanagement.repository.UserRepository;
import com.sdu.usermanagement.utility.FileNameGenerator;

import jakarta.transaction.Transactional;
import lombok.extern.log4j.Log4j2;

@Service
@Transactional
@Log4j2
@Primary
public class UserService implements  UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GenderRepository genderRepository;

    @Autowired
    private SectionRepository sectionRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private FileNameGenerator fileNameGenerator;

    @Value("${user-profile.upload-dir}")
    private String FOLDER_PATH;

    private String filePath;


    public ResponseEntity<String> saveUser(UserDTO userDTO, MultipartFile profileImageFile) {

        try {

            // if (userDTO.getGender() == null) {
            // return new ResponseEntity<>("Missing Gender Parameter",
            // HttpStatus.BAD_REQUEST);
            // }
            // if (userDTO.getSection() == null) {
            // return new ResponseEntity<>("Missing Section Parameter",
            // HttpStatus.BAD_REQUEST);
            // }
            Gender gender = genderRepository.findById(userDTO.getGender().getGenderId()).orElse(null);
            if (gender == null) {
                return new ResponseEntity<>("Gender not found", HttpStatus.BAD_REQUEST);
            }

            Section section = sectionRepository.findById(userDTO.getSection().getSectId()).orElse(null);

            if (section == null) {
                return new ResponseEntity<>("Section not found", HttpStatus.BAD_REQUEST);
            }
            User user = userDtoToEntity(userDTO);
            /* GET profile */
            ProfileImage profileImage = null;

            if (profileImageFile != null) {
                filePath = Paths
                        .get(FOLDER_PATH,
                                fileNameGenerator.generateUniqueFileName(profileImageFile.getOriginalFilename()))
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
                profileImage = ProfileImage.builder()
                        .imageName(profileImageFile.getOriginalFilename())
                        .imageType(profileImageFile.getContentType())
                        .imagePath(filePath)
                        .build();
                profileImageFile.transferTo(new File(filePath));
                user.setProfileImage(profileImage);
            }

            
            List<Role> roles = new ArrayList<>();
            Role role = roleRepository.findById(2).orElseThrow();
			roles.add(role);

            user.setGender(gender);
            user.setSection(section);
            user.setPassword(new BCryptPasswordEncoder().encode("user123"));
            user.setStatus(UserStatus.ACCEPTED);
            user.setRoles(roles);
            User saveUser = userRepository.saveAndFlush(user);

            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (Exception e) {
            // log the error
            log.error("Error while saving user: " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    public ResponseEntity<List<UserDTO>> findAllUser() {
        try {
            List<UserDTO> users = userRepository.findAllAcceptedUsers().stream().map(this::userEntityToDto)
                    .collect(Collectors.toList());

            return new ResponseEntity<>(users, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error while retrieving all user: ", e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    
    public ResponseEntity<Long> getTotalUserCount() {
        try {
            long totalUserCount = userRepository.count(); // Using count() to get the total number of users
            return new ResponseEntity<>(totalUserCount, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error while retrieving total user count: ", e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    public ResponseEntity<Long> getUserCountByGender(Integer genderId) {

        try {
            if (genderId == null || genderId < 0) {
                /* Bad Request - Invalid user_id format */
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

            long userByGender = userRepository.countByGender(genderId);
            return new ResponseEntity<>(userByGender, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error while retrieving user count by gender: ", e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    public ResponseEntity<UserDTO> findUserById(Integer user_id) {
        try {
            /* Bad Request */
            if (user_id == null || user_id < 0) {
                /* Bad Request - Invalid user_id format */
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            UserDTO userDTO = userEntityToDto(userRepository.findById(user_id).orElseThrow());
            /* Succesful */
            return new ResponseEntity<>(userDTO, HttpStatus.OK);
        } catch (Exception e) {
            /* Log the errrpr */
            log.error("Error while retrieving user by id : ", e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    public ResponseEntity<String> deleteUser(Integer user_id) {
        try {
            if (user_id == null || user_id < 0) {
                /* log the error */
                return new ResponseEntity<>("Invalid user_id format", HttpStatus.BAD_REQUEST);
            }
            if (!userRepository.existsById(user_id)) {
                return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
            }
            userRepository.deleteById(user_id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            /* Log the error */
            log.error("Error while deleting user : ", e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }


    public ResponseEntity<String> updateEmail(String email, Integer user_id) {

        try {
            if (email == null || user_id == null || user_id < 0) {
                return new ResponseEntity<>("Invalid/Null Email and User Id", HttpStatus.BAD_REQUEST);
            }
            userRepository.updateUserEmail(email, user_id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<List<UserDTO>> getAllUserBySectionId(Integer sect_id) {
        try {
            List<UserDTO> users = userRepository.findBySectionSectId(sect_id).stream().map(this::userEntityToDto)
                    .collect(Collectors.toList());

            return new ResponseEntity<>(users, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error while retrieving sections user: ", e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    public ResponseEntity<List<UserDTO>> getAllUserByDepartmentId(Integer dept_id) {

        try {
            List<UserDTO> userDTOs = userRepository.findAllDepartmentUser(dept_id).stream().map(this::userEntityToDto)
                    .collect(Collectors.toList());
            log.info(userDTOs.size());
            return new ResponseEntity<>(userDTOs, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error while finding the list of department user: " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /* Method to convert User Entity to User DTO */
    private UserDTO userEntityToDto(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setUserId(user.getUserId());
        userDTO.setCidNo(user.getCidNo());
        userDTO.setEmployeeId(user.getEmployeeId());
        userDTO.setFirstName(user.getFirstName());
        userDTO.setMiddleName(user.getMiddleName());
        userDTO.setLastName(user.getLastName());
        userDTO.setEmail(user.getEmail());
        userDTO.setMobileNo(user.getMobileNo());
        userDTO.setDob(user.getDob());
        userDTO.setAddress(user.getAddress());
        userDTO.setGender(user.getGender());
        userDTO.setSection(user.getSection());
        userDTO.setProfileImage(user.getProfileImage());

        return userDTO;
    }

    /* Method to convert User DTO to User Entity */
    private User userDtoToEntity(UserDTO userDTO) {
        User user = new User();
        user.setUserId(userDTO.getUserId());
        user.setEmployeeId(userDTO.getEmployeeId());
        user.setCidNo(userDTO.getCidNo());
        user.setFirstName(userDTO.getFirstName());
        user.setMiddleName(userDTO.getMiddleName());
        user.setLastName(userDTO.getLastName());
        user.setEmail(userDTO.getEmail());
        user.setMobileNo(userDTO.getMobileNo());
        user.setDob(userDTO.getDob());
        user.setAddress(userDTO.getAddress());
        user.setGender(userDTO.getGender());
        user.setSection(userDTO.getSection());
        user.setProfileImage(userDTO.getProfileImage());
        return user;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("User not found " + username));
        
    }

}

