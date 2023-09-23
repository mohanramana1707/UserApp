package com.practise.userApp.entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="address_table")
@Data    // for getters and setters
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class Address {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	
	private String line1;
	private String line2;
	private String line3;
	private String line4;
	private String postCode;
	
	
	// to avoid creation of another table for mapping btw(User & Address)
//	@ManyToOne
//	@JsonIgnore    // to avoid 
//	private User user;
	
	
	@ManyToMany(cascade = {CascadeType.MERGE,CascadeType.PERSIST},
				fetch = FetchType.EAGER,
				mappedBy = "address")  // mapped by refers to the "address" field in USER Class
	@JsonIgnore
	private List<User>  user;
	
	
	
	

}
