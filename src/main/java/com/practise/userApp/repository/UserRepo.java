package com.practise.userApp.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.practise.userApp.entity.User;

@Repository
public interface UserRepo extends JpaRepository<User, Integer> {
	
	@Query(value= "select s.* from user_table s "
				+ "where s.name LIKE %:keyword% or "
				+ "s.address LIKE %:keyword% or "
				+ "s.email LIKE %:keyword%",
				
			nativeQuery=true)   //nativeQuery -> used to write querry in SQL rather than jQuery
	Page<User> findByKeyword(Pageable pageble,@Param ("keyword") String keyword);

}
