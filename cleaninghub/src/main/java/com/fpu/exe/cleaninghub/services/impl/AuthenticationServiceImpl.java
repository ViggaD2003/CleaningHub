package com.fpu.exe.cleaninghub.services.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fpu.exe.cleaninghub.dto.request.SignInRequest;
import com.fpu.exe.cleaninghub.dto.request.SignUpRequest;
import com.fpu.exe.cleaninghub.dto.request.UserProfileDTO;
import com.fpu.exe.cleaninghub.dto.response.JwtAuthenticationResponse;
import com.fpu.exe.cleaninghub.email.EmailService;
import com.fpu.exe.cleaninghub.email.EmailTemplateName;
import com.fpu.exe.cleaninghub.entity.Role;
import com.fpu.exe.cleaninghub.entity.User;
import com.fpu.exe.cleaninghub.repository.MailTokenRepository;
import com.fpu.exe.cleaninghub.repository.RoleRepository;
import com.fpu.exe.cleaninghub.repository.TokenRepository;
import com.fpu.exe.cleaninghub.repository.UserRepository;
import com.fpu.exe.cleaninghub.services.interfc.AuthenticationService;
import com.fpu.exe.cleaninghub.services.interfc.JWTService;
import com.fpu.exe.cleaninghub.token.MailToken;
import com.fpu.exe.cleaninghub.token.Token;
import com.fpu.exe.cleaninghub.token.TokenType;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.List;

