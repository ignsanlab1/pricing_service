package com.example.api_gateway.service;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

    private final RouterValidator routerValidator;

    private final JwtUtils jwtUtils;

    public AuthenticationFilter(RouterValidator routerValidator, JwtUtils jwtUtils) {
        super(Config.class);
        this.routerValidator = routerValidator;
        this.jwtUtils = jwtUtils;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return ((exchange, chain) -> {
            var request = exchange.getRequest();

            ServerHttpRequest serverHttpRequest = null;
            if(routerValidator.isSecured.test(request)){
                if(authMissing(request)){
                    return onError(exchange, HttpStatus.UNAUTHORIZED);
                }
                List<String> authHeaders = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION);
                if (authHeaders == null || authHeaders.isEmpty()) {
                    return onError(exchange, HttpStatus.UNAUTHORIZED);
                }

                String authHeader = authHeaders.get(0);
                System.out.println(authHeader);
                if(authHeader != null && authHeader.startsWith("Bearer ")){
                    authHeader = authHeader.substring(7);
                } else {
                    return onError(exchange, HttpStatus.UNAUTHORIZED);
                }

                System.out.println(authHeader);
                if(jwtUtils.isExpired(authHeader)){
                    return onError(exchange, HttpStatus.UNAUTHORIZED);
                }

                System.out.println(authHeader);
                System.out.println(jwtUtils.extractUserId(authHeader).toString());
                serverHttpRequest = exchange.getRequest()
                        .mutate()
                        .header("userIdRequest", jwtUtils.extractUserId(authHeader).toString())
                        .build();
            }
            return chain.filter(exchange.mutate().request(serverHttpRequest).build());
        });
    }

    private Mono<Void> onError(ServerWebExchange exchange, HttpStatus httpStatus){
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(httpStatus);
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);

        DataBuffer dataBuffer = response.bufferFactory().wrap("{\"error\": \"Unauthorized\"}".getBytes());
        return response.writeWith(Mono.just(dataBuffer));
    }

    private boolean authMissing(ServerHttpRequest request){
        return !request.getHeaders().containsKey("Authorization");
    }

    public static class Config{
    }
}
