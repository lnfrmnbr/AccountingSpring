package com.example.accounting.service

import com.example.accounting.models.Project
import com.example.accounting.repository.ProjectRepository
import com.example.library.repository.BaseInterfaceRepository
import org.springframework.stereotype.Service

interface ProjectService: BaseInterfaceRepository<Project> {
}

@Service
class ProjectServiceImpl(private val projectRepository: ProjectRepository): ProjectService{
    override fun findAll(): List<Project> {
        return projectRepository.findAll()
    }

    override fun read(id: Long): Project? {
        return projectRepository.read(id)
    }

    override fun add(entity: Project): Long? {
        return projectRepository.add(entity)
    }

    override fun delete(id: Long): Boolean {
        return projectRepository.delete(id)
    }

    override fun edit(id: Long, entity: Project): Project? {
        return projectRepository.edit(id, entity)
    }

}