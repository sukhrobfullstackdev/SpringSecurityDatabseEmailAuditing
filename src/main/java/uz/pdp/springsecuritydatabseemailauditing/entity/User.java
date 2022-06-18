package uz.pdp.springsecuritydatabseemailauditing.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "users")
public class User implements UserDetails { // asosiy user va sistemaga kirish uchun principal bo'lishi uchun UserDetails dan implement qildim.
    @Id
    @GeneratedValue
    private UUID id; // bu userning takrorlanmas qismi
    @Column(nullable = false) // length bu database da eng ko'pi bilan 15 ta bosin divoman length ini
    private String firstName;
    @Column(nullable = false)
    private String lastName;
    @Column(nullable = false, unique = true)
    private String email;
    @Column(nullable = false)
    private String password;
    @CreationTimestamp // save amali bajarilganda osha vaqtini berib yuboradi.
    @Column(nullable = false, updatable = false)
    // updatable = false - bu degani client o'zi hohlagan date ni kirgaza olmasin
    private Timestamp createdAt;
    @UpdateTimestamp // bu edit bogan vaqtni berib yyuboradi.
    @Column(nullable = false, updatable = false)
    private Timestamp updatedAt;
    private String emailCode;
    // Bu UserDetails ning abstract methods  bularni @Override qilish kere
    @ManyToMany//(fetch = FetchType.EAGER) //EAGER ni qo'ysam avtomat role ni ham olib keladi.
    private Set<Role> roles;
    private boolean isAccountNonExpired = true;
    private boolean isAccountNonLocked = true;
    private boolean isCredentialsNonExpired = true;
    private boolean isEnabled; // buni default false qildim chunki emaildai kodni tasdiqlasa keyin true qilaman.

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles; // Role da GrantedAuthority dan implement qilip uni methodini @Override qilip shu @Override methodda RoleName enumni name() ini ya'ni valuesini bervordik.
    }

    @Override
    public String getUsername() {
        return this.email; // Username sifatida User ning email i ketadi.
    }

    @Override
    public boolean isAccountNonExpired() {
        return this.isAccountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.isAccountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return this.isCredentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return this.isEnabled;
    }
}




