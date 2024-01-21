package com.sdu.usermanagement.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "section")
public class Section{
    /* Define the field */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Section_Id")
    private int sectId;

    @Column(name = "Section_Name")
    private String sectName;

    @ManyToOne(
        fetch = FetchType.EAGER, 
        cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH
        }
    )
    @JoinColumn(name = "Dept_Id")
    private Department department;
    
}