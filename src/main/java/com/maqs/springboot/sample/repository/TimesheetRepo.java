package com.maqs.springboot.sample.repository;


import com.maqs.springboot.sample.model.Timesheet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * @author maqbool
 */
@Repository
public interface TimesheetRepo extends JpaRepository<Timesheet, UUID> {

    Page<Timesheet> findAll(@Nullable Specification<Timesheet> spec, Pageable pageable);

}