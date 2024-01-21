package com.sdu.usermanagement.dto;

import com.sdu.usermanagement.model.Department;
import lombok.Data;

@Data
public class SectionDTO {
    private int sectId;
    private String sectName;
    private Department department;
}
