package com.example.traininglogger.model.dtos

import javax.validation.constraints.Email

class CreateUserRequest(
    @Email val username: String,
    val fullname: String,
    val password: String,
    val rePassword: String
)