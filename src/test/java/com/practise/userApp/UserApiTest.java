package com.practise.userApp;

import java.net.URI;
import java.net.http.HttpHeaders;
import java.time.LocalDate;
import java.util.List;

import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.util.UriComponentsBuilder;

import com.practise.userApp.entity.Address;
import com.practise.userApp.entity.Name;
import com.practise.userApp.entity.User;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(classes =UserAppApplication.class,webEnvironment = WebEnvironment.RANDOM_PORT) // whenever this TEST class is called,UserAppApplication should run
@TestMethodOrder(OrderAnnotation.class)         // to define order of the TEST METHODS

public class UserApiTest {   // UNIT TEST for CONTROLLERS
	
	@LocalServerPort
	private int port;
	
	//admin
	private final TestRestTemplate restTempwithADMIN= new TestRestTemplate().withBasicAuth("springer1", "secret");
	//user
	private final TestRestTemplate restTempwithUSER= new TestRestTemplate().withBasicAuth("springer2", "secret");
	
	
	
	
	private final User user= User.builder().build() ;//new User() will be created Data will be populated by loadUser();             // we can use mockito instead of this
	private final Name name= Name.builder().firstName("firstName").secondName("secondName").build();
	private final Address address1=Address.builder().line1("address1-1").line2("address2-1").line3("address3-1").line4("address4-1").postCode("pincode-1").build();
	private final Address address2=Address.builder().line1("address1-2").line2("address2-2").line3("address3-2").line4("address4-2").postCode("pincode-2").build();
	
	
	private final org.springframework.http.HttpHeaders headers= new org.springframework.http.HttpHeaders();
	// for creating the Test URI
	public URI loadUri(String uri) {
		return URI.create(String.format("http://localhost:%s%s", port,uri) );
	}
	
	
	@BeforeEach               // before everyMethod User will be loaded
	public void loadUser() {   
		headers.setContentType(MediaType.APPLICATION_JSON);
		user.setName(name);
		user.setBirthDate(LocalDate.of(1990, 11, 12));
		user.setEmail("test@gmail.com");
		user.setAddress(List.of(address1,address2));
		
		address1.setUser(List.of(user));
		address2.setUser(List.of(user));
		
	}
	
	@Test
	@Order(1)
	public void whenValidUser_create() {
		final ResponseEntity<Object> replywithADMIN= restTempwithADMIN.exchange(loadUri("/users"), 
															HttpMethod.POST, 
															new HttpEntity<>(user,headers),   // user loaded from loadUser()
															Object.class);
		
		Assert.assertEquals(201, replywithADMIN.getStatusCode().value());  //201 or HttpStatus.CREATED
		Assert.assertNotNull(replywithADMIN.getHeaders().getLocation());
		
		final ResponseEntity<Object> replywithUSER= restTempwithUSER.exchange(loadUri("/users"), 
				HttpMethod.POST, 
				new HttpEntity<>(user,headers),   // user loaded form loadUser()
				Object.class);

			Assert.assertEquals(HttpStatus.FORBIDDEN.value(), replywithUSER.getStatusCode().value());  //201 or HttpStatus.CREATED
			Assert.assertNotNull(replywithUSER.getHeaders().getLocation());
		
	}
	
	@Test
	@Order(2)
	public void whenValidUser_retriveOneUser() {
		final ResponseEntity<Object> replywithADMIN= restTempwithADMIN.exchange(loadUri("/users/1"), HttpMethod.GET, new HttpEntity<>(user,headers), Object.class);
		
		Assert.assertEquals(200, replywithADMIN.getStatusCode().value());  //200 or HttpStatus.OK
		
		final ResponseEntity<Object> replywithUSER= restTempwithUSER.exchange(loadUri("/users/1"), HttpMethod.GET, new HttpEntity<>(user,headers), Object.class);
		
		Assert.assertEquals(HttpStatus.FORBIDDEN.value(), replywithUSER.getStatusCode().value());  //200 or HttpStatus.OK
		
		
	}
	
	@Test
	@Order(3)
	public void whenValidUser_retriveAllUsers() {
		final ResponseEntity<Object> replywithADMIN= restTempwithADMIN.exchange(loadUri("/users"), HttpMethod.GET, new HttpEntity<>(user,headers), Object.class);
		
		Assert.assertEquals(200, replywithADMIN.getStatusCode().value());
		
		final ResponseEntity<Object> replywithUSER= restTempwithUSER.exchange(loadUri("/users"), HttpMethod.GET, new HttpEntity<>(user,headers), Object.class);
		
		Assert.assertEquals(HttpStatus.FORBIDDEN.value(), replywithUSER.getStatusCode().value());
		
		
		
	}
	
