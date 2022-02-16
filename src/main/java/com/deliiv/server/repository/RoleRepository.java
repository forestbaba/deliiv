package com.deliiv.server.repository;

import com.deliiv.server.model.Role;
import com.deliiv.server.model.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(RoleName name);
}
