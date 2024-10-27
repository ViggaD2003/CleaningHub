package com.fpu.exe.cleaninghub.repository;

import com.fpu.exe.cleaninghub.entity.Payments;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<Payments, Integer> {

}
