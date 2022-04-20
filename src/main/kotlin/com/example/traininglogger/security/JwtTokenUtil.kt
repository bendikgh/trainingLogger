package com.example.traininglogger.security

import com.example.traininglogger.model.User
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.stereotype.Component
import java.io.Serializable
import java.util.*

@Component
class JwtTokenUtil : Serializable {
    fun getUsernameFromToken(token: String): String? {
        val username: String? = try {
            val claims = getClaimsFromToken(token)
            claims!!.subject
        } catch (e: Exception) {
            null
        }
        return username
    }

    fun getCreatedDateFromToken(token: String): Date? {
        val created: Date? = try {
            val claims = getClaimsFromToken(token)
            Date((claims!![CLAIM_KEY_CREATED] as Long?)!!)
        } catch (e: Exception) {
            null
        }
        return created
    }

    fun getExpirationDateFromToken(token: String): Date? {
        val expiration: Date?
        expiration = try {
            val claims = getClaimsFromToken(token)
            claims!!.expiration
        } catch (e: Exception) {
            null
        }
        return expiration
    }

    private fun getClaimsFromToken(token: String): Claims? {
        val claims: Claims? = try {
            Jwts.parser()
                .setSigningKey(Const.SECRET)
                .parseClaimsJws(token)
                .body
        } catch (e: Exception) {
            null
        }
        return claims
    }

    private fun generateExpirationDate(): Date {
        return Date(System.currentTimeMillis() + Const.EXPIRATION_TIME)
    }

    private fun isTokenExpired(token: String): Boolean {
        val expiration = getExpirationDateFromToken(token)
        return expiration!!.before(Date())
    }

    private fun isCreatedBeforeLastPasswordReset(created: Date, lastPasswordReset: Date?): Boolean {
        return lastPasswordReset != null && created.before(lastPasswordReset)
    }

    fun generateToken(userDetails: User): String {
        val claims: MutableMap<String, Any> = HashMap()
        claims[CLAIM_KEY_USERNAME] = userDetails.username
        claims[CLAIM_KEY_CREATED] = Date()
        return generateToken(claims)
    }

    fun generateToken(claims: Map<String, Any>): String {
        return Jwts.builder()
            .setClaims(claims)
            .setExpiration(generateExpirationDate())
            .signWith(SignatureAlgorithm.HS512, Const.SECRET)
            .compact()
    }

    fun canTokenBeRefreshed(token: String): Boolean {
        return !isTokenExpired(token)
    }

    fun refreshToken(token: String): String? {
        var refreshedToken: String?
        try {
            val claims = getClaimsFromToken(token)
            claims!![CLAIM_KEY_CREATED] = Date()
            refreshedToken = generateToken(claims)
        } catch (e: Exception) {
            refreshedToken = null
        }
        return refreshedToken
    }

    fun validateToken(token: String, user: User): Boolean {
        val username = getUsernameFromToken(token) ?: return false
        return username == user.username && !isTokenExpired(token)
    }

    companion object {
        private const val serialVersionUID = -5625635588908941275L
        private const val CLAIM_KEY_USERNAME = "sub"
        private const val CLAIM_KEY_CREATED = "created"
    }
}