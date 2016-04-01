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
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import com.olanh.olanh_entities.Place;
import com.olanh.olanh_entities.list.Places;
import com.olanh.olanh_entities.status.StatusAdd;
import com.olanh.olanh_entities.status.StatusDelete;
import com.olanh.olanh_entities.status.StatusGet;
import com.olanh.olanh_entities.status.StatusUpdate;
import com.olanh.pam_dataaccess.hibernate.DAOPlace;
import com.olanh.pam_dataaccess.util.DAOResponseUtil;
import com.olanh.restful_pam.util.ResourceUtil;

//@Singleton //To use this class like object. Just one instance.
@Path("/place")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ControllerPlace {

	private static DAOPlace daoPlace = new DAOPlace();
	
	// CREATE
	@POST
	public Response addPlace(Place place, @Context UriInfo uriInfo) throws URISyntaxException {
		DAOResponseUtil<StatusAdd, Place> daoResponse = ControllerPlace.daoPlace.addPlace(place);
		if (daoResponse.getStatus() == StatusAdd.OK){
			Place placeAdded = daoResponse.getResponse();
			URI uri = uriInfo.getAbsolutePathBuilder().path(placeAdded.getId() + "").build();
			return Response.created(uri).status(Status.CREATED).entity(placeAdded).build();
		}
		return Response.status(Status.INTERNAL_SERVER_ERROR).entity(ResourceUtil.getJson(daoResponse.getResponse(), Place.class)).build(); // TEMP
		//return Response.status(Status.INTERNAL_SERVER_ERROR).build();
	}

	/* READ
	 * The method below is optional if the parameters grow fast
	 * public String getPlaces(@BeanParam BeanPlace beanPlace){}
	*/
	@GET
	public Response getPlaces(@QueryParam("city") String city, @Context UriInfo uriInfo) throws URISyntaxException {
		DAOResponseUtil<StatusGet, Places> daoResponse = null;
		Places places;
		// Look by City
		if (city != null) {
			daoResponse = ControllerPlace.daoPlace.allPlacesByCity(city);
			places = daoResponse.getResponse(); //TEMP
			places.get(0).setCity(city); // to work with //TEMP
			// Look for all
		} else {
			daoResponse = ControllerPlace.daoPlace.allPlaces();
		}
		//Evaluate Response
		if (daoResponse != null){
			if (daoResponse.getStatus() == StatusGet.OK) {
				for (Place place : daoResponse.getResponse()){
					place.addLink(this.getUriForSelf(uriInfo, place),"self");
				}
				return Response.ok(daoResponse.getResponse()).build();
			}else if (daoResponse.getStatus() == StatusGet.NOT_FOUND){
				return Response.status(Status.NOT_FOUND).build();
			}
		}
		// TEMP
		for (Place place : daoResponse.getResponse()){
			place.addLink(this.getUriForSelf(uriInfo, place),"self");
		}
		return Response.serverError().entity(ResourceUtil.getJson(daoResponse.getResponse(), Places.class)).build();
		//return Response.serverError().build();
	}

	// GET
	@GET
	@Path("/{placeId}")
	public Response getPlace(@PathParam("placeId") long placeId, @Context UriInfo uriInfo) throws URISyntaxException {
		DAOResponseUtil<StatusGet, Place> daoResponse = ControllerPlace.daoPlace.getPlace(placeId);
		if (daoResponse.getStatus() == StatusGet.OK){
			Place place = daoResponse.getResponse();
			place.addLink(this.getUriForSelf(uriInfo, place),"self");
			return Response.ok(place).build();
		} else if (daoResponse.getStatus() == StatusGet.NOT_FOUND){
			return Response.status(Status.NOT_FOUND).build();
		}
		// TEMP
		daoResponse.getResponse().addLink("www.com","self");
		return Response.serverError().entity(daoResponse.getResponse()).build();
		//return Response.serverError().build();
	}

	// UPDATE
	@PUT
	@Path("/{placeId}")
	public Response updatePlace(@PathParam("placeId") long placeId, Place place) {
		DAOResponseUtil<StatusUpdate, Place> daoResponse = ControllerPlace.daoPlace.updatePlace(place);
		if (daoResponse.getStatus() == StatusUpdate.UPDATED){
			Place placeUpdated = daoResponse.getResponse();
			placeUpdated.setId(placeId);
			return Response.status(Status.ACCEPTED).entity(ResourceUtil.getJson(placeUpdated, Place.class)).build();
		}else if (daoResponse.getStatus() == StatusUpdate.NOT_FOUND){
			return Response.status(Status.NOT_FOUND).build();
		}
		// TEMP
		place.setId(placeId);
		return Response.serverError().entity(place).build();
		//return Response.serverError().build();
	}

	// DELETE
	@DELETE
	@Path("/{placeId}")
	public Response deletePlace(@PathParam("placeId") long placeId) {
		DAOResponseUtil<StatusDelete, Place> daoResponse = ControllerPlace.daoPlace.deletePlace(placeId);
			if (daoResponse.getStatus() == StatusDelete.DELETED){
				return Response.status(Status.ACCEPTED).build();
			} else if (daoResponse.getStatus() == StatusDelete.NOT_FOUND) {
				return Response.status(Status.NOT_FOUND).build();
			}
		return Response.status(Status.INTERNAL_SERVER_ERROR).build();
	}

	// ScheduleResource
	@Path("/{placeId}/schedule")
	public ControllerSchedule getControllerSchedule() {
		return new ControllerSchedule();
	}

	// Private Methods
	private String getUriForSelf(UriInfo uriInfo, Place place) {
		String url = uriInfo.getAbsolutePathBuilder()
							//.path(ControllerPlace.class)
							.path(Long.toString(place.getId()))
							.build().toString();
		return url;
	}
	
	
	
	// REFERENCE ONLY
	// CONTEXT
	/*
	 * @GET
	 * @Path("/context")
	 * @Produces(MediaType.TEXT_PLAIN) 
	 * public String webContext(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders){ 
	 *      return uriInfo.getAbsolutePath().toString() + " " + httpHeaders.getCookies(); 
	 * }
	 */
}
