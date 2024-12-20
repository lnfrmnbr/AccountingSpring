package com.example.accounting.controller

import com.example.accounting.models.Project
import com.example.accounting.service.ProjectService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/projects")
class ProjectController(private val projectService: ProjectService) {
    @GetMapping
    fun getAllProjects(): List<Project>? {
        return projectService.findAll()
    }

    @GetMapping("/{id}")
    fun readProject(@PathVariable id: Long): Project? {
        return projectService.read(id)
    }

    @PostMapping
    fun addProject(@RequestBody project: Project): Long? {
        return projectService.add(project)
    }

    @DeleteMapping("/{id}")
    fun deleteProject(@PathVariable id: Long) {
        projectService.delete(id)
    }

    @PutMapping("/{id}")
    fun editProject(@PathVariable id: Long, @RequestBody project: Project): Project? {
        return projectService.edit(id, project)
    }
}