import java.io.IOException;
import java.util.Optional;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    @Autowired
    private final PasswordEncoder passwordEncoder;
    @Autowired
    private final AuthenticationManager authenticationManager;
    @Autowired
    private final JWTService jwtService;
    @Autowired
    private final RoleRepository roleRepo;
    @Autowired
    private final TokenRepository tokenRepository;
    @Autowired
    private EmailService emailService;
    @Value("${application.mailing.frontend.activation-url}")
    private String activationUrl;
    @Autowired
    private final MailTokenRepository mailTokenRepository;
    @Autowired
    private UserRepository userRepository;


    @Override
    public void signUp(SignUpRequest signUpRequest) throws MessagingException{
        User user = new User();
        user.setEmail(signUpRequest.getEmail());
        user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
        user.setFirstName(signUpRequest.getFirstName());
        user.setLastName(signUpRequest.getLastName());
        Role role = roleRepo.findById(signUpRequest.getRoleId()).orElseThrow(() -> new IllegalArgumentException("Role not found !"));
        user.setRole(role);
        user.setStatus(false);
        user.setAccountLocked(false);
        userRepository.save(user);
        sendValidationEmail(user);
    }

    private void saveUserToken(User user, String jwtToken, String jwtRefreshToken) {
        Token token = Token.builder()
                .user(user)
                .token(jwtToken)
                .refreshToken(jwtRefreshToken)
                .tokenType(TokenType.BEARER)
                .revoked(false)
                .expired(false)
                .build();
        tokenRepository.save(token);
    }

    private String extractTokenFromHeader(HttpServletRequest request) {
        String authHeader = request.getHeader(AUTHORIZATION);
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        return null;
    }

    private void revokeAllUserToken(User user) {
        var validUserToken = tokenRepository.findAllValidTokensByUser((long) user.getId());
        if (validUserToken.isEmpty()) return;
        validUserToken.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserToken);
    }

    @Override
    public JwtAuthenticationResponse signIn(SignInRequest signInRequest) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(signInRequest.getEmail(), signInRequest.getPassword()));
        var user = userRepository.findByEmail(signInRequest.getEmail()).orElseThrow(() -> new UsernameNotFoundException("Email not found !"));
        if (!user.getStatus()) {
            throw new IllegalArgumentException("User is not active !");
        }
        var jwt = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);

        revokeAllUserToken(user);
        saveUserToken(user, jwt, refreshToken);


        return JwtAuthenticationResponse.builder()
                .token(jwt)
                .refreshToken(refreshToken)
                .build();
    }

    @Override
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        final String authHeader = request.getHeader(AUTHORIZATION);
        final String refreshToken;
        final String email;

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            response.setStatus(UNAUTHORIZED.value());
            response.setContentType("text/plain");
            try {
                response.getWriter().write("No JWT token found in the request header");
            } catch (IOException ex) {
                throw new BadRequestException(ex.getMessage() + "ERROR");
            }
            return;
        }
        refreshToken = authHeader.substring(7);
        email = jwtService.extractUsername(refreshToken);
        final Token currentRefreshToken = tokenRepository.findByRefreshToken(refreshToken).orElse(null);

        if (email != null && currentRefreshToken != null) {
            var user = this.userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("Email is not found !"));
            if ((jwtService.isTokenValid(refreshToken, user)) &&
                    !currentRefreshToken.isRevoked() && !currentRefreshToken.isExpired()) {
                var accessToken = jwtService.generateToken(user);
                var newRefreshToken = jwtService.generateRefreshToken(user);

                revokeAllUserToken(user);
                saveUserToken(user, accessToken, newRefreshToken);
                
                var authResponse = JwtAuthenticationResponse.builder()
                        .token(accessToken)
                        .refreshToken(newRefreshToken);
                new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
                ResponseEntity.ok(authResponse);
            } else {
                response.setStatus(UNAUTHORIZED.value());
                response.setContentType("text/plain");
                try {
                    response.getWriter().write("JWT token has expired and revoked");
                } catch (IOException ex) {
                    throw new BadRequestException(ex.getMessage() + "ERROR");
                }
            }
        } else {
            response.setStatus(UNAUTHORIZED.value());
            response.setContentType("text/plain");
            try {
                response.getWriter().write("Unauthorized");
            } catch (IOException e) {
//            logger.error("Error writing unauthorized response", e);
                throw new BadRequestException("ERROR");
            }
            ResponseEntity.status(UNAUTHORIZED).body("Unauthorized");
        }
    }

    @Override
    public Object getUserInformation(HttpServletRequest request) {
        String token = extractTokenFromHeader(request);
        if (token == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("No JWT token found in the request header");
        }

        final Token accessToken = tokenRepository.findByToken(token).orElse(null);
        if (accessToken == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("No JWT token is valid !");
        }
        String email = jwtService.extractUsername(token);
        var user = userRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("Not found user !"));
        if (user == null || !jwtService.isTokenValid(token, user) || accessToken.isRevoked() || accessToken.isExpired()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("JWT token has expired and revoked");
        }
        return user;
    }

    @Override
    public UserProfileDTO updateUserInfo(Integer id, UserProfileDTO userProfileDTO) {
//        User user = userRepository.findById(id).orElse(null);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email;
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        email = userDetails.getUsername();
        User user = userRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("Not found user !"));

        if (user != null) {
            user.setId(userProfileDTO.getId());
            user.setLastName(userProfileDTO.getLastName());
            user.setFirstName(userProfileDTO.getFirstName());
            user.setPhoneNumber(userProfileDTO.getPhoneNumber());
            user.setAddress(userProfileDTO.getAddress());
            user.setDob(userProfileDTO.getDob());
            user.setGender(userProfileDTO.getGender());

            User userResponse = userRepository.save(user);
            return mapToUserProfileDto(userResponse);
        } else {
            return null;
        }
    }

    private UserProfileDTO mapToUserProfileDto(User user) {
        UserProfileDTO userProfileDto = new UserProfileDTO();
        userProfileDto.setId(user.getId());
        userProfileDto.setFirstName(user.getFirstName());
        userProfileDto.setLastName(user.getLastName());
        userProfileDto.setGender(user.getGender());
        userProfileDto.setAddress(user.getAddress());
        userProfileDto.setDob(user.getDob());
        userProfileDto.setPhoneNumber(user.getPhoneNumber());
        return userProfileDto;
    }

    @Override
    public JwtAuthenticationResponse getTokenAndRefreshToken(UserDetails userDetails) throws BadRequestException {
        JwtAuthenticationResponse res = new JwtAuthenticationResponse();
        if (userDetails != null) {
            String email = userDetails.getUsername();
            User user = userRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("not found"));
            if (user != null) {
                Token token = tokenRepository.findAllValidTokensByUser2((long) user.getId());
                res.setToken(token.getToken());
                res.setRefreshToken(token.getRefreshToken());
            } else {
                throw new BadRequestException("Exception");
            }
            return res;
        } else {
            throw new BadRequestException("Exception");
        }
    }

    @Override
    public JwtAuthenticationResponse signInGoogle() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        Optional<User> userOptional = userRepository.findByEmail(email);

        User user = userOptional.orElseThrow(() -> new IllegalArgumentException("not found"));
        List<Token> listToken = tokenRepository.findAllValidTokensByUser((long) user.getId());
        Token token = new Token();
        if(listToken.size() == 1){
            token = listToken.get(0);
        }

        return JwtAuthenticationResponse.builder()
                .token(token.getToken())
                .refreshToken(token.getRefreshToken())
                .build();
    }

    @Override
    public void activateAccount(String token) throws MessagingException {
        MailToken mailToken = mailTokenRepository.findByToken(token)
        .orElseThrow(() -> new RuntimeException("Invalid token"));

        if(LocalDateTime.now().isAfter(mailToken.getExpiresAt())){
            sendValidationEmail(mailToken.getUser());
            throw new RuntimeException("Activation token has expired. A new token has been send to the same email address");
        }

        var user =  userRepository.findById(mailToken.getUser().getId())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        user.setStatus(true);
        user.setAccountLocked(true);
        userRepository.save(user);

        mailToken.setValidatedAt(LocalDateTime.now());
        mailTokenRepository.save(mailToken);
    }

    @Override
    public void updateAvatar(String urlImg) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String email = userDetails.getUsername();
        User user = userRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("not found"));
        if(user != null){
            urlImg = urlImg.replace("\"", "").trim();
            user.setImg(urlImg);
            userRepository.save(user);
        }
    }


    private String generateAndSaveActivationToken(User user) {
        // Generate a token
        String generatedToken = generateActivationCode(6);
        var token = MailToken.builder()
                .token(generatedToken)
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusMinutes(15))
                .user(user)
                .build();
        mailTokenRepository.save(token);

        return generatedToken;
    }

    private void sendValidationEmail(User user) throws MessagingException {
        var newToken = generateAndSaveActivationToken(user);

        emailService.sendEmail(
                user.getEmail(),
                user.getFullName(),
                EmailTemplateName.ACTIVATE_ACCOUNT,
                activationUrl,
                newToken,
                "Account activation"
        );
    }

    private String generateActivationCode(int length) {
        String characters = "0123456789";
        StringBuilder codeBuilder = new StringBuilder();

        SecureRandom secureRandom = new SecureRandom();

        for (int i = 0; i < length; i++) {
            int randomIndex = secureRandom.nextInt(characters.length());
            codeBuilder.append(characters.charAt(randomIndex));
        }

        return codeBuilder.toString();
    }
}
