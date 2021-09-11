package Devices;

import DTOs.Device;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.internal.mapping.Jackson2Mapper;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.*;

import static Contants.EndPoints.DEVICES;

/**
 * This is an example of testing class, for a real world scenario it would be more agnostic containing only assertions
 * the reason for that i to make the tests independent of the testing tool
 *
 * Some of the tests will fail, that is intentional, since some should be negative scenarios, In those tests I've made some
 * comments with questions that should have been raised during elaboration
 */
public class DevicesTest {
    private final List<Device> devicesToRemove = new ArrayList<>();

    @AfterClass
    /**
     * there are better ways to clean the data, using SQL is an example.
     *
     * for this test, i could replace the json file present in the mock app,
     * but it would require to hardcode the path for the mock directory.
     */
    public void cleanup(){
        if(devicesToRemove.isEmpty()) {
            return;
        }
        devicesToRemove.forEach(d -> {
            RestAssured.delete(DEVICES.getUrl(d.getId()));
        });
    }

    /* Positive Scenarios */
    @Test(priority = 1,
            description = """
                            GIVEN that I have the 'devices' endpoint properly configured to respond to POST requests 
                                creating new devices
                            WHEN I hit the 'devices' endpoint with POST request without any id as param
                            THEN I should see in the response code Status Code 201 
                                AND in the Response Body a JSON with the device created
                            """,
            dataProvider = "new-valid-device")
    public void postNewDevice(Device deviceProvided) {
        Device deviceCreated = RestAssured
                .given().contentType(ContentType.JSON).body(deviceProvided)
                .when().post(DEVICES.getUrl())
                .then().statusCode(201).extract().as(Device.class);
        devicesToRemove.add(deviceCreated);
        Assert.assertNotNull(deviceCreated);
        Assert.assertNotNull(deviceCreated.getId());
        Assert.assertEquals(deviceCreated.getType(),deviceProvided.getType());
    }

    @Test(priority = 2,
            description = """
                            GIVEN that I have the 'devices' endpoint properly configured to respond to POST requests 
                                creating new devices and preserving compatibility with older versions
                            WHEN I hit the 'devices' endpoint with POST request containing unknown fields in body
                            THEN I should see in the response code Status Code 201 
                                AND in the Response Body a JSON with only valid fields
                            """)
    public void postNewDeviceShouldIgnoreUnknownField() {
        Device deviceCreated = RestAssured
                .given().contentType(ContentType.JSON).body("{\"id\":\"111\",\"type\":\"containing unkown field\",\"unknownField\":true}")//shouldn't it be allowed to preserve compatibility?
                .when().post(DEVICES.getUrl())
                .then().statusCode(201).extract().as(Device.class);
        devicesToRemove.add(deviceCreated);
        Assert.assertTrue(deviceCreated.getId().equals(111L));
        Assert.assertEquals(deviceCreated.getType(),"containing unkown field");
    }

    @Test(priority = 3,
            description = """
                            GIVEN that I have the 'devices' endpoint properly configured to respond to GET requests 
                                returning all devices on database
                            WHEN I hit the 'devices' endpoint with a GET request without any id as param
                            THEN I should see a list with all devices already present by default on database
                            """)
    public void getAllDevices() {
        List<Device> devices = Arrays.asList(RestAssured
                .given().contentType(ContentType.JSON)
                .when().get(DEVICES.getUrl())
                .then().statusCode(200).extract().as(Device[].class,getMapper()));
        Assert.assertNotNull(devices);
        Assert.assertFalse(devices.isEmpty());
    }

    @Test(priority = 4,
            description = """
                            GIVEN that I have the 'devices' endpoint properly configured to respond to get requests
                                returning single device present on database based on id
                            WHEN I hit the 'devices' endpoint get request with valid device's id as param
                            THEN I should see in the response code Status Code 200 
                                AND in the Response Body a JSON with the device requested
                            """)
    public void getOneDevice() {
        Device deviceCreated = RestAssured
                .given().contentType(ContentType.JSON).body(new Device().setType("getOneDevice"))
                .post(DEVICES.getUrl())
                .then().extract().as(Device.class);

        Device deviceRetrieved = RestAssured
                .given().contentType(ContentType.JSON)
                .when().get(DEVICES.getUrl(deviceCreated.getId()))
                .then().statusCode(200).extract().as(Device.class);
        Assert.assertEquals(deviceCreated,deviceRetrieved);
    }

