package com.practise.userApp.controller;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.practise.userApp.entity.Address;
import com.practise.userApp.entity.Name;
import com.practise.userApp.entity.User;


import jakarta.annotation.PostConstruct;
import jakarta.validation.Valid;


@RestController
@RequestMapping
public class UserController {
	
	@Autowired
	com.practise.userApp.repository.UserRepo userRepo;
	
	//The PostConstruct annotation is used on a method that needs to be executed after dependency injection is done to performany initialization
	@PostConstruct  
	public void loadData()
	{
	// to add 500 random values to the DB
		
	  List<LocalDate> randomDates = LocalDate.of(1990, 1, 1).datesUntil(LocalDate.now()).limit(501).collect(Collectors.toList());
	  Collections.shuffle(randomDates);
	  
	  final Random random= new Random();
	  
	  List<User> users= IntStream.range(1, 501)
			  			.mapToObj(i-> 
			  			
			  			{
			  			final var name=	Name.builder()
			  							  .firstName( "firstName"+ (random.nextInt()%2 ==0?1:3))
			  							  .secondName("firstName"+ (random.nextInt()%2 ==0?1:3))
			  							  .build();
			  			
			  			//List of addressess
			  			final var address1=	Address.builder()
			  								.line1("address1" +(random.nextInt()%7 ==0?2:4) )
			  								.line2("address2" +(random.nextInt()%7 ==0?2:4) )
			  								.line3("address3" +(random.nextInt()%7 ==0?2:4) )
			  								.line4("address4" +(random.nextInt()%7 ==0?2:4) )
			  								.postCode("postcode" +(random.nextInt()%7 ==0?2:4) )
			  								.build();
			  			
			  			final var user1= User.builder()
				  				.name(name) 					//.name("name"+ (random.nextInt()%2 ==0?1:3))
				  				.birthDate(randomDates.get(i))
				  				.email("test"+(random.nextInt()%3 ==0?5:7)+"@gmail.com")
				  				.address(List.of(address1) ) 		// only 1 address
				  				.build();
				  				
			  			
			  			final var address2=	Address.builder()
  								.line1("address1" +(random.nextInt()%7 ==0?2:4) )
  								.line2("address2" +(random.nextInt()%7 ==0?2:4) )
  								.line3("address3" +(random.nextInt()%7 ==0?2:4) )
  								.line4("address4" +(random.nextInt()%7 ==0?2:4) )
  								.postCode("postcode" +(random.nextInt()%7 ==0?2:4) )
  								.build();
			  			address2.setUser(List.of(user1));
			  					 
	  							  
	  							  
			  				
				  			final var user= User.builder()
				  				.name(name) //.name("name"+ (random.nextInt()%2 ==0?1:3))
				  				.birthDate(randomDates.get(i))
				  				.email("test"+(random.nextInt()%3 ==0?5:7)+"@gmail.com")
				  				.address(List.of(address1,address2) ) 				// has 2 address
				  				.build();
				  			
				  			//address1.setUser(user);
				  			//address2.setUser(user);
				  			
				  			return user;
			  			}
			  
			  ).collect(Collectors.toList());
	  
	  userRepo.saveAll(users);
	
	}
	//=======================================ADD NEW USER=================================================
	
	@PostMapping("/users")
	public ResponseEntity<User> addUser(@Valid @RequestBody User user) {
		final var userDb = userRepo.save(user);
		
		final var location=ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(userDb.getId()).toUri();
		return ResponseEntity.created(location).build();  //201 response
	}
	
	//=======================================DELETE NEW USER=================================================
	
	@DeleteMapping("/users/{id}")  // path variable
	public ResponseEntity<User> deleteUser(@PathVariable String id) {
		final var userDb = userRepo.findById(Integer.parseInt(id))
						   		   .orElseThrow( 
						   		    ()-> new RuntimeException(String.format("User with ID as %s not found", id))
						   			 );
		userRepo.delete(userDb);
		
		return ResponseEntity.noContent().build();  //204
	}
	
	//=======================================UPDATE CURRENT USER=================================================
	
	@PutMapping("/users/{id}")  // path variable
	public ResponseEntity<User> updateUser(@PathVariable String id , @Valid  @RequestBody User user) {
		final var userDb = userRepo.findById(Integer.parseInt(id))
						   		   .orElseThrow( 
						   		    ()-> new RuntimeException(String.format("User with ID as %s not found", id))
						   			 );
		user.setId(userDb.getId());  // getting the USER by gn ID - replace the USER with the new Request BODY
		userRepo.save(user);
		
		return ResponseEntity.accepted().build();  // accepted 202 
	}
	
	//=======================================GET list of ALL USER=================================================
	
	@GetMapping("/users")  
	public ResponseEntity<List<User>> retriveAllUser() {
		
		return ResponseEntity.ok(userRepo.findAll());
	}
	
	//=========================================  GET FOR pagination and SOrting=================================================
	
	@GetMapping("/v1/users")  
	public ResponseEntity<Page<User>> retriveAllUserWithSearch(@RequestParam(required = false,defaultValue = "0") int pageNo,
																@RequestParam(required = false,defaultValue = "10") int pageSize,      // 10 items in a page
																@RequestParam(required = false,defaultValue = "id#desc") String[] sortAndOrder,  // DESC order
																@RequestParam(required = false,defaultValue = "") String searchCriteria)  // search in the DB(500 values)for that string and get the specific results 
	{
		
		final List<org.springframework.data.domain.Sort.Order> orders=Arrays.stream(sortAndOrder)
										.filter(s->s.contains("#"))
										.map(s->s.split("#"))
										.map( arr-> new Order(Direction.fromString(arr[1]),arr[0]) )
										.collect(Collectors.toList());
		
		Pageable paging = PageRequest.of(pageNo, pageSize,Sort.by(orders));
		
		
		return ResponseEntity.ok( searchCriteria.isBlank()?
								userRepo.findAll(paging): userRepo.findByKeyword(paging, searchCriteria) );
	}
	
	//=======================================GET USER by ID=================================================
	
	@PreAuthorize("hasRole('ADMIN')")    // only admin can access this method- method lvl Security
	@GetMapping("/users/{id}")    // path variable
	public ResponseEntity<EntityModel<User>> retriveUserById(@PathVariable String id ) {
		
		final var userDb = userRepo.findById(Integer.parseInt(id))
									.orElseThrow( 
											()-> new RuntimeException(String.format("User with ID as %s not found", id))
										);
		
		// building the link for retriveAllUser using SPRING HATEOUS and pass it to the header
		//A simple EntityModel wrapping a domain object and adding links to it
		
		//EntityModel & Link
		
		final EntityModel<User> model=EntityModel.of(userDb);
		
		final WebMvcLinkBuilder newLink= WebMvcLinkBuilder.linkTo(
				      WebMvcLinkBuilder.methodOn(this.getClass()).retriveAllUser()   //The methodOn() obtains the method mapping by making dummy invocation of the target method on the proxy controller
				      																//and sets the customerId as the path variable of the URI.
					);
		Link link=newLink.withRel("Additional User info link");
		
		model.add(link);
		
		
		
		return ResponseEntity.ok(model);
	}

}
