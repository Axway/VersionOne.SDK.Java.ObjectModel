/*(c) Copyright 2008, VersionOne, Inc. All rights reserved. (c)*/
package com.versionone.om;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.versionone.Oid;
import com.versionone.apiclient.APIException;
import com.versionone.apiclient.Asset;
import com.versionone.apiclient.Attribute;
import com.versionone.apiclient.IAssetType;
import com.versionone.apiclient.IAttributeDefinition;
import com.versionone.apiclient.IMetaModel;
import com.versionone.apiclient.MetaException;
import com.versionone.apiclient.OidException;
import com.versionone.apiclient.Query;
import com.versionone.apiclient.V1Exception;
import com.versionone.om.listvalue.CustomListValue;

public class WrapperManager {

    private static final String ENTITIES_FILE = "entities.properties";

    private final V1Instance instance;
    private final Object typeMapLock = new Object();
    private Map<String, List<Class<Entity>>> typeMap;
    // here we will store AssetTypes for specific MetaModel to avoid getting meta for AssetState
    private final Map<IMetaModel, List<IAssetType>> withoutAssetStates = new HashMap<IMetaModel, List<IAssetType>>();

    public WrapperManager(V1Instance instance) {
        this.instance = instance;
    }

    public <T extends Entity> T create(Class<T> clazz) {
        try {
            // This assumes that every entity will have a constructor that takes
            // a V1Instance
            Constructor<T> constructor = clazz
                    .getDeclaredConstructor(V1Instance.class);
            return constructor.newInstance(instance);
        } catch (Exception e) {
            throw new RuntimeException("Cannot create Entity:" + clazz.getSimpleName());
        }
    }

    /**
     * @param clazz    can be null.
     * @param id       unique ID.
     * @param validate validate type.
     * @return T object of the T type or null.
     */
    public <T extends Entity> T create(Class<T> clazz, AssetID id, boolean validate) {
        try {
            final Class<T> targetClass;

            if (clazz == CustomListValue.class) {
                targetClass = clazz;
            } else if (validate || (clazz == null)
                    || Modifier.isAbstract(clazz.getModifiers())) {
                targetClass = findType(id, validate);
            } else {
                targetClass = clazz;
            }

            if (targetClass == null) {
                return null;
            }

            Constructor<T> constructor = targetClass.getDeclaredConstructor(
                    AssetID.class, V1Instance.class);

            return constructor.newInstance(id, instance);
        } catch (Exception e) {
            // do nothing
        }
        return null;
    }

    private Map<String, List<Class<Entity>>> getTypeMap() {
        if (typeMap == null) {
            synchronized (typeMapLock) {
                if (typeMap == null) {
                    Map<String, List<Class<Entity>>> map =
                            new HashMap<String, List<Class<Entity>>>();
                    fillMap(map);
                    typeMap = map;
                }
            }
        }
        return typeMap;
    }

