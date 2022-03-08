package com.example.apprestjwt.security;

import com.example.apprestjwt.service.MyAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {
    @Autowired
    JwtProvider jwtProvider;
    @Autowired
    MyAuthService myAuthService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //Get token from Request
        String token = request.getHeader("Authorization");
        //Check whether token is not null and starts with "Bearer "
        if (token != null && token.startsWith("Bearer ")) {
            //Removing the word "Bearer " from token
            token = token.substring(7);
            //Validating token (Check whether token is not disturbed, not expired and so on)
            boolean validateToken = jwtProvider.validateToken(token);
            if (validateToken) {
                //Getting username from token
                String username = jwtProvider.getUsernameFromToken(token);
                //Getting UserDetails by username
                UserDetails userDetails = myAuthService.loadUserByUsername(username);
                //Creating Authentication using UserDetails
                UsernamePasswordAuthenticationToken userAuthenticated =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities());

                //Setting the user as authenticated (logged in)
                SecurityContextHolder.getContext().setAuthentication(userAuthenticated);
                System.out.println(SecurityContextHolder.getContext().getAuthentication().getCredentials());
            }
        }

        filterChain.doFilter(request,response);

    }
}
