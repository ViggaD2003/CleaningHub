package com.fpu.exe.cleaninghub.services.impl;

import com.fpu.exe.cleaninghub.dto.request.LocationRequest;
import com.fpu.exe.cleaninghub.dto.response.UserResponseDTO;
import com.fpu.exe.cleaninghub.entity.Role;
import com.fpu.exe.cleaninghub.entity.User;
import com.fpu.exe.cleaninghub.repository.RoleRepository;
import com.fpu.exe.cleaninghub.repository.TokenRepository;
import com.fpu.exe.cleaninghub.repository.UserRepository;
import com.fpu.exe.cleaninghub.services.interfc.JWTService;
import com.fpu.exe.cleaninghub.services.interfc.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    @Autowired
    private TokenRepository tokenRepository;
    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    private final JWTService jwtService;

    @Override
    public UserDetailsService userDetailsService() {
        return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
                return userRepository.findByEmail(username)
                        .orElseThrow(() -> new UsernameNotFoundException("Email not found"));
            }
        };
    }

    private User getCurrentUser(HttpServletRequest request) {
        String token = extractTokenFromHeader(request);
        if (token == null) {
            throw new IllegalArgumentException("No JWT token found in the request header");
        }
        final var accessToken = tokenRepository.findByToken(token).orElse(null);
        if (accessToken == null) {
            throw new IllegalArgumentException("No JWT token is valid!");
        }
        String email = jwtService.extractUsername(token);
        var user = userRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("Not Found User"));
        if (!jwtService.isTokenValid(token, user) || accessToken.isRevoked() || accessToken.isExpired()) {
            throw new IllegalArgumentException("JWT token has expired or been revoked");
        }
        return user;
    }

    private String extractTokenFromHeader(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7); // Remove "Bearer " prefix
        }
        return null;
    }

    @Override
    public UserResponseDTO getUserByEmail(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("Not found email !"));
        return mapToDto(user);
    }

    @Override
    public String updateLocationOfStaff(HttpServletRequest request, LocationRequest locationRequest) {
        User user = getCurrentUser(request);

        if(user != null) {
            user.setLatitude(locationRequest.getLatitude());
            user.setLongitude(locationRequest.getLongitude());
            userRepository.save(user);
            return "update success !";
        } else {
            return "something went wrong !";
        }
    }

    @Override
    public Page<UserResponseDTO> getAllUsers(String searchTerm, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<User> users = userRepository.searchUsers(searchTerm, pageable);
        return users.map(this::mapToDto);
    }

    @Override
    public User updateUserRoleAndStatus(Integer userId, Integer roleId, Boolean status) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            throw new RuntimeException("User not found with id: " + userId);
        }

        User user = userOptional.get();
        Optional<Role> roleOptional = roleRepository.findById(roleId);
        if (roleOptional.isEmpty()) {
            throw new RuntimeException("Role not found with id: " + roleId);
        }
        Role role = roleOptional.get();
        user.setRole(role);
        user.setStatus(status);
        return userRepository.save(user);
    }

    private UserResponseDTO mapToDto(User user){
        return UserResponseDTO.builder()
                .id(user.getId())
                .lastName(user.getLastName())
                .firstName(user.getFirstName())
                .gender(user.getGender())
                .status(user.getStatus())
                .phoneNumber(user.getPhoneNumber())
                .address(user.getAddress())
                .dob(user.getDob())
                .email(user.getEmail())
                .build();
    }

}
