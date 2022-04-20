package com.example.traininglogger.security

object Const {
   const val EXPIRATION_TIME = (24 * 60 * 60 * 1000 // 24 hours
           ).toLong()
   const val SECRET = "P@S5W0RD" // jwt password
   const val TOKEN_PREFIX = "Bearer" // Token prefix
   const val HEADER_STRING = "Authorization" // header key
}