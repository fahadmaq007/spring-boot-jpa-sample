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

    /**
     * List by given query params.
     * The query param can have the {@link com.maqs.springboot.sample.dto.SearchCriteria.Operation} as :suffix
     * For eg.
     * age:gt=25 Age is Greater Than 25 filter
     * date:btw=millis1,millis2
     *
     * @param params
     * @return
     * @throws ServiceException
     */
    Page<Person> listByQueryParams(Map<String, String> params) throws ServiceException;

    /**
     * List by given criteria (json string)
     * @param criteriaJson
     * @param sort
     * @param pageIndex
     * @param pageSize
     * @return
     * @throws ServiceException
     */
    Page<Person> list(String criteriaJson, String sort, Integer pageIndex, Integer pageSize) throws ServiceException;

}
