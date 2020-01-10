package com.griso.shop.repository;

import com.griso.shop.entities.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Optional;

public interface IUserRepo extends MongoRepository<User, String> {

    @Query("{'username': {$regex: ?0, $options: 'i'}}")
    Optional<User> findByUsername(String username);

}
