package com.sdu.usermanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;


import com.sdu.usermanagement.model.Department;


public interface DepartmentRepository extends JpaRepository<Department , Integer>{
   
}
