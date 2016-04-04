package com.olanh.restful_pam.controller;

import java.net.URISyntaxException;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.core.Response.Status;

import com.olanh.olanh_entities.Schedule;
import com.olanh.olanh_entities.status.StatusGet;
import com.olanh.olanh_entities.status.StatusUpdate;
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
			schedule.addLink(this.getUriForSelfPlace(uriInfo, placeId),"via");
			return Response.ok(schedule).build();
		} else if (daoResponse.getStatus() == StatusGet.NOT_FOUND){
			return Response.status(Status.NOT_FOUND).build();
		}
		// TEMP
		daoResponse.getResponse().addLink(this.getUriForSelf(uriInfo, daoResponse.getResponse()),"selfOffline");
		daoResponse.getResponse().addLink(this.getUriForSelfPlace(uriInfo, placeId),"via");
		return Response.serverError().entity(daoResponse.getResponse()).build();
		//return Response.serverError().build();
	}

	// UPDATE
	@PUT
	public Response updatePlace(@PathParam("placeId") long placeId, Schedule schedule) {
		DAOResponseUtil<StatusUpdate, Schedule> daoResponse = ControllerSchedule.daoSchedule.updateSchedule(schedule);
		if (daoResponse.getStatus() == StatusUpdate.UPDATED){
			Schedule scheduleUpdated= daoResponse.getResponse();
			scheduleUpdated.setId(placeId);
			return Response.status(Status.ACCEPTED).entity(scheduleUpdated).build();
		}else if (daoResponse.getStatus() == StatusUpdate.NOT_FOUND){
			return Response.status(Status.NOT_FOUND).build();
		}
		// TEMP
		schedule.setId(placeId);
		return Response.serverError().entity(schedule).build();
		//return Response.serverError().build();
	}

	

	// Private Methods
	private String getUriForSelf(UriInfo uriInfo, Schedule schedule) {
		String url = uriInfo.getAbsolutePathBuilder()
							.build().toString();
		return url;
	}

	private String getUriForSelfPlace(UriInfo uriInfo, long placeId) {
		String url = uriInfo.getBaseUriBuilder()
				.path(ControllerPlace.class)
				.path(placeId+"")
				.build().toString();
		return url;
	}
	
}
