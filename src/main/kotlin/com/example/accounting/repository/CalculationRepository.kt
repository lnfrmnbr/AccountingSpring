package com.example.accounting.repository

import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository
import org.springframework.jdbc.core.CallableStatementCreator
import java.sql.CallableStatement
import java.sql.Connection
import org.springframework.jdbc.datasource.DataSourceUtils
import javax.sql.DataSource
@Repository
class CalculationRepository {
    fun calculateTotalProfit(dataSource: DataSource): Int {
        val sql = "CALL public.calculateProfit(0)"
        var connection: Connection? = null
        var callableStatement: CallableStatement? = null

        return try {
            connection = DataSourceUtils.getConnection(dataSource)
            callableStatement = connection.prepareCall(sql)

            callableStatement.execute()
            0
        } catch (e: Exception) {
            e.printStackTrace()
            0
        } finally {
            callableStatement?.close()
            DataSourceUtils.releaseConnection(connection, dataSource)
        }
    }


    fun calculateOneProfit(id: Long, dataSource: DataSource): Int {
        val sql = "CALL public.calculateProfit(0)"
        var connection: Connection? = null
        var callableStatement: CallableStatement? = null

        return try {
            connection = DataSourceUtils.getConnection(dataSource)
            callableStatement = connection.prepareCall(sql)

            callableStatement.execute()
            0
        } catch (e: Exception) {
            e.printStackTrace()
            0
        } finally {
            callableStatement?.close()
            DataSourceUtils.releaseConnection(connection, dataSource)
        }
    }
}