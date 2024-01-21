package com.sdu.usermanagement.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "address")
public class Address{
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Address_Id")
    private int addressId;

    @Column(name = "Current_Address")
    private String currentAddress;

    @Column(name = "Permanent_Address")
    private String permanentAddress;

}