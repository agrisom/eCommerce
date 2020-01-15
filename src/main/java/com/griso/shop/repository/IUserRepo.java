package com.griso.shop.repository;

import com.griso.shop.entities.UserDB;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface IUserRepo extends MongoRepository<UserDB, String> {

    Optional<UserDB> findByUsername(String username);

}
