package APITESTING;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import org.junit.Assert;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class Restapi {
    static String newAddress = "80 Summer walk, Uk";
    public static void main(String[] args) {
        RestAssured.baseURI = "https://rahulshettyacademy.com";
        String response = given().log().all().queryParam("key", "qaclick123").header("Content-Type", "application/json")
                .body(Payload.addPlace()).when().post("maps/api/place/add/json").then().assertThat().statusCode(200)
                .body("scope", equalTo("APP")).header("Server", "Apache/2.4.18 (Ubuntu)").extract().response().asString();
        System.out.println(response);
        JsonPath js = new JsonPath(response);
        String placeid = js.getString("place_id");
        System.out.println(placeid);

        //update place


        given().log().all().queryParam("key", "qaclick123").header("Content-Type", "application/json")
               .body(Payload.updatePlace(placeid,newAddress)) .when().put("maps/api/place/update/json").then().assertThat().log().all().statusCode(200).body("msg", equalTo("Address successfully updated"));


        //get place
        String getresponse = given().log().all().queryParam("key",  "qaclick123").queryParam("place_id", placeid)
                .when().get("/maps/api/place/get/json").then().assertThat().statusCode(200).extract().asString();
        JsonPath js1 = new JsonPath(getresponse);
        String actualAddress = js1.getString("address");
        System.out.println(actualAddress);
        Assert.assertEquals(newAddress,actualAddress);
       // Assert.assertThat(actualAddress,equalTo(newAddress));

    }

}

         //.body("{\n" +
//                        "\"place_id\":\"" + placeid + "\",\n" +
//                        "\"address\":\"" + newAddress + "\",\n" +
//                        "\"key\":\"qaclick123\"\n" + "}")