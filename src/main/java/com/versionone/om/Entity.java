/*(c) Copyright 2008, VersionOne, Inc. All rights reserved. (c)*/
package com.versionone.om;

import com.versionone.DB.DateTime;
import com.versionone.om.filters.EntityFilter;
import com.versionone.om.listvalue.CustomListValue;
import com.versionone.om.listvalue.ListValue;
import com.versionone.util.V1Util;

import java.util.Collection;
import java.util.Date;

/**
 * Abstract representation of entities in the VersionOne system.
 */
public abstract class Entity {
    protected static final String DESCRIPTION_VALUE = "Description";
    protected static final String ASSET_VALUE = "Asset";
    protected static final String ASSET_STATE_VALUE = "AssetState";
    protected static final String CONTENT_TYPE_VALUE = "ContentType";
    protected static final String FILENAME_VALUE = "Filename";
    protected static final String NAME_VALUE = "Name";
    protected static final String CONTENT_VALUE = "Content";

    /**
     * Create date attribute.
     */
    public static final String CREATE_DATE_UTC = "CreateDateUTC";
    /**
     * Change date attribute.
     */
    public static final String CHANGE_DATE_UTC = "ChangeDateUTC";

    protected static final String DELETE_OPERATION = "Delete";

    private V1Instance instance;
    /**
     * ID of Entity on the server witch is represented by this object.
     */
    private AssetID assetID;
    private StubAssetID stubAssetID;

    /**
     * @return V1Instance this entity belongs to.
     */
    protected V1Instance getInstance() {
        return instance;
    }

    /**
     * Constructor used to represent an entity that DOES exist in the VersionOne
     * System.
     *
     * @param id Unique ID of this entity.
     * @param instance this entity belongs to.
     */
    protected Entity(AssetID id, V1Instance instance) {
        assetID = id;
        this.instance = instance;
    }

    /**
     * Constructor used to represent an entity that does NOT exist yet in the
     * VersionOne System.
     *
     * @param instance this entity belongs to.
     */
    protected Entity(V1Instance instance) {
        stubAssetID = new StubAssetID();
        this.instance = instance;
    }

    /**
     * Unique ID of this entity.
     *
     * @return id of this entity.
     */
    public AssetID getID() {
        return assetID;
    }

    Object getInstanceKey() {
        return (assetID != null) ? assetID : stubAssetID;
    }

    /**
     * @return Date this entity was last changed in UTC.
     */
    public DateTime getChangeDate() {
        return new DateTime(DateTime.convertUtcToLocal((Date) get(CHANGE_DATE_UTC, true)));
    }

    /**
     * @return Comment entered when this entity was last updated.
     */
    public String getChangeComment() {
        return (String) get("ChangeComment", false);
    }

    /**
     * @return Member or user that last changed this entity.
     */
    public Member getChangedBy() {
        return getRelation(Member.class, "ChangedBy", false);
    }

    /**
     * @return Date this entity was created in UTC.
     */
    public DateTime getCreateDate() {
        return new DateTime(DateTime.convertUtcToLocal((Date) get(CREATE_DATE_UTC, true)));
    }

    /**
     * @return Comment entered when this entity was created.
     */
    public String getCreateComment() {
        return (String) get("CreateComment", true);
    }

    /**
     * @return Member or user that created this entity.
     */
    public Member getCreatedBy() {
        return getRelation(Member.class, "CreatedBy", true);
    }

    String getEntityURL() {
        return instance.getEntityURL(this);
    }

    /**
     * Gets a simple value by name for this entity.
     *
     * @param name Name of the attribute.
     * @return An attribute value.
     */
    protected Object get(String name) {
        return instance.getProperty(this, name, true);
    }

    /**
     * Gets a simple value by name for this entity. Ignore cache if cachable is
     * false.
     *
     * @param name Name of the attribute.
     * @param cachable False if the attribute should not be cached.
     * @return An attribute value.
     */
    Object get(String name, boolean cachable) {
        return instance.getProperty(this, name, cachable);
    }

    /**
     * Sets a simple value by name for this entity.
     *
     * @param name Name of the attribute.
     * @param value The value to set the attribute to.
     */
    void set(String name, Object value) {
        instance.setProperty(this, name, value);
    }

    /**
     * Clears a cached attribute value.
     *
     * @param name Name of the attribute;
     *             if null, all attributes will be cleared from cache.
     */
    void clearCache(String name) {
        instance.clearCache(this, name);
    }

    /**
     * Get a total of an attribute thru a multi-relation possibly slicing by a
     * filter.
     *
     * @param multiRelationName
     * @param filter
     * @param numericAttributeName
     * @return total of an attribute.
     */
    Double getSum(String multiRelationName, EntityFilter filter,
            String numericAttributeName) {
        return instance.getSum(this, multiRelationName, filter,
                numericAttributeName);
    }

