package com.maqs.springboot.sample.repository;

import com.maqs.springboot.sample.dto.SearchCriteria;
import com.maqs.springboot.sample.exceptions.InvalidFilterOperationException;
import com.maqs.springboot.sample.util.Util;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

/**
 * The JPA {@link Specification} builder according the given {@link SearchCriteria}
 */
@Slf4j
public class SpecificationBuilder {

    public static <T> Specification<T> findBy(final SearchCriteria searchCriteria) {
        return new Specification<T>() {
            @Override
            public Predicate toPredicate(
                    Root<T> root,
                    CriteriaQuery<?> query, CriteriaBuilder builder) {
                List<Predicate> predicates = new ArrayList<>();
                if (searchCriteria != null) {
                    List<SearchCriteria.Filter> filterList = searchCriteria.getFilters();
                    if (filterList != null) {
                        for (SearchCriteria.Filter filter : filterList) {
                            Predicate p = buildPredicate(root, builder, filter);
                            if (p != null) {
                                predicates.add(p);
                            }
                        }
                    }
                }
                return builder.and(predicates.toArray(new Predicate[] {}));
            }
        };
    }

    /**
     * Builds the {@link Predicate} for each of the {@link com.maqs.springboot.sample.dto.SearchCriteria.Filter}.
     * @param root
     * @param builder
     * @param c
     * @param <T>
     * @return
     */
    public static <T> Predicate buildPredicate(Root<T> root,
                                               CriteriaBuilder builder,
                                               SearchCriteria.Filter c) {
        String fieldName = c.getField();
        SearchCriteria.Operation op = c.getOp();
        if (op == null) {
            op = SearchCriteria.Operation.eq;
        }
        Object value = c.getValue();
        Path expression = root.get(fieldName);
        Class<?> fieldClass = expression.getJavaType();
        Object classValue = Util.getValue(value, fieldClass);
        if (classValue != null) {
            value = classValue;
        }
        switch (op) {
            case eq:
                return builder.equal(expression, value);
            case ne:
                return builder.notEqual(expression, value);
            case cn:
            case like:
                return builder.like((Expression<String>) expression, "%" + value + "%");
            case sw:
                return builder.like((Expression<String>) expression, value + "%");
            case ew:
                return builder.like((Expression<String>) expression, "%" + value);
            case lt:
                return builder.lessThan(expression, (Comparable) value);
            case gt:
                return builder.greaterThan(expression, (Comparable) value);
            case le:
                return builder.lessThanOrEqualTo(expression, (Comparable) value);
            case ge:
                return builder.greaterThanOrEqualTo(expression, (Comparable) value);
            case btw:
                return createRangePredicate(builder, expression, value);
            default:
                return null;
        }
    }

    private static <Y extends Comparable<? super Y>> Predicate createRangePredicate(
            CriteriaBuilder builder, Expression field, Object value ) {
        if (value != null) {
            Object[] array = null;
            if (value instanceof List) {
                array = ((List)value).toArray();
            } else if (value instanceof Object[]) {
                array = (Object[]) value;
            } else {
                throw new InvalidFilterOperationException(SearchCriteria.Operation.btw + " expects a range with atleast two values as an Array or a List. For eg. 'value': [20, 30]");
            }
            Y start = null;
            Y end = null;
            if (array != null && array.length > 0) {
                start= (Y) array[0];
                if (array.length > 1) {
                    end = (Y) array[1];
                }
            }
            return createRangePredicate(builder, field, start, end);
        }
        return null;
    }

    private static <Y extends Comparable<? super Y>> Predicate createRangePredicate(
            CriteriaBuilder builder, Expression field, Object start, Object end ) {
        log.debug("applied range predicate <start, end>: " + " <" + start + ", " + end + ">");
        if( start != null && end != null ) {
            return builder.between(field, (Y) start, (Y) end);
        } else if ( start != null ) {
            return builder.greaterThanOrEqualTo(field, (Y) start);
        } else if ( end != null ) {
            return builder.lessThanOrEqualTo(field, (Y) end);
        }

        log.error("No range predicate is created as the values are null");
        return null;
    }

}

