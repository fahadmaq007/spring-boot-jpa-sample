package com.maqs.springboot.sample.services;

import com.maqs.springboot.sample.dto.SearchCriteria;
import com.maqs.springboot.sample.exceptions.InvalidFilterOperationException;
import com.maqs.springboot.sample.exceptions.ServiceException;
import com.maqs.springboot.sample.model.Person;
import com.maqs.springboot.sample.repository.PersonRepository;
import com.maqs.springboot.sample.repository.SpecificationBuilder;
import com.maqs.springboot.sample.util.Util;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

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
    public Page<Person> list(SearchCriteria searchCriteria, String sort, Integer pageIndex, Integer pageSize) throws ServiceException {
        Pageable pageable = Util.getPageRequest(sort, pageIndex, pageSize);
        log.debug("listing timesheets ... " + searchCriteria);

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
            String v = params.get(key);
            SearchCriteria.Operation op;
            String field = key;
            Object value = v;
            if (key.contains(":")) {
                int index = key.lastIndexOf(":");
                field = key.substring(0, index);
                String o = key.substring(index + 1).toUpperCase();
                try {
                    op = SearchCriteria.Operation.valueOf(o);
                } catch (Exception e) {
                    throw new InvalidFilterOperationException(o
                            + " is not available in " + SearchCriteria.Operation.values());
                }
            } else {
                op = SearchCriteria.Operation.EQ;
            }
            if (op == SearchCriteria.Operation.BTW) {
                if (v.contains(",")) {
                    String[] strs = v.split(",");
                    Object o1 = strs[0];
                    Object o2 = null;
                    if (StringUtils.isNumeric((String) o1)) {
                        o1 = Long.valueOf((String) o1);
                    }
                    if (strs[1] != null && strs[1].length() > 0) {
                        o2 = strs[1];
                        if (StringUtils.isNumeric((String) o2)) {
                            o2 = Long.valueOf((String) o2);
                        }
                    }
                    value = new Object[] { o1, o2 };
                }
            } else {
                if (StringUtils.isNumeric(v)) {
                    value = Long.valueOf(v);
                }
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
        return list(criteria, sort, page, size);
    }

    @Override
    public Page<Person> list(String criteriaJson, String sort, Integer page, Integer size) throws ServiceException {
        SearchCriteria criteria = Util.fromJson(criteriaJson, SearchCriteria.class);
        return list(criteria, sort, page, size);
    }

}
