package com.example.accounting.service

import com.example.accounting.repository.CalculationRepository
import liquibase.structure.core.Data
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Service
import javax.sql.DataSource

@Service
class CalculationService(private val jdbcTemplate: JdbcTemplate, private val calculationRepository: CalculationRepository) {
    @Autowired
    lateinit var dataSource: DataSource

    fun calculateTotalProfit(): Int {
        calculationRepository.calculateTotalProfit(dataSource)
        val result = jdbcTemplate.queryForObject(
            "SELECT calculate_profit()", Int::class.java
        )
        return result ?: 0
    }

    fun calculateOneProfit(id: Long): Int {
        calculationRepository.calculateOneProfit(id, dataSource)
        val result = jdbcTemplate.queryForObject(
            "SELECT calculate_profit(?)", Int::class.java, id
        )
        return result ?: 0
    }
}