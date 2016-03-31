package com.olanh.restful_pam.controller;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/signon")
public class ControllerSingOn {
	
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String testMessage(){
		return "This API requires login";
	}

}
