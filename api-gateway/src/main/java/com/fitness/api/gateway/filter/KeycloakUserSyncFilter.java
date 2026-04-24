package com.fitness.api.gateway.filter;

import com.fitness.api.gateway.dto.RegisterRequest;
import com.fitness.api.gateway.user.UserService;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;


@Component
@RequiredArgsConstructor
public class KeycloakUserSyncFilter implements WebFilter {
    private static final Logger LOGGER = LoggerFactory.getLogger(KeycloakUserSyncFilter.class);
    private final UserService userService;

    @Override
    public Mono<Void> filter(@NonNull ServerWebExchange exchange, @NonNull WebFilterChain filterChain) {
//        String userId = exchange.getRequest().getHeaders().getFirst("X-User-ID");
        String token = exchange.getRequest().getHeaders().getFirst("Authorization");
        RegisterRequest request = getUserDetails(token);

        if (token != null && request != null && request.keycloakId() != null) {
            return userService.validateUser(request.keycloakId())
                    .flatMap(exist -> {
                        if (!exist) {
                            // Register user
                            return userService.registerUser(request).then(Mono.empty());
                        } else {
                            LOGGER.info(":::: User already exists, Skipping sync.");
                        }
                        return Mono.empty();
                    })
                    .then(Mono.defer(() -> {
                        ServerHttpRequest mutatedRequest = exchange.getRequest().mutate()
                                .header("X-User-ID", request.keycloakId())
                                .build();
                        return filterChain.filter(exchange.mutate().request(mutatedRequest).build());
                    }));
        }
        return filterChain.filter(exchange);
    }

    private RegisterRequest getUserDetails(String token) {
        try {
            String tokenWithoutBearer = token.replace("Bearer ", "").trim();
            SignedJWT signedJWT = SignedJWT.parse(tokenWithoutBearer);
            JWTClaimsSet claims = signedJWT.getJWTClaimsSet();

            return new RegisterRequest(
                    claims.getStringClaim("email"),
                    claims.getStringClaim("sub"),
                    claims.getStringClaim("given_name"),
                    claims.getStringClaim("family_name")
            );
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
