package com.sdd.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangePasswordRequest {
    private String userId;
    private String newPassword;
    private String oldPassword;
}
