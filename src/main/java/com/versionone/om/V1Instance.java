/*(c) Copyright 2008, VersionOne, Inc. All rights reserved. (c)*/
package com.versionone.om;

import com.versionone.Oid;
import com.versionone.apiclient.*;
import com.versionone.apiclient.IAttributeDefinition.Aggregate;
import com.versionone.apiclient.Rank;
import com.versionone.om.filters.EntityFilter;
import com.versionone.om.listvalue.CustomListValue;
import com.versionone.om.listvalue.ListValue;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.*;

/**
 * Provides access to a single instance of a VersionOne application. This is the
 * starting point for any application.
 */
public class V1Instance {
    private static final String ATTACHMENT_V1 = "attachment.v1/";
    private static final String ASSETDETAIL_V1_OID = "assetdetail.v1/?oid=";
    private final Map<Object, Asset> assetCache = new HashMap<Object, Asset>();
    private ApiClientInternals apiClientInternals;
    private final WrapperManager wrapperManager;
    private V1InstanceGetter getter;
    private V1InstanceCreator creator;
    private InstanceConfiguration configuration;
    private boolean isValidationEnabled = false;


    /**
     * Initialize a V1Instance to communicate with an installation of VersionOne
     * at a given URL using Integrated Authentication.
     *
     * @param applicationPath Location of the VersionOne application. Ex:
     *                        http://server/versionone/
     */
    public V1Instance(String applicationPath) {
        this(applicationPath, null, null);
    }

    /**
     * Initialize a V1Instance to communicate with an installation of VersionOne
     * at a given URL using basic authentication.
     *
     * @param applicationPath Location of the VersionOne application. Ex:
     *                        http://server/versionone/
     * @param userName        VersionOne member's user name to authenticate
     *                        with. If using Integrated Authentication, must be
     *                        in "user@domain" format.
     * @param password        VersionOne member's password to authenticate
     *                        with.
     */
    public V1Instance(String applicationPath, String userName, String password) {
        this(applicationPath, userName, password, null);
    }

    /**
     * Initialize a V1Instance to communicate with an installation of VersionOne
     * through a proxy
     *
     * @param applicationPath Location of the VersionOne application. Ex:
     *                        http://server/versionone/
     * @param userName        VersionOne member's user name to authenticate
     *                        with. If using Integrated Authentication, must be
     *                        in "user@domain" format.
     * @param password        VersionOne member's password to authenticate
     *                        with.
     * @param proxySettings	  Settings for proxy to connect to VersionOne
     * 						  application.
     */
    public V1Instance(String applicationPath, String userName, String password, ProxySettings proxySettings) {
        // ensure the the app path has a trailing slash
        if (!applicationPath.endsWith("/")) {
            applicationPath += "/";
        }

        apiClientInternals = new ApiClientInternals(applicationPath, userName,
                password, proxySettings);
        wrapperManager = new WrapperManager(this);
    }

    /**
     * Http headers list for sending to the VersionOne
     * 	key - name of parameter
     *  value - value of parameter
     *
     * @return http headers lists
     */
    public Map<String, String> getCustomHttpHeaders() {
		return apiClientInternals.getCustomHttpHeaders();
	}

    /**
     * Cookies manager for working with cookies which send to the VersionOne server.
     *
     * @return cookies manager.
     */
    public CookiesManager getCookiesJar() {
		return apiClientInternals.getCookiesJar();
	}

    /**
     * Validate the application path, user name and password used to construct
     * this instance.
     *
     * @throws ApplicationUnavailableException
     *                                 The application path is unavailable or
     *                                 invalid.
     * @throws AuthenticationException The supplied user name or password was
     *                                 invalid.
     */
    public void validate() throws AuthenticationException,
            ApplicationUnavailableException {
        apiClientInternals.validate();
    }

    /**
     * @return The underlying MetaModel provided by the API Client.
     */
    IMetaModel getMetaModel() {
        return apiClientInternals.getMetaModel();
    }

    /**
     * @return The underlying localizer provided by the API Client.
     */
    private ILocalizer getLocalizer() {
        return apiClientInternals.getLocalizer();
    }

    /**
     * @return The underlying Services provided by the API Client.
     */
    IServices getServices() {
        return apiClientInternals.getServices();
    }

    /**
     * @return The underlying Attachments provided by the API Client.
     */
    private IAttachments getAttachments() {
        return apiClientInternals.getAttachments();
    }

    /**
     * @return The underlying V1Config provided by the API Client.
     */
    private IV1Configuration getV1Config() {
        return apiClientInternals.getV1Config();
    }

