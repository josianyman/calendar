package io.utu.project.calendar.api

import io.utu.project.util.RepositoryReader
import java.time.Instant
import java.util.UUID

interface ResourceApi {
    fun create(create: ResourceCreate): Resource
}

interface ResourceReaderApi : RepositoryReader<Resource, UUID>

interface Resource {
    val id: UUID
    val name: String
    val description: String
    val timezone: String
    val capacity: Int
    val createdAt: Instant
    val updatedAt: Instant
}

interface ResourceCreate {
    val name: String
    val description: String
    val timezone: String
    val capacity: Int
}