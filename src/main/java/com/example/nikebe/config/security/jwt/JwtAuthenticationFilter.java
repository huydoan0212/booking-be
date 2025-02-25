package com.example.nikebe.config.security.jwt;

import com.example.nikebe.domain.user.user.service.UserDetailServiceImpl;
import io.micrometer.common.lang.NonNull;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.security.GeneralSecurityException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserDetailServiceImpl userDetailsService;

    @SuppressWarnings("null")
    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");
        if (authHeader == null ||  !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }
        try{
            final String jwtToken = authHeader.substring(7);
            if(!jwtUtils.validateJwtToken(jwtToken)) {filterChain.doFilter(request, response);return;}
            final Object username = jwtUtils.extractUsername(jwtToken);
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if(username != null && authentication == null){

                UserDetails userDetails = this.userDetailsService.loadUserByUsername(username.toString());

                if(jwtUtils.isTokenValid(jwtToken, userDetails)){
                    UsernamePasswordAuthenticationToken authenticationToken = new  UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities());
                    logger.info(userDetails.getAuthorities());
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
                else throw new BadCredentialsException("The account has been banned", new Throwable("account"));
            }
        } catch (GeneralSecurityException e ) {
            throw new RuntimeException(e);
        }catch (BadCredentialsException e ) {
            throw new BadCredentialsException("Bad credentials", e);
        }
        filterChain.doFilter(request, response);
    }

}
