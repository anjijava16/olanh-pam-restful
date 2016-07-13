package com.olanh.restful_pam.rest.client;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.olanh.olanh_entities.Place;


public class ClientPAMAPI {
	
	public static void main(String[] args){
		
		Client client = ClientBuilder.newClient();
		
		WebTarget baseTarget = client.target("http://localhost:8080/restful-pam/pamwebapi/");
		WebTarget placeTarget = baseTarget.path("place");
		WebTarget placeIdTarget = placeTarget.path("{placeId}");
		//Response response = client.target("http://localhost:8080/restful-pam/pamwebapi/place/1").request().get();
		Response response = placeIdTarget
				.resolveTemplate("placeId", "9")
				.request(MediaType.APPLICATION_JSON)
				.get();//you can put something like Place.class in the get. but need to be implemented.
		Place place = response.readEntity(Place.class);
		
		System.out.println(place.toString());
	}

}
