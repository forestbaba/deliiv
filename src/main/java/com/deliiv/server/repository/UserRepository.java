package com.deliiv.server.repository;

import com.deliiv.server.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findUserByEmail(String e);
    User findUserByPhoneNumber(String e);
}
