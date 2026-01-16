package com.login.app.Repository;

import com.login.app.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import com.login.app.Entity.User;

@Repository
public interface userRepository extends JpaRepository<User, Long> {
    // Spring crea el SQL: "SELECT * FROM users WHERE username = ?" automáticamente
    Optional<User> findByUsername(String username);
    Optional<User> findByToken(String token);
}
