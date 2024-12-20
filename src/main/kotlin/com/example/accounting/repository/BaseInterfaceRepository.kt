package com.example.library.repository

interface BaseInterfaceRepository<T> {
    fun findAll(): List<T>
    fun read(id: Long): T?
    fun add(entity: T): Long?
    fun delete(id: Long): Boolean
    fun edit(id: Long, entity: T): T?
}