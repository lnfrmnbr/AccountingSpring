package com.example.accounting.controller

import com.example.accounting.service.CalculationService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class CalculationController(private val calculationService: CalculationService) {

    @GetMapping("/profit")
    fun getTotalProfit(): Int {
        return calculationService.calculateTotalProfit()
    }

    @GetMapping("/profit/{id}")
    fun getProfitById(@PathVariable id: Long): Int {
        return calculationService.calculateOneProfit(id)
    }
}