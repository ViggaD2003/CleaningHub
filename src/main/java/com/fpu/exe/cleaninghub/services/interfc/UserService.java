package com.fpu.exe.cleaninghub.services.interfc;

import com.fpu.exe.cleaninghub.dto.request.AccountRequest;
import com.fpu.exe.cleaninghub.dto.request.LocationRequest;
import com.fpu.exe.cleaninghub.dto.response.AccountResponseDto;
import com.fpu.exe.cleaninghub.dto.response.UserResponseDTO;
import com.fpu.exe.cleaninghub.entity.User;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import javassist.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.Optional;

public interface UserService {
    UserDetailsService userDetailsService();
    UserResponseDTO getUserByEmail(String email);
    String updateLocationOfStaff(HttpServletRequest request, LocationRequest locationRequest);
    Page<UserResponseDTO> getAllUsers(String searchTerm, int page, int size);
    User updateUserRoleAndStatus(Integer userId, Integer roleId, Boolean status);
    boolean updateRole(AccountRequest request);
    void deleteAccount(Integer userId);
    Page<AccountResponseDto> getAllNonAdminAccounts(String searchTerm, int page, int size);
}
