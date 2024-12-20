package com.example.accounting.service

import com.example.accounting.models.User
import com.example.accounting.repository.UserRepository
import org.slf4j.LoggerFactory
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service


@Service
class UserService(private val userRepository: UserRepository) {

    private val passwordEncoder = BCryptPasswordEncoder()

    fun register(username: String, password: String, admin: Boolean) {
        val hashedPassword = passwordEncoder.encode(password)
        val user = User(login = username, password = hashedPassword, admin = admin)
        userRepository.save(user)
    }

    fun authenticate(username: String, password: String): Boolean {
        val user = userRepository.findByLogin(username) ?: return false
        return passwordEncoder.matches(password, user.password)
    }

    fun isAdmin(username: String): Boolean {
        val user = userRepository.findByLogin(username) ?: return false
        return user.admin
    }
}
