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

public class Response {
	public String address;
	public String description;
	public String url;
	public Map<String, Object> geometry;
	public String refrence;

	@JsonProperty("Geometry")
	public Map<String, Object> getGeometry() {
		return geometry;
	}
	
	public Response setGeometry(String geometry) throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
        this.geometry =  mapper.readValue(geometry, 
        		MapType.construct(HashMap.class, 
        				SimpleType.construct(String.class),
        				SimpleType.construct(Object.class)));
		return this;
	}
	
	@JsonProperty("URL")
	public String getUrl() {
		return url;
	}
	
	public Response setUrl(String url) {
		this.url = url;
		return this;
	}
	
	@JsonProperty("Description")
	public String getDescription() {
		return description;
	}

	public Response setDescription(String description) {
		this.description = description;
		return this;
	}
	
	@JsonProperty("Address")
	public String getAddress() {
		return address;
	}

	public Response setAddress(String address) {
		this.address = address;
		return this;
	}
	
	@JsonProperty("Refrence")
	public String getRefrence() {
		return refrence;
	}

	public Response setRefrence(String refrence) {
		this.refrence = refrence;
		return this;
	}
}
