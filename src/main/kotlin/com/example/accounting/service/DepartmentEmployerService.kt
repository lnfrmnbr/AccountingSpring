package com.example.accounting.service

import com.example.accounting.models.DepartmentEmployer
import com.example.accounting.repository.DepartmentEmployerRepository
import com.example.library.repository.BaseInterfaceRepository
import org.springframework.stereotype.Service

interface DepartmentEmployerService: BaseInterfaceRepository<DepartmentEmployer> {
}

@Service
class DepartmentEmployerServiceImpl(private val departmentEmployerRepository: DepartmentEmployerRepository): DepartmentEmployerService{
    override fun findAll(): List<DepartmentEmployer> {
        return departmentEmployerRepository.findAll()
    }

    override fun read(id: Long): DepartmentEmployer? {
        return departmentEmployerRepository.read(id)
    }

    override fun add(entity: DepartmentEmployer): Long? {
        return departmentEmployerRepository.add(entity)
    }

    override fun delete(id: Long): Boolean {
        return departmentEmployerRepository.delete(id)
    }

    override fun edit(id: Long, entity: DepartmentEmployer): DepartmentEmployer? {
        return departmentEmployerRepository.edit(id, entity)
    }

}