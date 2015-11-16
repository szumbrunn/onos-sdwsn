package org.onosproject.provider.lldp.impl;

import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import org.junit.Before;
import org.junit.Test;
import org.onosproject.TestApplicationId;
import org.onosproject.net.Device;
import org.onosproject.net.DeviceId;
import org.onosproject.net.config.ConfigApplyDelegate;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class SuppressionConfigTest {
    private static final String APP_NAME = "SuppressionConfigTest";
    private static final TestApplicationId APP_ID = new TestApplicationId(APP_NAME);
    private static final DeviceId DEVICE_ID_1 = DeviceId.deviceId("of:1111000000000000");
    private static final DeviceId DEVICE_ID_2 = DeviceId.deviceId("of:2222000000000000");
    private static final Device.Type DEVICE_TYPE_1 = Device.Type.ROADM;
    private static final Device.Type DEVICE_TYPE_2 = Device.Type.FIBER_SWITCH;
    private static final String ANNOTATION_KEY_1 = "no_lldp";
    private static final String ANNOTATION_VALUE_1 = "true";
    private static final String ANNOTATION_KEY_2 = "sendLLDP";
    private static final String ANNOTATION_VALUE_2 = "false";

    private SuppressionConfig cfg;

    @Before
    public void setUp() throws Exception {
        ConfigApplyDelegate delegate = config -> { };
        ObjectMapper mapper = new ObjectMapper();
        cfg = new SuppressionConfig();
        cfg.init(APP_ID, LldpLinkProvider.CONFIG_KEY, JsonNodeFactory.instance.objectNode(), mapper, delegate);
    }

    @Test
    public void testDeviceIds() {
        Set<DeviceId> inputIds = new HashSet<DeviceId>() { {
            add(DEVICE_ID_1);
            add(DEVICE_ID_2);
        } };

        assertNotNull(cfg.deviceIds(inputIds));

        Set<DeviceId> outputIds = cfg.deviceIds();
        assertTrue(outputIds.contains(DEVICE_ID_1));
        assertTrue(outputIds.contains(DEVICE_ID_2));
        assertEquals(outputIds.size(), 2);
    }

    @Test
    public void testDeviceTypes() {
        Set<Device.Type> inputTypes = new HashSet<Device.Type>() { {
            add(DEVICE_TYPE_1);
            add(DEVICE_TYPE_2);
        } };

        assertNotNull(cfg.deviceTypes(inputTypes));

        Set<Device.Type> outputTypes = cfg.deviceTypes();
        assertTrue(outputTypes.contains(DEVICE_TYPE_1));
        assertTrue(outputTypes.contains(DEVICE_TYPE_2));
        assertEquals(outputTypes.size(), 2);
    }

    @Test
    public void testDeviceAnnotation() {
        Map<String, String> inputMap = new HashMap<String, String>() { {
            put(ANNOTATION_KEY_1, ANNOTATION_VALUE_1);
            put(ANNOTATION_KEY_2, ANNOTATION_VALUE_2);
        } };

        assertNotNull(cfg.annotation(inputMap));

        Map<String, String> outputMap = cfg.annotation();
        assertEquals(outputMap.get(ANNOTATION_KEY_1), ANNOTATION_VALUE_1);
        assertEquals(outputMap.get(ANNOTATION_KEY_2), ANNOTATION_VALUE_2);
        assertEquals(outputMap.size(), 2);
    }

}
