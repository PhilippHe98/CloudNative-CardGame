package de.hskl.cloudnative.security;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<AuthUser, String> {

    Optional<AuthUser> findByEmail(String email);
}