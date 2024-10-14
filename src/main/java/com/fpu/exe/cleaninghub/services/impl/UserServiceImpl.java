package com.fpu.exe.cleaninghub.services.impl;

import com.fpu.exe.cleaninghub.dto.response.UserResponseDTO;
import com.fpu.exe.cleaninghub.entity.User;
import com.fpu.exe.cleaninghub.repository.TokenRepository;
import com.fpu.exe.cleaninghub.repository.UserRepository;
import com.fpu.exe.cleaninghub.services.interfc.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private TokenRepository tokenRepository;

    @Autowired
    private UserRepository userRepository;




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

    @Override
    public UserResponseDTO getUserByEmail(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("Not found email !"));
        return mapToDto(user);
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