    private static void fillMap(Map<String, List<Class<Entity>>> map) {
        try {
            Properties entProps = new Properties();
            entProps.load(WrapperManager.class
                    .getResourceAsStream(ENTITIES_FILE));
            String entitiesString = entProps.getProperty("entities");
            String[] entitiesNames = entitiesString.split(",");
//            String packagePrefix = WrapperManager.class.getPackage().getName() + ".";

            for (String name : entitiesNames) {
//                String className = packagePrefix + name.trim();
                String className = name.trim();

                try {
                    Class clazz = Class.forName(className);

                    if (!Entity.class.isAssignableFrom(clazz)) {
                        continue;
                    }
                    Class<Entity> entityClass = clazz;
                    MetaDataAttribute metaDataAttribute = entityClass
                            .getAnnotation(MetaDataAttribute.class);

                    if (metaDataAttribute == null) {
                        continue;
                    }
                    String token = metaDataAttribute.value();

                    if (token.length() > 0) {
                        List<Class<Entity>> list = map.get(token);

                        if (list == null) {
                            list = new LinkedList<Class<Entity>>();
                            map.put(token, list);
                        }
                        list.add(entityClass);
                    }
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(ENTITIES_FILE +
                            " has incorrect record: " + name);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Cannot read information from "
                    + ENTITIES_FILE + "file");
        }

        Comparator<Class<Entity>> comporator = new Comparator<Class<Entity>>() {

            public int compare(Class<Entity> o1, Class<Entity> o2) {
                return getAssetStateFilter(o1) - getAssetStateFilter(o2);
            }
        };
        for (List<Class<Entity>> list : map.values()) {
            Collections.sort(list, Collections.reverseOrder(comporator));
        }
    }

    static short getAssetStateFilter(Class<?> type) {
        MetaDataAttribute mda = type.getAnnotation(MetaDataAttribute.class);
        return (mda != null) ? mda.assetState() : -1;
    }

    private <T extends Entity> Class<T> findType(AssetID id, boolean validate) throws SDKException {
        Oid oid = getOid(id);
        List<Class<Entity>> list = getTypeMap().get(
                oid.getAssetType().getToken());

        if (list != null) {
            boolean attribChecked = false;
            Attribute attrib = null;

            for (Class type : list) {
                short filterState = getAssetStateFilter(type);

                if ((filterState == -1) &&  (!validate || list.size()== 1))
                {
                    if(!validate)
                        return type;
                    Asset asset = getAsset(oid);
                    if(null != asset)
                    {
                        instance.setAsset(id, asset);
                        return type;
                    }
                    return null;
                }

                if (!attribChecked) {
                    attribChecked = true;
                    attrib = resolveAssetState(id, validate);
                }

                if (attrib == null) {
                    continue;
                }
                int state;
                try {
                    state = (attrib.getValue() != null) ? (Integer) attrib
                            .getValue() : -1;
                } catch (APIException e) {
                    throw new SDKException(e);
                }

                if ((filterState == -1) || (state == filterState)) {
                    return type;
                }
            }
        }
        return null;
    }

    private Attribute resolveAssetState(AssetID id, boolean validate) throws SDKException {
        Oid oid = getOid(id);
        IAttributeDefinition assetStateDef = oid.getAssetType()
                .getAttributeDefinition("AssetState");
        Asset asset = validate ? null : instance.getAsset(id);

        if (asset == null) {
            asset = getAsset(oid);

            if (asset == null) {
                return null;
            }
            instance.setAsset(id, asset);
        }
        Attribute attrib = asset.getAttribute(assetStateDef);

        if (attrib == null) {
            attrib = getAsset(oid).getAttribute(assetStateDef);
        }
        return attrib;
    }

    private Oid getOid(AssetID id) throws SDKException {
        try {
            return Oid.fromToken(id.getToken(), instance.getApiClient()
                    .getMetaModel());
        } catch (OidException e) {
            throw new SDKException(e);
        }
    }

    private Asset getAsset(Oid oid) throws SDKException {
    	createAssetStatesStorage();
        Query query = new Query(oid);

        IMetaModel model = instance.getApiClient().getMetaModel();
        if (!withoutAssetStates.get(model).contains(query.getAssetType()))
        {
	        try{
	        	query.getSelection().add(query.getAssetType().getAttributeDefinition("AssetState"));
	        }
	        catch(MetaException e){
	        	withoutAssetStates.get(model).add(query.getAssetType());
	        }
        }

        Asset[] assets;
        try {
            assets = instance.getApiClient().getServices().retrieve(query)
                    .getAssets();
        } catch (V1Exception e) {
            throw new SDKException(e);
        }
        return ((assets != null) && (assets.length > 0)) ? assets[0] : null;
    }

    private void createAssetStatesStorage() {
    	if (!withoutAssetStates.containsKey(instance.getApiClient().getMetaModel())) {
    		IMetaModel meta = instance.getApiClient().getMetaModel();
    		try {
    			IAssetType assetType = meta.getAssetType("Actual");
    			withoutAssetStates.put(meta, Arrays.asList(assetType));
    		} catch(MetaException e) {
    			withoutAssetStates.put(meta, new ArrayList<IAssetType>());
    		}
    	}
    }
}