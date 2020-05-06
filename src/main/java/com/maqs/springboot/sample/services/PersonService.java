package com.maqs.springboot.sample.services;

import com.maqs.springboot.sample.dto.SearchCriteria;
import com.maqs.springboot.sample.exceptions.InvalidFilterOperationException;
import com.maqs.springboot.sample.exceptions.ServiceException;
import com.maqs.springboot.sample.model.Person;
import com.maqs.springboot.sample.repository.PersonRepository;
import com.maqs.springboot.sample.repository.SpecificationBuilder;
import com.maqs.springboot.sample.util.Util;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
public class PersonService implements IPersonService {

    @Autowired
    private PersonRepository personRepository;

    private Set<String> pagingParams = new HashSet<>();

    {
        pagingParams.add("sort");
        pagingParams.add("page");
        pagingParams.add("size");
    }

    @Override
    public Page<Person> listByCriteria(SearchCriteria searchCriteria, String sort, Integer pageIndex, Integer pageSize) throws ServiceException {
        Pageable pageable = Util.getPageRequest(sort, pageIndex, pageSize);
        log.debug("listing timesheets ... " + searchCriteria + " by page " + pageable);

        Page<Person> page = null;
        try {
            if (searchCriteria != null) {
                Specification<Person> spec = SpecificationBuilder.findBy(searchCriteria);
                page = personRepository.findAll(spec, pageable);
            } else {
                page = personRepository.findAll(pageable);
            }
            log.info("fetched " + page);
        } catch (Exception e) {
            throw new ServiceException(e.getMessage(), e);
        }
        return page;
    }

    @Override
    public Page<Person> listByQueryParams(Map<String, String> params) throws ServiceException {
        SearchCriteria criteria = new SearchCriteria();
        for (String key: params.keySet()) {
            if (pagingParams.contains(key)) { // ignore paging params
                continue;
            }
            String value = params.get(key);
            SearchCriteria.Operation op;
            String field = key;
            if (key.contains(":")) {
                int index = key.lastIndexOf(":");
                field = key.substring(0, index);
                if (! Util.isFieldDefined(Person.class, field) ) {
                    continue;
                }
                String o = key.substring(index + 1);
                try {
                    op = SearchCriteria.Operation.valueOf(o);
                } catch (Exception e) {
                    throw new InvalidFilterOperationException(o
                            + " is an invalid operation, the available ones are "
                            + Arrays.asList(SearchCriteria.Operation.values()));
                }
            } else {
                op = SearchCriteria.Operation.eq;
            }

            criteria.addFilter(new SearchCriteria.Filter(field, op, value));
        }
        String sort = null;
        Integer page = null;
        Integer size = null;
        if (params != null) {
            sort = params.get("sort");
            String sPage = params.get("page");
            if (sPage != null) {
                page = Integer.valueOf(sPage);
            }
            String sSize = params.get("size");
            if (sSize != null) {
                size = Integer.valueOf(sSize);
            }
        }
        return listByCriteria(criteria, sort, page, size);
    }

    @Override
    public Page<Person> listByCriteriaAsJson(String criteriaJson, String sort, Integer page, Integer size) throws ServiceException {
        SearchCriteria criteria = Util.fromJson(criteriaJson, SearchCriteria.class);
        return listByCriteria(criteria, sort, page, size);
    }

    @Override
    public boolean store(List<Person> persons) throws ServiceException {
        try {
            personRepository.saveAll(persons);
        } catch (Exception e) {
            throw new ServiceException(e.getMessage(), e);
        }
        return true;
    }

}
