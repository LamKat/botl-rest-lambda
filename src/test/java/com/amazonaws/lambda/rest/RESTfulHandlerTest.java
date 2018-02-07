package com.amazonaws.lambda.rest;

import java.io.IOException;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.amazonaws.lambda.rest.data.Request;
import com.amazonaws.services.lambda.runtime.Context;
import com.fasterxml.jackson.annotation.JsonRawValue;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * A simple test harness for locally invoking your Lambda function handler.
 */
public class RESTfulHandlerTest {

    private static Request input;

    @BeforeClass
    public static void createInput() throws IOException {
        input = new Request();
        input.setLatitude(-1.1423298);
        input.setLongitude(52.9510015);
        input.setRadius(1.0);
    }

    private Context createContext() {
        TestContext ctx = new TestContext();

        // TODO: customize your context here if needed.
        ctx.setFunctionName("Your Function Name");

        return ctx;
    }

    @Test
    public void testRESTfulHandler() throws JsonProcessingException {
        RESTfulHandler handler = new RESTfulHandler();
        Context ctx = createContext();
        ObjectMapper mapper = new ObjectMapper();
//        String output = mapper.writeValueAsString(handler.handleRequest(input, ctx));
//        System.out.println(output);
    }
}
