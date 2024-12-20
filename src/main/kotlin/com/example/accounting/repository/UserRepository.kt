package com.example.accounting.repository
import com.example.accounting.models.User
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository
import org.springframework.jdbc.core.RowMapper

interface UserRepository {
    fun findByLogin(login: String): User?
    fun save(user: User)
}

@Repository
class UserRepositoryImpl(private val jdbcTemplate: JdbcTemplate): UserRepository{
    private val rowMapper = RowMapper<User> { rs, _ ->
        User(
            id = rs.getLong("id"),
            login = rs.getString("login"),
            password = rs.getString("password"),
            admin = rs.getBoolean("admin")
        )
    }

    override fun findByLogin(login: String): User? {
        val sql = "SELECT * FROM users WHERE login = ?"
        return jdbcTemplate.query(sql, rowMapper, login).firstOrNull()
    }

    override fun save(user: User) {
        val sql = "INSERT INTO users (login, password, admin) VALUES (?, ?, ?)"
        jdbcTemplate.update(sql, user.login, user.password, user.admin)
    }

}
