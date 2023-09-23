package com.practise.userApp.controller;

import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;

import lombok.Builder;
import lombok.Data;


//===============Static & Dynamic FIlters in RESTAPI=========================================

// just for understanding creating the inside the same file

@Data
@Builder
@JsonIgnoreProperties(value = {"gender","email"},allowGetters = true)
//Annotation that can be used to either suppress serialization of properties  or ignore processing ofJSON properties read (during deserialization). 
@JsonFilter("dynamicFilter")
class UserFilter {
	
	private String firstName;
	private String lastName;
	private String gender;
	private String address;
	private String phone;
	private String email;
	@JsonIgnore
	private String password;
	
	
}

@RestController
public class UserFilterController {

	
	@GetMapping("/v1/userfilter")
	public UserFilter userStaticFilterMapping() {
		return new UserFilter("firstName", "lastName", "gender", "address", "phone", "email", "password");
		
	}
	
	@GetMapping("/v2/userfilter")
	public MappingJacksonValue userDynamicFilterMapping() {
		 UserFilter myBean= new UserFilter("firstName", "lastName", "gender", "address", "phone", "email", "password");
		
		
		MappingJacksonValue mjv= new MappingJacksonValue(myBean);
		FilterProvider filter=new SimpleFilterProvider()
									.addFilter("dynamicFilter", SimpleBeanPropertyFilter.filterOutAllExcept("firstName","phone"));
		mjv.setFilters(filter);
		
		return mjv;
		
	}
}