    /**
     * @return configuration settings for the VersionOne application instance.
     * @throws ApplicationUnavailableException
     *                      if any connection problems occur
     * @throws SDKException if any problems occur with reading settings
     */
    public InstanceConfiguration getConfiguration() throws ApplicationUnavailableException, SDKException {
        if (configuration == null) {
            try {
                configuration = new InstanceConfiguration(getV1Config()
                        .isEffortTracking(), TrackingLevel.valueOf(getV1Config()
                        .getStoryTrackingLevel()), TrackingLevel
                        .valueOf(getV1Config().getDefectTrackingLevel()),
                        getV1Config().getMaxAttachmentSize());
            } catch (ConnectionException e) {
                throw new ApplicationUnavailableException(e);
            } catch (APIException e) {
                throw new SDKException("Cannot get configuration.", e);
            }
        }
        return configuration;
    }

    /**
     *
     * @return true if validator for required fields enabled
     */
    public boolean isValidationEnabled() {
    	return isValidationEnabled;
    }

    /**
     *
     * @param status Required validator fields status
     */
    public void setValidationEnabled(boolean status) {
    	isValidationEnabled = status;
    }

    /**
     * @return the currently logged in member.
     */
    public Member getLoggedInMember() {
        try {
            return get().memberByID(
                    AssetID.valueOf(getServices().getLoggedIn().getToken()));
        } catch (ConnectionException e) {
            throw new ApplicationUnavailableException(e);
        } catch (V1Exception e) {
            throw new SDKException("Cannot get logged in Members.", e);
        }
    }

    /**
     * @return a read-only collection of root level Project entities.
     */
    public Collection<Project> getProjects() {
        IAssetType projectAssetType = getMetaModel().getAssetType("Scope");
        Query query = new Query(projectAssetType, projectAssetType
                .getAttributeDefinition("Parent"));
        FilterTerm assetStateTerm = new FilterTerm(projectAssetType
                .getAttributeDefinition("AssetState"));
        assetStateTerm.notEqual(AssetState.Closed);
        query.setFilter(new AndFilterTerm(new IFilterTerm[]{assetStateTerm}));
        query.getOrderBy().majorSort(
                projectAssetType.getAttributeDefinition("Name"),
                OrderBy.Order.Ascending);

        return queryToEntityEnum(Project.class, query);
    }

    /**
     * @return a read-only collection of enumerable of Member entities.
     */
    public Collection<Member> getMembers() {
        IAssetType memberAssetType = getMetaModel().getAssetType("Member");
        Query query = new Query(memberAssetType);
        FilterTerm assetStateTerm = new FilterTerm(memberAssetType
                .getAttributeDefinition("AssetState"));
        assetStateTerm.notEqual(AssetState.Closed);
        query.setFilter(new AndFilterTerm(new IFilterTerm[]{assetStateTerm}));
        query.getOrderBy().majorSort(
                memberAssetType.getAttributeDefinition("Name"),
                OrderBy.Order.Ascending);

        return queryToEntityEnum(Member.class, query);
    }

    /**
     * @return a read-only collection of Team entities.
     */
    public Collection<Team> getTeams() {
        IAssetType teamAssetType = getMetaModel().getAssetType("Team");
        Query query = new Query(teamAssetType);
        FilterTerm assetStateTerm = new FilterTerm(teamAssetType
                .getAttributeDefinition("AssetState"));
        assetStateTerm.notEqual(AssetState.Closed);
        query.setFilter(new AndFilterTerm(new IFilterTerm[]{assetStateTerm}));
        query.getOrderBy().majorSort(
                teamAssetType.getAttributeDefinition("Name"),
                OrderBy.Order.Ascending);

        return queryToEntityEnum(Team.class, query);
    }

    /**
     * @return a read-only collection of TestSuites.
     */
    public Collection<TestSuite> getTestSuites() {
        IAssetType teamAssetType = getMetaModel().getAssetType("TestSuite");
        Query query = new Query(teamAssetType);
        FilterTerm assetStateTerm = new FilterTerm(teamAssetType
                .getAttributeDefinition("AssetState"));
        assetStateTerm.notEqual(AssetState.Closed);
        query.setFilter(new AndFilterTerm(new IFilterTerm[]{assetStateTerm}));
        query.getOrderBy().majorSort(
                teamAssetType.getAttributeDefinition("Name"),
                OrderBy.Order.Ascending);

        return queryToEntityEnum(TestSuite.class, query);
    }

    /**
     * Allows access to the underlying API Client structures. Only use this when
     * you need access that this library does not provide.
     *
     * @return object used to access API Client methods.
     */
    public ApiClientInternals getApiClient() {
        return apiClientInternals;
    }

