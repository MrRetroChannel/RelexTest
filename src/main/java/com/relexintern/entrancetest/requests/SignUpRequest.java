package com.relexintern.entrancetest.requests;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SignUpRequest {
    String email;
    String name;
    String surname;
    String username;
    String password;
}
