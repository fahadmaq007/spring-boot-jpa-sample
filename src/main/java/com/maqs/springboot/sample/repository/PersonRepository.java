package com.maqs.springboot.sample.repository;


import com.maqs.springboot.sample.model.Person;
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
public interface PersonRepository extends JpaRepository<Person, UUID> {

    /**
     * JPA {@link Specification} & {@link Pageable} details to fetched records accordingly.
     * @param spec
     * @param pageable
     * @return
     */
    Page<Person> findAll(@Nullable Specification<Person> spec, Pageable pageable);

}