	@Test
	@Order(4)
	public void whenValidUser_updateUser() {
		
		name.setFirstName("updatedFirstName");
		name.setSecondName("updatedSecondName");
		
		user.setName(name);
		final ResponseEntity<Object> replywithADMIN= restTempwithADMIN.exchange(loadUri("/users/1"), HttpMethod.PUT, new HttpEntity<>(user,headers), Object.class);
		
		Assert.assertEquals(HttpStatus.ACCEPTED.value(), replywithADMIN.getStatusCode().value()); // 202 -httpStatus code 
		
		final ResponseEntity<Object> replywithUSER= restTempwithUSER.exchange(loadUri("/users/1"), HttpMethod.PUT, new HttpEntity<>(user,headers), Object.class);
		
		Assert.assertEquals(HttpStatus.FORBIDDEN.value(), replywithUSER.getStatusCode().value()); // 202 -httpStatus code 
		
		
	}
	
	@Test
	@Order(5)
	public void whenValidUser_deleteUser() {
		user.setName(name);
		final ResponseEntity<Object> replywithADMIN= restTempwithADMIN.exchange(loadUri("/users/1"), HttpMethod.DELETE, new HttpEntity<>(user,headers), Object.class);
		
		Assert.assertEquals(HttpStatus.NO_CONTENT.value(), replywithADMIN.getStatusCode().value());  //204 -httpStatus code 
		
		final ResponseEntity<Object> replywithUSER= restTempwithUSER.exchange(loadUri("/users/1"), HttpMethod.DELETE, new HttpEntity<>(user,headers), Object.class);
		
		Assert.assertEquals(HttpStatus.FORBIDDEN.value(), replywithUSER.getStatusCode().value());  //204 -httpStatus code 
		
		
	}
	
	
	
	@Test
	@Order(6)
	public void whenValidUser_retriveAllUsersWithFilterAndVersion() {
		
		// we can use like this
		
//		String url=String.format("http://localhost:%s%s", port,"/v1/users") ;
//		UriComponentsBuilder.fromHttpUrl(url)
//							.queryParam("", "")
//							.build()
//							.toUri();
		
		
		final ResponseEntity<Object> replywithADMIN= restTempwithADMIN.exchange(loadUri("/v1/users"), HttpMethod.GET, new HttpEntity<>(user,headers), Object.class);
		
		Assert.assertEquals(200, replywithADMIN.getStatusCode().value());
		
		
		final ResponseEntity<Object> replywithUSER= restTempwithUSER.exchange(loadUri("/v2/users"), HttpMethod.GET, new HttpEntity<>(user,headers), Object.class);
		
		Assert.assertEquals(HttpStatus.FORBIDDEN.value(), replywithUSER.getStatusCode().value());
		
		
		
	}
	
	
	//======================================FAILURE CASES-INVALID cases======================================================
	
	
	@Test
	@Order(11)
	public void whenInvalid_User_shouldFail(){
		
		final ResponseEntity<Object > invalidUser= new TestRestTemplate().withBasicAuth("springer1234", "secret")
												   .exchange(loadUri("/users"), HttpMethod.GET, new HttpEntity<>(user,headers), Object.class);
		
		Assert.assertEquals(HttpStatus.UNAUTHORIZED.value(), invalidUser.getStatusCode().value());
		
	}
	
	@Test
	@Order(12)
	public void whenInValid_id_shouldFail() {
		
		final ResponseEntity<Object> replywithADMIN= restTempwithADMIN.exchange(loadUri("/users/1000"), HttpMethod.GET, new HttpEntity<>(user,headers), Object.class);
		
		String id="1000";
		String expected=String.format("User with ID as %s not found", 1000);  // as per the controller method
		
		Assert.assertTrue(replywithADMIN.getBody().toString().contains(expected));  //200 or HttpStatus.OK
		
	
	}
	
	@Test
	@Order(13)
	public void whenInvalid_format_shouldFail(){   // no such endpoint
		
		final ResponseEntity<Object > reply= restTempwithADMIN
												   .exchange(loadUri("/users/as"), HttpMethod.GET, new HttpEntity<>(user,headers), Object.class);
		
		Assert.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), reply.getStatusCode().value());
		
	}
	
	@Test
	@Order(14)
	public void when_InValid_dataFormat_shouldFail() {
		
		user.setEmail("sfkgfkgkfgfkkf"); // overriding the invalid email id from user object ,(without @ for email id)
		
		final ResponseEntity<Object> replywithADMIN= restTempwithADMIN.exchange(loadUri("/users"), 
															HttpMethod.POST, 
															new HttpEntity<>(user,headers),   // user loaded from loadUser()
															Object.class);
		
		Assert.assertEquals(HttpStatus.BAD_REQUEST.value(), replywithADMIN.getStatusCode().value()); 
		Assert.assertNotNull(replywithADMIN.getHeaders().getLocation());	
		
	}
	
	
	@Test
	@Order(15)   // authorization for Delete mapping
	public void whenInvalid_methodCall_shouldFail(){
		
		final ResponseEntity<Object > invalidUser= new TestRestTemplate().withBasicAuth("springer1", "secret")
												   .exchange(loadUri("/users"), HttpMethod.DELETE, new HttpEntity<>(user,headers), Object.class);
		
		Assert.assertEquals(HttpStatus.METHOD_NOT_ALLOWED.value(), invalidUser.getStatusCode().value());
		Assert.assertNotNull(invalidUser.getHeaders().getLocation());
		
	}
	
	
	
	

}
