package com.maqs.springboot.sample.util;

import org.apache.commons.lang.time.DateUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.text.ParseException;
import java.util.Date;

public class Util {
    private Util() {}

    public static final String DATE_FORMAT = "yyyy-MM-dd";

    public static final String TIME_FORMAT = "HH:mm:ss";

    public static final String DATETIME_FORMAT = DATE_FORMAT + " " + TIME_FORMAT;

    public static final Integer DEFAULT_PAGE_INDEX = 0;

    public static final Integer DEFAULT_PAGE_SIZE = 50;

    public static Pageable getPageRequest(String sort, Integer pageIndex, Integer pageSize) {
        if (pageIndex == null) {
            pageIndex = DEFAULT_PAGE_INDEX;
        }
        if (pageSize == null) {
            pageSize = DEFAULT_PAGE_SIZE;
        }

        Sort sortBy = null;
        if (sort != null) {
            Sort.Direction d = Sort.Direction.ASC;
            String param = sort;
            if (sort.startsWith("-")) {
                d = Sort.Direction.DESC;
                param = sort.substring(1);
            }
            sortBy = Sort.by(d, param);

            return PageRequest.of(pageIndex, pageSize, sortBy);
        }
        return PageRequest.of(pageIndex, pageSize);
    }

    public static Date parseDate(Object value) {
        try {
            if (value instanceof String) {
                String date = (String) value;
                return DateUtils.parseDate(date, new String[] { DATE_FORMAT, DATETIME_FORMAT, TIME_FORMAT });
            } else if (value instanceof Long) {
                Long l = (Long) value;
                return new Date(l);
            }
        } catch (ParseException e) {
            throw new IllegalArgumentException(e.getMessage(), e);
        }
        return null;
    }
}
