package com.example.accounting.repository

import com.example.accounting.models.Department
import com.example.accounting.models.Project
import com.example.library.repository.BaseInterfaceRepository
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.support.GeneratedKeyHolder
import org.springframework.stereotype.Service
import java.sql.Date
import java.sql.Statement
import java.time.format.DateTimeFormatter

interface ProjectRepository: BaseInterfaceRepository<Project> {
}

@Service
class ProjectRepositoryImpl(private val jdbcTemplate: JdbcTemplate): ProjectRepository{
    val projectSql = """
        SELECT p.id AS project_id, p.name AS project_name, p.cost, p.date_beg, p.date_end, p.date_end_real, p.id AS department_id, d.name AS department_name 
        FROM projects p
        JOIN departments d ON p.department_id = d.id
    """.trimIndent()

    override fun findAll(): List<Project> {
        return jdbcTemplate.query(projectSql) { rs, _ ->
            Project(
                id = rs.getLong("project_id"),
                name = rs.getString("project_name"),
                department = Department(
                    id = rs.getLong("department_id"),
                    name = rs.getString("department_name")
                ),
                cost = rs.getInt("cost"),
                dateBeg = rs.getString("date_beg"),
                dateEnd = rs.getString("date_end"),
                dateEndReal = rs.getString("date_end_real")
            )
        }.toList()
    }

    override fun read(id: Long): Project? {
        val projectSqlRead = "$projectSql WHERE p.id = ?"
        return jdbcTemplate.query(projectSqlRead, { rs, _ ->
            Project(
                id = rs.getLong("project_id"),
                name = rs.getString("project_name"),
                department = Department(
                    id = rs.getLong("department_id"),
                    name = rs.getString("department_name")
                ),
                cost = rs.getInt("cost"),
                dateBeg = rs.getString("date_beg"),
                dateEnd = rs.getString("date_end"),
                dateEndReal = rs.getString("date_end_real")
            )
        }, id).toList().firstOrNull()
    }

    override fun add(entity: Project): Long? {
        val sqlInsertWork = "INSERT INTO projects (name, cost, department_id, date_beg, date_end, date_end_real) VALUES (?, ?, ?, ?, ?, ?)"

        val keyHolder = GeneratedKeyHolder()

        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        jdbcTemplate.update({ connection ->
            val ps = connection.prepareStatement(sqlInsertWork, Statement.RETURN_GENERATED_KEYS)
            ps.setString(1, entity.name)
            ps.setInt(2, entity.cost)
            entity.department.id?.let { ps.setLong(3, it) }

            if (entity.dateBeg != null && entity.dateBeg != "") {
                ps.setDate(4, Date.valueOf(entity.dateBeg))
            } else {
                ps.setNull(4, java.sql.Types.TIMESTAMP)
            }

            if (entity.dateEnd != null && entity.dateEnd != "") {
                ps.setDate(5, Date.valueOf(entity.dateEnd))
            } else {
                ps.setNull(5, java.sql.Types.TIMESTAMP)
            }

            if (entity.dateEndReal != null && entity.dateEndReal != "") {
                ps.setDate(6, Date.valueOf(entity.dateEndReal))
            } else {
                ps.setNull(6, java.sql.Types.TIMESTAMP)
            }
            ps
        }, keyHolder)

        val projectId = keyHolder.keyList.firstOrNull()?.get("id")?.toString()?.toLong()

        return projectId
    }

    override fun delete(id: Long): Boolean {
        val sql = "DELETE FROM projects WHERE id = ?"
        val affectedRows = jdbcTemplate.update(sql, id)
        return affectedRows > 0
    }

    override fun edit(id: Long, entity: Project): Project? {
        val sqlUpdate = "UPDATE projects SET name = ?, cost = ?, department_id = ?, date_beg = ?, date_end = ?, date_end_real = ? WHERE id = ?"

        var dateBeg: Date? = null
        if (entity.dateBeg != null && entity.dateBeg != ""){
            dateBeg = Date.valueOf(entity.dateBeg)
        }

        var dateEnd: Date? = null
        if (entity.dateEnd != null && entity.dateEnd != ""){
            dateEnd = Date.valueOf(entity.dateEnd)
        }

        var dateEndReal: Date? = null
        if (entity.dateEndReal != "" && entity.dateEndReal != null){
            dateEndReal = Date.valueOf(entity.dateEndReal)
        }

        jdbcTemplate.update(sqlUpdate, entity.name, entity.cost, entity.department.id, dateBeg, dateEnd, dateEndReal, id)

        val sqlSelect = "$projectSql WHERE p.id = ?"
        val resultList = jdbcTemplate.query(sqlSelect, { rs, _ ->
            Project(
                id = rs.getLong("project_id"),
                name = rs.getString("project_name"),
                department = Department(
                    id = rs.getLong("department_id"),
                    name = rs.getString("department_name")
                ),
                cost = rs.getInt("cost"),
                dateBeg = rs.getString("date_beg"),
                dateEnd = rs.getString("date_end"),
                dateEndReal = rs.getString("date_end_real")
            )
        }, id)

        return resultList.singleOrNull()
    }

}