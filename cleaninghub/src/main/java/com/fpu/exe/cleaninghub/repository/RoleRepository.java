package com.fpu.exe.cleaninghub.repository;

import com.fpu.exe.cleaninghub.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {

}
