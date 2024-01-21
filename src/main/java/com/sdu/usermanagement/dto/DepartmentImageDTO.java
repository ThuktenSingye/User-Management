package com.sdu.usermanagement.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DepartmentImageDTO {
    private int deptImageId;
    private String deptImageName;
    private String deptImageType;
    private String deptImagePath;
}
