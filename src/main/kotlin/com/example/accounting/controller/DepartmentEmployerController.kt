package com.example.accounting.controller

import com.example.accounting.models.DepartmentEmployer
import com.example.accounting.service.DepartmentEmployerService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/department_employers")
class DepartmentEmployerController(private val departmentEmployerService: DepartmentEmployerService) {
    @GetMapping
    fun getAllDepartments(): List<DepartmentEmployer>? {
        return departmentEmployerService.findAll()
    }

    @GetMapping("/{id}")
    fun readDepartment(@PathVariable id: Long): DepartmentEmployer? {
        return departmentEmployerService.read(id)
    }

    @PostMapping
    fun addDepartment(@RequestBody departmentEmployer: DepartmentEmployer): Long? {
        return departmentEmployerService.add(departmentEmployer)
    }

    @DeleteMapping("/{id}")
    fun deleteDepartment(@PathVariable id: Long) {
        departmentEmployerService.delete(id)
    }

    @PutMapping("/{id}")
    fun editDepartment(@PathVariable id: Long, @RequestBody departmentEmployer: DepartmentEmployer): DepartmentEmployer? {
        return departmentEmployerService.edit(id, departmentEmployer)
    }
}