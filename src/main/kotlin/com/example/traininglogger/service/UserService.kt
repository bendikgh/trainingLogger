package com.example.traininglogger.service

import com.example.traininglogger.datasource.UserRepository
import com.example.traininglogger.model.User
import com.example.traininglogger.model.dtos.CreateUserRequest
import com.example.traininglogger.security.UserView
import com.example.traininglogger.security.UserViewMapper
import org.springframework.stereotype.Service
import javax.transaction.Transactional
import javax.validation.ValidationException
import org.springframework.security.crypto.password.PasswordEncoder


@Service
class UserService(
    val userRepository: UserRepository,
    val passwordEncoder: PasswordEncoder,
    val userViewMapper: UserViewMapper
) {

    @Transactional
    fun create(request: CreateUserRequest): UserView {
        if (userRepository.findByUsername(request.username).isPresent) {
            throw ValidationException("Username exists!")
        }
        if (request.password != request.rePassword) {
            throw ValidationException("Passwords don't match!")
        }
        var user: User = User(request)
        user.password = passwordEncoder.encode(request.password)
        user = userRepository.save(user)
        return userViewMapper.toUserView(user)
    }
}