    /**
     * @param apiClient used by all SDK classes to access to API Client.
     */
    public void setApiClient(ApiClientInternals apiClient) {
        apiClientInternals = apiClient;
    }

    private static AttributeSelection flattenSelection(
            IAssetType querytype, List<IAttributeDefinition> orig) {
        AttributeSelection selection = new AttributeSelection();

        for (IAttributeDefinition definition : orig) {
            IAttributeDefinition newDefinition = null;

            if (definition.getAssetType().isA(querytype)) {

                if ((definition.getBase() == null)
                        || (definition.getAssetType().equals(querytype))) {
                    newDefinition = definition;
                } else if (definition.getBase().getAssetType().isA(querytype)) {
                    newDefinition = definition.getBase();
                } else {
                    newDefinition = querytype.getAttributeDefinition(definition
                            .getName());
                }
            } else if (querytype.isA(definition.getAssetType())) {
                newDefinition = querytype.getAttributeDefinition(definition
                        .getName());
            }

            if ((newDefinition != null) && (!selection.contains(newDefinition))) {
                selection.add(newDefinition);
            }
        }
        return selection;
    }

    private static List<IAttributeDefinition> getSuggestedSelection(
            IAssetType assetType, Class<? extends Entity> type) {
        AttributeSelection result = new AttributeSelection();

        for (Class curType = type; curType != null; curType = curType.getSuperclass()) {
            MetaDataAttribute attributes = (MetaDataAttribute) curType
                    .getAnnotation(MetaDataAttribute.class);

            if (attributes == null) {
                continue;
            }
            String names = attributes.defaultAttributeSelectionNames();

            if ((names == null) || (names.trim().length() == 0)) {
                continue;
            }
            String[] attributesNames = names.split(",");

            for (String name : attributesNames) {
                IAttributeDefinition def = assetType.getAttributeDefinition(name);

                if ((def == null) || (result.contains(def))) {
                    continue;
                }
                result.add(def);
            }
        }
        return result;
    }

    <T extends Entity> Collection<T> queryToEntityEnum(Class<T> clazz, Query query)
            throws ApplicationUnavailableException, SDKException {
        try {
            AttributeSelection assetStateSelection = new AttributeSelection();
            try {
                IAttributeDefinition assetStateDef = query.getAssetType().getAttributeDefinition("AssetState");
                assetStateSelection.add(assetStateDef);
            } catch (MetaException e) {
                //Do nothing
            }
            List<IAttributeDefinition> suggestedSelection = getSuggestedSelection(query.getAssetType(), clazz);
            AttributeSelection flattenedSelection = flattenSelection(query.getAssetType(), query.getSelection());
            query.setSelection(AttributeSelection.merge(assetStateSelection, suggestedSelection, flattenedSelection));
            return assetEnumToEntityEnum(clazz, getServices().retrieve(query).getAssets());
        } catch (ConnectionException e) {
            throw new ApplicationUnavailableException(e);
        } catch (V1Exception e) {
            throw new SDKException("Cannot execute query.", e);
        }
    }

    private <T extends Entity> Collection<T> assetEnumToEntityEnum(
            Class<T> clazz, Asset[] assets) {
        List<T> members = new LinkedList<T>();

        for (Asset asset : assets) {
            AssetID id = new AssetID(asset.getOid().getToken());
            setAsset(id, asset);
            T wrapped = createWrapper(clazz, id, false);

            // we could get an asset from a query that has no SDK wrapper, skip
            // it here
            if (wrapped != null) {
                members.add(wrapped);
            }
        }
        return Collections.unmodifiableList(members);
    }

    RequiredFieldValidator getRequiredFieldValidator() {
        return new RequiredFieldValidator(getMetaModel(), getServices());
    }

    AssetID commit(Entity entity, String comment) throws DataException,
            ApplicationUnavailableException, EntityValidationException {
        try {

        	Asset asset = getAsset(entity);
            validateEntity(entity, asset);
            getServices().save(asset, comment);

            // We cache this asset, and we don't want moments in the cache
            asset.setOid(asset.getOid().getMomentless());
		    AssetID assetId = new AssetID(asset.getOid().getToken());
            setAsset(assetId, asset);
            return assetId ;
        } catch (ConnectionException e) {
			if (e.getServerResponseCode() == -1)
            	throw new ApplicationUnavailableException(getLocalizer().resolve(e.getMessage()), e);
			else
				throw new DataException(getLocalizer().resolve(e.getMessage()), e);
        } catch (V1Exception e) {
            throw new DataException(getLocalizer().resolve(e.getMessage()), e);
        }
    }

