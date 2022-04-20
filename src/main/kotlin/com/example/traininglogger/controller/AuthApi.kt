package com.example.traininglogger.controller

import com.example.traininglogger.model.User
import com.example.traininglogger.model.dtos.CreateUserRequest
import com.example.traininglogger.security.AuthRequest
import com.example.traininglogger.security.JwtTokenUtil
import com.example.traininglogger.security.UserView
import com.example.traininglogger.security.UserViewMapper
import com.example.traininglogger.service.UserService
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid


@RestController
@RequestMapping(path = ["api/public"])
class AuthApi(
    private val authenticationManager: AuthenticationManager,
    private val jwtTokenUtil: JwtTokenUtil,
    private val userViewMapper: UserViewMapper,
    private val userService: UserService
    ) {

    @PostMapping("login")
    fun login(@RequestBody @Valid request: AuthRequest): ResponseEntity<UserView> {
        try {
            val tmp = UsernamePasswordAuthenticationToken(
                request.username, request.password
            )

            val authenticate: Authentication = this.authenticationManager
                .authenticate(tmp)

            val user: User = authenticate.principal as User

            return ResponseEntity.ok().header(
                HttpHeaders.AUTHORIZATION,
                jwtTokenUtil.generateToken(user)
            ).body(userViewMapper.toUserView(user))
        } catch (ex: BadCredentialsException) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()
        }
    }

    @PostMapping("register")
    fun register(@RequestBody @Valid request: CreateUserRequest): UserView {
        return userService.create(request)
    }

}