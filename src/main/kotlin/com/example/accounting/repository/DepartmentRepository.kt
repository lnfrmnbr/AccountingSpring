package com.example.accounting.repository

import com.example.accounting.models.Department
import com.example.library.repository.BaseInterfaceRepository
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.support.GeneratedKeyHolder
import org.springframework.stereotype.Repository
import java.sql.Statement

interface DepartmentRepository : BaseInterfaceRepository<Department> {
}

@Repository
class DepartmentRepositoryImpl(private val jdbcTemplate: JdbcTemplate): DepartmentRepository {
    private val departmentRowMapper = RowMapper<Department> { rs, _ ->
        Department(
            id = rs.getLong("id"),
            name = rs.getString("name")
        )
    }

    override fun findAll(): List<Department> {
        val sql = "SELECT * FROM departments"
        return jdbcTemplate.query(sql, departmentRowMapper)
    }

    override fun read(id: Long): Department? {
        val sql = "SELECT * FROM departments WHERE id = ?"
        return jdbcTemplate.queryForObject(sql, departmentRowMapper, id)
    }

    override fun add(entity: Department): Long? {
        val sql = """  
        INSERT INTO departments (name) 
        VALUES (?) 
    """.trimIndent()

        val keyHolder = GeneratedKeyHolder()
        jdbcTemplate.update({ connection ->
            val ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
            ps.setString(1, entity.name)
            ps
        }, keyHolder)

        return keyHolder.keyList.firstOrNull()?.get("id")?.toString()?.toLong()
    }

    override fun delete(id: Long): Boolean {
        val sql = "DELETE FROM departments WHERE id = ?"
        val affectedRows = jdbcTemplate.update(sql, id)
        return affectedRows > 0
    }

    override fun edit(id: Long, entity: Department): Department? {
        val sqlUpdate = "UPDATE departments SET name = ? WHERE id = ?"

        jdbcTemplate.update(sqlUpdate, entity.name, id)

        val sqlSelect = "SELECT id, name FROM departments WHERE id = ?"

        return jdbcTemplate.query(sqlSelect, departmentRowMapper, id).singleOrNull()
    }
}