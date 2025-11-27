package com.rutaflex.trackflexv3.filter;

import com.rutaflex.trackflexv3.service.security.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7);

        try {
            String username = jwtService.extractUsername(token);

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                if (jwtService.isTokenValid(token, userDetails)) {

                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails,
                                    null,
                                    userDetails.getAuthorities()
                            );

                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }

            filterChain.doFilter(request, response);

        } catch (io.jsonwebtoken.ExpiredJwtException ex) {
            sendError(response, 401, "TOKEN_EXPIRED", "El token ha expirado. Por favor inicie sesión nuevamente.");

        } catch (io.jsonwebtoken.MalformedJwtException ex) {
            sendError(response, 400, "TOKEN_INVALID", "El token enviado es inválido o está corrupto.");

        } catch (io.jsonwebtoken.SignatureException ex) {
            sendError(response, 401, "INVALID_SIGNATURE", "Firma JWT inválida. El token no es confiable.");

        } catch (io.jsonwebtoken.UnsupportedJwtException ex) {
            sendError(response, 400, "UNSUPPORTED_TOKEN", "El tipo de token no es soportado.");

        } catch (IllegalArgumentException ex) {
            sendError(response, 400, "EMPTY_TOKEN", "No se pudo procesar el token, está vacío o malformado.");

        } catch (Exception ex) {
            sendError(response, 500, "TOKEN_ERROR", "Error inesperado al procesar el token.");
        }
    }

    private void sendError(HttpServletResponse response,
                           int status,
                           String code,
                           String message) throws IOException {

        response.setStatus(status);
        response.setContentType("application/json");

        String json = String.format(
                "{ \"error\": \"%s\", \"message\": \"%s\", \"status\": %d }",
                code, message, status
        );

        response.getWriter().write(json);
    }
}

