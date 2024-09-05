package com.fpu.exe.cleaninghub.repository;


import com.fpu.exe.cleaninghub.token.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {
    @Query("SELECT t from Token t inner join User u on t.user.id=u.id " +
            "where u.id= :userId and (t.expired =false or t.revoked =false)")
    List<Token> findAllValidTokensByUser(Long userId);

    Optional<Token> findByToken(String token);

    Optional<Token> findByRefreshToken(String refreshToken);
    @Query("SELECT t from Token t inner join User u on t.user.id=u.id " +
            "where u.id= :userId and (t.expired =false or t.revoked =false)")
    Token findAllValidTokensByUser2(Long userId);


//    @Query("SELECT t FROM Token t WHERE t.userId = :userId and t.token = false and t.refreshToken = fa")
//    Token findByUserId(Integer userId);
}