    /**
     * Get Rank attribute for this Entity.
     *
     * @param attributeName Name of the Rank attribute.
     * @return A Rank object.
     */
    Rank<? extends Entity> getRank(String attributeName) {
        return instance.getRank(this, attributeName);
    }

    /**
     * Get a relation by name for this entity.
     *
     * @param valuesClass Class type of T.
     * @param name Name of the relation attribute.
     * @return The related asset.
     */
    <T extends Entity> T getRelation(Class<T> valuesClass, String name) {
        return getRelation(valuesClass, name, true);
    }

    /**
     * Get a relation by name for this entity. Ignore cache if cachable is
     * false.
     *
     * @param valuesClass Class type of T.
     * @param name Name of the relation attribute.
     * @param cachable False if should not be cached.
     * @return The related asset.
     */
    <T extends Entity> T getRelation(Class<T> valuesClass, String name,
            boolean cachable) {
        return instance.getRelation(this, valuesClass, name, cachable);
    }

    /**
     * Sets a relation by name for this entity.
     *
     * @param name Name of the relation attribute.
     * @param value What to set the relation attribute to.
     */
    void setRelation(String name, Entity value) {
        instance.setRelation(this, name, value);
    }

    /**
     * Get a multi-value relation by name for this entity.
     *
     * @param name Name of the relation attribute.
     * @return IEntityCollection of T.
     */
    <T extends Entity> EntityCollection<T> getMultiRelation(String name) {
        return getMultiRelation(name, true);
    }

    /**
     * Get a multi-value relation by name for this entity. Ignore cache if
     * cachable is false.
     *
     * @param name Name of the relation attribute.
     * @param cachable False if should not be cached.
     * @return IEntityCollection of T.
     */
    <T extends Entity> EntityCollection<T> getMultiRelation(String name,
            boolean cachable) {
        return instance.getMultiRelation(this, name, cachable);
    }

    /**
     * Save any changes to this entity to the VersionOne System with a comment.
     *
     * @param comment Comment.
     * @throws DataException Thrown when a rule or security violation has
     *                 occurred.
	 * @throws ApplicationUnavailableException Thrown when network connection
	 * 				   to V1 Server is lost.
     */
    public void save(String comment) throws DataException, ApplicationUnavailableException {
        assetID = instance.commit(this, comment);
        stubAssetID = null;
    }

    /**
     * Save any changes to this entity to the VersionOne System.
     *
     * @throws DataException Thrown when a rule or security violation has
     *                 occurred.
	 * @throws ApplicationUnavailableException Thrown when network connection
	 * 				   to V1 Server is lost.
     */
    public void save() throws DataException, ApplicationUnavailableException  {
        save(null);
    }

    /**
     * Compares this {@code Entity} with the specified object. The result is
     * {@code true} if and only if the argument is not {@code null} and is an
     * {@code Entity} with the same {@link com.versionone.om.AssetID}.
     *
     * @param obj The object to compare this {@code Entity} against.
     * @return {@code true} if the given object represents the same
     *         {@code Entity} on the server, {@code false} otherwise.
     */
    @Override
    public boolean equals(Object obj) {

        if (obj == this) {
            return true;
        }

        if (obj instanceof Entity) {
            Entity other = (Entity) obj;
            return V1Util.equals(assetID, other.assetID)
                    && V1Util.equals(stubAssetID, other.stubAssetID);
        }
        return false;
    }

    /**
     * To calculate hashCode used {@link #assetID} or {@link #stubAssetID}
     * field.
     *
     * @return a hash code value for this object.
     * @see AssetID#hashCode().
     */
    @Override
    public int hashCode() {
        return getInstanceKey().hashCode();
    }

    /**
     * @return AssetID token for the Entity.
     * @see AssetID#toString().
     */
    @Override
    public String toString() {
        return getID().toString();
    }

    /**
     * Method getListRelation.
     *
     * @param valuesClass Class<T>.
     * @param name String.
     * @return T.
     */
    <T extends ListValue> T getListRelation(Class<T> valuesClass, String name) {
        return getRelation(valuesClass, name, true);
    }

    /**
     * Sets a List relation by name.
     *
     * @param attributeName The name of the Relation Attribute.
     * @param value The name of the List value to be set in the relation.
     * @param valuesClass Class type of T.
     */
    <T extends ListValue> void setListRelation(String attributeName,
            String value, Class<T> valuesClass) {
        instance.setListRelation(this, attributeName, value, valuesClass);
    }

