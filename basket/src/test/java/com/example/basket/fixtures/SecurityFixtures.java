package com.example.basket.fixtures;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class SecurityFixtures {
    @Autowired
    private JwtEncoder jwtEncoder;

    public String generateToken(String username, List<String> roles){
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .subject(username)
                .expiresAt(Instant.now().plus(24, ChronoUnit.HOURS))
                .claim("scope", String.join(" ", roles))
                .build();
        return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }
}
