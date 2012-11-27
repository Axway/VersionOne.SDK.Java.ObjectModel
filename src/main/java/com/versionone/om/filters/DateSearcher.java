/*(c) Copyright 2008, VersionOne, Inc. All rights reserved. (c)*/
package com.versionone.om.filters;

import com.versionone.DB;
import com.versionone.apiclient.FilterTerm;
import static com.versionone.apiclient.FilterTerm.Operator.GreaterThanOrEqual;
import static com.versionone.apiclient.FilterTerm.Operator.LessThan;

import java.util.Calendar;
import static java.util.Calendar.MILLISECOND;
import java.util.GregorianCalendar;

/**
 * When SDK user add term:
 * <ul>
 * <li>date&gt;YYYY-MM-DD HH:MM:SS.TTT we build query: date&gt;=YYYY-MM-DD HH:MM:(SS+1)
 * <li>date&gt;=YYYY-MM-DD HH:MM:SS.TTT we build query: date&gt;=YYYY-MM-DD HH:MM:SS
 * <li>date&lt;YYYY-MM-DD HH:MM:SS.TTT we build query: date&lt;YYYY-MM-DD HH:MM:SS
 * <li>date&lt;=YYYY-MM-DD HH:MM:SS.TTT we build query: date&lt;YYYY-MM-DD HH:MM:(SS+1)
 * <li>date=YYYY-MM-DD HH:MM:SS.TTT we build query: date&gt;=YYYY-MM-DD HH:MM:SS & date&lt;YYYY-MM-DD HH:MM:(SS+1)
 * </ul>
 */
public class DateSearcher extends ComparisonSearcher<DB.DateTime> {

    /**
     * Adds comparison term. Only specified {@link com.versionone.apiclient.FilterTerm.Operator} supported:
     * <ul>
     * <li>GreaterThan
     * <li>GreaterThanOrEqual
     * <li>LessThan
     * <li>LessThanOrEqual
     * <li>equal
     * </ul>
     *
     * @param op   comparison operator.
     * @param date value to compare with.
     * @throws IllegalStateException if wrong operator or wrong operators composition supplied.
     */
    @Override
    public void addTerm(FilterTerm.Operator op, DB.DateTime date) throws IllegalStateException {
        final GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(date.getValue());
        cal.set(MILLISECOND, 0);
        switch (op) {
            case GreaterThan:
                cal.add(Calendar.SECOND, 1);
                super.addTerm(GreaterThanOrEqual, new DB.DateTime(cal.getTime()));
                break;
            case GreaterThanOrEqual:
                super.addTerm(GreaterThanOrEqual, new DB.DateTime(cal.getTime()));
                break;
            case LessThan:
                super.addTerm(LessThan, new DB.DateTime(cal.getTime()));
                break;
            case LessThanOrEqual:
                cal.add(Calendar.SECOND, 1);
                super.addTerm(LessThan, new DB.DateTime(cal.getTime()));
                break;
            case Equal:
                super.addTerm(GreaterThanOrEqual, new DB.DateTime(cal.getTime()));
                cal.add(Calendar.SECOND, 1);
                super.addTerm(LessThan, new DB.DateTime(cal.getTime()));
                break;
            default:
                throw new IllegalStateException("This operation is not supported: " + op);
        }
    }
}
