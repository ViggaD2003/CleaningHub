package com.fpu.exe.cleaninghub.services.interfc;

import com.fpu.exe.cleaninghub.dto.request.SignInRequest;
import com.fpu.exe.cleaninghub.dto.request.SignUpRequest;
import com.fpu.exe.cleaninghub.dto.request.UserProfileDTO;
import com.fpu.exe.cleaninghub.dto.response.JwtAuthenticationResponse;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.coyote.BadRequestException;
import org.springframework.security.core.userdetails.UserDetails;
import java.io.IOException;

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

}
