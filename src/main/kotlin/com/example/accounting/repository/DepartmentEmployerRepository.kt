package com.example.accounting.repository

import com.example.accounting.models.Department
import com.example.accounting.models.DepartmentEmployer
import com.example.accounting.models.Employer
import com.example.library.repository.BaseInterfaceRepository
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.support.GeneratedKeyHolder
import java.sql.Statement

interface DepartmentEmployerRepository: BaseInterfaceRepository<DepartmentEmployer> {
}

@Repository
class DepartmentEmployerRepositoryImpl(private val jdbcTemplate: JdbcTemplate) : DepartmentEmployerRepository {

    private val departmentEmployerRowMapper = RowMapper<DepartmentEmployer> { rs, _ ->
        DepartmentEmployer(
            id = rs.getLong("id"),
            department = Department(
                id = rs.getLong("department_id"),
                name = rs.getString("department_name")
            ),
            employer = Employer(
                id = rs.getLong("employee_id"),
                firstName = rs.getString("first_name"),
                fatherName = rs.getString("father_name"),
                lastName = rs.getString("last_name"),
                position = rs.getString("position"),
                salary = rs.getInt("salary")
            )
        )
    }

    override fun findAll(): List<DepartmentEmployer> {
        val sql = """
            SELECT de.id, d.id AS department_id, d.name AS department_name,
            e.id AS employee_id, e.first_name, e.father_name,
            e.last_name, e.position, e.salary
            FROM departments_employees de
            LEFT JOIN departments d ON de.department_id = d.id
            LEFT JOIN employees e ON de.employee_id = e.id
        """.trimIndent()
        return jdbcTemplate.query(sql, departmentEmployerRowMapper)
    }

    override fun read(id: Long): DepartmentEmployer? {
        val sql = """
            SELECT de.id, d.id AS department_id, d.name AS department_name,
                   e.id AS employee_id, e.first_name, e.father_name,
                   e.last_name, e.position, e.salary
            FROM departments_employees de
            LEFT JOIN departments d ON de.department_id = d.id
            LEFT JOIN employees e ON de.employee_id = e.id
            WHERE de.id = ?
        """.trimIndent()
        return jdbcTemplate.queryForObject(sql, departmentEmployerRowMapper, id)
    }

    override fun add(entity: DepartmentEmployer): Long? {
        val sqlInsertDepartmentEmployer = """
            INSERT INTO departments_employees (department_id, employee_id)
            VALUES (?, ?)
        """.trimIndent()

        val keyHolder = GeneratedKeyHolder()
        jdbcTemplate.update({ connection ->
            val ps = connection.prepareStatement(sqlInsertDepartmentEmployer, Statement.RETURN_GENERATED_KEYS)
            entity.department.id?.let { ps.setLong(1, it) }
            entity.employer.id?.let { ps.setLong(2, it) }
            ps
        }, keyHolder)

        return keyHolder.keyList.firstOrNull()?.get("id")?.toString()?.toLong()
    }

    override fun delete(id: Long): Boolean {
        val sql = "DELETE FROM departments_employees WHERE id = ?"
        val affectedRows = jdbcTemplate.update(sql, id)
        return affectedRows > 0
    }

    override fun edit(id: Long, entity: DepartmentEmployer): DepartmentEmployer? {
        val sqlUpdate = """
            UPDATE departments_employees 
            SET department_id = ?, employee_id = ?
            WHERE id = ?
        """.trimIndent()

        jdbcTemplate.update(sqlUpdate,
            entity.department.id,
            entity.employer.id,
            id
        )

        return read(id)
    }
}