    @Test(priority = 5,
            description = """
                            GIVEN that I have the 'devices' endpoint properly configured to respond to PUT requests 
                                updating field values on database
                            WHEN I hit the 'devices' endpoint PUT request with valid device's id as param
                            THEN I should see in the response code Status Code 200 
                                AND in the Response Body a JSON with the device updated
                            """,
            dataProvider = "update-valid-device")
    public void putDevice(Device deviceProvided) {
        RestAssured
                .given().contentType(ContentType.JSON).body(deviceProvided)
                .post(DEVICES.getUrl())
                .then().extract().as(Device.class);
        devicesToRemove.add(deviceProvided);
        deviceProvided.setType("Updated type");
        Device deviceUpdated = RestAssured
                .given().contentType(ContentType.JSON).body(deviceProvided)
                .when().put(DEVICES.getUrl(deviceProvided.getId()))
                .then().statusCode(200).extract().as(Device.class);
        Assert.assertNotNull(deviceUpdated);
        Assert.assertEquals(deviceUpdated.getType(),deviceProvided.getType());
    }

    /* Negative Scenarios */
    @Test(priority = 6,
            description = """
                            GIVEN that I have the 'devices' endpoint properly configured to respond to get requests
                                returning single device present on database based on id only for valid ids
                            WHEN I hit the 'devices' endpoint get request with invalid device's id as param
                            THEN I should see in the response code Status Code 404
                                AND in the Response Body a JSON with empty value
                            """)
    public void getOneDeviceInvalidId() {
        RestAssured
                .given().contentType(ContentType.JSON)
                .when().get(DEVICES.getUrl(291188L))
                .then().statusCode(404).extract().as(Device.class);
    }

    @Test(priority = 7,
            description = """
                            GIVEN that I have the 'devices' endpoint properly configured to respond to POST requests 
                                creating new devices
                            WHEN I hit the 'devices' endpoint with POST request containing missing info in body
                            THEN I should see in the response code Status Code 500 
                                AND in the Response Body a JSON empty
                            """,
            dataProvider = "new-invalid-device")
    public void postNewDeviceNegativeScenarioEmptyValues(Device deviceProvided) {
        RestAssured
                .given().contentType(ContentType.JSON).body(deviceProvided)
                .when().post(DEVICES.getUrl())
                .then().statusCode(500);
    }

    @Test(priority = 8,
            description = """
                            GIVEN that I have the 'devices' endpoint properly configured to respond to POST requests 
                                creating new devices
                            WHEN I hit the 'devices' endpoint with POST request containing invalid body
                            THEN I should see in the response code Status Code 500 
                                AND in the Response Body a JSON empty
                            """)
    public void postNewDeviceNegativeScenariosInvalidContent() {
        RestAssured
                .given().contentType(ContentType.JSON).body("{\"id\":\"something\",\"type\":10}")//should it be accepted?
                .when().post(DEVICES.getUrl())
                .then().statusCode(500);
    }

    @Test(priority = 5,
            description = """
                            GIVEN that I have the 'devices' endpoint properly configured to respond to PUT requests 
                                only for valid ids
                            WHEN I hit the 'devices' endpoint PUT request with invalid device's id as param
                            THEN I should see in the response code Status Code 404
                                AND in the Response Body a JSON empty
                            """,
            dataProvider = "update-valid-device")
    public void putDeviceNegativeScenariosInvalidContent(Device deviceProvided) {
        RestAssured
                .given().contentType(ContentType.JSON).body(deviceProvided)
                .when().put(DEVICES.getUrl(deviceProvided.getId()*-1000))
                .then().statusCode(404);
    }


    @DataProvider(name = "new-valid-device")
    public Object[][] provideNewValidDevice(){
        return new Object[][]{{new Device().setType("Type1")},{new Device().setId(998L).setType("Type2")},{new Device().setId(999L)}};
    }

    @DataProvider(name = "new-invalid-device")
    public Object[][] provideNewInvalidDevice(){
        return new Object[][]{
                {//should accept empty device?
                    new Device()
                },
                {//should accept negative values on id?
                    new Device().setId(-1L)
                }};
    }

    @DataProvider(name = "update-valid-device")
    public Object[][] provideUpdateValidDevice(){
        return new Object[][]{{new Device().setId(9998L).setType("update with id 998")},{new Device().setId(9999L)}};
    }

    private Jackson2Mapper getMapper() {
        return new Jackson2Mapper((type, s) -> {
            ObjectMapper om = new ObjectMapper().findAndRegisterModules();
            om.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            return om;
        });
    }

}