package com.relexintern.entrancetest.requests;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SignInRequest {
    String username;
    String password;
}
