package com.sdu.usermanagement.dto;

import com.sdu.usermanagement.model.DepartmentImage;

import lombok.Data;

@Data
public class DepartmentDTO {
    private int deptId;
    private String deptName;
    private String deptDescription;
    private DepartmentImage departmentImage;
}
