/*(c) Copyright 2008, VersionOne, Inc. All rights reserved. (c)*/
package com.versionone.om.filters;

import com.versionone.DB.DateTime;
import com.versionone.apiclient.AttributeSelection;
import com.versionone.apiclient.IAssetType;
import com.versionone.apiclient.IAttributeDefinition;
import com.versionone.apiclient.IFilterTerm;
import com.versionone.apiclient.OrderBy;
import com.versionone.apiclient.QueryFind;
import com.versionone.om.Entity;
import com.versionone.om.MetaRenamedAttribute;
import com.versionone.om.V1Instance;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * Base class of all filters.
 * <p/>
 * There is some feature with filtering by
 * {@link #createDateUtc} or {@link #changeDateUtc}.
 * <p/>
 * When Entity date and filtering date differs less then
 * one second, the results are undefined. E.g. when
 * filtering by ChangeDateUTC>'2009-02-17 14:45:30.182'
 * an Asset with ChangeDateUTC='2009-02-17T14:45:30.183'  |
 * is not returning.
 * <p/>
 * When trying to filter out Assets by ChangeDateUTC
 * GreaterThan some Asset's ChangeDateUTC, result may
 * includes this Asset.
 * (Like in case of using GreaterThanOrEqual).
 * So better not to differ GreaterThan and
 * GreaterThanOrEqual or LessThan and LessThanOrEqual.
 * <p/>
 * The same situation with createDateUtc.
 */
public abstract class EntityFilter {

    private static final boolean USE_ARRAY_LISTS = false;
    private static final int INITIAL_ARRAY_LISTS_SIZE = 10;

    protected static <T> List<T> newList() {
        if (USE_ARRAY_LISTS) {
            return new ArrayList<T>(INITIAL_ARRAY_LISTS_SIZE);
        }
        return new LinkedList<T>();
    }

    /**
     * Specifies a string to search for and an optional set of fields to search
     * in.
     */
    public static class StringSearcher {
        private String searchString;

        /**
         * Specify the fields to search in. Add the names of any string fields
         * on the assets you are searching. If not set, the search will search
         * in Name and Description.
         */
        public final Collection<String> fields = newList();

        /**
         * @return The string to search for.
         */
        public String getSearchString() {
            return searchString;
        }

        /**
         * @param value The string to search for.
         */
        public void setSearchString(String value) {
            searchString = value;
        }
    }

    /**
     * Used by the filtering code to know what types of entities to return when
     * applying the filter.
     *
     * @return class of Entity filter used for.
     */
    public abstract Class<? extends Entity> getEntityType();

    /**
     * Add the names of the properties you wish to sort on. The first in the
     * list is the primary sort, the second is the secondary sort, etc.
     */
    public final List<String> orderBy = newList();

    /**
     * Specify the fields to search in. Add the names of any string (free text)
     * fields on the assets you are searching. If not set, the search will
     * search in Name, Description, and ShortName if there is one.
     */
    public final StringSearcher find = new StringSearcher();

    /**
     * Filtering conditions that restrict creation date of Entity.
     */
    public final DateSearcher createDateUtc = new DateSearcher();

    /**
     * Filtering conditions that restrict modification date of Entity.
     */
    public final DateSearcher changeDateUtc = new DateSearcher();

    /**
     * Checks existance of {@link MetaRenamedAttribute} annotation of getter
     * method of specified property of corresponding Entity class. If annotation
     * found, returning its realName() value. If there is no corresponding
     * getter with this annotation defined - returning passed propertyName
     * parameter.
     *
     * @param propertyName name of the property to find getter for
     * @return resolved name if annotation was found, or passed propertyName
     *         parameter - otherwise.
     */
    private String resolvePropertyName(String propertyName) {
        final Class<? extends Entity> clazz = getEntityType();
        final String methodName = getterName(propertyName);
        final Method[] methods = clazz.getDeclaredMethods();

        for (Method method : methods) {

            if (method.getName().equals(methodName)) {
                MetaRenamedAttribute renamed = method
                        .getAnnotation(MetaRenamedAttribute.class);

                if (renamed != null) {
                    return renamed.value();
                }
            }
        }

        return propertyName;
    }

    /**
     * Returns a getter method name by property name. <p/> Ex. "type" ->
     * "getType".
     *
     * @param propertyName name of the property.
     * @return name of the getter method.
     */
    private static String getterName(String propertyName) {
        if ((propertyName == null) || (propertyName.length() == 0)) {
            return propertyName;
        }
        return "get" + Character.toUpperCase(propertyName.charAt(0))
                + propertyName.substring(1);
    }

    /**
     * Create representation one filter term on a query.
     *
     * @param assetType information about Asset type.
     * @param instance The type this filter belongs to.
     * @return created {@code IFilterTerm}.
     */
    public IFilterTerm buildFilter(IAssetType assetType, V1Instance instance) {
        FilterBuilder builder = new FilterBuilder(assetType, instance);

        internalModifyFilter(builder);
        internalModifyState(builder);
        return builder.root.hasTerm() ? builder.root : null;
    }

    void internalModifyFilter(FilterBuilder builder) {
    	builder.comparison(Entity.CREATE_DATE_UTC, createDateUtc);
    	builder.comparison(Entity.CHANGE_DATE_UTC, changeDateUtc);
    }

    void internalModifyState(FilterBuilder builder) {
    }

    /**
     * Create {@code OrderBy} object with information about ordering.
     *
     * @param assetType      information about Asset type.
     * @param defaultOrderBy defines methods for the definition of a VersionOne Attribute.
     * @return created {@code OrderBy}.
     */
    public OrderBy buildOrderBy(IAssetType assetType,
                                IAttributeDefinition defaultOrderBy) {
        OrderBy order = new OrderBy();

        if (orderBy.size() > 0) {

            for (String s : orderBy) {
                order.minorSort(assetType
                        .getAttributeDefinition(resolvePropertyName(s)),
                        OrderBy.Order.Ascending);
            }
        } else {
            if (defaultOrderBy != null)
                order.minorSort(defaultOrderBy, OrderBy.Order.Ascending);
        }
        return order;
    }

    /**
     * Create {@code QueryFind} by {@code assetType} and {@code EntityFilter.find}.
     *
     * @param assetType information about Asset type.
     * @return created object {@code QueryFind}.
     */
    public QueryFind buildFind(IAssetType assetType) {
        String searchString = find.getSearchString();

        if ((searchString != null) && (searchString.trim().length() != 0)) {
            AttributeSelection attributes = new AttributeSelection();

            if (find.fields.size() > 0) {
                for (String field : find.fields) {
                    attributes
                            .add(assetType
                                    .getAttributeDefinition(resolvePropertyName(field)));
                }
            } else {
                if (assetType.getShortNameAttribute() != null) {
                    attributes.add(assetType.getShortNameAttribute());
                }
                if (assetType.getNameAttribute() != null) {
                    attributes.add(assetType.getNameAttribute());
                }
                if (assetType.getDescriptionAttribute() != null) {
                    attributes.add(assetType.getDescriptionAttribute());
                }
            }
            return new QueryFind(find.getSearchString(), attributes);
        }
        return null;
    }
}