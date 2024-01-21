package com.sdu.usermanagement.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.sdu.usermanagement.model.Token;
public interface TokenRepository extends JpaRepository<Token, Integer> {

    Optional<Token> findByToken(String token);
}
