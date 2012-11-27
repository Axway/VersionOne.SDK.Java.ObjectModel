package com.versionone.om.tests;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.versionone.apiclient.APIException;
import com.versionone.apiclient.ConnectionException;
import com.versionone.apiclient.OidException;
import com.versionone.om.Defect;
import com.versionone.om.Entity;
import com.versionone.om.EntityValidationException;
import com.versionone.om.EntityValidator;

public class EntityValidatorTester extends BaseSDKTester {

    private EntityValidator validator;

    @Before
    public void setUp() {
        validator = new EntityValidator(getInstance());
    }

    @Test
    public void fieldValidationTest() throws APIException, ConnectionException, OidException {
        Defect defect = getInstance().create().defect("new defect", getSandboxProject());
        defect.save();
        Assert.assertTrue(validator.validate(defect, "Name"));
    }

    @Test
    //TODO in .NET sdk we can't set required fields as null. it seems defect in ApiClient.Attribute.checkNull
    public void nameAsNull() throws APIException, ConnectionException, OidException {
        Defect defect = getInstance().create().defect("Defect1", getSandboxProject());
        Assert.assertEquals(0, validator.validate(defect).size());

        defect.setName(null);

        List<String> validationErrors = validator.validate(defect);
        Assert.assertEquals(1, validationErrors.size());
        Assert.assertTrue(validationErrors.contains("Name"));
    }

    @Test
    public void entityCollectionSuccessfulValidationTest() throws APIException, ConnectionException, OidException {
        Defect[] defects = new Defect[] {
        		getInstance().create().defect("Defect1", getSandboxProject()),
        		getInstance().create().defect("Defect2", getSandboxProject()),
        		getInstance().create().defect("Defect3", getSandboxProject()),
                                        };
        Map<Entity, List<String>> validationResults = validator.validate(defects);
        Iterator it = validationResults.entrySet().iterator();
        while (it.hasNext()) {
            Entry<Entity, List<String>> pairs = (Entry<Entity, List<String>>) it.next();
            System.out.println(pairs.getKey() + " = " + pairs.getValue());
            Assert.assertEquals(0, pairs.getValue().size());
        }
    }

    @Test
    public void validationSuccessOnSaveTest() {
        boolean validationEnabledState = getInstance().isValidationEnabled();
        getInstance().setValidationEnabled(true);

        try {
            Defect defect = getInstance().create().defect("Defect1", getSandboxProject());
            defect.save();
        } catch(EntityValidationException ex) {
            Assert.fail("Filled attribute values should not fail validation");
        }

        getInstance().setValidationEnabled(validationEnabledState);
    }
}
