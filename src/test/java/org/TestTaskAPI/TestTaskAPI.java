package org.TestTaskAPI;

import static io.restassured.RestAssured.*;

import com.github.javafaker.Faker;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.hamcrest.Matchers;
import org.json.simple.JSONObject;
import org.testng.Assert;
import org.testng.annotations.*;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;


import static org.hamcrest.Matchers.equalTo;

import java.util.HashMap;


public class TestTaskAPI {

    final String url= "http://localhost:3000/_taskApp/v1/";
    private String token;
    private String refreshToken;


    Faker faker = new Faker();

    HashMap <String,Object> requestBody= new HashMap<String,Object>();


    @BeforeTest
    public void GetToken(){
        baseURI= url;



        requestBody.put("email","kofi.asante@gmail.com");
        requestBody.put("password","600@Yolanda");
        requestBody.put("username", "kofi@__45");
        requestBody.put("role","taskManager");


        JSONObject jsonRequestBody= new JSONObject(requestBody);

        Response response=given()
                .header("Content-type","application-json")
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(jsonRequestBody.toJSONString())
                .post("/login")
                .then()
                .statusCode(200)
                .body("message",equalTo("Login Successfully"))
                .extract()
                .response();




        this.token= "Bearer ".concat(response.path("token"));
        this.refreshToken=response.path("refreshToken");

    }


    @Test
    public void TestRegisterUser(){
        baseURI=url;
        HashMap <String,Object> newUserRequestBody= new HashMap<String,Object>();
        String userName=faker.name().firstName();
        newUserRequestBody.put("username",userName);
        newUserRequestBody.put("email",userName.concat("@gmail.com"));
        newUserRequestBody.put("password","600@Yolanda");
        newUserRequestBody.put("role","taskManager");

        JSONObject jsonRequestBody= new JSONObject(newUserRequestBody);


        given()
                .header("Content-type","applcation-json")
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(jsonRequestBody.toJSONString())
                .post("/register")
                .then()
                .statusCode(200)
                .time(Matchers.lessThan(2000L))
                .body("message",equalTo("User registered Successfully"));



    }


    @Test
    public void TestUserLogin(){
        baseURI= url;

        JSONObject jsonRequestBody= new JSONObject(requestBody);

        Response response=given()
                .header("Content-type","application-json")
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(jsonRequestBody.toJSONString())
                .post("/login")
                .then()
                .statusCode(200)
                .body("message",equalTo("Login Successfully"))
                .extract()
                .response();


        String refreshToken= response.path("refreshToken");

        this.token= "Bearer ".concat(response.path("token"));

        System.out.println(this.token);

        this.refreshToken=response.path("refreshToken");
    }



    @Test
    public void getActiveUser() throws  NullPointerException{
        baseURI=url;

        Response activeUserData=given()
                .header("Content-type","application-json")
                .header("Authorization",this.token)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .when()
                .get("/activeUser")
                .then()
                .statusCode(200)
                .extract()
                .response();

        Assert.assertEquals(activeUserData.body().path("data.username"),requestBody.get("username"));
        Assert.assertEquals(activeUserData.body().path("data.email"),requestBody.get("email").toString().toLowerCase());
        Assert.assertEquals(activeUserData.body().path("data.role").toString(),"["+requestBody.get("role")+"]");

    }

    @Test
    public void getAllUsers(){
        baseURI= url;

        Response response=given()
                .header("Content-type","application-json")
                .header("Authorization",this.token)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .when()
                .get("/getAllUsers")
                .then()
                .statusCode(200)
                .time(Matchers.lessThan(2000L))
                .assertThat().body(matchesJsonSchemaInClasspath("org/schemas/allUsersSchema.json"))
                .extract().response();

        Assert.assertEquals(response.path("message"),"All users are");


    }


}
