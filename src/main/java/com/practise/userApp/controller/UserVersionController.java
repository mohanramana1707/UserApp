package com.practise.userApp.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

record UserV1(String name) {}
record UserV2(String firstName,String secondName) {	}

@RestController
public class UserVersionController {
	
	//Response
	UserV1 populateUserV1() {
		return new UserV1("Mohan Ram");
	}
	
	UserV2 populateUserV2() {
		return new UserV2("Mohan" ," Ram");
	}
	//======================================================================
	
	//TYPE 1-URL Versioning
	
	@GetMapping(value="v1/person")
	public  UserV1 getUserV1Details() {
		return populateUserV1();
	}
	
	@GetMapping(value="v2/person")
	public  UserV2 getUserV2Details() {
		return populateUserV2();
	}
	
	//TYPE 2-Param Versioning
	
	@GetMapping(value="v1/person", params="version=1")
	public  UserV1 getUserV1Param() {
		return populateUserV1();
	}
	
	@GetMapping(value="v2/person", params="version=2")
	public  UserV2 getUserV2Param() {
		return populateUserV2();
	}
	
	//TYPE 3-Custom Request Header Versioning
	
	@GetMapping(value="v1/person", headers="API-VERSION-1")
	public  UserV1 getUserV1Headers() {
		return populateUserV1();
	}
	
	@GetMapping(value="v2/person", headers="API-VERSION-2")
	public  UserV2 getUserV2Headers() {
		return populateUserV2();
	}
	
	//TYPE 4-Produces Versioning
	
	@GetMapping(value="person/produces", headers="application/vnd.api-v1+json")
	public  UserV1 getUserV1Produces() {
		return populateUserV1();
	}
	
	@GetMapping(value="person/produces", headers="application/vnd.api-v2+json")
	public  UserV2 getUserV2Produces() {
		return populateUserV2();
	}

}
