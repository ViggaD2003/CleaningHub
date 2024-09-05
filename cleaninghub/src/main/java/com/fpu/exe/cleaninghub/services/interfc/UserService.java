package com.fpu.exe.cleaninghub.services.interfc;

import com.fpu.exe.cleaninghub.dto.response.UserResponseDTO;
import com.fpu.exe.cleaninghub.entity.User;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import javassist.NotFoundException;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.Optional;

public interface UserService {
    UserDetailsService userDetailsService();
    UserResponseDTO getUserByEmail(String email);

//    UserResponseDTO updateUserInfo(Integer id, UserDTO userDTO);

//    void changePassword(ChangePasswordRequest request, Principal connectedUser);


//    User verifyUserAccount(String email, HttpServletRequest request) throws MessagingException, UnsupportedEncodingException;
//    boolean verifyPassword(String code);
//    void sendVerificationCodeEmail(User user, String siteUrl) throws MessagingException, UnsupportedEncodingException;
//    Optional<User> findByEmail(String email);
//
//    Boolean changeForgotPassword(String newPassword, String confirmPassword, String username) throws NotFoundException;
}
