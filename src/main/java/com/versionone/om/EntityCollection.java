/*(c) Copyright 2008, VersionOne, Inc. All rights reserved. (c)*/
package com.versionone.om;

import java.util.AbstractCollection;
import java.util.Iterator;

public class EntityCollection<D extends Entity> extends AbstractCollection<D> {

    private final V1Instance instance;
    private final Entity entity;
    private final String readAttributeName;
    private final String writeAttributeName;

    public EntityCollection(V1Instance instance, Entity entity,
            String readAttributeName, String writeAttributeName) {
        this.instance = instance;
        this.entity = entity;
        this.readAttributeName = readAttributeName;
        this.writeAttributeName = writeAttributeName;
    }

    /**
     * Add item to collection.
     *
     * @param item item for addition (must be Entity or subclass).
     * @return if item add then will return true in the other case false.
     * @throws UnsupportedOperationException if object is read only.
     */
    @Override
    public boolean add(D item) throws UnsupportedOperationException {
        readOnlyGuardCondition();
        instance.addRelation(entity, writeAttributeName, item);
        entity.save();
        return true;
    }

    /**
     * Removes all of the elements from this collection.
     *
     * @throws UnsupportedOperationException if object is read only.
     * @see java.util.Collection#clear().
     */
    @Override
    public void clear() {
        readOnlyGuardCondition();

        for (D item : this) {
            instance.removeRelation(entity, writeAttributeName, item);
        }
        entity.save();
    }

    @Override
    public boolean contains(Object item) throws IllegalArgumentException {
        if (!(item instanceof Entity)) {
            throw new IllegalArgumentException();
        }
        return instance.multiRelationContains(entity, readAttributeName,
                (Entity) item);
    }

    public void copyTo(D[] array, int arrayIndex)
            throws IndexOutOfBoundsException, IllegalArgumentException {
        if (arrayIndex < 0) {
            throw new IndexOutOfBoundsException(
                    "arrayIndex cannot be less than 0: " + arrayIndex);
        }

        if (array == null) {
            throw new IllegalArgumentException("array must not be null");
        }

        if (array.length < arrayIndex) {
            throw new IllegalArgumentException(
                    "arrayIndex is greater than the length of the array.");
        }

        if ((arrayIndex + size()) > array.length) {
            throw new IllegalArgumentException(
                    "The number of elements is greater than the length "
                            + "of the array. Array length:" + array.length
                            + "; arrayIndex:" + arrayIndex
                            + "; Count:" + size()
                            + ".");
        }

        for (D entity : this) {
            array[arrayIndex++] = entity;
        }
    }

    @Override
    public boolean remove(Object item) throws UnsupportedOperationException {
        readOnlyGuardCondition();

        if (contains(item)) {
            instance.removeRelation(entity, writeAttributeName, (Entity) item);
            entity.save();
            return true;
        }
        return false;
    }

    @Override
    public int size() {
        return instance.getMultiRelationCount(entity, readAttributeName);
    }

    public boolean isReadOnly() {
        return instance.isMultiRelationIsReadOnly(entity, writeAttributeName);
    }

    @Override
    public Iterator<D> iterator() {
        return (Iterator<D>) instance.internalGetMultiRelation(entity,
                readAttributeName, null).iterator();
    }

    /**
     * Verification on read only status.
     *
     * @throws UnsupportedOperationException if object is read only.
     */
    private void readOnlyGuardCondition() throws UnsupportedOperationException {
        if (isReadOnly()) {
            throw new UnsupportedOperationException(
                    "The collection is Read-only");
        }
    }

    public EntityCollection<D> asReadOnly() {
        return new ReadOnly<D>(instance, entity, readAttributeName,
                writeAttributeName);
    }

    private class ReadOnly<T extends Entity> extends EntityCollection<T> {

        public ReadOnly(V1Instance instance, Entity entity,
                String readAttributeName, String writeAttributeName) {
            super(instance, entity, readAttributeName, writeAttributeName);
        }

        @Override
        public boolean isReadOnly() {
            return true;
        }
    }
}
