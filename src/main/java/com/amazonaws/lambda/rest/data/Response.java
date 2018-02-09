package com.amazonaws.lambda.rest.data;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParseException;
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
	private GeoJsonProp prop = new GeoJsonProp();

	@JsonProperty("geometry")
	public Map<String, Object> getGeometry() {
		return geometry;
	}
	
	@JsonProperty("type")
	public String getType() {
		return "Feature";
	}
	
	@JsonProperty("properties")
	public GeoJsonProp getProp() {
		return prop;
	}
	
	
	public static class GeoJsonProp {
		@JsonProperty("address")
		private String address;
		@JsonProperty("description")
		private String description;
		@JsonProperty("url")
		private String url;
		@JsonProperty("refrence")
		private String refrence;
		
		@JsonProperty("url")
		public String getUrl() {
			return url;
		}

		@JsonProperty("description")
		public String getDescription() {
			return description;
		}

		@JsonProperty("address")
		public String getAddress() {
			return address;
		}
		
		@JsonProperty("refrence")
		public String getRefrence() {
			return refrence;
		}
	}
	
	/*
	 * AWS lambda uses a JSON serialiser that doesn't like the @JsonRawValue tag. 
	 * So we need to pull the valid json out of raw format -_-
	 */
	public Response setGeometry(String geometry) throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
        this.geometry =  mapper.readValue(geometry, 
        		MapType.construct(HashMap.class, 
        				SimpleType.construct(String.class),
        				SimpleType.construct(Object.class)));
		return this;
	}
	
	public Response setUrl(String url) {
		this.prop.url = url;
		return this;
	}
	
	public Response setDescription(String description) {
		this.prop.description = description;
		return this;
	}

	public Response setAddress(String address) {
		this.prop.address = address;
		return this;
	}

	public Response setRefrence(String refrence) {
		this.prop.refrence = refrence;
		return this;
	}
}
