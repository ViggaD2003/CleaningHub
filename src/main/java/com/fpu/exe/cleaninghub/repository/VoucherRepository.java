package com.fpu.exe.cleaninghub.repository;

import com.fpu.exe.cleaninghub.entity.Voucher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VoucherRepository extends JpaRepository<Voucher, Integer> {
}
