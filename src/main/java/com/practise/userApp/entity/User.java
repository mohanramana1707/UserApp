package com.practise.userApp.entity;

import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinColumns;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="user_table")
@Data    // for getters and setters
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
//	@NotBlank
//	@Size(min=3,max = 50, message = "name is required with atleast 3 characters")  // default ,message if it fails
	@Embedded
	private Name name;   // separate class, but same table
	
	@PastOrPresent
	private LocalDate birthDate;
	@Email
	private String email;
	
	 // 1 user has 1 address
	// @OneToOne(cascade = CascadeType.ALL)
	//private Address address;    // separate class, different table
	
	
	 // 1 user has many addresses
//	@OneToMany(cascade = CascadeType.ALL,mappedBy = "user")
//	private List<Address>  address;  
	
	// many to many
	
	@ManyToMany(cascade = CascadeType.ALL ,fetch = FetchType.EAGER)
	@JoinTable(name="user_address",
		joinColumns = @JoinColumn(name="user_id",referencedColumnName = "id"),
		inverseJoinColumns = @JoinColumn(name="address_id",referencedColumnName = "id")	
			  )
	private List<Address>  address;  
	
	
	
	

}
