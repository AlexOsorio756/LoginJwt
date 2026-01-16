package com.login.app.Repository;

import com.login.app.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface userRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByToken(String token);
    Optional<User> findByValidationToken(String validationToken);
    Optional<User> findByEmail(String email);
}
