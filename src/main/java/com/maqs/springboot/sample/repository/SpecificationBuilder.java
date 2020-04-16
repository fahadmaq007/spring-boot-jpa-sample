package com.maqs.springboot.sample.repository;

import com.maqs.springboot.sample.dto.SearchCriteria;
import com.maqs.springboot.sample.util.Util;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
public class SpecificationBuilder {

    public static <T> Specification<T> findBy(final SearchCriteria searchCriteria) {

        return new Specification<T>() {

            @Override
            public Predicate toPredicate(
                    Root<T> root,
                    CriteriaQuery<?> query, CriteriaBuilder builder) {

                List<Predicate> predicates = new ArrayList<>();

                List<SearchCriteria.Clause> clauseList = searchCriteria.getClauses();
                if (clauseList != null) {
                    for (SearchCriteria.Clause clause: clauseList) {
                        Predicate p = buildPredicate(root, builder, clause);
                        if (p != null) {
                            predicates.add(p);
                        }
                    }
                }
                return builder.and(predicates.toArray(new Predicate[] {}));
            }
        };
    }

    public static <T> Predicate buildPredicate(Root<T> root,
                                 CriteriaBuilder builder,
                                 SearchCriteria.Clause c) {
        String fieldName = c.getField();
        SearchCriteria.Operation op = c.getOp();
        Object value = c.getValue();
        Path expression = root.get(fieldName);

        switch (op) {
            case EQ:
                return builder.equal(expression, value);
            case NE:
                return builder.notEqual(expression, value);
            case LIKE:
                return builder.like((Expression<String>) expression, "%" + value + "%");
            case LT:
                return builder.lessThan(expression, (Comparable) value);
            case GT:
                return builder.greaterThan(expression, (Comparable) value);
            case LTE:
                return builder.lessThanOrEqualTo(expression, (Comparable) value);
            case GTE:
                return builder.greaterThanOrEqualTo(expression, (Comparable) value);
            case BTW:
                return createRangePredicate(builder, expression, value);
            default:
                return null;
        }
    }

    private static <Y extends Comparable<? super Y>> Predicate createRangePredicate(
            CriteriaBuilder builder, Expression field, Object value ) {
        Class<?> fieldClass = field.getJavaType();
        if (value != null) {
            Object[] array = null;
            if (value instanceof List) {
                array = ((List)value).toArray();
            } else if (value instanceof Object[]) {
                array = (Object[]) value;
            }
            if (array.length >= 2) {
                Y start;
                Y end;
                if (Date.class == fieldClass) {
                    start = (Y) Util.parseDate(array[0]);
                    end = (Y) Util.parseDate(array[1]);
                } else {
                    start = (Y) array[0];
                    end = (Y) array[1];
                }
                return createRangePredicate(builder, field, start, end);
            }
        }
        return null;
    }

    private static <Y extends Comparable<? super Y>> Predicate createRangePredicate(
            CriteriaBuilder builder, Expression field, Object start, Object end ) {
        log.debug("applied range predicate on field: " + field + " <" + start + ", " + end + ">");
        if( start != null && end != null ) {
            return builder.between(field, (Y) start, (Y) end);
        } else if ( start != null ) {
            return builder.greaterThanOrEqualTo(field, (Y) start);
        } else {
            return builder.lessThanOrEqualTo(field, (Y) end);
        }
    }

}
