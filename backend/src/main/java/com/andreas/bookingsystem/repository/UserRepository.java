package com.andreas.bookingsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.andreas.bookingsystem.model.User;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

}
