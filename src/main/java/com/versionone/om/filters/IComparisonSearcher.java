package com.versionone.om.filters;

import com.versionone.apiclient.FilterTerm;
import java.util.Map;

/**
 * Comparison searcher that lets specify complex search constraints.
 *
 **/
public interface IComparisonSearcher<T> {
    /// <summary>
    /// Return optimized term collection.
    /// </summary>
    Map<FilterTerm.Operator, T> getTerms();

    /// <summary>
    /// Add new filtering term.
    /// </summary>
    /// <param name="op">Filtering operator.</param>
    /// <param name="value">Value to compare with.</param>
    void addTerm(FilterTerm.Operator op, T value);

    /// <summary>
    /// A shortcut for setting up range search, from minimum to maximum.
    /// </summary>
    /// <param name="min">Minimum value.</param>
    /// <param name="max">Maximum value.</param>
    void range(T min, T max);

    /// <summary>
    /// Clear internal term collection.
    /// </summary>
    void clear();
}