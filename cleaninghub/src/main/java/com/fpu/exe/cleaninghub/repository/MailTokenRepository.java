package com.fpu.exe.cleaninghub.repository;

import com.fpu.exe.cleaninghub.token.MailToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MailTokenRepository extends JpaRepository<MailToken, Integer> {

    Optional<MailToken> findByToken(String token);
}