    private void validateEntity(Entity entity, Asset asset) throws ConnectionException, APIException, OidException {
        if (isValidationEnabled) {
            EntityValidator validator = new EntityValidator(this);
		    List<String> invalidAttributes = validator.validate(asset);

            if(invalidAttributes.size() > 0) {
                throw new EntityValidationException(entity, invalidAttributes);
            }

        }
    }

    <T extends Entity> T createNew(Class<T> clazz, Entity inTheContextOf)
            throws SDKException {
        Oid contextOid = getOid(inTheContextOf);
        String assetTypeToken = getAssetTypeToken(clazz);
        IAssetType typeToCreate = getMetaModel().getAssetType(assetTypeToken);
        Asset shell;
        try {
            shell = getServices().createNew(typeToCreate, contextOid);
        } catch (V1Exception e) {
            throw new SDKException("Cannot create new Entity:" +
                    clazz.getSimpleName(), e);
        }
        T result = createWrapper(clazz);
        setAsset(result.getInstanceKey(), shell);
        return result;
    }

    String getEntityURL(Entity entity) {
        if (entity instanceof TestSuite)
            return null;

        if (entity instanceof BaseAsset || entity instanceof Attachment) {
            return apiClientInternals.getApplicationPath()
                    + ASSETDETAIL_V1_OID + entity.getID();
        }
        return null;
    }

    String getAttachmentURL(Attachment attachment) {
        return apiClientInternals.getApplicationPath() + ATTACHMENT_V1
                + getOid(attachment).getKey();
    }

    boolean canExecuteOperation(Entity subject, String operationName)
            throws SDKException {
        try {
            Asset asset = getAsset(subject);
            IOperation toExecute = asset.getAssetType().getOperation(operationName);
            return (Boolean) retrieveAttribute(asset.getOid(),
                    toExecute.getValidatorAttribute(), false).getValue();
        } catch (APIException e) {
            throw new SDKException(e);
        }
    }

    /**
     * Executes an Operation on an Entity, assuming it is safe to do so.
     *
     * @param clazz         Class of expected Entity to return.
     * @param subject       asset will be found for.
     * @param operationName operator name.
     * @return object identifier.
     * @throws UnsupportedOperationException in case invalid state for the
     *                                       Operation.
     */
    <T extends Entity> T executeOperation(Class<T> clazz, Entity subject,
                                          String operationName) throws UnsupportedOperationException {
        Oid operationResult = executeOperation(subject, operationName);
        AssetID id = new AssetID(operationResult.getToken());

        return createWrapper(clazz, id, false);
    }

    /**
     * Be sure to call Entity.Save() before or after calling this depending on
     * the operation result.
     *
     * @param subject       asset will be found for.
     * @param operationName operator name.
     * @return object identifier.
     * @throws UnsupportedOperationException in case invalid state for the
     *                                       Operation.
     */
    Oid executeOperation(Entity subject, String operationName)
            throws UnsupportedOperationException {
        Asset asset = getAsset(subject);

        return executeOperation(asset, operationName);
    }

    private Oid executeOperation(Asset subject, String operationName)
            throws UnsupportedOperationException {
        try {
            IOperation toExecute = subject.getAssetType().getOperation(
                    operationName);
            Oid result = getServices().executeOperation(toExecute,
                    subject.getOid());
            return result.getMomentless();
        } catch (APIException e) {
            throw new UnsupportedOperationException(operationName, e);
        }
    }

    private Oid getOid(Entity entity) {
        if (entity == null) {
            return Oid.Null;
        }
        Asset contextAsset = getAsset(entity);
        return contextAsset.getOid();
    }

    private Asset getAsset(Oid oid) {
        return getAsset(new AssetID(oid.getToken()), oid.getAssetType().getToken());
    }

    /**
     * Try to find an asset in the asset cache or create one for this entity.
     *
     * @param entity asset will be found for.
     * @return An asset that will exist in the asset cache.
     */
    private Asset getAsset(Entity entity) {
        return getAsset(entity.getInstanceKey(), getAssetTypeToken(entity.getClass()));
    }

