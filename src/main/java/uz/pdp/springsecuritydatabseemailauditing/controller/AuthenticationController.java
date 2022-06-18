package uz.pdp.springsecuritydatabseemailauditing.controller;

import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.pdp.springsecuritydatabseemailauditing.payload.LoginDto;
import uz.pdp.springsecuritydatabseemailauditing.payload.Message;
import uz.pdp.springsecuritydatabseemailauditing.payload.RegisterDto;
import uz.pdp.springsecuritydatabseemailauditing.service.AuthenticationService;

@RestController
@RequestMapping(value = "/authentication")
public class AuthenticationController {
    final AuthenticationService authenticationService;

    public AuthenticationController(@Lazy AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/register")
    public ResponseEntity<Message> register(@RequestBody RegisterDto registerDto) {
        return authenticationService.register(registerDto);
    }

    @GetMapping("/confirmEmail")
    public ResponseEntity<Message> confirmEmail(@RequestParam String emailCode, @RequestParam String sendingEmail) {
        return authenticationService.confirmEmail(emailCode, sendingEmail);
    }

    @PostMapping("/login")
    public ResponseEntity<Message> login(@RequestBody LoginDto loginDto) {
        return authenticationService.login(loginDto);
    }
}
