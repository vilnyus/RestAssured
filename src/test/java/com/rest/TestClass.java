package com.rest;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.config.LogConfig;
import io.restassured.filter.log.LogDetail;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import static io.restassured.RestAssured.*;
import static io.restassured.matcher.RestAssuredMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class TestClass {
    RequestSpecification requestSpecification;

//    @BeforeClass()
//    public void beforeClass(){
//        requestSpecification = with().
//                baseUri("https://api.postman.com").
//                header("X-Api-Key", "PMAK-610294b97cf9bc003484e6ef-bbee37fbc8767f08d1db6aaedd430829d0").
//                log().all();
//    }

    @BeforeClass()
    public void beforeClassBuilderStyle(){
        RequestSpecBuilder specificationBuilder = new RequestSpecBuilder();

        specificationBuilder.setBaseUri("https://api.postman.com");
        specificationBuilder.addHeader("X-Api-Key", "PMAK-610294b97cf9bc003484e6ef-bbee37fbc8767f08d1db6aaedd430829d0");
        specificationBuilder.addHeader("My_Header", "my_header_value"); //Custome header
        specificationBuilder.log(LogDetail.ALL);

        requestSpecification = specificationBuilder.build();
    }

    @Test
    public void BlackListHeaders(){
        Set<String> headers = new HashSet<>();
        headers.add("X-Api-Key");
        headers.add("Accept");
        given().
            baseUri("https://api.postman.com").
            header("X-Api-Key", "PMAK-610294b97cf9bc003484e6ef-bbee37fbc8767f08d1db6aaedd430829d0").
                config(config.logConfig(LogConfig.logConfig().blacklistHeaders(headers))).
            log().all().
        when().
                get("/workspaces").
        then().
                assertThat().
                log().all().
                statusCode(200);
    }

    @Test
    public void validate_bdd_Style(){
        RequestSpecification requestSpecification = given().
                baseUri("https://api.postman.com").
                header("X-Api-Key", "PMAK-610294b97cf9bc003484e6ef-bbee37fbc8767f08d1db6aaedd430829d0");
        given().spec(requestSpecification).
        when().
                get("/workspaces").
        then().
                assertThat().
                statusCode(200);
    }

    @Test
    public void reuse_specifications(){
        given().spec(requestSpecification).
                when().
                get("/workspaces").
                then().
                assertThat().
                statusCode(200);
    }

    @Test
    public void non_bdd_style_validation(){
        Response response = given().spec(requestSpecification).get("/workspaces");
        assertThat(response.statusCode(), is(equalTo(200)));
        assertThat(response.path("workspaces[0].name"), is(equalTo("My Test Workspace")));
    }
}
