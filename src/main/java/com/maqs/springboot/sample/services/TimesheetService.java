package com.maqs.springboot.sample.services;

import com.maqs.springboot.sample.dto.SearchCriteria;
import com.maqs.springboot.sample.exceptions.ServiceException;
import com.maqs.springboot.sample.model.Timesheet;
import com.maqs.springboot.sample.repository.SpecificationBuilder;
import com.maqs.springboot.sample.repository.TimesheetRepo;
import com.maqs.springboot.sample.util.Util;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
@Qualifier("timesheetServiceApi")
@Slf4j
public class TimesheetService implements ITimesheetService {

    @Autowired
    private TimesheetRepo timesheetRepo;

    @Override
    public Page<Timesheet> list(SearchCriteria searchCriteria, String sort, Integer pageIndex, Integer pageSize) throws ServiceException {
        Pageable pageable = Util.getPageRequest(sort, pageIndex, pageSize);
        log.debug("listing timesheets ... " + searchCriteria);

        Page<Timesheet> page = null;
        try {
            if (searchCriteria != null) {
                Specification<Timesheet> spec = SpecificationBuilder.findBy(searchCriteria);
                page = timesheetRepo.findAll(spec, pageable);
            } else {
                page = timesheetRepo.findAll(pageable);
            }
            log.info("fetched " + page);
        } catch (Exception e) {
            throw new ServiceException(e.getMessage(), e);
        }

        return page;
    }
}
