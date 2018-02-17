package com.amazonaws.lambda.rest;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import com.amazonaws.lambda.rest.data.Request;
import com.amazonaws.lambda.rest.data.Response;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

public class RESTfulHandler implements RequestHandler<Request, List<Response>> {

	private static String password = System.getenv("BOTL_DATABASE_PASSWORD");
	private static String username = System.getenv("BOTL_DATABASE_USERNAME");
	private static String endpoint = System.getenv("BOTL_DATABASE_ENDPOINT");
	private static String port = System.getenv("BOTL_DATABASE_PORT");
	final static String QUERY = "SELECT CONCAT('[', GROUP_CONCAT(CONCAT( '{\"Reference\": \"', "
			+ "app.Refrence, '\", \"Description\": \"', app.Description, '\", \"URL\": \"', app.URL, "
			+ "'\", \"Comments\": ', COALESCE( ( SELECT CONCAT('[', GROUP_CONCAT(CONCAT('{\"Name\": \"', "
			+ "comm.Name, '\", \"Comment\": \"', comm.Comment, '\"}') SEPARATOR ', '), ']') FROM "
			+ "comments comm WHERE comm.ApplicationFK = app.ApplicationID ) , 'null'), '}' ) "
			+ "SEPARATOR ', '), ']' ) as Applications, ST_AsGeoJSON(app.Geometry) "
			+ "as Geom FROM applications app WHERE st_within(app.Geometry, "
			+ "envelope(linestring(point(?, ?), point(?, ?)))) GROUP BY "
			+ "app.Geometry ORDER BY st_distance(point(?, ?), app.Geometry)";
	
	/*
		SELECT 
			CONCAT(
				'[',
				GROUP_CONCAT(CONCAT(
								'{"Reference": "', 
								app.Refrence,
								'", "Description": "', 
								app.Description, 
								'", "URL": "', 
								app.URL, 
								'", "Comments": ', 
								COALESCE(
									(
										SELECT CONCAT('[', GROUP_CONCAT(CONCAT('{"Name": "', comm.Name, '", "Comment": "', comm.Comment, '"}') SEPARATOR ', '), ']')
										FROM comments comm
										WHERE comm.ApplicationFK = app.ApplicationID
									) , 'null'), 
								'}'
							) SEPARATOR ', '),
				']'
				) as Applications,
			ST_AsGeoJSON(app.Geometry) as Geom
		FROM applications app 
		WHERE st_within(app.Geometry, envelope(linestring(point(?, ?), point(?, ?)))) 
		GROUP BY app.Geometry
		ORDER BY st_distance(point(?, ?), app.Geometry)
	 * 
	 */
	
	
	
    @Override
    public List<Response> handleRequest(Request input, Context context) {
    	context.getLogger().log("Request Lat: " + input.getLatitude());
    	context.getLogger().log("Request Long: " + input.getLongitude());
    	context.getLogger().log("Request Rad: " + input.getRadius());
    	List<Response> data = new ArrayList<Response>();
    	Optional<Request> inputMonad = Optional.of(input);
    	
    	Double lat = inputMonad
    			.map(Request::getLatitude)
    			.filter(rangeInclusive(-90.0, 90.0))
    			.orElseThrow(() -> new IllegalArgumentException("[BadRequest] Illegal latitude"));
    	
    	Double lng = inputMonad
    			.map(Request::getLongitude)
    			.filter(rangeInclusive(-180.0, 180.0))
    			.orElseThrow(() -> new IllegalArgumentException("[BadRequest] Illegal longitude"));
    	
    	Double radius = inputMonad
    			.map(Request::getRadius)
    			.orElse(1.0);
    	 
    	/*
    	 * ======================================================
    	 * How to calculate size of 1° of longitude at Latitude x
    	 * ======================================================
    	 * 1° of Longitude = cos(Latitude) * length of 1° at equator
    	 * 1° at equator ~ 111km 
    	 */
    	Double lat1 = lat - (radius / 111);
    	Double lat2 = lat + (radius / 111);
    	Double lng1 = lng - (radius / Math.abs(Math.cos(Math.toRadians(lat)) * 111));
    	Double lng2 = lng + (radius / Math.abs(Math.cos(Math.toRadians(lat)) * 111));
    	
    	try {
			Connection conn = DriverManager.getConnection(
					"jdbc:mysql://" + endpoint + ":" + port + "?useSSL=false", 
					username, 
					password);

	    	conn.setCatalog("botl");
	    	PreparedStatement ps = conn.prepareStatement(QUERY);
	    	ps.setDouble(1, lng1);
	    	ps.setDouble(2, lat1);
	    	ps.setDouble(3, lng2);
	    	ps.setDouble(4, lat2);
	    	ps.setDouble(5, lng);
	    	ps.setDouble(6, lat);
	    	System.out.println(ps.toString());
		    ResultSet rs = ps.executeQuery();
		    
		    while (rs.next()) {
				data.add(new Response(rs.getString("Applications"), rs.getString("Geom")));
			}
			//context.getLogger().log("Successfully executed query.  Result: " + currentTime);
		}catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("[InternalServerError] SQL Error");
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("[InternalServerError] IO Exception");
		} 
		
		return data;

	}

	private static Predicate<Double> rangeInclusive(Double min, Double max) {
    	return (val -> val >= -90.0 && val <= 90.0);
    }
}
