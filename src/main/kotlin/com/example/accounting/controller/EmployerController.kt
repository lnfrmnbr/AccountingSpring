package com.example.accounting.controller

import com.example.accounting.models.Employer
import com.example.accounting.service.EmployerService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/employees")
class EmployerController(private val employerService: EmployerService) {
    @GetMapping
    fun getAllEmployees(): List<Employer>? {
        return employerService.findAll()
    }

    @GetMapping("/{id}")
    fun readEmployer(@PathVariable id: Long): Employer? {
        return employerService.read(id)
    }

    @PostMapping
    fun addEmployer(@RequestBody employer: Employer): Long? {
        return employerService.add(employer)
    }

    @DeleteMapping("/{id}")
    fun deleteEmployer(@PathVariable id: Long) {
        employerService.delete(id)
    }

    @PutMapping("/{id}")
    fun editEmployer(@PathVariable id: Long, @RequestBody employer: Employer): Employer? {
        return employerService.edit(id, employer)
    }
}