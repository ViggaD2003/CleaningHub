package com.fpu.exe.cleaninghub.repository;

import com.fpu.exe.cleaninghub.entity.Voucher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
@Repository
public interface VoucherRepository extends JpaRepository<Voucher, Integer> {
    @Query("SELECT v FROM Voucher v WHERE v.amount > 0")
    List<Voucher> findAllByQuantityMoreThanZero();
}
