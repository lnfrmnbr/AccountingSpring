package com.example.accounting.controller

import com.example.accounting.models.Department
import com.example.accounting.service.DepartmentService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/departments")
class DepartmentController(private val departmentService: DepartmentService) {
    @GetMapping
    fun getAllDepartments(): List<Department>? {
        return departmentService.findAll()
    }

    @GetMapping("/{id}")
    fun readDepartment(@PathVariable id: Long): Department? {
        return departmentService.read(id)
    }

    @PostMapping
    fun addDepartment(@RequestBody department: Department): Long? {
        return departmentService.add(department)
    }

    @DeleteMapping("/{id}")
    fun deleteDepartment(@PathVariable id: Long) {
        departmentService.delete(id)
    }

    @PutMapping("/{id}")
    fun editDepartment(@PathVariable id: Long, @RequestBody department: Department): Department? {
        return departmentService.edit(id, department)
    }
}