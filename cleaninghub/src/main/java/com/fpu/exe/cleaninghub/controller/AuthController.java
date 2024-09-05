    package com.fpu.exe.cleaninghub.controller;


    import com.fpu.exe.cleaninghub.config.LogoutService;
    import com.fpu.exe.cleaninghub.dto.request.SignInRequest;
    import com.fpu.exe.cleaninghub.dto.request.SignUpRequest;
    import com.fpu.exe.cleaninghub.dto.request.UserProfileDTO;
    import com.fpu.exe.cleaninghub.dto.response.JwtAuthenticationResponse;
    import com.fpu.exe.cleaninghub.services.interfc.AuthenticationService;
    import com.fpu.exe.cleaninghub.utils.wapper.API;
    import jakarta.servlet.http.HttpServletRequest;
    import jakarta.servlet.http.HttpServletResponse;
    import lombok.RequiredArgsConstructor;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.http.HttpStatus;
    import org.springframework.http.ResponseEntity;
    import org.springframework.security.core.Authentication;
    import org.springframework.web.bind.annotation.*;
    import java.io.IOException;

    @RestController
    @RequestMapping("/api/v1/auth")
    @RequiredArgsConstructor
    public class AuthController {
        @Autowired
        private final AuthenticationService authenticationService;
        @Autowired
        private final LogoutService logoutService;


        @PostMapping("/signup")
        public ResponseEntity<JwtAuthenticationResponse> signup(@RequestBody SignUpRequest signUpRequest){
            return ResponseEntity.ok(authenticationService.signUp(signUpRequest));
        }

        @PostMapping("/signin")
        public ResponseEntity<JwtAuthenticationResponse> signin(@RequestBody SignInRequest signInRequest){

            return ResponseEntity.ok(authenticationService.signIn(signInRequest));
        }

//        @GetMapping("/signingoogle")
//        public void handleGoogleCallback( HttpServletResponse response) throws IOException {
//            String accessToken = authenticationService.signingoogle();
//            response.sendRedirect("http://localhost:5173/login-success?token=" + accessToken);
//        }

        @PostMapping("/refresh")
        public void refresh(HttpServletRequest request, HttpServletResponse response) throws IOException {
            authenticationService.refreshToken(request, response);
        }

        @GetMapping("/hello")
        public ResponseEntity<String> hello(){
            return ResponseEntity.ok("hello World");
        }


        @PostMapping("/logout")
        public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
            logoutService.logout(request, response, authentication);
        }

        @GetMapping("/account")
        public ResponseEntity<Object> getCurrentLoginUser(HttpServletRequest request) {
            return ResponseEntity.ok(API.Response.success(authenticationService.getUserInformation(request)));
        }

        @PutMapping("/account/update_profile/{id}")
        public ResponseEntity<Object> updateUserProfile(@PathVariable("id") Integer id, @RequestBody UserProfileDTO userProfileDTO){
            UserProfileDTO userResponse = authenticationService.updateUserInfo(id, userProfileDTO);
            if(userResponse != null){
                return ResponseEntity.ok(API.Response.success(userResponse));
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update user profile information");
            }
        }
    }
