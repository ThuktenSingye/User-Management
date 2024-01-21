package com.sdu.usermanagement.dto;

import com.sdu.usermanagement.model.Gender.GenderType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GenderDTO {
    private int genderId;
    private GenderType genderType;
}
