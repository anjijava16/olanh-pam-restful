package com.olanh.restful_pam.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import com.google.gson.Gson;
import com.olanh.olanh_entities.Place;
import com.thoughtworks.xstream.XStream;

public class ResourceUtil {

	public static <T> String getJson(Object object, Class<T> classT){
		Gson gson = new Gson();
		return gson.toJson(object, classT);
	}
	
	public static <T> T getFromJson(String json, Class<T> classT){
		Gson gson = new Gson();
		return gson.fromJson(json, classT);
	}
	
	public static String getXML(List<Place> places){
		XStream xstream = new XStream();
		xstream.alias("Place", Place.class);
		return xstream.toXML(places);
	}
	
	public static String getXML(Place places){
		XStream xstream = new XStream();
		xstream.alias("Place", Place.class);
		return xstream.toXML(places);
	}
	
	public static String getJsonFromInputStream(InputStream is){
		BufferedReader br = null;
		StringBuilder sb = new StringBuilder();
		String line;
		try {
			br = new BufferedReader(new InputStreamReader(is));
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return sb.toString();
	}
}