    /**
     * Find an asset in the asset cache or create one for this id.
     *
     * @param id             asset will be found for.
     * @param assetTypeToken The Asset Type Token of the asset to create if one
     *                       does not already exist.
     * @return An Asset that will exist in the asset cache.
     */
    private Asset getAsset(Object id, String assetTypeToken) {
        Asset result = assetCache.get(id);

        if (result == null) {
            try {
                IAssetType assetType = getMetaModel().getAssetType(
                        assetTypeToken);

                if (id instanceof AssetID) {
                    AssetID assetId = (AssetID) id;
                    result = new Asset(Oid.fromToken(assetId.getToken(),
                            getMetaModel()));
                } else {
                    result = new Asset(assetType);
                }
                setAsset(id, result);
            } catch (OidException e) {
                throw new ApplicationUnavailableException(e);
            }
        }
        return result;
    }

    /**
     * Retrieve an asset stored in asset cache.
     *
     * @param id asset will be found by.
     * @return an Asset or null if the asset cannot be found in the cache.
     */
    public Asset getAsset(Object id) {
        return assetCache.get(id);
    }

    public void setAsset(Object id, Asset asset) {
        assetCache.put(id, asset);
    }

    static <T extends Entity> String getDefaultOrderByToken(Class<T> entityType) {
        MetaDataAttribute attribute = entityType
                .getAnnotation(MetaDataAttribute.class);

        if (attribute != null) {
            return attribute.defaultOrderByToken();
        }
        throw new SDKException("Missing MetaDataAttribute on type "
                + entityType.getName());
    }

    static <T extends Entity> String getAssetTypeToken(Class<T> entityType) {
        MetaDataAttribute attribute = entityType
                .getAnnotation(MetaDataAttribute.class);

        if (attribute != null) {
            return attribute.value();
        }
        throw new SDKException("Missing MetaDataAttribute on type "
                + entityType.getName());
    }

    private <T extends Entity> T createWrapper(Class<T> clazz) {
        return wrapperManager.create(clazz);
    }

    private <T extends Entity> T createWrapper(Class<T> clazz, AssetID id,
                                               boolean validate) {
        return wrapperManager.create(clazz, id, validate);
    }

    private Attribute retrieveAttribute(Oid oid, IAttributeDefinition def,
                                        boolean cachable) throws SDKException {
        Query query = new Query(oid);
        query.getSelection().add(def);
        QueryResult queryResult;
        try {
            queryResult = getServices().retrieve(query);
            Asset asset = queryResult.getAssets()[0];

            if (!cachable) {
                return asset.getAttribute(def);
            }
            Asset cached = getAsset(asset.getOid());
            Attribute attribute = cached.ensureAttribute(def);

            if (def.isMultiValue()) {
                for (Object value : asset.getAttribute(def).getValues()) {
                    cached.loadAttributeValue(def, value);
                }
            } else {
                cached.loadAttributeValue(def, asset.getAttribute(def).getValue());
            }
            attribute.acceptChanges();
            return attribute;
        } catch (V1Exception e) {
            throw new SDKException(e);
        }
    }

    private Attribute getAttribute(Entity entity, String name, boolean cachable) {
        Asset asset = getAsset(entity);
        IAttributeDefinition def = asset.getAssetType().getAttributeDefinition(
                name);
        Attribute attribute = cachable ? asset.getAttribute(def) : null;

        if (attribute == null) {
            attribute = retrieveAttribute(asset.getOid(), def, cachable);
        }
        return attribute;
    }

    /**
     * Clear an attribute from cache of specified Entity.
     *
     * @param entity to clear attribute of.
     * @param name   of the attribute to clear;
     *               if null, all attributes will be cleared from cache.
     */
    void clearCache(Entity entity, String name) {
        final Asset asset = getAsset(entity);
        IAttributeDefinition def = null;
        if (name != null) {
            def = asset.getAssetType().getAttributeDefinition(name);
        }
        asset.clearAttributeCache(def);
    }

    boolean isAttributeExists(Entity entity, String name) {
        Asset asset = getAsset(entity);

        try {
            return asset.getAssetType().getAttributeDefinition(name) != null;
        } catch (MetaException e) {
            return false;
        }
    }

    InputStream getReader(Entity entity) throws ApplicationUnavailableException {
        Oid oid = getOid(entity);
        try {
            return getAttachments().getReader(oid.getKey().toString());
        } catch (ConnectionException e) {
            throw new ApplicationUnavailableException(e);
        }
    }

    OutputStream getWriter(Entity entity,
                           String contentType) throws ApplicationUnavailableException {
        Oid oid = getOid(entity);
        try {
            return getAttachments().getWriter(oid.getKey().toString(), contentType);
        } catch (ConnectionException e) {
            throw new ApplicationUnavailableException(e);
        }
    }

