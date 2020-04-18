package com.maqs.springboot.sample.util;

import com.google.gson.Gson;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

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
            } else if (value instanceof Date) {
                return (Date) value;
            }
        } catch (ParseException e) {
            throw new IllegalArgumentException(e.getMessage(), e);
        }
        return null;
    }

    /**
     * Compare the order of list is by given field in both lists.
     *
     * @param field Field to look for
     * @param l1 First list
     * @param l2 Second list
     * @param <T> Any entity that has a declared 'field' param.
     * @return true if all the elements match the both list otherwise false.
     */
    public static <T> boolean compare(String field, List<T> l1, List<T> l2) {
        if (l1.size() != l2.size()) {
            return false;
        }
        try {
            for (int i = 0; i < l1.size(); i++) {
                T t1 = l1.get(i);
                T t2 = l2.get(i);

                Field f1 = t1.getClass().getDeclaredField(field);
                f1.setAccessible(true);
                Field f2 = t2.getClass().getDeclaredField(field);
                f2.setAccessible(true);
                Object o1 = f1.get(t1);
                Object o2 = f2.get(t2);
                if (! o1.equals(o2)) {
                    return false;
                }
            }
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage(), e);
        }

        return true;
    }

    /**
     * Traverses the list to see the given order is maintained.
     *
     * @param field Field to look for
     * @param list List of entities
     * @param asc true if expected to be in ascending order, otherwise false.
     * @param <T> Any entity that has a declared 'field' param.
     *
     * @return true if is in given order, otherwise false.
     */
    public static <T> boolean isInOrder(String field, List<T> list, boolean asc) {
        if (list == null || list.isEmpty()) {
            return true;
        }
        try {
            Comparable prev = null;
            for (int i = 0; i < list.size(); i++) {
                T t1 = list.get(i);

                Field f1 = t1.getClass().getDeclaredField(field);
                f1.setAccessible(true);
                Comparable current = (Comparable) f1.get(t1);

                if (asc) {
                    if (prev != null && prev.compareTo(current) > 0) {
                        return false;
                    }
                } else {
                    if (prev != null && prev.compareTo(current) < 0) {
                        return false;
                    }
                }
                prev = current;
            }
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage(), e);
        }

        return true;
    }

    /**
     * Traverses the list to see whether the given field has only given value in it.
     * For eg. status field having value 10: If the status has other than 10, it returns false.
     *
     * @param field Field to look for
     * @param value Value of the field
     * @param list List of entities
     * @param <T> Any entity that has a declared 'field' param.
     *
     * @return true if the list has only the given criteria, otherwise false.
     */
    public static <T> boolean hasOnlyGivenCriteria(String field, Object value, List<T> list) {
        if (list == null || list.isEmpty()) {
            return true;
        }
        try {
            for (int i = 0; i < list.size(); i++) {
                T t1 = list.get(i);

                Field f1 = t1.getClass().getDeclaredField(field);
                f1.setAccessible(true);
                Object current = f1.get(t1);
                if (current != null && ! current.equals(value)) {
                    return false;
                }
            }
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage(), e);
        }

        return true;
    }

    /**
     * Generates the json string.
     *
     * @param src
     *            source object to be converted.
     * @return json string
     */
    public static String toJson(Object src) {
        Gson gson = new Gson();
        return gson.toJson(src);
    }

    /**
     * Generates the entity of a given class from json.
     *
     * @param json
     *            json string
     * @param clazz
     *            entity's class to be converted to.
     * @return entity
     */
    public static <T> T fromJson(String json, Class<T> clazz) {
        Gson gson = new Gson();
        return gson.fromJson(json, clazz);
    }
}
