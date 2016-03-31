package com.olanh.restful_pam.bean;

import javax.ws.rs.QueryParam;

public class BeanPlace {
	@QueryParam("city") String city;

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

}
