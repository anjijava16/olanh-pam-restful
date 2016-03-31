package com.olanh.restful_pam.filter_request;

import java.io.IOException;
import java.util.List;
import java.util.StringTokenizer;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import org.glassfish.jersey.internal.util.Base64;

@Provider
public class FilterSecurity implements ContainerRequestFilter {
	
	private static final String AUTHORIZATION_HEADER_KEY = "Authorization";
	private static final String AUTHORIZATION_HEADER_PREFIX = "Basic ";

	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException {
		List<String> authHeader = requestContext.getHeaders().get(AUTHORIZATION_HEADER_KEY);
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
		}
		Response unauthorizedStatus = Response
										.status(Response.Status.UNAUTHORIZED)
										.entity("Wrong username or password, try again.")
										.build();
		requestContext.abortWith(unauthorizedStatus);
	}
}
