package com.sdu.usermanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddressDTO {
    private int addressId;
    private String currentAddress;
    private String permanentAddress;

}
