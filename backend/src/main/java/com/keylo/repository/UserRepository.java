package com.keylo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.List;
import com.keylo.model.User;
//! Requirements: 
//define the userrepo, give it the tools of jpa repo
//for the User entitiy and their primary key is longg, saying look in this table/entity in params
// make a custom method: existsByEmail()
//return true or false (boolean) if it exists or not
// takes params email
//ex : boolean UserRepository.existsByEmail(blair@gmail.com)

public interface UserRepository extends JpaRepository<User, Long>{
    /*return type*/ boolean existsByEmail(String email); //*validation */

    Optional<User> findByEmail(String email); //find by email (optional return type in case its null to avoid error)


}