    void commitWriteStream(Entity entity)
            throws ApplicationUnavailableException, AttachmentLengthExceededException {
        Oid oid = getOid(entity);

        try {
            getAttachments().setWriter(oid.getKey().toString());
        } catch (AttachmentLengthException e) {
            throw new AttachmentLengthExceededException(e);
        } catch (ConnectionException e) {
            throw new ApplicationUnavailableException(e);
        }
    }

    Object getProperty(Entity entity, String name,
                       boolean cachable) throws SDKException {
        try {
            return getAttribute(entity, name, cachable).getValue();
        } catch (APIException e) {
            throw new SDKException(
                    "Cannot get property:" + name + " for:" + entity, e);
        }
    }

    void setProperty(Entity entity, String name, Object value) throws SDKException {
        Asset asset = getAsset(entity);
        IAttributeDefinition def = asset.getAssetType().getAttributeDefinition(
                name);
        try {
            asset.setAttributeValue(def, value);
        } catch (APIException e) {
            throw new SDKException(
                    "Cannot set property:" + name + " for:" + entity, e);
        }
    }

    <T extends Entity> T getRelation(Entity entity, Class<T> clazz,
                                     String name, boolean cachable) {
        Oid oid = (Oid) getProperty(entity, name, cachable);
        if (oid.isNull()) {
            return null;
        }
        return createWrapper(clazz, new AssetID(oid.getToken()), false);
    }

    <T extends Entity> void setRelation(Entity entity, String name,
                                        T value) throws SDKException {
        Oid oid;
        try {
            oid = (value == null) ? Oid.Null : Oid.fromToken(value.getID()
                    .getToken(), getMetaModel());
        } catch (OidException e) {
            throw new SDKException(
                    "Cannot set relation:" + name + " for:" + entity, e);
        }
        setProperty(entity, name, oid);
    }

    <T extends Entity> void addRelation(Entity entity, String name,
                                        T value) throws SDKException {
        Asset asset = getAsset(entity);
        IAttributeDefinition def =
                asset.getAssetType().getAttributeDefinition(name);
        Oid oid;
        try {
            oid = Oid.fromToken(value.getID().getToken(), getMetaModel());
            asset.addAttributeValue(def, oid);
        } catch (V1Exception e) {
            throw new SDKException(
                    "Cannot add relation:" + name + " for:" + entity, e);
        }
    }

    <T extends Entity> void removeRelation(Entity entity, String name,
                                           T value) throws SDKException {
        Asset asset = getAsset(entity);
        IAttributeDefinition def = asset.getAssetType().getAttributeDefinition(
                name);
        Oid oid;
        try {
            oid = Oid.fromToken(value.getID().getToken(), getMetaModel());
            asset.removeAttributeValue(def, oid);
        } catch (V1Exception e) {
            throw new SDKException(
                    "Cannot remove relation:" + name + " for:" + entity, e);
        }
    }

    <T extends Entity> EntityCollection<T> getMultiRelation(
            Entity entity, String name, boolean cachable) {
        return new EntityCollection<T>(this, entity, name, name);
    }

    <T extends Entity> EntityCollection<T> getMultiRelation(
            Entity entity, String readName, String writeName, boolean cachable) {
        return new EntityCollection<T>(this, entity, readName, writeName);
    }

    <T extends Entity> boolean multiRelationContains(Entity entity,
                                                     String attributeName, T value) {
        return internalGetMultiRelation(entity, attributeName, value.getClass())
                .contains(value);
    }

    int getMultiRelationCount(Entity entity, String attributeName) {
        Double valueDouble = (Double) getProperty(entity, attributeName
                + ".@Count", false);

        return valueDouble.intValue();
    }

    <T extends Entity> Collection<T> internalGetMultiRelation(
            Entity entity, String attributeName, Class<T> clazz) {
        Object[] oids = getAttribute(entity, attributeName, false).getValues();

        return oidEnumToEntityCollection(Arrays.asList(oids), clazz);
    }

    private <T extends Entity> Collection<T> oidEnumToEntityCollection(
            List<Object> oids, Class<T> clazz) {
        List<T> members = new LinkedList<T>();
        for (Object oid : oids) {
            AssetID id = new AssetID(((Oid)oid).getToken());
            T tWrapper = createWrapper(clazz, id, false);

            // we could get an asset from a query that has no SDK wrapper, skip
            // it here
            if (tWrapper != null) {
                members.add(tWrapper);
            }
        }
        return Collections.unmodifiableList(members);
    }

    boolean isMultiRelationIsReadOnly(Entity entity, String attributeName) {
        return getMetaModel()
                .getAssetType(getAssetTypeToken(entity.getClass()))
                .getAttributeDefinition(attributeName).isReadOnly();
    }

