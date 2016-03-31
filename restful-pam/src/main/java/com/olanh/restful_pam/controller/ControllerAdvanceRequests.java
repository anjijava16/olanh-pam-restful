package com.olanh.restful_pam.controller;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.olanh.olanh_entities.Schedule;

@Path(value = "special/{pathparam}/advance")
public class ControllerAdvanceRequests {

	@PathParam("pathparam") private String pathParam;
	@QueryParam("value") private Schedule queryParam;
	
	//http://localhost:8080/restful-pam/pamwebapi/special/yes/advance?value=1;2;3;4;5;6;7;8;8;9;0
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String testMethod(){
		return pathParam + " " + queryParam.toString();
	}
	
	
}
