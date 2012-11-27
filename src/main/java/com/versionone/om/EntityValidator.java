package com.versionone.om;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.versionone.apiclient.APIException;
import com.versionone.apiclient.Asset;
import com.versionone.apiclient.ConnectionException;
import com.versionone.apiclient.IAttributeDefinition;
import com.versionone.apiclient.OidException;
import com.versionone.apiclient.RequiredFieldValidator;

/**
 * Entity validator that can be used on-demand and is hooked into entity Save/Create actions
 */
public class EntityValidator {
    private final V1Instance v1Instance;
    private final RequiredFieldValidator validator;

    /**
     * Entity validator constructor.
     *
     * @param instance - V1Instance to retrieve APIClient required field validator .
     */
    public EntityValidator(V1Instance instance) {
        v1Instance = instance;
        validator = v1Instance.getRequiredFieldValidator();
    }

    /**
     * Validate single attribute of an entity.
     *
     * @param entity - Entity.
     * @param attribute - Name of attribute to be validated.
     * @return true, if attribute value is valid; false, otherwise.
     * @throws OidException
     * @throws ConnectionException
     * @throws APIException
     */
    public boolean validate(Entity entity, String attribute) throws APIException, ConnectionException, OidException {
        Asset asset = v1Instance.getAsset(entity.getInstanceKey()) != null ? v1Instance.getAsset(entity.getInstanceKey()) : v1Instance.getAsset(entity.getID());
        IAttributeDefinition attributeDefinition = asset.getAssetType().getAttributeDefinition(attribute);
        return validator.validate(asset, attributeDefinition);
    }

    /**
     * Validate single Entity.
     * @param entity - Entity to validate.
     * @return Collection of invalid attribute names.
     * @throws OidException
     * @throws ConnectionException
     * @throws APIException
     */
    public List<String> validate(Entity entity) throws APIException, ConnectionException, OidException {
    	Asset asset = v1Instance.getAsset(entity.getInstanceKey()) != null ? v1Instance.getAsset(entity.getInstanceKey()) : v1Instance.getAsset(entity.getID());
        return validate(asset);
    }

    /**
     * Validate a collection of entities.
     * @param entities - Entities to validate.
     * @return Map where Entities are keys, and corresponding validation results are values (@see Validate(Entity)).
     * @throws OidException
     * @throws ConnectionException
     * @throws APIException
     */
    public Map<Entity, List<String>> validate(Entity[] entities) throws APIException, ConnectionException, OidException {
        Map<Entity, List<String>> results = new HashMap<Entity, List<String>>();

        for (Entity entity : entities) {
            results.put(entity, validate(entity));
        }

        return results;
    }

    /**
     * Pass asset to required field validator.
     * We cannot resolve asset by AssetID because in newly created entities it is null until Commit() is actually executed, that's why it is used directly.
     * @param asset - Asset to validate.
     * @return Collection of invalid attribute names.
     * @throws OidException
     * @throws ConnectionException
     * @throws APIException
     */
    List<String> validate(Asset asset) throws APIException, ConnectionException, OidException {
        List<IAttributeDefinition> result = validator.validate(asset);
        return getAttributeNames(result);
    }

    private static List<String> getAttributeNames(List<IAttributeDefinition> attributeDefinitions) {
        List<String> attributeNames = new ArrayList<String>(attributeDefinitions.size());

        for (IAttributeDefinition attributeDefinition : attributeDefinitions) {
            attributeNames.add(attributeDefinition.getName());
        }

        return attributeNames;
    }
}
