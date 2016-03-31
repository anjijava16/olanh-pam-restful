package com.olanh.restful_pam.body_writer;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;

import com.olanh.olanh_entities.Place;
import com.olanh.restful_pam.util.ResourceUtil;

@Provider
@Produces("application/placejson")
public class BWPlace implements MessageBodyWriter<Place> {

	@Override
	public boolean isWriteable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
		return Place.class.isAssignableFrom(type);
	}

	@Override
	public void writeTo(Place t, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType,
			MultivaluedMap<String, Object> httpHeaders, OutputStream entityStream)
					throws IOException, WebApplicationException {
		entityStream.write(ResourceUtil.getJson(t, Place.class).getBytes());
	}

	@Override
	public long getSize(Place t, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
		// Deprecated
		return -1;
	}
}
