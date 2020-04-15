package com.maqs.springboot.sample.dto;

import java.util.ArrayList;
import java.util.List;

public class SearchCriteria {

    private List<Clause> clauses;

    public SearchCriteria() {

    }

    public SearchCriteria(List<Clause> clauses) {
        this.clauses = clauses;
    }

    public void setClauses(List<Clause> clauses) {
        this.clauses = clauses;
    }

    public void addClause(Clause c) {
        if (this.clauses == null) {
            this.clauses = new ArrayList<>();
        }
        this.clauses.add(c);
    }

    public List<Clause> getClauses() {
        return clauses;
    }

    @Override
    public String toString() {
        return "SearchCriteria{" +
                "clauses=" + clauses +
                '}';
    }

    public static class Clause {
        String field;
        Operation op;
        Object value;

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
            return "Clause{" +
                    "field='" + field + '\'' +
                    ", op=" + op +
                    ", value=" + value +
                    '}';
        }
    }


    public enum Operation {
        EQ, NE,

        LIKE,

        LT, GT, LTE, GTE,

        BTW
    }
}

