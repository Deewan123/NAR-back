package com.sdd.service;


import com.sdd.login.LoginRequest;
import com.sdd.request.BockUserRequest;
import com.sdd.request.ChangePasswordRequest;
import com.sdd.request.CreateUserRequest;
import com.sdd.response.*;

import java.util.List;

public interface UserAccountServices {

    ApiResponse<LoginResponse> login(LoginRequest loginRequest);
    ApiResponse<LogoutResponse> createUser(CreateUserRequest loginRequest);
    ApiResponse<LogoutResponse> blockUser(BockUserRequest bockUserRequest);
    ApiResponse<ChangePasswordResponse> changePassword(ChangePasswordRequest changePasswordRequest);
    ApiResponse<LogoutResponse> logout(String logId);
    ApiResponse<List<AllUserResponse>> getAllUser();
}