    public <T extends ListValue> T getListValueByName(Class<T> clazz,
                                                         String value) {
        String typeToken = getAssetTypeToken(clazz);
        IAssetType listType = getMetaModel().getAssetType(typeToken);
        return getListValueByName(listType, value, clazz);
    }

    private <T extends ListValue> T getListValueByName(
            IAssetType listType, String value, Class<T> listValuesClass) throws SDKException {
        IAttributeDefinition nameDef = listType.getAttributeDefinition("Name");

        Query query = new Query(listType);
        FilterTerm nameTerm = new FilterTerm(nameDef);
        nameTerm.equal(value);
        query.setFilter(nameTerm);
        query.getOrderBy().majorSort(nameDef, OrderBy.Order.Ascending);

        QueryResult result;
        try {
            result = getServices().retrieve(query);
        } catch (V1Exception e) {
            throw new SDKException("Cannot getListValueByName:" + value, e);
        }

        if (result.getAssets().length == 0) {
            throw new IndexOutOfBoundsException("There is no " + value
                    + " value with name: " + listType.getToken());
        }
        return createWrapper(listValuesClass, new AssetID(result.getAssets()[0]
                .getOid().getToken()), false);
    }

    <T extends ListValue> void setListRelation(Entity entity, String name,
                                               String value, Class<T> valuesClass) {
        T valueEntity = null;
        if (value != null) {
            valueEntity = getListValueByName(valuesClass, value);
        }
        setRelation(entity, name, valueEntity);
    }

    void setCustomListRelation(Entity entity, String name, String value) {
        CustomListValue valueEntity = null;
        if (value != null) {
            valueEntity = getCustomListValueByName(entity, name, value);
        }
        setRelation(entity, name, valueEntity);
    }

    CustomListValue getCustomListValueByName(Entity entity, String name,
                                             String value) {
        IAssetType relatedType = getRelatedType(entity, name);
        return getListValueByName(relatedType, value, CustomListValue.class);
    }

    private IAssetType getRelatedType(Entity entity, String name) {
        IAssetType owningType = getMetaModel().getAssetType(
                getAssetTypeToken(entity.getClass()));
        IAttributeDefinition listTypeDef = owningType.getAttributeDefinition(name);
        return listTypeDef.getRelatedAsset();
    }

    CustomListValue getCustomRelation(Entity entity, String name) {
        return getCustomRelation(entity, name, true);
    }

    CustomListValue getCustomRelation(Entity entity, String name, boolean cachable) {
        return getRelation(entity, CustomListValue.class, name, cachable);
    }

    public Object getPropertyOnCustomType(
            CustomListValue entity, String name, boolean cachable) throws SDKException {
        try {
            return getAttributeOnCustomType(entity, name, cachable).getValue();
        } catch (APIException e) {
            throw new SDKException("Cannot get property:" + name, e);
        }
    }

    private Attribute getAttributeOnCustomType(
            Entity entity, String name, boolean cachable) throws SDKException {
        Asset asset;
        try {
            asset = getAsset(
                    Oid.fromToken(entity.getID().getToken(), getMetaModel()));
        } catch (OidException e) {
            throw new SDKException("Cannot get attribute:" + name, e);
        }
        IAttributeDefinition def =
                asset.getAssetType().getAttributeDefinition(name);
        Attribute attribute = cachable ? asset.getAttribute(def) : null;

        if (attribute == null) {
            attribute = retrieveAttribute(asset.getOid(), def, cachable);
        }
        return attribute;
    }

    Collection<CustomListValue> getCustomListTypeValues(
            Entity entity, String attributeName) throws SDKException {
        IAssetType typeToGet = getRelatedType(entity, attributeName);
        Query query = new Query(typeToGet);

        FilterTerm assetStateTerm =
                new FilterTerm(typeToGet.getAttributeDefinition("AssetState"));
        assetStateTerm.notEqual(AssetState.Closed);
        query.setFilter(new AndFilterTerm(new FilterTerm[]{assetStateTerm}));
        List<CustomListValue> values = new LinkedList<CustomListValue>();

        try {
            for (Asset asset : getServices().retrieve(query).getAssets()) {
                values.add(createWrapper(CustomListValue.class,
                        new AssetID(asset.getOid().getToken()), false));
            }
        } catch (V1Exception e) {
            throw new SDKException(e);
        }
        return values;
    }

    <T extends Entity> com.versionone.om.Rank<T> getRank(T entity,
                                                         String attributeName) {
        return new com.versionone.om.Rank<T>(this, entity, attributeName);
    }

