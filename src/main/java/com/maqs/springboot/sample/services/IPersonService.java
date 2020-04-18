package com.maqs.springboot.sample.services;

import com.maqs.springboot.sample.dto.SearchCriteria;
import com.maqs.springboot.sample.exceptions.ServiceException;
import com.maqs.springboot.sample.model.Person;
import org.springframework.data.domain.Page;

import java.util.Map;

/**
 * Interface for Person API
 *
 * @author maqbool
 */
public interface IPersonService {

    /**
     * List by given criteria
     * All the params are optional.
     *
     * @param searchCriteria
     * @param sort
     * @param pageIndex
     * @param pageSize
     * @return
     * @throws ServiceException
     */
    Page<Person> list(SearchCriteria searchCriteria, String sort, Integer pageIndex, Integer pageSize) throws ServiceException;

    Page<Person> listByFilters(Map<String, String> filters, String sort, Integer pageIndex, Integer pageSize) throws ServiceException;
}
