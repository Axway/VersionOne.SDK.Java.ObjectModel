/*(c) Copyright 2008, VersionOne, Inc. All rights reserved. (c)*/
package com.versionone.om;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Name of the data object for creating request URL to server.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface MetaDataAttribute {
    static final String NULL = "";

    String value() default NULL;

    /**
     * @return list of names separated by comma.
     */
    String defaultAttributeSelectionNames() default NULL;

    String defaultOrderByToken() default NULL;

    short assetState() default -1;
}