    void rankAbove(Entity entity, Entity aboveEntity, String attributeName) {
        Rank rank = new Rank(getProperty(aboveEntity, attributeName, false));
        setProperty(entity, attributeName, rank.before());
    }

    void rankBelow(Entity entity, Entity belowEntity, String attributeName) {
        Rank rank = new Rank(getProperty(belowEntity, attributeName, false));
        setProperty(entity, attributeName, rank.after());
    }

    boolean isRankAbove(Entity entity, Entity aboveEntity, String attributeName) {
        Rank entityRank = new Rank(getProperty(entity, attributeName, false));
        Rank aboveRank = new Rank(getProperty(aboveEntity, attributeName, false));
        return entityRank.compareTo(aboveRank) < 0;
    }

    boolean isRankBelow(Entity entity, Entity belowEntity, String attributeName) {
        Rank entityRank = new Rank(getProperty(entity, attributeName, false));
        Rank belowRank = new Rank(getProperty(belowEntity, attributeName, false));
        return entityRank.compareTo(belowRank) > 0;
    }

    Double getSum(Entity entity, String multiRelationName, EntityFilter filter,
                  String numericAttributeName) throws SDKException {
        Oid oid = getOid(entity);

        IAttributeDefinition multiRelation = oid.getAssetType()
                .getAttributeDefinition(multiRelationName);
        IAssetType relatedType = multiRelation.getRelatedAsset();
        IAttributeDefinition filtered = multiRelation;

        if (filter != null) {
            IFilterTerm term = filter.buildFilter(relatedType, this);

            if (term != null) {
                try {
                    filtered = multiRelation.filter(term);
                } catch (APIException e) {
                    throw new SDKException(e);
                }
            }
        }
        IAttributeDefinition relatedAttribute = relatedType
                .getAttributeDefinition(numericAttributeName);
        IAttributeDefinition joined = filtered.join(relatedAttribute);
        IAttributeDefinition sum = joined.aggregate(Aggregate.Sum);

        try {
            return (Double) retrieveAttribute(oid, sum, false).getValue();
        } catch (APIException e) {
            throw new SDKException(e);
        }
    }

    boolean checkTracking(Workitem workitem) {
        TrackingLevel level = getTrackingLevel(workitem);

        if ((workitem instanceof PrimaryWorkitem)
                && (level == TrackingLevel.SecondaryWorkitem)) {
            return false;
        } else if ((workitem instanceof SecondaryWorkitem)
                && (level == TrackingLevel.PrimaryWorkitem)) {
            return false;
        }
        return true;
    }

    /**
     * Call this before setting DetailEstimate or ToDo on a workitem.
     *
     * @param workitem DetailEstimate or ToDo will be set for.
     * @throws IllegalStateException If setting DetailEstimate is not allowed at
     *                               this level.
     */
    void preventTrackingLevelAbuse(Workitem workitem) {
        TrackingLevel level = getTrackingLevel(workitem);

        if (workitem instanceof PrimaryWorkitem) {

            if (level == TrackingLevel.SecondaryWorkitem) {
                throw new IllegalStateException("You cannot set DetailEstimate "
                        + "or ToDo on this item, nor can you log effort, because"
                        + " the system is configured to track Detail Estimate "
                        + "and ToDo at the Task/Test level.");
            }
        } else if (level == TrackingLevel.PrimaryWorkitem) {
            throw new IllegalStateException("You cannot set DetailEstimate or "
                    + "ToDo on this item, nor can you log effort, because the "
                    + "system is configured to track Detail Estimate and ToDo "
                    + "at the Story/Defect level.");
        }
    }

    private TrackingLevel getTrackingLevel(Workitem workitem) {
        Workitem parent;

        if (workitem instanceof SecondaryWorkitem) {
            parent = ((SecondaryWorkitem) workitem).getParent();
        } else {
            parent = (PrimaryWorkitem) workitem;
        }

        if (parent instanceof Story) {
            return getConfiguration().storyTrackingLevel;
        } else if (parent instanceof Defect) {
            return getConfiguration().defectTrackingLevel;
        }
        throw new IllegalArgumentException(
                "Expected a Story, a Defect or a child work item of one.");
    }

    /**
     * @return Getter for various assets in the system.
     */
    public V1InstanceGetter get() {
        if (getter == null) {
            getter = new V1InstanceGetter(this);
        }
        return getter;
    }

    WrapperManager getWrapperManager() {
        return wrapperManager;
    }

    /**
     * @return Creator for various assets in the system.
     */
    public V1InstanceCreator create() {
        if (creator == null) {
            creator = new V1InstanceCreator(this);
        }
        return creator;
    }
}