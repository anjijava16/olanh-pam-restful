package com.olanh.restful_pam.body_reader;

import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.ws.rs.Consumes;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.Provider;

import com.olanh.olanh_entities.Place;
import com.olanh.restful_pam.util.ResourceUtil;

//THIS BODY READER IS NOT WORKING> IS NOT COMPLETED.
@Provider
@Consumes("application/placejson")
public class BRPlace implements MessageBodyReader<Place> {

	@Override
	public boolean isReadable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
		return Place.class.isAssignableFrom(type);
	}

	@Override
	public Place readFrom(Class<Place> type, Type genericType, Annotation[] annotations, MediaType mediaType,
			MultivaluedMap<String, String> httpHeaders, InputStream entityStream)
					throws IOException, WebApplicationException {
		String stringPlace = ResourceUtil.getJsonFromInputStream(entityStream);
		Place place = ResourceUtil.getFromJson(stringPlace, Place.class);
		return place;
	}
}
