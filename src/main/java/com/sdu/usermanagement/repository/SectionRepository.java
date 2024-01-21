package com.sdu.usermanagement.repository;
import com.sdu.usermanagement.model.Section;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SectionRepository extends JpaRepository<Section, Integer>{
    List<Section> findByDepartmentDeptId(Integer deptId);
}