    /**
     * Gets the value of a Relation to a Custom List Type.
     *
     * @param attributeName The name of the relation attribute.
     * @return A representing the related Custom List Type, or null.
     * @see V1Instance#getCustomListTypeValues(Entity, String).
     */
    CustomListValue getCustomListValue(String attributeName) {
        return instance.getCustomRelation(this, attributeName);
    }

    /**
     * Method setCustomListRelation.
     *
     * @param name String.
     * @param value String.
     */
    void setCustomListRelation(String name, String value) {
        instance.setCustomListRelation(this, name, value);
    }

    /**
     * Method containsAttribute.
     *
     * @param attributeName String.
     * @return boolean.
     */
    boolean containsAttribute(String attributeName) {
        return instance.isAttributeExists(this, attributeName);
    }

    class SimpleCustomAttributeDictionary implements ICustomAttributeDictionary {

        public SimpleCustomAttributeDictionary() {
            super();
        }

        /**
         * Method get.
         *
         * @param attributeName String.
         * @return Object.
         * @see com.versionone.om.ICustomAttributeDictionary#get(String).
         */
        public Object get(String attributeName) {
            return Entity.this.get("Custom_" + attributeName);
        }

        /**
         * Method set.
         *
         * @param attributeName String.
         * @param attributeValue Object.
         * @see com.versionone.om.ICustomAttributeDictionary#set(String,
         *      Object).
         */
        public void set(String attributeName, Object attributeValue) {
            Entity.this.set("Custom_" + attributeName, attributeValue);
        }

        /**
         * Method getNumeric.
         *
         * @param attributeName String.
         * @return Double.
         * @see com.versionone.om.ICustomAttributeDictionary#getNumeric(String).
         */
        public Double getNumeric(String attributeName) {
            return (Double) get(attributeName);
        }

        /**
         * Method getBoolean.
         *
         * @param attributeName String.
         * @return Boolean.
         * @see com.versionone.om.ICustomAttributeDictionary#getBoolean(String).
         */
        public Boolean getBoolean(String attributeName) {
            return (Boolean) get(attributeName);
        }

        /**
         * Method getDate.
         *
         * @param attributeName String.
         * @return DateTime.
         * @see com.versionone.om.ICustomAttributeDictionary#getDate(String).
         */
        public DateTime getDate(String attributeName) {
            return new DateTime(get(attributeName));
        }

        /**
         * Method getString.
         *
         * @param attributeName String.
         * @return String.
         * @see com.versionone.om.ICustomAttributeDictionary#getString(String).
         */
        public String getString(String attributeName) {
            return (String) get(attributeName);
        }

        /**
         * Method containsKey.
         *
         * @param attributeName String.
         * @return boolean.
         * @see com.versionone.om.ICustomAttributeDictionary#containsKey(String).
         */
        public boolean containsKey(String attributeName) {
            return containsAttribute("Custom_" + attributeName);
        }
    }

    class ListCustomAttributeDictionary implements ICustomDropdownDictionary {
        public ListCustomAttributeDictionary() {
            super();
        }

        /**
         * Method get.
         *
         * @param attributeName String.
         * @return IListValueProperty.
         * @see com.versionone.om.ICustomDropdownDictionary#get(String).
         */
        public IListValueProperty get(String attributeName) {
            return new CustomListValueProperty(attributeName);
        }

        /**
         * Method containsKey.
         *
         * @param attributeName String.
         * @return boolean.
         * @see com.versionone.om.ICustomDropdownDictionary#containsKey(String).
         */
        public boolean containsKey(String attributeName) {
            return containsAttribute("Custom_" + attributeName);
        }
    }

    /**
     * Method getListTypeValues.
     *
     * @param valuesClass Class<T>.
     * @return Collection<T>.
     */
    <T extends ListValue> Collection<T> getListTypeValues(Class<T> valuesClass) {
        return instance.get().listTypeValues(valuesClass);
    }

    abstract class BaseListValueProperty<T extends ListValue> implements
            IListValueProperty {
        protected final String attributeName;

        /**
         * Constructor for BaseListValueProperty.
         *
         * @param attributeName String.
         */
        BaseListValueProperty(String attributeName) {
            this.attributeName = attributeName;
        }

        /**
         * Method toString.
         *
         * @return String.
         */
        @Override
        public String toString() {
            return getCurrentValue();
        }

        /**
         * Method getCurrentValue.
         *
         * @return String.
         * @see com.versionone.om.IListValueProperty#getCurrentValue().
         */
        public abstract String getCurrentValue();

        /**
         * Method setCurrentValue.
         *
         * @param value String.
         * @see com.versionone.om.IListValueProperty#setCurrentValue(String).
         */
        public abstract void setCurrentValue(String value);

        /**
         * Method getAllValues.
         *
         * @return String[].
         * @see com.versionone.om.IListValueProperty#getAllValues().
         */
        public String[] getAllValues() {
            String[] res = new String[items().size()];
            int i=0;
            for (T value : items()) {
                res[i++] = value.getName();
            }
            return res;
        }

        /**
         * Method items.
         *
         * @return Collection of items.
         */
        protected abstract Collection<T> items();

        /**
         * Method isValid.
         *
         * @param value String.
         * @return boolean.
         * @see com.versionone.om.IListValueProperty#isValid(String).
         */
        public boolean isValid(String value) {
            if (value == null) {
                return true;
            }

            for (T item : items()) {
                if (item.getName().equals(value)) {
                    return true;
                }
            }
            return false;
        }

        /**
         * Method clearCurrentValue.
         *
         * @see com.versionone.om.IListValueProperty#clearCurrentValue().
         */
        public abstract void clearCurrentValue();
    }

