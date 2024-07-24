package com.mercadolivro.security

import com.mercadolivro.exception.AuthenticationException
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.util.*

@Component
class JwtUtil {

    @Value("\${jwt.expiration}")
    private val expiration: Long? = null

    @Value("\${jwt.secret}")
    private val secret: String? = null

    private val secretKey by lazy { Keys.hmacShaKeyFor(secret!!.toByteArray(Charsets.UTF_8)) }

    fun generateToken(id: Int): String {
        return Jwts.builder()
            .setSubject(id.toString())
            .setExpiration(Date(System.currentTimeMillis() + expiration!!))
            .signWith(secretKey)
            .compact()
    }

    fun isValidToken(token: String): Boolean {
        val claims =  getClaims(token)
        if(!(claims.subject != null && claims.expiration != null && !Date().after(claims.expiration))) {
            return false
        }
        return true
    }

    private fun getClaims(token: String): Claims {
        return try {
            val jwtParser = Jwts.parserBuilder().setSigningKey(secretKey).build()
            val claimsJws = jwtParser.parseClaimsJws(token)
            claimsJws.body
        } catch (ex: Exception) {
            throw AuthenticationException("Invalid Token", "9999")
        }
    }

    fun getSubject(token: String): String {
        return getClaims(token).subject
    }
}