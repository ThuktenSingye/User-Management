package com.sdu.usermanagement.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "department_image")
public class DepartmentImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Dept_Image_Id")
    private int deptImageId;

    @Column(name = "Dept_Image_Name")
    private String deptImageName;

    @Column(name = "Dept_Image_Type")
    private String deptImageType;
    
    @Column(name = "Dept_Image_Path")
    private String deptImagePath;

}
