//package com.atquil.jwt_oauth2;
//
//import com.atquil.jwt_oauth2.service.*;
//import com.fasterxml.jackson.databind.*;
//import jakarta.servlet.*;
//import jakarta.servlet.http.*;
//import lombok.*;
//import org.springframework.beans.factory.annotation.*;
//import org.springframework.security.authentication.*;
//import org.springframework.security.core.*;
//import org.springframework.security.web.authentication.*;
//import org.springframework.security.web.util.matcher.*;
//
//import java.io.*;
//import java.util.*;
//
//public class CustomAuthFilter extends AbstractAuthenticationProcessingFilter {
//    @Autowired
//    public CustomAuthFilter() {
//        super(new AntPathRequestMatcher("/sign-in", "POST"));
//
//    }
//
//    @Override
//    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException {
//        ObjectMapper mapper = new ObjectMapper();
//        Map<String, String> requestMap = mapper.readValue(request.getInputStream(), Map.class);
//        String email = requestMap.get("email");
//        String password = requestMap.get("password");
//
////        UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(email, password);
//
//        return new AuthService().getAuthentication();
//    }
//
//    @Override
//    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException {
//        // handle successful authentication
//        try {
//            chain.doFilter(request, response);
//        } catch (ServletException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    @Override
//    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException {
//        // handle unsuccessful authentication
//        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, failed.getMessage());
//    }
//}