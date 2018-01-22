package com.amazonaws.lambda.rest;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

import com.amazonaws.lambda.rest.data.Request;
import com.amazonaws.lambda.rest.data.Response;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

public class RESTfulHandler implements RequestHandler<Request, Response[]> {

	private static String password = System.getenv("BOTL_DATABASE_PASSWORD");
	private static String username = System.getenv("BOTL_DATABASE_USERNAME");
	private static String endpoint = System.getenv("BOTL_DATABASE_ENDPOINT");
	private static String port = System.getenv("BOTL_DATABASE_PORT");
	
    @Override
    public Response[] handleRequest(Request input, Context context) {
    	Response[] r = {new Response()};
    	r[0].address = "8 Glenside<br>Woodthorpe<br>Nottinghamshire<br>NG5 4NT";
    	r[0].description = "NMA for application 2016/0970.";
    	r[0].url = "https://pawam.gedling.gov.uk/online-applications/caseDetails.do?caseType=Application&keyVal=OUOLO3HL0CA00";
    	r[0].date = "1502236800000";
    	r[0].Geometry = new Double [][] {{52.991322,-1.1123620},
			{52.991224,-1.1125279},
			{52.991460,-1.1128359},
			{52.991540,-1.1126853},
			{52.991322,-1.1123620}};
    	
    	
    	return r;
    	//return getFile("test.json");
    	/*
        String currentTime = "unavailable";
//		context.getLogger().log("Input: " + input + "\n");
//		context.getLogger().log("lat: " + input.getLatitude() + "\n");
//		context.getLogger().log("long: " + input.getLongitude() + "\n");
//		context.getLogger().log("endpoint: " + endpoint + "\n");
//		context.getLogger().log("port: " + port + "\n");
//		context.getLogger().log("username: " + username + "\n");
//		context.getLogger().log("password: " + password + "\n");
		
		try {
			Connection conn = DriverManager.getConnection(
					"jdbc:mysql://" + endpoint + ":" + port + "?useSSL=false", 
					username, 
					password);

			Statement stmt = conn.createStatement();
		    ResultSet resultSet = stmt.executeQuery("SELECT NOW()");
		    

			if (resultSet.next()) {
				currentTime = resultSet.getObject(1).toString();
			}
			context.getLogger().log("Successfully executed query.  Result: " + currentTime);
		}catch (SQLException e) {
			e.printStackTrace();
		} 
		
		
		return currentTime + " @ " + input.getLongitude() + " " + input.getLatitude();*/
	}

    
    private String getFile(String fileName) {

    	StringBuilder result = new StringBuilder("");

    	//Get file from resources folder
    	ClassLoader classLoader = getClass().getClassLoader();
    	File file = new File(classLoader.getResource(fileName).getFile());
    	System.out.println(file.exists());

    	try (Scanner scanner = new Scanner(file)) {

    		while (scanner.hasNextLine()) {
    			String line = scanner.nextLine();
    			result.append(line).append("\n");
    		}

    		scanner.close();

    	} catch (IOException e) {
    		e.printStackTrace();
    	}

    	return result.toString();

      }

    
}
