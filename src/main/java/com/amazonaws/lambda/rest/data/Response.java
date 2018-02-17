package com.amazonaws.lambda.rest.data;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.MapType;
import com.fasterxml.jackson.databind.type.SimpleType;
/*
 * POJO response item that serialises as a GeoJSON feature object
 * 
 * @see http://geojson.org/geojson-spec.html#feature-objects
 */
public class Response {	
	@JsonProperty("geometry")
	private Map<String, Object> geometry;
	@JsonProperty("properties")
	private GeoJsonProp properties = new GeoJsonProp();

	public Response(String applications, String geometry ) throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		this.geometry = mapper.readValue(geometry, 
				new TypeReference<Map<String,Object>>() {});
		this.properties.applications = mapper.readValue(applications, 
				new TypeReference<List<Map<String,Object>>>() {});
	}
	
	@JsonProperty("geometry")
	public Map<String, Object> getGeometry() {
		return geometry;
	}
	
	@JsonProperty("type")
	public String getType() {
		return "Feature";
	}
	
	@JsonProperty("properties")
	public GeoJsonProp getProperties() {
		return properties;
	}
	
	
	
	
	public static class GeoJsonProp {
		@JsonProperty("applications")
		private List<Map<String, Object>>  applications;
		@JsonProperty("applications")
		public List<Map<String, Object>>  getApplications() {
			return applications;
		}
	}
	
	/*
	 * AWS lambda uses a JSON serialiser that doesn't like the @JsonRawValue tag. 
	 * So we need to pull the valid json out of raw format -_-
	 */
	private Map<String, Object> deserialise(String json) throws IOException {
		ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(json, 
        		MapType.construct(HashMap.class, 
        				SimpleType.construct(String.class),
        				SimpleType.construct(Object.class)));
	}
}
