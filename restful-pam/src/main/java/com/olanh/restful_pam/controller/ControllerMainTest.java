package com.olanh.restful_pam.controller;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;


@Path(value = "/test")
public class ControllerMainTest {
	
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String testMethod() {
		return "Web Service is on. wujuu";
	}

}
