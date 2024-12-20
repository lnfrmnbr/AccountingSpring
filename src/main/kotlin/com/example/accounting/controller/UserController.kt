package com.example.accounting.controller
import com.example.accounting.models.Department
import com.example.accounting.models.User
import com.example.accounting.service.UserService
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping
class AuthController(private val userService: UserService) {
    private val logger = LoggerFactory.getLogger(UserService::class.java)

    @PostMapping("/register")
    fun register(@RequestBody user: User): Boolean {
        userService.register(user.login, user.password, user.admin)
        return true
    }

    @PostMapping("/login")
    fun login(@RequestBody user: User): Boolean {
        return if (userService.authenticate(user.login, user.password)) {
            true
        } else {
            false
        }
    }

    @PostMapping("/admin")
    fun isAdmin(@RequestBody user: User): Boolean {
        return if (userService.isAdmin(user.login)) {
            true
        } else {
            false
        }
    }
}
