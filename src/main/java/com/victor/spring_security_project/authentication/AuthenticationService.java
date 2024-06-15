package com.victor.spring_security_project.authentication;

import com.victor.spring_security_project.email.EmailService;
import com.victor.spring_security_project.email.EmailTemplateName;
import com.victor.spring_security_project.exception.EmailAlreadyExistsException;
import com.victor.spring_security_project.jwt.JwtService;
import com.victor.spring_security_project.role.RoleRepository;
import com.victor.spring_security_project.token.Token;
import com.victor.spring_security_project.token.TokenRepository;
import com.victor.spring_security_project.user.User;
import com.victor.spring_security_project.user.UserRepository;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDateTime;


@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final EmailService emailService;
    private final TokenRepository tokenRepository;

    public void registerUser(RegisterRequest registerRequest) throws MessagingException {

        var userRole = roleRepository.findByName("USER")
                // TODO: better exception handling
                .orElseThrow(() -> new IllegalStateException("ROLE USER was not initialized"));

        User user = User.builder()
                .firstName(registerRequest.firstname())
                .lastName(registerRequest.lastname())
                .email(registerRequest.email())
                .password(passwordEncoder.encode(registerRequest.password()))
                .enabled(false)
                .role(userRole)
                .build();


        userRepository.save(user);

        sendValidationEmail(user);
    }

    private void sendValidationEmail(User user) throws MessagingException {
        var newToken = generateAndSaveToken(user);
        emailService.sendEmail(
                user.getEmail(),
                user.getFullName(),
                EmailTemplateName.ACTIVATE_ACCOUNT,
                newToken,
                "Account Activation"
        );
    }

    private String generateAndSaveToken(User user) {
        String generatedToken = generateActivationToken(6);

        var token = Token.builder()
                .token(generatedToken)
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusMinutes(10))
                .user(user)
                .build();

        tokenRepository.save(token);

        return generatedToken;
    }

    private String generateActivationToken(int length) {
        String characters = "0123456789";
        StringBuilder tokenBuilder = new StringBuilder();
        SecureRandom secureRandom = new SecureRandom();
        for (int i = 0; i < length; i++) {
            int randomIndex = secureRandom.nextInt(characters.length());
            tokenBuilder.append(characters.charAt(randomIndex));
        }
        return tokenBuilder.toString();
    }

    public void registerAdmin(RegisterRequest registerRequest) {

        if (userRepository.findByEmail(registerRequest.email()).isPresent()) {
            throw new EmailAlreadyExistsException("Email already exists");
        }

        var adminRole = roleRepository.findByName("ADMIN")
                // TODO: better exception handling
                .orElseThrow(() -> new IllegalStateException("ROLE USER was not initialized"));


        User user = User.builder()
                .firstName(registerRequest.firstname())
                .lastName(registerRequest.lastname())
                .email(registerRequest.email())
                .password(passwordEncoder.encode(registerRequest.password()))
                .enabled(true)
                .role(adminRole)
                .build();

        // catch email already exists error and throw DataIntegrityViolationException
        // then add this exception to the GlobalExceptionHandler

//        try {
            userRepository.save(user);
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
    }

    @Transactional
    public void activateAccount(String token) throws MessagingException {
        var savedToken = tokenRepository.findByToken(token)
                .orElseThrow(()-> new RuntimeException("Invalid Token"));

        if (LocalDateTime.now().isAfter(savedToken.getExpiresAt())) {
            sendValidationEmail(savedToken.getUser());
            throw new
                    RuntimeException(
                            "Activation token has expired. A new token has been sent "
                                    + "to the same email address"
            );
        }

        var user = userRepository.findById(savedToken.getUser().getId())
                .orElseThrow(()-> new UsernameNotFoundException("User not found"));

        user.setEnabled(true);
        userRepository.save(user);

        savedToken.setValidatedAt(LocalDateTime.now());
        tokenRepository.save(savedToken);
    }

    public String login(LoginRequest loginRequest) {
        log.info("Login service method hit");

        log.info("After userRepository Starting Authentication through API endpoint");
        Authentication authenticationRequest = UsernamePasswordAuthenticationToken
                .unauthenticated(loginRequest.username(), loginRequest.password());

        log.info("Hit unauthenticated");

        try {
            var auth = authenticationManager
                    .authenticate(authenticationRequest);

            log.info("---------------------------------------");
            log.info(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
            log.info(SecurityContextHolder.getContext().getAuthentication().getAuthorities().toString());

            log.info("Authentication successful for user: {}", authenticationRequest.getName());

            var user = (User) auth.getPrincipal();

            return jwtService.generateToken(user);
        } catch (BadCredentialsException e) {
            log.error("Invalid username or password for user: {}", loginRequest.username());
            return "Invalid username or password";
        } catch (AuthenticationException e) {
            log.error("Authentication failed for user: {}. Reason: {}", loginRequest.username(), e.getMessage());
            return "Authentication failed";
        }
    }
}
