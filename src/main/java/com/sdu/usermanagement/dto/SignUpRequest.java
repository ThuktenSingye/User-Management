package com.sdu.usermanagement.dto;

import lombok.Data;

@Data
public class SignUpRequest {
    private int employeeId;
    private String firstName;
    private String middleName;
	private String lastName;
	private String email;
	private String password;
}
