package com.maqs.springboot.sample.controller;

import com.maqs.springboot.sample.dto.SearchCriteria;
import com.maqs.springboot.sample.exceptions.ServiceException;
import com.maqs.springboot.sample.model.Timesheet;
import com.maqs.springboot.sample.services.ITimesheetService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/timesheets", produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class TimesheetController {

    @Autowired
    @Qualifier("timesheetServiceApi")
    private ITimesheetService timesheetServiceApi;

    @PostMapping()
    public ResponseEntity<Page<Timesheet>> listTimesheets(
            @RequestParam(value="page", required = false) Integer page,
            @RequestParam(value="size", required = false) Integer size,
            @RequestParam(value="sort", required = false) String sort,
            @RequestBody(required = false) SearchCriteria searchCriteria) throws ServiceException {
        Page<Timesheet> timesheetPage = timesheetServiceApi.list(searchCriteria, sort, page, size);
        return new ResponseEntity<>(timesheetPage, HttpStatus.OK);
    }
}
