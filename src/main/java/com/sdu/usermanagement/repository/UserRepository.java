package com.sdu.usermanagement.repository;
import com.sdu.usermanagement.model.User;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Integer> {



    Optional<User> findByEmail(String email);

    List<User> findBySectionSectId(Integer sectId);

    @Query("SELECT u FROM User u WHERE u.status = 'ACCEPTED'")
    List<User> findAllAcceptedUsers();


    @Modifying
    @Query(value = "UPDATE user SET Email = :email WHERE User_Id = :userId", nativeQuery = true)
    void updateUserEmail(@Param("email") String email,@Param("userId") Integer userId);
    
    @Query(value = "SELECT COUNT(*) FROM user u JOIN gender g ON u.Gender_Id = g.Gender_Id WHERE u.Gender_Id= :genderId", nativeQuery = true)
    long countByGender(@Param("genderId") Integer genderId);

    @Query(value = "SELECT " + 
                    "u.User_Id, u.Employee_Id, u.CID_No, u.FName, " + 
                    "u.MName, u.LName, u.Mobile_No, u.Email, u.DOB, " +
                    "u.Address_Id, u.Gender_Id, u.Section_Id, u.Image_Id " +
                    "FROM user u JOIN section s ON u.section_Id = s.section_Id " +
                    "JOIN department d on s.Dept_Id = d.Dept_Id "+
                    "WHERE d.Dept_Id = :deptId",
        nativeQuery = true)
    List<User> findAllDepartmentUser(@Param("deptId") Integer deptId);


        @Query(value = "SELECT u.* FROM user_role ur " +
                "JOIN user u ON ur.User_Id = u.User_Id " +
                "WHERE ur.Role_Id = :adminRoleId", nativeQuery = true)
    List<User> findUsersWithAdminRole(@Param("adminRoleId") Integer adminRoleId);
}