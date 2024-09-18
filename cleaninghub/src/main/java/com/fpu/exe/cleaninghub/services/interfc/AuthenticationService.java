package com.fpu.exe.cleaninghub.services.interfc;

import com.fpu.exe.cleaninghub.dto.request.ChangePasswordRequest;
import com.fpu.exe.cleaninghub.dto.request.SignInRequest;
import com.fpu.exe.cleaninghub.dto.request.SignUpRequest;
import com.fpu.exe.cleaninghub.dto.request.UserProfileDTO;
import com.fpu.exe.cleaninghub.dto.response.JwtAuthenticationResponse;
import com.fpu.exe.cleaninghub.entity.User;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import javassist.NotFoundException;
import org.apache.coyote.BadRequestException;
import org.springframework.security.core.userdetails.UserDetails;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.Principal;

public interface AuthenticationService {
    void signUp(SignUpRequest signUpRequest) throws MessagingException;

    JwtAuthenticationResponse signIn(SignInRequest signInRequest);

    //    JwtAuthenticationResponse refreshToken(RefreshTokenRequest refreshTokenRequest);
    void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException;

    Object getUserInformation(HttpServletRequest request);

    UserProfileDTO updateUserInfo(Integer id, UserProfileDTO userProfileDTO);

   JwtAuthenticationResponse getTokenAndRefreshToken(UserDetails userDetails) throws BadRequestException;

    JwtAuthenticationResponse signInGoogle();

    void activateAccount(String token) throws MessagingException ;

    void updateAvatar(String urlImg);

    void changePassword(ChangePasswordRequest request, Principal connectedUser);



    User verifyUserAccount(String email, HttpServletRequest request) throws MessagingException, UnsupportedEncodingException;
    void sendVerificationCodeEmail(User user, String siteUrl) throws MessagingException, UnsupportedEncodingException;
    Boolean changeForgotPassword(String newPassword, String confirmPassword, String username) throws NotFoundException;

}
