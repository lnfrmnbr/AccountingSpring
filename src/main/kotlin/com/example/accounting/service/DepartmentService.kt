package com.example.accounting.service

import com.example.accounting.models.Department
import com.example.accounting.repository.DepartmentRepository
import com.example.library.repository.BaseInterfaceRepository
import org.springframework.stereotype.Service

interface DepartmentService: BaseInterfaceRepository<Department> {
}

@Service
class DepartmentServiceImpl(private val departmentRepository: DepartmentRepository): DepartmentService{
    override fun findAll(): List<Department> {
        return departmentRepository.findAll()
    }

    override fun read(id: Long): Department? {
        return departmentRepository.read(id)
    }

    override fun add(entity: Department): Long? {
        return departmentRepository.add(entity)
    }

    override fun delete(id: Long): Boolean {
        return departmentRepository.delete(id)
    }

    override fun edit(id: Long, entity: Department): Department? {
        return departmentRepository.edit(id, entity)
    }

}