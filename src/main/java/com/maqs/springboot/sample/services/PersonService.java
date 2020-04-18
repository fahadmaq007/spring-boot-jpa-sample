package com.maqs.springboot.sample.services;

import com.maqs.springboot.sample.dto.SearchCriteria;
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

import java.util.Map;

@Service
@Slf4j
public class PersonService implements IPersonService {

    @Autowired
    private PersonRepository personRepository;

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
    public Page<Person> listByFilters(Map<String, String> filters, String sort, Integer pageIndex, Integer pageSize) throws ServiceException {
        SearchCriteria criteria = new SearchCriteria();
        for (String key: filters.keySet()) {
            String v = filters.get(key);
            log.debug(key + " " + v + " " + v.getClass() );
            SearchCriteria.Operation op;
            String field = key;
            Object value = v;
            if (key.contains(":")) {
                int index = key.lastIndexOf(":");
                field = key.substring(0, index);
                String o = key.substring(index + 1);
                //TODO: handle exception
                op = SearchCriteria.Operation.valueOf(o.toUpperCase());
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
        return list(criteria, sort, pageIndex, pageSize);
    }
}
