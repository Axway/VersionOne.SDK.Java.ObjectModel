package com.versionone.om.tests;

import com.versionone.DB.DateTime;
import com.versionone.apiclient.FilterTerm.Operator;
import static com.versionone.apiclient.FilterTerm.Operator.GreaterThan;
import static com.versionone.apiclient.FilterTerm.Operator.GreaterThanOrEqual;
import static com.versionone.apiclient.FilterTerm.Operator.LessThan;
import static com.versionone.apiclient.FilterTerm.Operator.LessThanOrEqual;
import com.versionone.om.filters.ComparisonSearcher;

import org.junit.Assert;
import org.junit.Test;

import java.util.Calendar;
import java.util.HashMap;

public class ComparisonSearcherTester {

    @Test
    public void optimize() {
        ComparisonSearcher<DateTime> comparison = new ComparisonSearcher<DateTime>();
        DateTime date1 = DateTime.now();
        DateTime date2 = date1.add(Calendar.SECOND, 2);

        // date1 < date2
        comparison.addTerm(LessThan, date1);
        comparison.addTerm(LessThanOrEqual, date2);
        HashMap<Operator, DateTime> terms = comparison.getTerms();
        Assert.assertEquals(LessThan, terms.keySet().iterator().next());
        Assert.assertEquals(date1, terms.values().iterator().next());

        // date1 < date2
        comparison = new ComparisonSearcher<DateTime>();
        comparison.addTerm(LessThanOrEqual, date1);
        comparison.addTerm(LessThan, date2);
        terms = comparison.getTerms();
        Assert.assertEquals(LessThanOrEqual, terms.keySet().iterator().next());
        Assert.assertEquals(date1, terms.values().iterator().next());

        // date1 > date2
        comparison = new ComparisonSearcher<DateTime>();
        comparison.addTerm(GreaterThan, date1);
        comparison.addTerm(GreaterThanOrEqual, date2);
        terms = comparison.getTerms();
        Assert.assertEquals(GreaterThanOrEqual, terms.keySet().iterator().next());
        Assert.assertEquals(date2, terms.values().iterator().next());

        // date1 > date2
        comparison = new ComparisonSearcher<DateTime>();
        comparison.addTerm(GreaterThanOrEqual, date1);
        comparison.addTerm(GreaterThan, date2);
        terms = comparison.getTerms();
        Assert.assertEquals(GreaterThan, terms.keySet().iterator().next());
        Assert.assertEquals(date2, terms.values().iterator().next());
    }
}
