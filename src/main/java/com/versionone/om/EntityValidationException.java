package com.versionone.om;

import java.util.List;

public class EntityValidationException extends RuntimeException {
	private static final long serialVersionUID = 8600999229842945050L;
	/**
	 * Invalid attribute names.
	 */
	public final List<String> InvalidAttributeNames;
	/**
	 *  Entity that caused validation to fail.
	 */
    public final Entity Entity;

    /**
     * Validation exception constructor.
     * @param entity - Invalid entity.
     * @param invalidAttributeNames - Validation result - invalid attribute names.
     */
    public EntityValidationException(Entity entity, List<String> invalidAttributeNames) {
        Entity = entity;
        InvalidAttributeNames = invalidAttributeNames;
    }
}
