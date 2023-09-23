package com.practise.userApp.entity;

import java.time.LocalDate;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data    // for getters and setters
@Builder
@NoArgsConstructor
@AllArgsConstructor

@Embeddable
public class Name {
	
	private String firstName;
	private String secondName;
	

}
