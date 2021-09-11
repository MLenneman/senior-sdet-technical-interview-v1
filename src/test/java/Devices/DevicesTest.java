package Devices;

import Contants.EndPoints;
import DTOs.Device;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSenderOptions;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.*;

import static Contants.EndPoints.DEVICES;

public class DevicesTest {
    private final List<Device> devicesToRemove = new ArrayList<>();

    @AfterClass
    public void cleanup(){
        if(devicesToRemove.isEmpty()) {
            return;
        }
        devicesToRemove.forEach(d -> {
            RestAssured.delete(DEVICES.getUrl(d.getId()));
        });
    }

    @Test(priority = 1,
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
                .then().statusCode(200).extract().as(Device[].class));
        Assert.assertNotNull(devices);
        Assert.assertFalse(devices.isEmpty());
    }

    @Test(priority = 2,
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

    @Test(priority = 3,
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

    @Test(priority = 4,
            description = """
                            GIVEN that I have the 'devices' endpoint properly configured to respond to PUT requests 
                                updating field values on database
                            WHEN I hit the 'devices' endpoint PUT request with valid device's id as param
                            THEN I should see in the response code Status Code 200 
                                AND in the Response Body a JSON with the device updated
                            """,
            dataProvider = "update-valid-device")
    public void putDevice(Device deviceProvided) {
        Device deviceCreated = RestAssured
                .given().contentType(ContentType.JSON).body(deviceProvided)
                .when().put(DEVICES.getUrl(deviceProvided.getId()))
                .then().statusCode(200).extract().as(Device.class);
        Assert.assertNotNull(deviceCreated);
        Assert.assertEquals(deviceCreated.getType(),deviceProvided.getType());
    }

    @Test(priority = 5,
            description = """
                            GIVEN that I have the 'devices' endpoint properly configured to respond to get requests
                                returning single device present on database based on id only for valid ids
                            WHEN I hit the 'devices' endpoint get request with invalid device's id as param
                            THEN I should see in the response code Status Code 404
                                AND in the Response Body a JSON with empty value
                            """)
    public void getOneDeviceInvalid() {
        RestAssured
                .given().contentType(ContentType.JSON)
                .when().get(DEVICES.getUrl(291188L))
                .then().statusCode(404).extract().as(Device.class);
    }


    @DataProvider(name = "new-valid-device")
    public Object[][] provideNewValidDevice(){
        return new Object[][]{{new Device().setType("Type1")},{new Device().setId(998L).setType("Type2")},{new Device().setId(999L)}};
    }

    @DataProvider(name = "new-invalid-device")
    public Object[][] provideNewInvalidDevice(){
        return new Object[][]{{new Device()},{new Device().setId(-1L)}};
    }

    @DataProvider(name = "update-valid-device")
    public Object[][] provideUpdateValidDevice(){
        return new Object[][]{{new Device().setId(998L).setType("update with id 998")},{new Device().setId(999L)}};
    }

    @DataProvider(name = "update-invalid-device")
    public Object[][] provideUpdateInvalidDevice(){
        return new Object[][]{{new Device().setId(null).setType("update with id null")},{new Device().setId(0L).setType("update with id zero")}};
    }



}