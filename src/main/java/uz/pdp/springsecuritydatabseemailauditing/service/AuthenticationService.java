package uz.pdp.springsecuritydatabseemailauditing.service;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import uz.pdp.springsecuritydatabseemailauditing.entity.User;
import uz.pdp.springsecuritydatabseemailauditing.entity.enums.RoleName;
import uz.pdp.springsecuritydatabseemailauditing.payload.LoginDto;
import uz.pdp.springsecuritydatabseemailauditing.payload.Message;
import uz.pdp.springsecuritydatabseemailauditing.payload.RegisterDto;
import uz.pdp.springsecuritydatabseemailauditing.repository.AuthenticationRepository;
import uz.pdp.springsecuritydatabseemailauditing.repository.RoleRepository;
import uz.pdp.springsecuritydatabseemailauditing.security.JWTProvider;

import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

@Service
public class AuthenticationService implements UserDetailsService {
    final AuthenticationRepository authenticationRepository;
    final PasswordEncoder passwordEncoder;
    final RoleRepository roleRepository;
    final JavaMailSender javaMailSender;
    final AuthenticationManager authenticationManager;
    final JWTProvider jwtProvider;

    public AuthenticationService(JWTProvider jwtProvider, AuthenticationManager authenticationManager, JavaMailSender javaMailSender, RoleRepository roleRepository, AuthenticationRepository authenticationRepository, PasswordEncoder passwordEncoder) {
        this.authenticationRepository = authenticationRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
        this.javaMailSender = javaMailSender;
        this.authenticationManager = authenticationManager;
        this.jwtProvider = jwtProvider;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> optionalUser = authenticationRepository.findByEmail(username);
        if (optionalUser.isPresent()) {
            return optionalUser.get();
        } else {
            throw new UsernameNotFoundException(username + " is not found!");
        }
    }

    public ResponseEntity<Message> register(RegisterDto registerDto) {
        if (!authenticationRepository.existsByEmail(registerDto.getEmail())) {
            User user = new User();
            user.setFirstName(registerDto.getFirstName());
            user.setLastName(registerDto.getLastName());
            user.setEmail(registerDto.getEmail());
            user.setPassword(passwordEncoder.encode(registerDto.getPassword()));
            user.setRoles(Collections.singleton(roleRepository.findByRoleName(RoleName.ROLE_USER)));
            user.setEmailCode(UUID.randomUUID().toString());
            authenticationRepository.save(user);
            Boolean email = sendEmail(registerDto.getEmail(), user.getEmailCode());
            if (email) {
                return ResponseEntity.status(HttpStatus.CREATED).body(new Message(true, "The user is successfully registered, please check your email to activate your account!"));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(new Message(false, "An error occurred while sending the message to your email, please make sure the information you entered is correct, and try one more time!"));
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(new Message(false, "The email is already in use!"));
        }
    }

    public Boolean sendEmail(String sendingEmail, String emailCode) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("sukhrobdev83@gmail.com");
            message.setTo(sendingEmail);
            message.setSubject("Email account confirmation message!");
            message.setText("<a href='http://localhost:8080/authentication/confirmEmail?emailCode=" + emailCode + "&sendingEmail=" + sendingEmail + "'>Confirm your email!</a>");
            javaMailSender.send(message);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public ResponseEntity<Message> confirmEmail(String emailCode, String sendingEmail) {
        Optional<User> optionalUser = authenticationRepository.findByEmailAndEmailCode(sendingEmail, emailCode);
        //Optional<User> isOptionalUserRegistered = authenticationRepository.findByEmailAndEmailCodeAndEnabled(sendingEmail, null, true);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setEnabled(true);
            user.setEmailCode(null);
            authenticationRepository.save(user);
            return ResponseEntity.status(HttpStatus.OK).body(new Message(true, "Your account is confirmed!"));
        }
//        else if (isOptionalUserRegistered.isPresent()) {
//            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(new Message(false, "Your account is already confirmed in our system, please login!"));
//        }
        else {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(new Message(false, "Your account was not confirmed!"));
        }
    }

    public ResponseEntity<Message> login(LoginDto loginDto) {
        try {
            Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword()));
            User user = (User) authenticate.getPrincipal();
            String token = jwtProvider.generateToken(loginDto.getUsername(), user.getRoles());
            return ResponseEntity.status(HttpStatus.OK).body(new Message(true,"You have successfully logged in!",token));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(new Message(false,"Password or login is incorrect!"));
        }
    }
}
