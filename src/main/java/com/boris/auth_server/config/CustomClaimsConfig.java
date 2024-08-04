package com.boris.auth_server.config;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.core.OAuth2Token;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;

import java.util.Arrays;
import java.util.Collections;


@Configuration
public class CustomClaimsConfig {

    @Autowired
    private HttpServletRequest request;

//    ეს ქვედა იმიტომ გაკეთდა რომ claims დაემატება ტოკენში როცა ტოკენი გენერირდება

    @Bean
    public OAuth2TokenCustomizer<JwtEncodingContext> jwtTokenCustomizer(){
        return (context -> {
            if (OAuth2TokenType.ACCESS_TOKEN.equals(context.getTokenType())) {
                context.getClaims().claims(claims -> {
                    claims.put("user-name", request.getHeader("UserName"));
                    claims.put("user-id", request.getHeader("UserId"));
                    claims.put("user-provider", request.getHeader("UserProvider"));
                    claims.put("authorities", request.getHeader("UserRole") != null ?
                            Arrays.stream(request.getHeader("UserRole")
                                    .split("\\s*,\\s*")).toList():Collections.emptyList());
                    System.out.println("token claims "+claims);
                    System.out.println(context.getJwsHeader());
                });
            }
        });
    }
}
