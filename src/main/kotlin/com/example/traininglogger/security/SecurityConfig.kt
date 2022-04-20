package com.example.traininglogger.security

import com.example.traininglogger.datasource.UserRepository
import com.example.traininglogger.model.User
import org.springframework.context.annotation.Bean
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import org.springframework.web.filter.CorsFilter
import java.text.MessageFormat
import javax.servlet.http.HttpServletResponse


@EnableWebSecurity
class SecurityConfig(
    private val jwtTokenFilter: JwtTokenFilter,
    private val customUserDetailsService: CustomUserDetailsService) : WebSecurityConfigurerAdapter() {

    override fun configure(auth: AuthenticationManagerBuilder?) {
            auth?.userDetailsService(customUserDetailsService)
    }

    override fun configure(http: HttpSecurity?) {
        // Enable CORS and disable CSRF
        var http_ = http!!.cors().and().csrf().disable();

        // Set session management to stateless
        http_ = http_
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and();

        // Set unauthorized requests exception handler
        http_ = http_
            .exceptionHandling()
            .authenticationEntryPoint { _, response, ex ->
                response.sendError(
                    HttpServletResponse.SC_UNAUTHORIZED,
                    ex.message
                )
            }.and();

        // Set permissions on endpoints
        http_.authorizeHttpRequests()
            .antMatchers("/api/public/**").permitAll()
            .anyRequest().authenticated();

        // Add JWT token filter
        http_.addFilterBefore(
            jwtTokenFilter,
            UsernamePasswordAuthenticationFilter::class.java
        );
    }

    @Bean
    fun PasswordEncoder(): PasswordEncoder = BCryptPasswordEncoder();

    @Bean
    fun corsFilter(): CorsFilter {
        val source = UrlBasedCorsConfigurationSource()
        val config = CorsConfiguration()
        config.allowCredentials = true
        config.addAllowedOrigin("*")
        config.addAllowedHeader("*")
        config.addAllowedMethod("*")
        source.registerCorsConfiguration("/**", config)
        return CorsFilter(source)
    }

    @Bean
    override fun authenticationManagerBean(): AuthenticationManager {
        return super.authenticationManagerBean()
    }

}