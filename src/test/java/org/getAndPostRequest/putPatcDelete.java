package org.getAndPostRequest;

import static io.restassured.RestAssured.*;

import io.restassured.http.ContentType;
import static org.hamcrest.Matchers.*;

import org.json.simple.JSONObject;
import org.testng.*;
import org.testng.annotations.Test;


public class putPatcDelete {
    @Test
    public void Put(){
        baseURI= "https://reqres.in";

        JSONObject requestBody= new JSONObject();

        requestBody.put("name","Asante Kofi Atah");
        requestBody.put("job","Quality Assurance Engineer");

        given()
                .header("Content-type", "application-json")
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(requestBody.toJSONString())
                .when()
                .post("/api/users/2")
                .then()
                .statusCode(201)
                .body("name",equalTo("Asante Kofi Atah"))
                .body("job",equalTo("Quality Assurance Engineer"))
                .log().all();
    }
    @Test
    public void Patch(){
        baseURI="https://reqres.in";

        JSONObject request= new JSONObject();

        request.put("name", "James Asante");
        request.put("job", "Software Engineer");

        given()
                .header("Content-type","application-json")
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(request.toJSONString())
                .patch("/api/users/2")
                .then()
                .statusCode(200)
                .body("name",equalTo("James Asante"))
                .body("job",equalTo("Software Engineer"))
                .log().all();
    }

    @Test
    public void delete(){
        baseURI="https://reqres.in";
        given()
                .header("content-type","application-json")
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .delete("/api/users/2")
                .then()
                .statusCode(204);
    }


}
