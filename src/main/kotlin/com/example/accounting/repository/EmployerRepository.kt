package com.example.accounting.repository

import com.example.accounting.models.Employer
import com.example.library.repository.BaseInterfaceRepository
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.support.GeneratedKeyHolder
import org.springframework.stereotype.Repository
import java.sql.Date
import java.sql.Statement
import java.time.LocalDate

interface EmployerRepository: BaseInterfaceRepository<Employer> {
}

@Repository
class EmployerRepositoryImpl(private val jdbcTemplate: JdbcTemplate): EmployerRepository {
    private val employerRowMapper = RowMapper<Employer> { rs, _ ->
        Employer(
            id = rs.getLong("id"),
            firstName = rs.getString("first_name"),
            fatherName = rs.getString("father_name"),
            lastName = rs.getString("last_name"),
            position = rs.getString("position"),
            salary = rs.getInt("salary")
        )
    }

    override fun findAll(): List<Employer> {
        val sql = "SELECT * FROM employees"
        return jdbcTemplate.query(sql, employerRowMapper)
    }

    override fun read(id: Long): Employer? {
        val sql = "SELECT * FROM employees WHERE id = ?"
        return jdbcTemplate.queryForObject(sql, employerRowMapper, id)
    }

    override fun add(entity: Employer): Long? {
        val sql = """  
        INSERT INTO employees (first_name, father_name, last_name, position, salary) 
        VALUES (?, ?, ?, ?, ?) 
    """.trimIndent()

        val keyHolder = GeneratedKeyHolder()
        jdbcTemplate.update({ connection ->
            val ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
            ps.setString(1, entity.firstName)
            ps.setString(2, entity.fatherName)
            ps.setString(3, entity.lastName)
            ps.setString(4, entity.position)
            ps.setInt(5, entity.salary)
            ps
        }, keyHolder)

        return keyHolder.keyList.firstOrNull()?.get("id")?.toString()?.toLong()
    }

    override fun delete(id: Long): Boolean {
        val sql = "DELETE FROM employees WHERE id = ?"
        val affectedRows = jdbcTemplate.update(sql, id)
        return affectedRows > 0
    }

    override fun edit(id: Long, entity: Employer): Employer? {
        val sqlUpdate = "UPDATE employees SET first_name = ?, father_name = ?, last_name = ?, position = ?, salary = ? WHERE id = ?"

        jdbcTemplate.update(sqlUpdate, entity.firstName, entity.fatherName, entity.lastName, entity.position, entity.salary, id)

        val sqlSelect = "SELECT id, first_name, father_name, last_name, position, salary FROM employees WHERE id = ?"

        return jdbcTemplate.query(sqlSelect, employerRowMapper, id).singleOrNull()
    }
}