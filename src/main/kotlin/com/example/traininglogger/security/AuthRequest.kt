package com.example.traininglogger.security

import javax.validation.constraints.Email

class AuthRequest(
    @Email var username: String?,
    var password: String?
) {
    constructor() : this(null, null);
}