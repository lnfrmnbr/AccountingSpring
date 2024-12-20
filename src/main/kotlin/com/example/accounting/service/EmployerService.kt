package com.example.accounting.service

import com.example.accounting.models.Employer
import com.example.accounting.repository.EmployerRepository
import com.example.library.repository.BaseInterfaceRepository
import org.springframework.stereotype.Service

interface EmployerService: BaseInterfaceRepository<Employer> {
}

@Service
class EmployerServiceImpl(private val employerRepository: EmployerRepository): EmployerService{
    override fun findAll(): List<Employer> {
        return employerRepository.findAll()
    }

    override fun read(id: Long): Employer? {
        return employerRepository.read(id)
    }

    override fun add(entity: Employer): Long? {
        return employerRepository.add(entity)
    }

    override fun delete(id: Long): Boolean {
        return employerRepository.delete(id)
    }

    override fun edit(id: Long, entity: Employer): Employer? {
        return employerRepository.edit(id, entity)
    }

}