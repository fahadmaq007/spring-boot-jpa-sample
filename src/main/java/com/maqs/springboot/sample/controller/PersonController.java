package com.maqs.springboot.sample.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.maqs.springboot.sample.dto.SearchCriteria;
import com.maqs.springboot.sample.exceptions.ServiceException;
import com.maqs.springboot.sample.model.Person;
import com.maqs.springboot.sample.services.IPersonService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping(value = "/persons",
        produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class PersonController {

    @Autowired
    private IPersonService personService;

    /**
     * The REST api to list the entities
     * @param page - Page Number
     * @param size - Page Size
     * @param sort - Sort by attribute (Use '-' for DESC order. For. eg. -age)
     * @param searchCriteria - The criteria {@link SearchCriteria}
     * @return
     * @throws ServiceException
     */
    @PostMapping()
    public ResponseEntity<Page<Person>> list(
            @RequestParam(value="page", required = false) Integer page,
            @RequestParam(value="size", required = false) Integer size,
            @RequestParam(value="sort", required = false) String sort,
            @RequestBody(required = false) SearchCriteria searchCriteria) throws ServiceException {
        Page<Person> ePage = personService.list(searchCriteria, sort, page, size);
        return new ResponseEntity<>(ePage, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Page<Person>> listByQueryParams(@RequestParam Map<String, String> params) throws Exception {
        Page<Person> ePage = personService.listByQueryParams(params);
        return new ResponseEntity<>(ePage, HttpStatus.OK);
    }

    @GetMapping(value = "/json")
    public ResponseEntity<Page<Person>> listByCriteriaAsJson(
            @RequestParam(value="page", required = false) Integer page,
          @RequestParam(value="size", required = false) Integer size,
          @RequestParam(value="sort", required = false) String sort,
          @RequestParam(value="criteria", required = false) String criteria) throws Exception {
        Page<Person> ePage = personService.list(criteria, sort, page, size);
        return new ResponseEntity<>(ePage, HttpStatus.OK);
    }
}
