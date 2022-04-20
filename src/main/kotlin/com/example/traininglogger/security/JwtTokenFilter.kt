package com.example.traininglogger.security

import com.example.traininglogger.datasource.UserRepository
import com.example.traininglogger.model.User
import org.springframework.http.HttpHeaders
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class JwtTokenFilter(val jwtTokenUtil: JwtTokenUtil, val userRepository: UserRepository) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if(header.isNullOrEmpty() || !header.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // Get jwt token and validate
        val token: String = header.split(" ")[1].trim();

        val username: String = jwtTokenUtil.getUsernameFromToken(token) ?: return;

        // Get user identity and set it on the spring security context
        val user: User = userRepository
            .findByUsername(username).get() ?: return;

        if(!jwtTokenUtil.validateToken(token, user)) {
            filterChain.doFilter(request, response);
            return;
        }

        val auth = UsernamePasswordAuthenticationToken(
                user,null, user.authorities
            );

        auth.details = WebAuthenticationDetailsSource().buildDetails(request);

        SecurityContextHolder.getContext().authentication = auth;

        filterChain.doFilter(request, response);

    }
}