package com.olanh.restful_pam.controller;

import java.net.URI;
import java.net.URISyntaxException;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
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
import com.olanh.olanh_entities.Schedule;
import com.olanh.olanh_entities.list.Schedules;
import com.olanh.olanh_entities.status.StatusAdd;
import com.olanh.olanh_entities.status.StatusDelete;
import com.olanh.olanh_entities.status.StatusGet;
import com.olanh.olanh_entities.status.StatusUpdate;
import com.olanh.pam_dataaccess.hibernate.DAOSchedule;
import com.olanh.pam_dataaccess.util.DAOResponseUtil;
import com.olanh.restful_pam.util.ResourceUtil;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ControllerSchedule {

	private static DAOSchedule daoSchedule = new DAOSchedule();
	
	// CREATE
	@POST
	public Response addSchedule(Schedule schedule, @Context UriInfo uriInfo) throws URISyntaxException {
		DAOResponseUtil<StatusAdd, Schedule> daoResponse = ControllerSchedule.daoSchedule.addSchedule(schedule);
		if (daoResponse.getStatus() == StatusAdd.OK){
			Schedule scheduleAdded = daoResponse.getResponse();
			URI uri = uriInfo.getAbsolutePathBuilder().path(scheduleAdded.getId() + "").build();
			return Response.created(uri).status(Status.CREATED).entity(scheduleAdded).build();
		}
		return Response.status(Status.INTERNAL_SERVER_ERROR).entity(daoResponse.getResponse()).build(); // TEMP
		//return Response.status(Status.INTERNAL_SERVER_ERROR).build();
	}

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
		return Response.ok().entity(daoResponse.getResponse()).build();
		//return Response.serverError().build();
	}

	//UPDATE
	@PUT
	public Response updateSchedule(@PathParam("placeId") long placeId, Schedule schedule) {
		DAOResponseUtil<StatusUpdate, Schedule> daoResponse = ControllerSchedule.daoSchedule.updateSchedule(schedule);
		if (daoResponse.getStatus() == StatusUpdate.UPDATED){
			Schedule scheduleUpdated = daoResponse.getResponse();
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

	//DELETE
	@DELETE
	public Response deleteSchedule(@PathParam("placeId") long placeId,  @QueryParam("scheduleId") long scheduleId) {
		DAOResponseUtil<StatusDelete, Schedule> daoResponse = ControllerSchedule.daoSchedule.deleteSchedule(scheduleId);
			if (daoResponse.getStatus() == StatusDelete.DELETED){
				return Response.status(Status.ACCEPTED).build();
			} else if (daoResponse.getStatus() == StatusDelete.NOT_FOUND) {
				return Response.status(Status.NOT_FOUND).build();
			}
		return Response.status(Status.INTERNAL_SERVER_ERROR).build();
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
