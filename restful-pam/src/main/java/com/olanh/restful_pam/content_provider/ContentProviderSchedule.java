package com.olanh.restful_pam.content_provider;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;

import javax.ws.rs.ext.ParamConverter;
import javax.ws.rs.ext.ParamConverterProvider;
import javax.ws.rs.ext.Provider;

import com.olanh.olanh_entities.Schedule;

@Provider
public class ContentProviderSchedule implements ParamConverterProvider {

	@Override
	public <T> ParamConverter<T> getConverter(final Class<T> rawType, Type genericType, Annotation[] annotations) {
		if(rawType.getName().equals(Schedule.class.getName())){
			return new ParamConverter<T>(){

				@Override
				public T fromString(String value) {
					Schedule schedule = new Schedule();
					List<String> scheduleList = Arrays.asList(value.split(";"));
					for (int index = 0; index < scheduleList.size(); index++){
						switch (index){
						case 0:
							schedule.setId(1+Long.getLong(scheduleList.get(index)));
							break;
						case 1:
							schedule.setMondayClose(scheduleList.get(index));
							break;
						case 2:
							schedule.setMondayOpen(scheduleList.get(index));
							break;
						case 3:
							schedule.setTuesdayClose(scheduleList.get(index));
							break;
						case 4:
							schedule.setTuesdayOpen(scheduleList.get(index));
							break;
						case 5:
							schedule.setWednesdayClose(scheduleList.get(index));
							break;
						case 6:
							schedule.setWednesdayOpen(scheduleList.get(index));
							break;
						case 7:
							schedule.setThursdayClose(scheduleList.get(index));
							break;
						case 8:
							schedule.setThursdayOpen(scheduleList.get(index));
							break;
						case 9:
							schedule.setFridayClose(scheduleList.get(index));
							break;
						case 10:
							schedule.setFridayOpen(scheduleList.get(index));
							break;
						case 11:
							schedule.setSaturdayClose(scheduleList.get(index));
							break;
						case 12:
							schedule.setSaturdayOpen(scheduleList.get(index));
							break;
						case 13:
							schedule.setSundayClose(scheduleList.get(index));
							break;
						case 14:
							schedule.setSundayOpen(scheduleList.get(index));
							break;
						default:
							break;
							
						}
					}
					return rawType.cast(schedule);
				}

				@Override
				public String toString(T myBean) {
					if (myBean == null){
						return null;
					}
					return myBean.toString();
				}
				
			};
		}
		return null;
	}
}
