package com.olanh.restful_pam.filter_response;
import java.io.IOException;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.Provider;


@Provider
public class FilterCORS implements ContainerResponseFilter {

	@Override
	public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext)
			throws IOException {
		MultivaluedMap<String, Object> responseHeaders = responseContext.getHeaders();
		responseHeaders.add("Access-Control-Allow-Origin", requestContext.getHeaderString("Origin"));
		responseHeaders.add("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT");
		responseHeaders.add("Access-Control-Allow-Headers", "Authorization,Content-Type,Origin,Access-Control-Request-Method,Access-Control-Request-Headers");
		
		
		/*List<String> authHeader = requestContext.getHeaders().ad.get(AUTHORIZATION_HEADER_KEY);
		if (authHeader != null && authHeader.size() > 0) {
			String authToken = authHeader.get(0);
			authToken = authToken.replaceFirst(AUTHORIZATION_HEADER_PREFIX, "");
			String decodingString = Base64.decodeAsString(authToken);
			StringTokenizer tokenizer = new StringTokenizer(decodingString, ":");
			String username = null, password = null;
			if (tokenizer.hasMoreTokens()){
				username = tokenizer.nextToken();
			}
			if (tokenizer.hasMoreTokens()){
				password = tokenizer.nextToken();
			}
			if (username != null && password != null){
				if ("hector".equals(username) &&"olan".equals(password)){
					return;
				}
			}
		}*/
		return;
		/*Response unauthorizedStatus = Response
										.status(Response.Status.UNAUTHORIZED)
										.entity("Wrong username or password, try again.")
										.build();
		requestContext.abortWith(unauthorizedStatus);*/
		
	}
}