    class ListValueProperty<T extends ListValue> extends
            BaseListValueProperty<T> {

        private final Class<T> valuesClass;

        /**
         * Constructor for ListValueProperty.
         *
         * @param valuesClass Class<T>.
         * @param attributeName String.
         */
        ListValueProperty(Class<T> valuesClass, String attributeName) {
            super(attributeName);
            this.valuesClass = valuesClass;
        }

        /**
         * Method getCurrentValue.
         *
         * @return String.
         * @see com.versionone.om.IListValueProperty#getCurrentValue().
         */
        @Override
        public String getCurrentValue() {
            T value = getSelectedValue();

            if (value != null) {
                return value.toString();
            }
            return null;
        }

        /**
         * Method setCurrentValue.
         *
         * @param value String.
         * @see com.versionone.om.IListValueProperty#setCurrentValue(String).
         */
        @Override
        public void setCurrentValue(String value) {
            setListRelation(attributeName, value, valuesClass);
        }

        /**
         * Method getValues.
         *
         * @return Collection<T>.
         */
        public Collection<T> getValues() {
            return getListTypeValues(valuesClass);
        }

        /**
         * Method items.
         *
         * @return Collection<T>.
         */
        @Override
        protected Collection<T> items() {
            return getValues();
        }

        /**
         * Method getSelectedValue.
         *
         * @return T.
         */
        private T getSelectedValue() {
            return getListRelation(valuesClass, attributeName);
        }

        /**
         * Method clearCurrentValue.
         *
         * @see com.versionone.om.IListValueProperty#clearCurrentValue().
         */
        @Override
        public void clearCurrentValue() {
            setRelation(attributeName, null);
        }
    }

    class CustomListValueProperty extends
            BaseListValueProperty<CustomListValue> {
        /**
         * Constructor for CustomListValueProperty.
         *
         * @param attributeName String.
         */
        public CustomListValueProperty(String attributeName) {
            super("Custom_" + attributeName);
        }

        /**
         * Method getCurrentValue.
         *
         * @return String.
         * @see com.versionone.om.IListValueProperty#getCurrentValue().
         */
        @Override
        public String getCurrentValue() {
            CustomListValue value = getCustomListValue(attributeName);

            if (value != null) {
                return value.getName();
            }
            return null;
        }

        /**
         * Method setCurrentValue.
         *
         * @param value String.
         * @see com.versionone.om.IListValueProperty#setCurrentValue(String).
         */
        @Override
        public void setCurrentValue(String value) {
            setCustomListRelation(attributeName, value);
        }

        /**
         * Method getValues.
         *
         * @return Collection<CustomListValue>.
         */
        public Collection<CustomListValue> getValues() {
            return getCustomListTypeValues(attributeName);
        }

        /**
         * Method items.
         *
         * @return Collection<CustomListValue>.
         */
        @Override
        protected Collection<CustomListValue> items() {
            return getValues();
        }

        /**
         * Method clearCurrentValue.
         *
         * @see com.versionone.om.IListValueProperty#clearCurrentValue()
         */
        @Override
        public void clearCurrentValue() {
            setRelation(attributeName, null);
        }
    }

    /**
     * Get a list value for this entity by name.
     *
     * @param valuesClass class type of T.
     * @return list value for this entity.
     */
    <T extends ListValue> IListValueProperty getListValue(Class<T> valuesClass,
            String name) {
        return new ListValueProperty<T>(valuesClass, name);
    }

    /**
     * Gets a list of possible Custom List Type values for a relationship
     * attribute.
     *
     * @param attributeName The "friendly" name of the attribute.
     * @return list of possible Custom List Type values for a relationship
     *         attribute.
     */
    Collection<CustomListValue> getCustomListTypeValues(String attributeName) {
        return instance.getCustomListTypeValues(this, attributeName);
    }
}
