package com.maqs.springboot.sample.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * The Search Criteria DTO, encapsulates the list of {@link Filter}, can have one or more filters.
 *
 * @author maqbool
 */
public class SearchCriteria {

    private List<Filter> filters;

    public SearchCriteria() {

    }

    public SearchCriteria(List<Filter> filters) {
        this.filters = filters;
    }

    public void setFilters(List<Filter> filters) {
        this.filters = filters;
    }

    public void addFilter(Filter c) {
        if (this.filters == null) {
            this.filters = new ArrayList<>();
        }
        this.filters.add(c);
    }

    public List<Filter> getFilters() {
        return filters;
    }

    @Override
    public String toString() {
        return "SearchCriteria{" +
                "filters=" + filters +
                '}';
    }

    /**
     * Use this class to define the filter criteria.
     * For eg. {"field": "age", "op": "gt", "value":30}
     */
    public static class Filter {
        String field;
        Operation op = Operation.eq;
        Object value;

        public Filter() { }

        public Filter(String field, Object value) {
            this(field, Operation.eq, value);
        }

        public Filter(String field, Operation op, Object value) {
            setField(field);
            setOp(op);
            setValue(value);
        }

        public String getField() {
            return field;
        }

        public void setField(String field) {
            this.field = field;
        }

        public Operation getOp() {
            return op;
        }

        public void setOp(Operation op) {
            this.op = op;
        }

        public Object getValue() {
            return value;
        }

        public void setValue(Object value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return "Filter{" +
                    "field='" + field + '\'' +
                    ", op=" + op +
                    ", value=" + value +
                    '}';
        }
    }


    /**
     * The self explanatory Operations
     */
    public enum Operation {
        /**
         * equalTo & notEqualTo
         */
        eq, ne,

        /**
         * Text operaions: Like / Contains text
         */
        cn, // Contains
        like, // Like
        sw, // startsWith
        ew, // endsWith

        /**
         * LessThan, GreaterThan, LessThanOrEqualTo & GreaterThanOrEqualTo
         */
        lt, gt, le, ge,

        /**
         * Between range.
         * For eg. { "field": "dob", "op": "btw", "value": [1544725100000, 1544725900000 ]}
         * Fetch records between a range of two dates. The date can be a String ("2020-03-03"),
         * a Long (timeInMillis) or an instance of {@link java.util.Date} itself.
         *
         * The range can also be a normal on usual Strings or Numbers.
         * For eg.
         * { "field": "age", "op": "btw", "value": [15, 25 ]} - Age between 15 to 25
         * { "field": "name", "op": "btw", "value": ["a", "f" ]} - Name between 'a' & 'f'
         */
        btw
    }
}

