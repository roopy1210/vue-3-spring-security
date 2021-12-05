package com.roopy.api.security.jwt.payload.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TokenResponse {

    private String accessToken;

    private String username;

    private String displayName;

    private List<String> roles;
}
