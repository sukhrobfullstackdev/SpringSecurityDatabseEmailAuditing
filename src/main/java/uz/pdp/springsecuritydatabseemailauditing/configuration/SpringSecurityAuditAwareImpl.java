package uz.pdp.springsecuritydatabseemailauditing.configuration;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import uz.pdp.springsecuritydatabseemailauditing.entity.User;

import java.util.Optional;
import java.util.UUID;

public class SpringSecurityAuditAwareImpl implements AuditorAware<UUID> {
    @Override
    public Optional<UUID> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && !authentication.getPrincipal().equals("anonymousUser")) { // authentication.isAuthenticated()- authenticate bo'ganmi shuni tekshirvoti
            User user = (User) authentication.getPrincipal();
            return Optional.of(user.getId());
        } else {
            return Optional.empty();
        }
    }
}
