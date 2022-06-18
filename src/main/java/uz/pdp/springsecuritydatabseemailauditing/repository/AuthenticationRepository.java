package uz.pdp.springsecuritydatabseemailauditing.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.pdp.springsecuritydatabseemailauditing.entity.User;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AuthenticationRepository extends JpaRepository<User, UUID> {
    boolean existsByEmail(String email);
    Optional<User> findByEmailAndEmailCode(String email, String emailCode);
    Optional<User> findByEmail(String email);
    // Optional<User> findByEmailAndEmailCodeAndEnabled(String email, String emailCode, boolean enabled);
}
