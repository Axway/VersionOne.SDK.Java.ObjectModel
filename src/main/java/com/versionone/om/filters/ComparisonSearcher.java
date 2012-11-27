/*(c) Copyright 2008, VersionOne, Inc. All rights reserved. (c)*/
package com.versionone.om.filters;

import com.versionone.apiclient.FilterTerm;
import static com.versionone.apiclient.FilterTerm.Operator.Exists;
import static com.versionone.apiclient.FilterTerm.Operator.GreaterThan;
import static com.versionone.apiclient.FilterTerm.Operator.GreaterThanOrEqual;
import static com.versionone.apiclient.FilterTerm.Operator.LessThan;
import static com.versionone.apiclient.FilterTerm.Operator.LessThanOrEqual;
import static com.versionone.apiclient.FilterTerm.Operator.NotExists;

import java.util.HashMap;

/**
 * Specifies a set of filter expressions to compare specified attribute with. To specify >, <, >=, <=, =, != condition
 * used {@link com.versionone.apiclient.FilterTerm.Operator}.
 */
public class ComparisonSearcher<T extends Comparable<T>> implements IComparisonSearcher<T> {

    private HashMap<FilterTerm.Operator, T> terms = new HashMap<FilterTerm.Operator, T>();

    public void addTerm(FilterTerm.Operator op) throws IllegalStateException {
        addTerm(op, null);
    }

    public void addTerm(FilterTerm.Operator op, T value) throws IllegalStateException {
        final boolean operatorNotExists = !terms.containsKey(op);
        final T old = operatorNotExists ? null : terms.get(op);
        switch (op) {
            case Exists:
                if (terms.containsKey(NotExists)) {
                    throw new IllegalStateException("Cannot contain both " + op + " and " + NotExists + " terms.");
                }
                if (operatorNotExists) {
                    terms.put(op, value);
                }
                break;
            case NotExists:
                if (terms.containsKey(Exists)) {
                    throw new IllegalStateException("Cannot contain both " + op + " and " + Exists + " terms.");
                }
                if (operatorNotExists) {
                    terms.put(op, value);
                }
                break;
            case Equal:
            case NotEqual:
                if (operatorNotExists) {
                    terms.put(op, value);
                } else {
                    throw new IllegalStateException("Cannot contain more than one " + op + " term.");
                }
                break;
            case GreaterThan:
            case GreaterThanOrEqual:
                if (operatorNotExists) {
                    terms.put(op, value);
                } else if (old.compareTo(value) < 0) { // new > old
                    terms.put(op, value);
                }
                break;
            case LessThan:
            case LessThanOrEqual:
                if (operatorNotExists) {
                    terms.put(op, value);
                } else if (old.compareTo(value) > 0) { // new < old
                    terms.put(op, value);
                }
                break;
        }
    }

    public void range(T min, T max) {
        if(min == null || max == null) {
            throw new IllegalArgumentException("Range bound values cannot be null");
        }

        if(min.compareTo(max) > 0) {
            throw new IllegalArgumentException("Lower bound of a range cannot exceed upper bound");
        }

        addTerm(FilterTerm.Operator.GreaterThanOrEqual, min);
        addTerm(FilterTerm.Operator.LessThanOrEqual, max);
    }

    public void clear() {
        terms.clear();
    }

    private void optimize() {
        final T less = terms.get(LessThan);
        final T lessOrEqual = terms.get(LessThanOrEqual);
        if (less != null && lessOrEqual != null) {
            if (lessOrEqual.compareTo(less) >= 0) {
                terms.remove(LessThanOrEqual);
            } else {
                terms.remove(LessThan);
            }
        }

        final T greater = terms.get(GreaterThan);
        final T greaterOrEqual = terms.get(GreaterThanOrEqual);
        if (greater != null && greaterOrEqual != null) {
            if (greaterOrEqual.compareTo(greater) <= 0) {
                terms.remove(GreaterThanOrEqual);
            } else {
                terms.remove(GreaterThan);
            }
        }
    }

    public HashMap<FilterTerm.Operator, T> getTerms() {
        optimize();
        return terms;
    }
}
