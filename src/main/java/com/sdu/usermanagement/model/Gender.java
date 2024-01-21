package com.sdu.usermanagement.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "gender")
public class Gender{
    public enum GenderType {
        Male, Female, Others
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Gender_Id")
    private int genderId;

    @Enumerated(EnumType.STRING)
    @Column(name = "Gender_Type")
    private GenderType genderType;
  
}