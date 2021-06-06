package com.peachprivacy.userservice.authentication

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.crypto.bcrypt.BCrypt
import org.springframework.stereotype.Service
import java.security.Key
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*
import javax.annotation.PostConstruct

@Service
class AuthenticationService @Autowired constructor(val userRepository: UserRepository) {
    val salt: String = BCrypt.gensalt()

    @Value("\${jwt.secret}")
    lateinit var secret: String
    lateinit var key: Key

    @PostConstruct
    fun initializeKey() {
        key = Keys.hmacShaKeyFor(secret.toByteArray())
    }

    fun register(email: String, password: String): Boolean {
        if (userRepository.existsByEmail(email)) return false
        userRepository.save(Account().apply {
            this.email = email
            this.password = BCrypt.hashpw(password, salt)
            this.role = "default"
        })
        return true
    }

    fun login(email: String, password: String): String? {
        val user = userRepository.findByEmail(email) ?: return null
        if (!BCrypt.checkpw(password, user.password)) return null
        return generateToken(
            user.id.toString(),
            user.email,
            listOf(user.role, user.id.toString(), "any")
        )
    }

    fun generateToken(id: String, email: String, authorities: List<String>): String =
        Jwts.builder()
            .setClaims(mutableMapOf("id" to id, "authorities" to authorities))
            .setSubject(email)
            .setIssuedAt(Date())
            .setExpiration(
                Date.from(
                    LocalDateTime.now().plusDays(14L).atZone(ZoneId.systemDefault()).toInstant()
                )
            )
            .signWith(key)
            .compact()
}