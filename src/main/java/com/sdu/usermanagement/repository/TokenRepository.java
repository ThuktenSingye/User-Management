package com.sdu.usermanagement.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.sdu.usermanagement.model.Token;
public interface TokenRepository extends JpaRepository<Token, Integer> {

    @Query(value = "SELECT * FROM token  " +
            "INNER JOIN user ON token.user_id = user.User_Id " +
            "WHERE token.user_id = :userId AND (token.Expired = false OR token.Revoked = false)", nativeQuery = true)
    List<Token> findAllValidTokenByUserId(@Param("userId") Integer userId);

    Optional<Token> findByToken(String token);
}
