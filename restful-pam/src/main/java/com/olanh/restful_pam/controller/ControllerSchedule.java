package com.olanh.restful_pam.controller;

import java.net.URISyntaxException;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.core.Response.Status;

import com.olanh.olanh_entities.Place;
import com.olanh.olanh_entities.Schedule;
import com.olanh.olanh_entities.status.StatusGet;
import com.olanh.pam_dataaccess.hibernate.DAOSchedule;
import com.olanh.pam_dataaccess.util.DAOResponseUtil;

@Path("/")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ControllerSchedule {

	private static DAOSchedule daoSchedule = new DAOSchedule();
	
	// GET
	@GET
	public Response getSchedule(@PathParam("placeId") long placeId, @QueryParam("scheduleId") long scheduleId, @Context UriInfo uriInfo) throws URISyntaxException {
		DAOResponseUtil<StatusGet, Schedule> daoResponse = ControllerSchedule.daoSchedule.getSchedule(scheduleId);
		if (daoResponse.getStatus() == StatusGet.OK){
			Schedule schedule = daoResponse.getResponse();
			schedule.addLink(this.getUriForSelf(uriInfo, schedule),"self");
			schedule.addLink(this.getUriForSelfPlace(uriInfo, schedule),"via");
			return Response.ok(schedule).build();
		} else if (daoResponse.getStatus() == StatusGet.NOT_FOUND){
			return Response.status(Status.NOT_FOUND).build();
		}
		// TEMP
		daoResponse.getResponse().addLink("www.com" + placeId,"self");
		return Response.serverError().entity(daoResponse.getResponse()).build();
		//return Response.serverError().build();
	}
	

	// Private Methods
	private String getUriForSelf(UriInfo uriInfo, Schedule schedule) {
		String url = uriInfo.getAbsolutePathBuilder()
							//.path(ControllerPlace.class)
							.path(Long.toString(schedule.getId()))
							.build().toString();
		return url;
	}
	private String getUriForSelfPlace(UriInfo uriInfo, Schedule schedule) {
		String url = uriInfo.getAbsolutePathBuilder()
							//.path(ControllerPlace.class)
							.build().toString();
		return url;
	}
	
}
