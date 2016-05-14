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

import com.olanh.olanh_entities.MenuItem;
import com.olanh.olanh_entities.list.MenuItems;
import com.olanh.olanh_entities.status.StatusAdd;
import com.olanh.olanh_entities.status.StatusDelete;
import com.olanh.olanh_entities.status.StatusGet;
import com.olanh.olanh_entities.status.StatusUpdate;
import com.olanh.pam_dataaccess.hibernate.DAOMenuItem;
import com.olanh.pam_dataaccess.util.DAOResponseUtil;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ControllerMenuItem {

	private static DAOMenuItem daoMenuItem = new DAOMenuItem();

	// CREATE
	@POST
	public Response addMenuItem(MenuItem menuItem, @Context UriInfo uriInfo) throws URISyntaxException {
		DAOResponseUtil<StatusAdd, MenuItem> daoResponse = ControllerMenuItem.daoMenuItem.addMenuItem(menuItem);
		if (daoResponse.getStatus() == StatusAdd.OK) {
			MenuItem menuItemAdded = daoResponse.getResponse();
			URI uri = uriInfo.getAbsolutePathBuilder().path(menuItemAdded.getId() + "").build();
			return Response.created(uri).status(Status.CREATED).entity(menuItemAdded).build();
		}
		return Response.status(Status.OK).entity(daoResponse.getResponse()).build(); // TEMP
		// return Response.status(Status.INTERNAL_SERVER_ERROR).build();
	}

	// GET
	@GET
	public Response getMenuItems(@PathParam("placeId") long placeId, @Context UriInfo uriInfo)
			throws URISyntaxException {
		DAOResponseUtil<StatusGet, MenuItems> daoResponse = ControllerMenuItem.daoMenuItem.allMenuItems(placeId);
		if (daoResponse.getStatus() == StatusGet.OK) {
			MenuItems menuItems = daoResponse.getResponse();
			for (MenuItem menuItem : menuItems) {
				menuItem.addLink(this.getUriForSelf(uriInfo, menuItem), "self");
				menuItem.addLink(this.getUriForSelfPlace(uriInfo, placeId), "via");
			}
			return Response.ok(menuItems).build();
		} else if (daoResponse.getStatus() == StatusGet.NOT_FOUND) {
			return Response.status(Status.NOT_FOUND).build();
		}
		// TEMP
		for (MenuItem menuItem : daoResponse.getResponse()) {
			menuItem.addLink(this.getUriForSelf(uriInfo, menuItem), "selfOffline");
			menuItem.addLink(this.getUriForSelfPlace(uriInfo, placeId), "via");
		}
		return Response.status(Status.OK).entity(daoResponse.getResponse()).build();
		// return
		// Response.serverError().entity(daoResponse.getResponse()).build();

	}

	// UPDATE
	@PUT
	public Response updateMenuItem(@PathParam("placeId") long placeId, MenuItem menuItem) {
		DAOResponseUtil<StatusUpdate, MenuItem> daoResponse = ControllerMenuItem.daoMenuItem.updateMenuItem(menuItem);
		if (daoResponse.getStatus() == StatusUpdate.UPDATED) {
			MenuItem menuItemUpdated = daoResponse.getResponse();
			menuItemUpdated.setPlaceId(placeId);
			return Response.status(Status.ACCEPTED).entity(menuItemUpdated).build();
		} else if (daoResponse.getStatus() == StatusUpdate.NOT_FOUND) {
			return Response.status(Status.NOT_FOUND).build();
		}
		// TEMP
		menuItem.setId(placeId);
		return Response.ok().entity(menuItem).build();
		// return Response.serverError().build();
	}

	// DELETE
	@DELETE
	public Response deleteMenuItem(@PathParam("placeId") long placeId, @QueryParam("MenuItemUpdated") long menuItemId) {
		DAOResponseUtil<StatusDelete, MenuItem> daoResponse = ControllerMenuItem.daoMenuItem.deleteMenuItem(menuItemId);
		if (daoResponse.getStatus() == StatusDelete.DELETED) {
			return Response.status(Status.ACCEPTED).build();
		} else if (daoResponse.getStatus() == StatusDelete.NOT_FOUND) {
			return Response.status(Status.NOT_FOUND).build();
		}
		return Response.status(Status.INTERNAL_SERVER_ERROR).build();
	}

	// Private Methods
	private String getUriForSelf(UriInfo uriInfo, MenuItem menuItem) {
		String url = uriInfo.getAbsolutePathBuilder().build().toString();
		return url;
	}

	private String getUriForSelfPlace(UriInfo uriInfo, long menuItem) {
		String url = uriInfo.getBaseUriBuilder().path(ControllerPlace.class).path(menuItem + "").build().toString();
		return url;
	}

}
