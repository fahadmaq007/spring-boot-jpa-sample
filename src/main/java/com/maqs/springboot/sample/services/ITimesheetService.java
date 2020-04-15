package com.maqs.springboot.sample.services;

import com.maqs.springboot.sample.dto.SearchCriteria;
import com.maqs.springboot.sample.exceptions.ServiceException;
import com.maqs.springboot.sample.model.Timesheet;
import org.springframework.data.domain.Page;

/**
 * Interface for timesheet API
 *
 * @author maqbool
 */
public interface ITimesheetService {

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
    Page<Timesheet> list(SearchCriteria searchCriteria, String sort, Integer pageIndex, Integer pageSize) throws ServiceException;
}
