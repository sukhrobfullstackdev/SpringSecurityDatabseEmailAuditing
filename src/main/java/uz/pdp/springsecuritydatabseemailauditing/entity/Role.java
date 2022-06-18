package uz.pdp.springsecuritydatabseemailauditing.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import uz.pdp.springsecuritydatabseemailauditing.entity.enums.RoleName;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Role implements GrantedAuthority {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Enumerated(EnumType.STRING) // Enum lar default holatda database raqam bo'lib qoladi, shunig uchun manga String qivoldim.
    private RoleName roleName;

    @Override
    public String getAuthority() {
        return roleName.name(); // roleName - o'zini bersak Enum shklda bovoti , shuning uchun ichidagi String valueni ovoldim.
    }
}
